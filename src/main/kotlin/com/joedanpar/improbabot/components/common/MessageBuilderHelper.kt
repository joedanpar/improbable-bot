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

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.Message
import org.springframework.stereotype.Component
import java.awt.Color
import java.time.temporal.TemporalAccessor

@Component
class MessageBuilderHelper(
        private var messageBuilder: MessageBuilder = MessageBuilder(),
        private var embedBuilder: EmbedBuilder = EmbedBuilder()
) {

    fun setTitle(title: String): MessageBuilderHelper {
        embedBuilder.setTitle(title)
        return this
    }

    fun setColor(color: Color): MessageBuilderHelper {
        embedBuilder.setColor(color)
        return this
    }

    fun setColor(avatarUrl: String): MessageBuilderHelper {
        embedBuilder.setColor(UserHelper.getAverageColor(avatarUrl))
        return this
    }

    fun setTimestamp(temporalAccessor: TemporalAccessor): MessageBuilderHelper {
        embedBuilder.setTimestamp(temporalAccessor)
        return this
    }

    fun setAuthor(name: String): MessageBuilderHelper {
        embedBuilder.setAuthor(name)
        return this
    }

    fun append(text: String): MessageBuilderHelper {
        messageBuilder.append(text)
        return this
    }

    fun setDescription(description: String): MessageBuilderHelper {
        embedBuilder.setDescription(description)
        return this
    }

    fun appendDescription(description: String): MessageBuilderHelper {
        embedBuilder.appendDescription(description)
        return this
    }

    fun addField(name: String, value: String, inline: Boolean): MessageBuilderHelper {
        embedBuilder.addField(name, value, inline)
        return this
    }

    fun setFooter(footer: String, url: String): MessageBuilderHelper {
        embedBuilder.setFooter(footer, url)
        return this
    }

    fun build(): Message {
        return messageBuilder.setEmbed(embedBuilder.build()).build()
    }
}