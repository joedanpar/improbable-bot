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
package com.joedanpar.improbabot.listeners;

import com.joedanpar.improbabot.command.parser.CommandParser;
import com.joedanpar.improbabot.util.Reference;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import static com.joedanpar.improbabot.util.Reference.ADMIN_TEST_CHANNEL;

@RequiredArgsConstructor
public class CommandListener extends ListenerAdapter {

    private final boolean debug;

    private CommandParser parser = CommandParser.getInstance();

    public void onMessageReceived(final MessageReceivedEvent event) {
        if (isValidCommand(event)) {
            parser.parseEvent(event);
        }
    }

    private boolean isValidCommand(final MessageReceivedEvent event) {
        return (!debug || ADMIN_TEST_CHANNEL.equalsIgnoreCase(event.getChannel().getId())) &&
               !event.getAuthor().isBot() && event.getMessage().getContent().toLowerCase().startsWith("@improbabot");
    }
}
