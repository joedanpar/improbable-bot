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
package com.joedanpar.improbabot.util;

import lombok.experimental.UtilityClass;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.Collection;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@UtilityClass
public class MessageHelper {
    public static void sendMessage(final MessageChannel channel, final String message, final String... parameters) {
        sendMessage(channel, message, asList(parameters));
    }

    public static void sendMessage(final MessageChannel channel, final String message,
                                   final Collection<String> parameters) {
        if (isEmpty(parameters)) {
            channel.sendMessage(message).queue();
        } else {
            channel.sendMessage(format(message, parameters)).queue();
        }
    }
}
