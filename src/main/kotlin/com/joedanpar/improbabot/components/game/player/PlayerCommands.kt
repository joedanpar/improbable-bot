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
package com.joedanpar.improbabot.components.game.player

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.Command.Category
import com.jagrosh.jdautilities.command.CommandBuilder
import com.jagrosh.jdautilities.command.CommandEvent
import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import com.joedanpar.improbabot.components.common.UserHelper
import net.dv8tion.jda.core.entities.ChannelType.PRIVATE
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.utils.Checks.check
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.*

@Component
class PlayerCommands @Autowired
constructor(private val waiter: EventWaiter, private val service: PlayerService) {

    private val category = Category("Game")
    private val builders = HashMap<Member, PlayerBuilder>()

    @Bean
    @Qualifier("rootCommand")
    fun PlayerCommand(@Qualifier("playerCommand") commands: List<Command>): Command {
        return CommandBuilder()
                .setCategory(category)
                .setName("player")
                .setArguments("{create|remove|list}")
                .setHelp("Used in the manipulation of your Player Character.")
                .setChildren(commands)
                .setHelpBiConsumer { event, command ->
                    val sb = StringBuilder("Help for **${command.name}**:\n")

                    for (child in command.children) {
                        sb.append("`${event.client.prefix}${child.name} ${child.arguments}` - ${child.help}\n")
                    }

                    event.replyInDm(sb.toString())
                    event.reactSuccess()
                }
                .build { command, event -> }
    }

    @Bean
    @Qualifier("playerCommand")
    fun createPlayerCommand(): Command {
        return CommandBuilder()
                .setCategory(category)
                .setName("create")
                .setHelp("Starts the Player Creation Wizard if no arguments are given, " +
                        "otherwise simply creates a new player with the given arguments.")
                .setArguments("name gender race")
                .build { command, event -> createPlayer(event) }
    }

    private fun createPlayer(event: CommandEvent) {
        builders[event.member] = PlayerBuilder()
        val builder = builders[event.member]
        val args = event.args.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        if (args.isEmpty()) {
            PlayerCreationWizard.Builder()
                    .setEventWaiter(waiter)
                    .setUsers(event.author)
                    .setService(service)
                    .setPlayerBuilder(builder!!)
                    .setEmbedColor(UserHelper.getAverageColor(event.author.avatarUrl))
                    .build()
                    .display(event.channel)
        } else {
            check(args.size >= 3, "Invalid number of arguments.")
            builder!!.setServerId(event.guild.id)
                    .setUserId(event.author.id)
                    .setName(args[0])
                    .setGender(args[1])
                    .setRace(args[2])
        }
    }

    @Bean
    @Qualifier("playerCommand")
    fun removePlayerCommand(): Command {
        return CommandBuilder()
                .setCategory(category)
                .setName("remove")
                .setHelp("Removes the given Player.")
                .build { command, event -> removePlayer(event) }
    }

    private fun removePlayer(event: CommandEvent) {
        TODO()
    }

    @Bean
    @Qualifier("playerCommand")
    fun listPlayersCommand(): Command {
        return CommandBuilder()
                .setCategory(category)
                .setName("list")
                .setHelp("Lists all Players on the current server.  " +
                        "When this command is run from a DM with Improbabot, " +
                        "it will instead list all Players you have across all servers.")
                .setGuildOnly(false)
                .build { command, event -> listPlayers(event) }
    }

    private fun listPlayers(event: CommandEvent) {
        for (player in if (PRIVATE == event.channelType) {
            service.getObjectsByUser(event.author.id)
        } else
            service.getAllObjectsByServerId(event.guild.id)) {
            event.reply(player.render())
        }
    }
}
