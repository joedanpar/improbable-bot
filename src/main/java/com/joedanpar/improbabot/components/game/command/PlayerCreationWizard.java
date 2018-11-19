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
package com.joedanpar.improbabot.components.game.command;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Menu.Builder;
import com.jagrosh.jdautilities.menu.SelectionDialog;
import com.joedanpar.improbabot.components.common.MessageHelper;
import com.joedanpar.improbabot.components.game.player.PlayerBuilder;
import com.joedanpar.improbabot.components.game.player.PlayerService;
import lombok.Getter;
import lombok.val;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.utils.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jagrosh.jdautilities.menu.SelectionDialog.SELECT;
import static com.joedanpar.improbabot.components.common.Emojis.BUTTON;
import static com.joedanpar.improbabot.components.game.player.Gender.FEMALE;
import static com.joedanpar.improbabot.components.game.player.Gender.MALE;

@Service
public class PlayerCreationWizard {

    private EventWaiter   waiter;
    private PlayerService service;
    private MessageHelper messageHelper;

    @Getter
    private Map<Pair<Guild, User>, PlayerBuilder> builders = new HashMap<>();
    @Getter
    private List<Builder>                         dialogs  = new ArrayList<>();

    @Autowired
    public PlayerCreationWizard(final EventWaiter waiter, final PlayerService service,
                                final MessageHelper messageHelper) {
        this.waiter = waiter;
        this.service = service;
        this.messageHelper = messageHelper;
    }

    public void execute(final CommandEvent event) {
        builders.put(Pair.of(event.getGuild(), event.getAuthor()), new PlayerBuilder());

        displayDialog(event);
    }

    private void displayDialog(final CommandEvent event) {
        new SelectionDialog.Builder()
                .addUsers(event.getAuthor())
                .setText("Please select your gender")
                .setChoices("Male", "Female")
                .setDefaultEnds(BUTTON, "")
                .setSelectedEnds(SELECT, "")
                .useLooping(true)
                .setEventWaiter(waiter)
                .setSelectionConsumer((message, selection) -> {
                    val selectedGender = selection == 1
                            ? MALE
                            : FEMALE;
                    builders.get(Pair.of(event.getGuild(), event.getAuthor()))
                            .setGender(selectedGender);
                })
                .setCanceled(message -> message.delete().queue())
                .build()
                .display(event.getChannel());
    }
}
