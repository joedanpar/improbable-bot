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
package com.joedanpar.improbabot.components.command;

import com.joedanpar.improbabot.components.common.MessageHelper;
import com.joedanpar.improbabot.components.config.ConfigService;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.joedanpar.improbabot.components.config.ConfigConstants.CONVERSATION_CHANNELS;
import static com.joedanpar.improbabot.components.config.ConfigConstants.ERROR_ANNOUNCE_CHANNEL;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Log4j2
@Component
public class CommandParser {

    private ConfigService                configService;
    private MessageHelper                messageHelper;
    @Getter
    private Map<String, AbstractCommand> commands = new HashMap<>();

    @Autowired
    private CommandParser(final ConfigService configService, final MessageHelper messageHelper) {
        this.configService = configService;
        this.messageHelper = messageHelper;
    }

    void parseEvent(final MessageReceivedEvent event) {
        val message = event.getMessage().getContentStripped();

        if (isNotBlank(message)) {
            val command = commands.get(getCommand(message));

            if (command != null) {
                try {
                    event.getChannel().sendTyping().queue();
                    command.execute(event);
                } catch (Exception e) {
                    log.error("Error executing command " + command.getName(), e);
                    reportError(event, e);
                }
            } else {
                val channelList = configService.getValuesByName(event.getGuild().getId(), CONVERSATION_CHANNELS);
                if (!channelList.contains(event.getChannel().getId())) {
                    messageHelper.reactUnknownResponse(event.getMessage());
                }
            }
        }
    }

    private String getCommand(final String message) {
        val splitMsg = message.split(" ");

        if (splitMsg.length > 1) {
            return splitMsg[1] != null
                    ? splitMsg[1].toLowerCase()
                    : EMPTY;
        }

        return null;
    }

    private void reportError(final MessageReceivedEvent event, final Exception e) {
        val config = configService.getConfig(event.getGuild().getId(), ERROR_ANNOUNCE_CHANNEL);
        if (config.size() > 0) {
            messageHelper.sendMessage(getChannel(event.getJDA(), event.getGuild().getId(), config.get(0).getValue()),
                                      e.toString() +
                                      "\n" + e.getStackTrace()[0].toString());
        }
    }

    private MessageChannel getChannel(final JDA jda, final String serverId, final String channel) {
        return jda.getGuildById(serverId).getTextChannelById(channel);
    }

    void addCommands(final List<AbstractCommand> commands) {
        commands.forEach(this::addCommand);
    }

    void addCommand(final AbstractCommand command) {
        commands.put(command.getName(), command);
    }
}
