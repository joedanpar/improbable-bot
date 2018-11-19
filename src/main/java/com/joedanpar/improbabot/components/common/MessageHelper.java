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
package com.joedanpar.improbabot.components.common;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static com.joedanpar.improbabot.components.common.Emojis.*;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.springframework.util.StringUtils.isEmpty;

@Component
public class MessageHelper {

    public void sendMessage(final MessageChannel channel, final String message, final String... parameters) {
        sendMessage(channel, message, asList(parameters));
    }

    public void sendMessage(final MessageChannel channel, final String message,
                            final Collection<String> parameters) {
        if (isEmpty(parameters)) {
            channel.sendMessage(message).queue();
        } else {
            channel.sendMessage(format(message, parameters)).queue();
        }
    }

    public void reactUnknownResponse(final Message message) {
        message.addReaction(QUESTION_MARK).queue();
    }

    public void addReaction(final Message message, final String emoji) {
        message.addReaction(emoji).queue();
    }

    public void addReaction(final Message message, final Emote emoji) {
        message.addReaction(emoji).queue();
    }

    public void sendMessage(final MessageChannel channel, final Message message) {
        channel.sendMessage(message).queue();
    }

    public void reactAccordingly(final Message message, final boolean isSuccessful) {
        if (isSuccessful) {
            reactSuccessfulResponse(message);
        } else {
            reactUnsuccessfulResponse(message);
        }
    }

    public void reactSuccessfulResponse(final Message message) {
        message.addReaction(CHECK_MARK).queue();
    }

    public void reactUnsuccessfulResponse(final Message message) {
        message.addReaction(CROSS_X).queue();
    }

    public void sendPrivateMessage(final User user, final Message message) {
        user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(message).queue());
    }

    public void sendPrivateMessage(final User user, final String message) {
        user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(message).queue());
    }
}
