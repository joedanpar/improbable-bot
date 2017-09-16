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
package com.joedanpar.improbabot.command;

import com.joedanpar.improbabot.util.RolePermission;
import lombok.val;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.joedanpar.improbabot.util.MessageHelper.sendMessage;
import static com.joedanpar.improbabot.util.RolePermission.EVERYONE;

public abstract class AbstractCommand {

    int     length;
    String  description;
    String  name;
    boolean hidden;
    RolePermission permissionLevel = EVERYONE;

    public void execute(final MessageReceivedEvent event) {
        if (userHasPermission(event.getGuild(), event.getMember())) {
            executeCommand(event);
        } else {
            sendMessage(event.getChannel(), "Insufficient privileges for %s command, %s required.", name,
                        permissionLevel.toString());
        }
    }

    private boolean userHasPermission(final Guild guild, final Member member) {
        if (EVERYONE.equals(permissionLevel)) {
            return true;
        }

        val role = guild.getRolesByName(permissionLevel.getRoleName(), true).get(0);
        return guild.getMembersWithRoles(role).contains(member);
    }

    protected abstract void executeCommand(final MessageReceivedEvent event);
}
