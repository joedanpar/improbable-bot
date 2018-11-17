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
import com.joedanpar.improbabot.components.common.RolePermission;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;

import static com.joedanpar.improbabot.components.common.RolePermission.EVERYONE;
import static com.joedanpar.improbabot.components.common.RolePermission.OWNER;

@Log4j2
@Data
public abstract class AbstractCommand {

    protected int            length;
    protected String         description;
    protected String         name;
    protected RolePermission permissionLevel  = EVERYONE;
    protected boolean        checkExactLength = true;
    protected String         helpText;

    @Autowired
    protected MessageHelper messageHelper;

    public void execute(final MessageReceivedEvent event) {
        if (userHasPermission(event.getGuild(), event.getAuthor())) {
            val message = event.getMessage().getContentStripped();
            log.info("Executing command {} by {}: {}", name, event.getAuthor().getName(), message);
            if (getSubcommand(message).equalsIgnoreCase("help")) {
                displayHelp(event);
            } else {
                executeCommand(event);
            }
        } else {
            messageHelper.reactUnsuccessfulResponse(event.getMessage());
        }
    }

    private boolean userHasPermission(final Guild guild, final User user) {
        return userHasPermission(guild, user, this.permissionLevel);
    }

    protected String getSubcommand(final String command) {
        return getCommandLength(command) >= 3
                ? command.split(" ")[2].toLowerCase()
                : "";
    }

    protected void displayHelp(final MessageReceivedEvent event) {
        val help = helpText != null
                ? helpText
                : description;
        messageHelper.sendPrivateMessage(event.getAuthor(), help);
        messageHelper.reactSuccessfulResponse(event.getMessage());
    }

    protected abstract void executeCommand(final MessageReceivedEvent event);

    protected boolean userHasPermission(final Guild guild, final User user, final RolePermission rolePermission) {
        if (EVERYONE.equals(permissionLevel)) {
            return true;
        } else if (OWNER.equals(permissionLevel)) {
            return user.getId().equals(OWNER.getRoleName());
        } else {
            return guild.getMembersWithRoles(guild.getRolesByName(rolePermission.getRoleName(), true).get(0))
                        .contains(guild.getMember(user));
        }
    }

    protected int getCommandLength(final String content) {
        return content.split(" ").length;
    }

    @Override
    public String toString() {
        return name + ": " + description;
    }

    protected boolean isCommandLengthCorrect(final String content) {
        return isCommandLengthCorrect(content, length);
    }

    protected boolean isCommandLengthCorrect(final String content, final int length) {
        val commandLength = getCommandLength(content);

        if (checkExactLength) {
            return commandLength == length;
        } else {
            return commandLength >= length;
        }
    }
}
