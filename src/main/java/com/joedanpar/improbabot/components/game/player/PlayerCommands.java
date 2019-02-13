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
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.dv8tion.jda.core.entities.ChannelType.PRIVATE;
import static net.dv8tion.jda.core.utils.Checks.check;

@Log4j2
@Component
public class PlayerCommands {

    private final Category                   category = new Category("Game");
    private final Map<Member, PlayerBuilder> builders = new HashMap<>();

    private final EventWaiter                waiter;
    private final PlayerService              service;

    @Autowired
    public PlayerCommands(final EventWaiter waiter, final PlayerService service) {
        this.waiter = waiter;
        this.service = service;
    }

    @Bean
    @Qualifier("rootCommand")
    public Command PlayerCommand(@Qualifier("playerCommand") List<Command> commands) {
        return new CommandBuilder()
                .setCategory(category)
                .setName("player")
                .setArguments("{create|remove|list}")
                .setHelp("Used in the manipulation of your Player Character.")
                .setChildren(commands)
                .setHelpBiConsumer(((event, command) -> {
                    val sb = new StringBuilder("Help for **").append(command.getName()).append("**:\n");

                    for (val child : command.getChildren()) {
                        sb.append("`")
                          .append(event.getClient().getPrefix())
                          .append(child.getName())
                          .append(" ")
                          .append(child.getArguments())
                          .append("` - ")
                          .append(child.getHelp())
                          .append("\n");
                    }

                    event.replyInDm(sb.toString());
                    event.reactSuccess();
                }))
                .build((command, event) -> {});
    }

    @Bean
    @Qualifier("playerCommand")
    private Command createPlayerCommand() {
        return new CommandBuilder()
                .setCategory(category)
                .setName("create")
                .setHelp("Starts the Player Creation Wizard if no arguments are given, " +
                         "otherwise simply creates a new player with the given arguments.")
                .setArguments("name gender race")
                .build((command, event) -> createPlayer(event));
    }

    private void createPlayer(final CommandEvent event) {
        builders.put(event.getMember(), new PlayerBuilder());
        val builder = builders.get(event.getMember());
        val args    = event.getArgs().split("\\s+");

        if (args.length == 1 && args[0].isEmpty()) {
            new PlayerCreationWizard.Builder()
                    .setEventWaiter(waiter)
                    .setUsers(event.getAuthor())
                    .setService(service)
                    .setPlayerBuilder(builder)
                    .build()
                    .display(event.getChannel());
        } else {
            check(args.length >= 3, "Invalid number of arguments.");
            builder.setServerId(event.getGuild().getId())
                   .setUserId(event.getAuthor().getId())
                   .setName(args[0])
                   .setGender(args[1])
                   .setRace(args[2]);
        }
    }

    @Bean
    @Qualifier("playerCommand")
    private Command removePlayerCommand() {
        return new CommandBuilder()
                .setCategory(category)
                .setName("remove")
                .setHelp("Removes the given Player.")
                .build((command, event) -> removePlayer(event));
    }

    private void removePlayer(final CommandEvent event) {
        // TODO
    }

    @Bean
    @Qualifier("playerCommand")
    private Command listPlayersCommand() {
        return new CommandBuilder()
                .setCategory(category)
                .setName("list")
                .setHelp("Lists all Players on the current server." +
                         "When this command is run from a DM with Improbabot, " +
                         "it will instead list all Players you have across all servers.")
                .setGuildOnly(false)
                .build((command, event) -> listPlayers(event));
    }

    private void listPlayers(final CommandEvent event) {
        for (val player : PRIVATE.equals(event.getChannelType())
                ? service.getObjectsByUser(event.getAuthor().getId())
                : service.getAllObjectsByServerId(event.getGuild().getId())) {
            event.reply(renderPlayer(player));
        }
    }

    private MessageEmbed renderPlayer(final Player player) {
        return new EmbedBuilder().setTitle(player.getName())
                                 .addField("Gender", player.getGender(), false)
                                 .build();
    }
}
