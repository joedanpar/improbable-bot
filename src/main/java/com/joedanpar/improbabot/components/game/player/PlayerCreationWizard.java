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
package com.joedanpar.improbabot.components.game.player;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Menu;
import com.jagrosh.jdautilities.menu.SelectionDialog;
import com.joedanpar.improbabot.components.dialog.TextEntryDialog;
import com.joedanpar.improbabot.components.dialog.WizardDialog;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.jagrosh.jdautilities.menu.SelectionDialog.SELECT;
import static com.joedanpar.improbabot.components.common.Emojis.BUTTON;
import static java.util.Arrays.asList;
import static net.dv8tion.jda.core.utils.Checks.notNull;

class PlayerCreationWizard extends WizardDialog {

    private static String[] genders = {
            "Male",
            "Female",
            "Other"
    };
    private static String[] races   = {
            "Human",
            "Elf",
            "Dwarf",
            "Orc"
    };

    private PlayerBuilder builder;
    private PlayerService service;

    private PlayerCreationWizard(final EventWaiter waiter, final Set<User> users, final Set<Role> roles,
                                 final long timeout, final TimeUnit unit, final PlayerBuilder builder,
                                 final PlayerService service) {
        super(waiter, users, roles, timeout, unit);

        this.builder = builder;
        this.service = service;

        dialogs.addAll(buildDialogs());
        reset();
    }

    private Collection<Menu> buildDialogs() {
        return asList(buildNameDialog(),
                      buildGenderDialog(),
                      buildRaceDialog());
    }

    private Menu buildNameDialog() {
        return new TextEntryDialog.Builder()
                .setEventWaiter(waiter)
                .setUsers(users.toArray(new User[0]))
                .setRoles(roles.toArray(new Role[0]))
                .setText("Please enter your player's name")
                .setDescription("Name")
                .setReactAction((message, reaction) -> {
                    builder.setServerId(message.getGuild().getId())
                           .setUserId(message.getAuthor().getId())
                           .setName(((TextEntryDialog) dialogs.get(currentMenu.previousIndex())).getValue());
                    display(message);
                })
                .setFinalAction(message -> {
                    message.delete().queue();
                    reset();
                })
                .build();
    }

    private Menu buildGenderDialog() {
        return new SelectionDialog.Builder()
                .setEventWaiter(waiter)
                .setUsers(users.toArray(new User[0]))
                .setRoles(roles.toArray(new Role[0]))
                .setText("Please select your player's gender")
                .setChoices(genders)
                .setDefaultEnds(BUTTON, "")
                .setSelectedEnds(SELECT, "")
                .useLooping(true)
                .setSelectionConsumer((message, selection) -> {
                    if (selection != 3) {
                        builder.setGender(genders[selection - 1]);
                    } else {
                        currentMenu.add(buildGenderEntryDialog(message));
                    }

                    display(message);
                })
                .setCanceled(message -> message.delete().queue())
                .build();
    }

    private Menu buildRaceDialog() {
        return new SelectionDialog.Builder()
                .setEventWaiter(waiter)
                .setUsers(users.toArray(new User[0]))
                .setRoles(roles.toArray(new Role[0]))
                .setText("Please select your player's Race")
                .setChoices(races)
                .setDefaultEnds(BUTTON, "")
                .setSelectedEnds(SELECT, "")
                .useLooping(true)
                .setSelectionConsumer((message, selection) -> {
                    builder.setRace(races[selection - 1]);
                    display(message);
                })
                .setCanceled(message -> message.delete().queue())
                .build();
    }

    private Menu buildGenderEntryDialog(final Message message) {
        return new TextEntryDialog.Builder()
                .setEventWaiter(waiter)
                .setUsers(users.toArray(new User[0]))
                .setRoles(roles.toArray(new Role[0]))
                .setDescription("Please enter your player's gender")
                .setText("Gender")
                .setReactAction((response, reaction) -> {
                    builder.setGender(((TextEntryDialog) currentMenu).getValue());
                    display(message);
                })
                .setFinalAction(response -> {
                    message.delete().queue();
                    reset();
                }).build();
    }

    public static class Builder extends WizardDialog.Builder<Builder, PlayerCreationWizard> {

        private PlayerBuilder playerBuilder;
        private PlayerService service;

        public Builder setService(final PlayerService service) {
            this.service = service;
            return this;
        }

        public Builder setPlayerBuilder(final PlayerBuilder playerBuilder) {
            this.playerBuilder = playerBuilder;
            return this;
        }

        @Override
        public PlayerCreationWizard build() {
            notNull(playerBuilder, "A PlayerBuilder is required.");
            notNull(service, "A PlayerService is required.");
            return new PlayerCreationWizard(waiter, users, roles, timeout, unit, playerBuilder, service);
        }
    }
}
