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
package com.joedanpar.improbabot.components.config;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.Command.Category;
import com.jagrosh.jdautilities.command.CommandBuilder;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.joedanpar.improbabot.components.common.MessageHelper;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ConfigCommands {

    private final Category category = new Category("Admin");

    private ConfigService service;
    private MessageHelper messageHelper;

    @Autowired
    public ConfigCommands(final ConfigService service, final MessageHelper messageHelper) {
        this.service = service;
        this.messageHelper = messageHelper;
    }

    @Bean
    public Command configCommand() {
        return new CommandBuilder()
                .setCategory(category)
                .setName("config")
                .setRequiredRole("admin")
                .setChildren(addConfigCommand(), removeConfigCommand(), listConfigsCommand())
                .build((command, event) -> {});
    }

    private Command addConfigCommand() {
        return new CommandBuilder()
                .setCategory(category)
                .setName("add")
                .setRequiredRole("admin")
                .setArguments("[configKey] [configValue]")
                .build((command, event) -> addConfig(event));
    }

    private Command removeConfigCommand() {
        return new CommandBuilder()
                .setCategory(category)
                .setName("remove")
                .setRequiredRole("admin")
                .build((command, event) -> removeConfig(event));
    }

    private Command listConfigsCommand() {
        return new CommandBuilder()
                .setCategory(category)
                .setName("list")
                .setRequiredRole("admin")
                .build((command, event) -> listConfigs(event));
    }

    private void addConfig(final CommandEvent event) {
        val args = event.getArgs().split("\\s+");
        new ConfigBuilder().setServerId(event.getGuild().getId())
                           .setKey(args[3])
                           .setValue(args[4])
                           .build();
    }

    private void removeConfig(final CommandEvent event) {
        val args = event.getArgs().split("\\s+");
        service.removeObject(new ConfigBuilder().setServerId(event.getGuild().getId())
                                                .setKey(args[3])
                                                .setValue(args[4])
                                                .build());
//        messageHelper.reactSuccessfulResponse(event.getMessage());
    }

    private void listConfigs(final CommandEvent event) {
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
