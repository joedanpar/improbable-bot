/*******************************************************************************
 * This file is part of Improbable Bot.
 *
 *    Improbable Bot is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Improbable Bot is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with Improbable Bot.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.joedanpar.improbabot.command.parser;

import com.joedanpar.improbabot.command.AbstractCommand;
import com.joedanpar.improbabot.command.HelpCommand;
import lombok.Getter;
import lombok.val;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

import static com.joedanpar.improbabot.Improbabot.getJda;
import static com.joedanpar.improbabot.util.MessageHelper.sendMessage;
import static com.joedanpar.improbabot.util.Reference.ADMIN_SERVER_ID;
import static com.joedanpar.improbabot.util.Reference.ADMIN_TEST_CHANNEL;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class CommandParser {

    @Getter
    private static final CommandParser instance = new CommandParser();

    @Getter
    private Map<String, AbstractCommand> commands;

    private CommandParser() {
        commands = new HashMap<>();
        commands.put("help", new HelpCommand());
    }

    public void parseEvent(final MessageReceivedEvent event) {
        val message = event.getMessage().getContent();

        if (isNotBlank(message)) {
            val command = commands.get(getCommand(message));

            if (command != null) {
                try {
                    command.execute(event);
                } catch (Exception e) {
                    sendMessage(getChannel(ADMIN_TEST_CHANNEL), e.toString() + "\n" + e.getStackTrace()[0]);
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

    private MessageChannel getChannel(final String channel) {
        return getJda().getGuildById(ADMIN_SERVER_ID).getTextChannelById(channel);
    }
}
