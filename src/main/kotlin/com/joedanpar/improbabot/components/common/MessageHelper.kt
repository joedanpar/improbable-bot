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

package com.joedanpar.improbabot.components.common

import com.joedanpar.improbabot.components.common.Emojis.CHECK_MARK
import com.joedanpar.improbabot.components.common.Emojis.CROSS_X
import com.joedanpar.improbabot.components.common.Emojis.QUESTION_MARK
import net.dv8tion.jda.api.entities.Emote
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils.isEmpty
import java.lang.String.format
import java.util.Arrays.asList

@Component
class MessageHelper {

    fun sendMessage(channel: MessageChannel, message: String, vararg parameters: String) {
        sendMessage(channel, message, asList(*parameters))
    }

    fun sendMessage(channel: MessageChannel, message: String, parameters: Collection<String>) {
        if (isEmpty(parameters)) {
            channel.sendMessage(message).queue()
        } else {
            channel.sendMessage(format(message, parameters)).queue()
        }
    }

    fun reactUnknownResponse(message: Message) {
        message.addReaction(QUESTION_MARK).queue()
    }

    fun addReaction(message: Message, emoji: String) {
        message.addReaction(emoji).queue()
    }

    fun addReaction(message: Message, emoji: Emote) {
        message.addReaction(emoji).queue()
    }

    fun sendMessage(channel: MessageChannel, message: Message) {
        channel.sendMessage(message).queue()
    }

    fun reactAccordingly(message: Message, isSuccessful: Boolean) {
        if (isSuccessful) {
            reactSuccessfulResponse(message)
        } else {
            reactUnsuccessfulResponse(message)
        }
    }

    fun reactSuccessfulResponse(message: Message) {
        message.addReaction(CHECK_MARK).queue()
    }

    fun reactUnsuccessfulResponse(message: Message) {
        message.addReaction(CROSS_X).queue()
    }

    fun sendPrivateMessage(user: User, message: Message) {
        user.openPrivateChannel().queue { privateChannel -> privateChannel.sendMessage(message).queue() }
    }

    fun sendPrivateMessage(user: User, message: String) {
        user.openPrivateChannel().queue { privateChannel -> privateChannel.sendMessage(message).queue() }
    }
}
