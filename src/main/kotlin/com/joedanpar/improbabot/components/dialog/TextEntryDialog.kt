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
package com.joedanpar.improbabot.components.dialog

import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import com.jagrosh.jdautilities.menu.Menu
import com.joedanpar.improbabot.components.common.Emojis.CHECK_MARK
import com.joedanpar.improbabot.components.common.Emojis.CROSS_X
import com.joedanpar.improbabot.components.common.MessageBuilderHelper
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.requests.RestAction
import java.awt.Color
import java.util.concurrent.TimeUnit
import java.util.function.BiConsumer
import java.util.function.Consumer

class TextEntryDialog private constructor(waiter: EventWaiter, users: Set<User>, roles: Set<Role>, timeout: Long,
                                          unit: TimeUnit, private val color: Color, private val text: String?, private val description: String?,
                                          private val reactAction: BiConsumer<Message, MessageReaction>,
                                          private val finalAction: Consumer<Message>) : Menu(waiter, users, roles, timeout, unit) {

    internal var value: String = ""

    private fun getMessage(): Message {
        val builder = MessageBuilderHelper()

        if (text != null) {
            builder.append(text)
        }

        if (description != null) {
            builder.setColor(color)
                    .setDescription(if (value.isNotEmpty()) {
                        value
                    } else {
                        description
                    })
        }

        return builder.build()
    }

    override fun display(channel: MessageChannel) {
        initialize(channel.sendMessage(getMessage()))
    }

    override fun display(message: Message) {
        initialize(message.editMessage(message))
    }

    private fun initialize(action: RestAction<Message>) {
        action.queue { message ->
            message.addReaction(CHECK_MARK).queue()
            message.addReaction(CROSS_X).queue({ textEditDialog(message) }, { textEditDialog(message) })
        }
    }

    private fun textEditDialog(message: Message) {
        // Text Reply Event
        waiter.waitForEvent(MessageReceivedEvent::class.java,
                { event ->
                    if (event.message.contentStripped.isEmpty()) {
                        return@waitForEvent false
                    }

                    return@waitForEvent isValidUser(event.author, event.guild)
                },
                { event ->
                    value = event.message.contentStripped
                    event.message.delete().queue()
                    display(message)
                }, timeout, unit, { finalAction.accept(message) })

        // Accept/Cancel React Event
        waiter.waitForEvent(MessageReactionAddEvent::class.java, { event ->
            if (event.messageId != message.id) {
                return@waitForEvent false
            }

            val reactionEmote = event.reactionEmote
            if (!(CHECK_MARK == reactionEmote.name || CROSS_X == reactionEmote.name)) {
                return@waitForEvent false
            }

            return@waitForEvent isValidUser(event.user, event.guild)
        }, { event: MessageReactionAddEvent ->
            if (CHECK_MARK == event.reactionEmote.name) {
                reactAction.accept(message, event.reaction)
            } else {
                finalAction.accept(message)
            }
        }, timeout, unit, { finalAction.accept(message) })
    }

    class Builder : Menu.Builder<Builder, TextEntryDialog>() {

        private var color: Color? = null
        private var text: String? = null
        private var reactAction: BiConsumer<Message, MessageReaction>? = null
        private var finalAction: Consumer<Message>? = null
        private var description: String? = null

        fun setColor(color: Color): Builder {
            this.color = color
            return this
        }

        fun setText(text: String): Builder {
            this.text = text
            return this
        }

        fun setDescription(description: String): Builder {
            this.description = description
            return this
        }

        fun setReactAction(reactAction: BiConsumer<Message, MessageReaction>): Builder {
            this.reactAction = reactAction
            return this
        }

        fun setFinalAction(finalAction: Consumer<Message>): Builder {
            this.finalAction = finalAction
            return this
        }

        override fun build(): TextEntryDialog {
            return TextEntryDialog(waiter, users, roles, timeout, unit, color!!, text, description, reactAction!!,
                    finalAction!!)
        }
    }
}
