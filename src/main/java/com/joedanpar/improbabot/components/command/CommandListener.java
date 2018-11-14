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
import com.joedanpar.improbabot.components.info.HelpCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommandListener extends ListenerAdapter {

    @Value("${debugEnabled:false}")
    private boolean debugEnabled;

    private CommandParser commandParser;
    private MessageHelper messageHelper;

    @Autowired
    public CommandListener(final CommandParser commandParser, final List<AbstractCommand> commands,
                           final MessageHelper messageHelper) {
        this.commandParser = commandParser;
        this.messageHelper = messageHelper;
        addCommands(commands);
    }

    private void addCommands(final List<AbstractCommand> commands) {
        commandParser.addCommands(commands);
        commandParser.addCommand(new HelpCommand(commandParser.getCommands(), messageHelper));
    }

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if (isBotMention(event) && checkDebug(event.getChannel().getName())) {
            commandParser.parseEvent(event);
        }
    }

    private boolean isBotMention(final MessageReceivedEvent event) {
        return !event.getAuthor().isBot()
               && event.getMessage().getContentStripped().toLowerCase().startsWith("@improbabot");
    }

    private boolean checkDebug(final String channelName) {
        return !debugEnabled || channelName.equals("admin-test");
    }
}
