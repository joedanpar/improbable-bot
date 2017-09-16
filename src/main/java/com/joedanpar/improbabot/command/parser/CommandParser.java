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

import com.joedanpar.improbabot.Improbabot;
import com.joedanpar.improbabot.command.AbstractCommand;
import com.joedanpar.improbabot.command.HelpCommand;
import com.joedanpar.improbabot.util.MessageHelper;
import com.joedanpar.improbabot.util.Reference;
import lombok.Getter;
import lombok.val;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

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

        if (StringUtils.isNotBlank(message)) {
            val command = commands.get(getCommand(message));

            if (command != null) {
                try {
                    command.execute(event);
                } catch (Exception e) {
                    MessageHelper.sendMessage(getChannel(Reference.ADMIN_TEST_CHANNEL), e.toString() + "\n" + e.getStackTrace()[0]);
                }
            }
        }
    }

    private String getCommand(final String message) {
        val splitMsg = message.split(" ");

        if (splitMsg.length > 1) {
            return splitMsg[1] != null ? splitMsg[1].toLowerCase() : StringUtils.EMPTY;
        }

        return null;
    }

    private MessageChannel getChannel(final String channel) {
        return Improbabot.getJda().getGuildById(Reference.ADMIN_SERVER_ID).getTextChannelById(channel);
    }
}
