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

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.Command.Category;
import com.jagrosh.jdautilities.command.CommandBuilder;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.joedanpar.improbabot.components.common.MessageHelper;
import com.joedanpar.improbabot.components.game.command.PlayerCreationWizard;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.utils.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class PlayerCommands {

    private final Category                              category = new Category("Game");
    private final Map<Pair<Guild, User>, PlayerBuilder> builders = new HashMap<>();
    private       PlayerService                         service;
    private       PlayerCreationWizard                  wizard;
    private       MessageHelper                         messageHelper;

    @Autowired
    public PlayerCommands(final PlayerService service, final PlayerCreationWizard wizard,
                          final MessageHelper messageHelper) {
        this.service = service;
        this.wizard = wizard;
        this.messageHelper = messageHelper;
    }

    @Bean
    public Command PlayerCommand() {
        return new CommandBuilder()
                .setCategory(category)
                .setName("player")
                .setChildren(createPlayerCommand(), removePlayerCommand(), listPlayersCommand())
                .build((command, event) -> {});
    }

    private Command createPlayerCommand() {
        return new CommandBuilder()
                .setCategory(category)
                .setName("create")
                .build((command, event) -> createPlayer(event));
    }

    private Command removePlayerCommand() {
        return new CommandBuilder()
                .setCategory(category)
                .setName("remove")
                .build((command, event) -> removePlayer(event));
    }

    private Command listPlayersCommand() {
        return new CommandBuilder()
                .setCategory(category)
                .setName("list")
                .build((command, event) -> listPlayers(event));
    }

    private void createPlayer(final CommandEvent event) {
        wizard.execute(event);
    }

    private void removePlayer(final CommandEvent event) {
        // TODO
    }

    private void listPlayers(final CommandEvent event) {
        // TODO
    }
}
