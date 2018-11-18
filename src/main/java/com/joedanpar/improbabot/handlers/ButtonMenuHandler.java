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
package com.joedanpar.improbabot.handlers;

import com.jagrosh.jdautilities.menu.ButtonMenu.Builder;
import com.jagrosh.jdautilities.menu.Menu;
import lombok.val;
import net.dv8tion.jda.core.entities.MessageReaction.ReactionEmote;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ButtonMenuHandler extends AbstractMenuHandler<Builder> {
    @Override
    public Builder configureBuilder(final Menu.Builder builder, final Map<String, Object> parameters) {
        val _builder = super.configureBuilder(builder, parameters);

        configureText(_builder, (String) parameters.get("text"));
        configureDescription(_builder, (String) parameters.get("description"));
        configureChoices(_builder, (List<String>) parameters.get("choices"));
        configureAction(_builder, (Consumer<ReactionEmote>) parameters.get("action"));
//        configureCancel(_builder, (Runnable) parameters.get("cancel"));

        return _builder;
    }

    private void configureText(final Builder builder, final String text) {
        if (isNotBlank(text)) {
            builder.setText(text);
        }
    }

    private void configureDescription(final Builder builder, final String description) {
        if (isNotBlank(description)) {
            builder.setDescription(description);
        }
    }

    private void configureChoices(final Builder builder, final List<String> choices) {
        if (isNotEmpty(choices)) {
            builder.setChoices(choices.toArray(new String[0]));
        }
    }

    private void configureAction(final Builder builder, final Consumer<ReactionEmote> action) {
        if (action != null) {
            builder.setAction(action);
        }
    }

    /*private void configureCancel(final Builder builder, final Runnable cancel) {
        if (cancel != null) {
            builder.setCancel(cancel);
        }
    }*/
}
