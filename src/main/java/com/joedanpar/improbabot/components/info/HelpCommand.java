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
package com.joedanpar.improbabot.components.info;

import com.joedanpar.improbabot.components.command.AbstractCommand;
import com.joedanpar.improbabot.components.common.MessageHelper;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.awt.Color.BLUE;

public class HelpCommand extends AbstractCommand {

    private Map<String, AbstractCommand> commands;

    public HelpCommand(final Map<String, AbstractCommand> commands, final MessageHelper messageHelper) {
        length = 2;
        name = "help";
        description = "displays this message";
        this.commands = commands;
        this.messageHelper = messageHelper;
    }

    @Override
    protected void executeCommand(final MessageReceivedEvent event) {
        MessageBuilder msgBuilder = new MessageBuilder();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("List of Commands");
        embedBuilder.setColor(BLUE);

        List<String> commandKeys = new ArrayList<>(commands.keySet());
        Collections.sort(commandKeys);
        commandKeys.stream()
                   .filter(key -> userHasPermission(event.getGuild(), event.getAuthor(),
                                                    commands.get(key).getPermissionLevel()))
                   .forEach(key -> embedBuilder.addField(key, commands.get(key).getDescription(), false));

        embedBuilder.setFooter("User \"help\" at the end of any command to get more information about it", null);

        msgBuilder.setEmbed(embedBuilder.build());
        messageHelper.sendPrivateMessage(event.getAuthor(), msgBuilder.build());
        messageHelper.reactSuccessfulResponse(event.getMessage());
    }
}
