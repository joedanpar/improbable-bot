/*******************************************************************************
 * This file is part of Improbable Bot.
 *
 *     Improbable Bot is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Improbable Bot is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Improbable Bot.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.joedanpar.improbabot.components.admin;

import com.joedanpar.improbabot.components.command.AbstractCommand;
import com.joedanpar.improbabot.components.config.ConfigBuilder;
import com.joedanpar.improbabot.components.config.ConfigService;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

import static com.joedanpar.improbabot.components.common.RolePermission.ADMIN;

@Log4j2
@Component
public class AdminCommand extends AbstractCommand {

    private ConfigService service;

    public AdminCommand(final ConfigService configService) {
        length = 3;
        name = "admin";
        description = "Admin commands";
        permissionLevel = ADMIN;
        this.service = configService;
    }

    @Override
    public void executeCommand(final MessageReceivedEvent event) {
        switch (getSubcommand(event.getMessage().getContentStripped())) {
            case "config-add":
                if (isCommandLengthCorrect(event.getMessage().getContentStripped(), 5)) {
                    addConfig(event);
                } else {
                    messageHelper.reactUnsuccessfulResponse(event.getMessage());
                }
                break;
            case "config-remove":
                if (isCommandLengthCorrect(event.getMessage().getContentStripped(), 5)) {
                    removeConfig(event);
                } else {
                    messageHelper.reactUnsuccessfulResponse(event.getMessage());
                }
                break;
            case "config-list":
                if (isCommandLengthCorrect(event.getMessage().getContentStripped(), 3)) {
                    listConfigs(event);
                } else {
                    messageHelper.reactUnsuccessfulResponse(event.getMessage());
                }
                break;
            default:
                messageHelper.reactUnknownResponse(event.getMessage());
                break;
        }
    }

    private void addConfig(final MessageReceivedEvent event) {
        val message = event.getMessage().getContentStripped();
        messageHelper.reactAccordingly(event.getMessage(), service.createObject(
                new ConfigBuilder().setServerId(event.getGuild().getId())
                                   .setKey(message.split(" ")[3])
                                   .setValue(message.split(" ")[4])
                                   .build()));
    }

    private void removeConfig(final MessageReceivedEvent event) {
        val message = event.getMessage().getContentStripped();
        service.removeObject(new ConfigBuilder().setServerId(event.getGuild().getId())
                                                .setKey(message.split(" ")[3])
                                                .setValue(message.split(" ")[4])
                                                .build());
        messageHelper.reactSuccessfulResponse(event.getMessage());
    }

    private void listConfigs(final MessageReceivedEvent event) {
        val configsByServerId = service.getAllObjectsByServerId(event.getGuild().getId());
        val serverName        = event.getGuild().getName();

        if (configsByServerId.isEmpty()) {
            messageHelper.sendMessage(event.getChannel(), "No configs found for server %s", serverName);
            return;
        }

        val msgBuilder   = new MessageBuilder();
        val embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Configs for " + serverName);

        for (val config : configsByServerId) {
            embedBuilder.addField(config.getKey(), config.getValue(), false);
        }

        msgBuilder.setEmbed(embedBuilder.build());
        messageHelper.sendMessage(event.getChannel(), msgBuilder.build());
    }
}
