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
package com.joedanpar.improbabot.components.config

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.Command.Category
import com.jagrosh.jdautilities.command.CommandBuilder
import com.jagrosh.jdautilities.command.CommandEvent
import com.joedanpar.improbabot.components.common.MessageBuilderHelper
import com.joedanpar.improbabot.components.common.MessageHelper
import net.dv8tion.jda.core.utils.Checks.check
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class ConfigCommands @Autowired
constructor(private val service: ConfigService, private val messageHelper: MessageHelper) {

    private val category = Category("Admin")

    @Bean
    @Qualifier("rootCommand")
    fun configCommand(@Qualifier("configCommand") commands: List<Command>): Command = CommandBuilder()
            .setCategory(category)
            .setName("config")
            .setRequiredRole("admin")
            .setArguments("{add|remove|list}")
            .setHelp("Used in the configuration of Improbabot on a given server.")
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

    @Bean
    @Qualifier("configCommand")
    fun addConfigCommand(): Command = CommandBuilder()
            .setCategory(category)
            .setName("add")
            .setRequiredRole("admin")
            .setArguments("configKey configValue")
            .build { command, event -> addConfig(event) }

    private fun addConfig(event: CommandEvent) {
        val args = event.args.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        check(args.size == 2, "Incorrect amount of arguments! Expected 2, Gave ${args.size}")
        ConfigBuilder().setServerId(event.guild.id)
                .setName(args[0])
                .setValue(args[1])
                .build()
    }

    @Bean
    @Qualifier("configCommand")
    fun removeConfigCommand(): Command = CommandBuilder()
            .setCategory(category)
            .setName("remove")
            .setRequiredRole("admin")
            .build { command, event -> removeConfig(event) }

    private fun removeConfig(event: CommandEvent) {
        val args = event.args.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        check(args.size == 2, "Incorrect amount of arguments! Expected 2, Gave ${args.size}")
        service.removeObject(ConfigBuilder().setServerId(event.guild.id)
                .setName(args[0])
                .setValue(args[1])
                .build())
    }

    @Bean
    @Qualifier("configCommand")
    fun listConfigsCommand(): Command = CommandBuilder()
            .setCategory(category)
            .setName("list")
            .setRequiredRole("admin")
            .build { command, event -> listConfigs(event) }

    private fun listConfigs(event: CommandEvent) {
        val configsByServerId = service.getAllObjectsByServerId(event.guild.id)
        val serverName = event.guild.name

        if (configsByServerId.isEmpty()) {
            messageHelper.sendMessage(event.channel, "No configs found for server $serverName")
            return
        }

        val msgBuilder = MessageBuilderHelper()
        msgBuilder.setTitle("Configs for $serverName")

        for (config in configsByServerId) {
            msgBuilder.addField(config.name, config.value, false)
        }

        messageHelper.sendMessage(event.channel, msgBuilder.build())
    }
}
