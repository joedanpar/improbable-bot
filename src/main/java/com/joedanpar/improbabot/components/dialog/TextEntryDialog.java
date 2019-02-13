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
package com.joedanpar.improbabot.components.dialog;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Menu;
import lombok.Getter;
import lombok.val;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.requests.RestAction;

import java.awt.*;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.joedanpar.improbabot.components.common.Emojis.CHECK_MARK;
import static com.joedanpar.improbabot.components.common.Emojis.CROSS_X;

public class TextEntryDialog extends Menu {

    private final Color                                color;
    private final String                               text;
    private final String                               description;
    private final BiConsumer<Message, MessageReaction> reactAction;
    private final Consumer<Message>                    finalAction;

    @Getter
    private String value = "";

    private TextEntryDialog(final EventWaiter waiter, final Set<User> users, final Set<Role> roles, final long timeout,
                            final TimeUnit unit, final Color color, final String text, final String description,
                            final BiConsumer<Message, MessageReaction> reactAction,
                            final Consumer<Message> finalAction) {
        super(waiter, users, roles, timeout, unit);

        this.color = color;
        this.text = text;
        this.description = description;
        this.reactAction = reactAction;
        this.finalAction = finalAction;
    }

    @Override
    public void display(MessageChannel channel) {
        initialize(channel.sendMessage(getMessage()));
    }

    @Override
    public void display(Message message) {
        initialize(message.editMessage(getMessage()));
    }

    private void initialize(RestAction<Message> action) {
        action.queue(message -> {

            message.addReaction(CHECK_MARK).queue();
            message.addReaction(CROSS_X).queue(aVoid -> textEditDialog(message), aVoid -> textEditDialog(message));
        });
    }

    private void textEditDialog(final Message message) {
        // Text Reply Event
        waiter.waitForEvent(MessageReceivedEvent.class,
                            event -> {
                                if (event.getMessage().getContentStripped().isEmpty()) {
                                    return false;
                                }

                                return isValidUser(event.getAuthor(), event.getGuild());
                            },
                            event -> {
                                value = event.getMessage().getContentStripped();
                                event.getMessage().delete().queue();
                                display(message);
                            }, timeout, unit, () -> finalAction.accept(message));

        // Accept/Cancel React Event
        waiter.waitForEvent(MessageReactionAddEvent.class, event -> {
            if (!event.getMessageId().equals(message.getId())) {
                return false;
            }

            val reactionEmote = event.getReactionEmote();
            if (!(CHECK_MARK.equals(reactionEmote.getName()) || CROSS_X.equals(reactionEmote.getName()))) {
                return false;
            }

            return isValidUser(event.getUser(), event.getGuild());
        }, (MessageReactionAddEvent event) -> {
            if (CHECK_MARK.equals(event.getReactionEmote().getName())) {
                reactAction.accept(message, event.getReaction());
            } else {
                finalAction.accept(message);
            }
        }, timeout, unit, () -> finalAction.accept(message));
    }

    private Message getMessage() {
        val builder = new MessageBuilder();

        if (text != null) {
            builder.append(text);
        }

        if (description != null) {
            builder.setEmbed(new EmbedBuilder().setColor(color)
                                               .setDescription(!value.isEmpty()
                                                                       ? value
                                                                       : description)
                                               .build());
        }

        return builder.build();
    }

    public static class Builder extends Menu.Builder<Builder, TextEntryDialog> {

        private Color                                color;
        private String                               text;
        private BiConsumer<Message, MessageReaction> reactAction;
        private Consumer<Message>                    finalAction;
        private String                               description;

        @Override
        public TextEntryDialog build() {
            return new TextEntryDialog(waiter, users, roles, timeout, unit, color, text, description, reactAction,
                                       finalAction);
        }

        public Builder setColor(final Color color) {
            this.color = color;
            return this;
        }

        public Builder setText(final String text) {
            this.text = text;
            return this;
        }

        public Builder setDescription(final String description) {
            this.description = description;
            return this;
        }

        public Builder setReactAction(final BiConsumer<Message, MessageReaction> reactAction) {
            this.reactAction = reactAction;
            return this;
        }

        public Builder setFinalAction(Consumer<Message> finalAction) {
            this.finalAction = finalAction;
            return this;
        }
    }
}
