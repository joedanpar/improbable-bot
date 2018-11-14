package com.joedanpar.improbabot.components.admin;

import com.joedanpar.improbabot.components.command.AbstractCommand;
import com.joedanpar.improbabot.components.config.Config;
import com.joedanpar.improbabot.components.config.ConfigService;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

import static com.joedanpar.improbabot.components.common.RolePermission.BOT_ADMIN;

@Component
@Log4j2
public class AdminCommand extends AbstractCommand {

    private ConfigService service;

    public AdminCommand(final ConfigService configService) {
        length = 3;
        name = "admin";
        description = "Admin commands";
        permissionLevel = BOT_ADMIN;
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
        val config  = new Config(event.getGuild().getId(), message.split(" ")[3], message.split(" ")[4]);
        messageHelper.reactAccordingly(event.getMessage(), service.addConfig(config));
    }

    private void removeConfig(final MessageReceivedEvent event) {
        val message = event.getMessage().getContentStripped();
        val config  = new Config(event.getGuild().getId(), message.split(" ")[3], message.split(" ")[4]);
        service.removeConfig(config);
        messageHelper.reactSuccessfulResponse(event.getMessage());
    }

    private void listConfigs(final MessageReceivedEvent event) {
        val configsByServerId = service.getConfigsByServerId(event.getGuild().getId());
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
