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

import com.joedanpar.improbabot.util.MessageHelper;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HelpCommand extends AbstractCommand {
    @Override
    protected void executeCommand(final MessageReceivedEvent event) {
        MessageHelper.sendMessage(event.getChannel(), "Help is on the way, citizen!");
    }
}
