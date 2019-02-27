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
package com.joedanpar.improbabot.beans

import com.jagrosh.jdautilities.command.*
import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import com.joedanpar.improbabot.Constants.PREFIX
import com.joedanpar.improbabot.components.common.Emojis.CHECK_MARK
import com.joedanpar.improbabot.components.common.Emojis.CROSS_X
import com.joedanpar.improbabot.components.common.Emojis.QUESTION_MARK
import com.nincodedo.recast.RecastAPI
import com.nincodedo.recast.RecastAPIBuilder
import net.dv8tion.jda.core.AccountType.BOT
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.OnlineStatus.DO_NOT_DISTURB
import net.dv8tion.jda.core.entities.Game.playing
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.utils.SessionControllerAdapter
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import java.lang.System.gc
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors.newCachedThreadPool
import javax.security.auth.login.LoginException

@Configuration
@ComponentScan("com.joedanpar.improbabot")
class ApplicationBean : Logging {

    @Value("\${debugEnabled}")
    val isDebug: Boolean = false

    @Value("\${ownerId}")
    val ownerId: String? = null

    @Value("\${botToken}")
    private val botToken: String? = null

    @Value("\${serverInvite}")
    private val serverInvite: String? = null

    @Value("\${recastToken}")
    private val recastToken: String? = null

    @Value("\${shardToken:}")
    private val shardToken: String? = null

    @Value("\${totalShards:-1}")
    private val totalShards: Int = 0

    @Bean
    @Autowired
    @Throws(InterruptedException::class)
    fun jda(waiter: EventWaiter, client: CommandClient): JDA {
        var jda: JDA? = null

        try {
            jda = JDABuilder(BOT)
                    .setToken(botToken)
                    .setStatus(DO_NOT_DISTURB)
                    .setGame(playing("Loading..."))
                    .addEventListener(waiter, client)
                    .build()
        } catch (e: LoginException) {
            logger.error("Failed to login", e)
        }

        jda!!.awaitReady()
        return jda
    }

    @Bean
    fun recastAPI(): RecastAPI {
        return RecastAPIBuilder(recastToken).build()
    }

    @Bean
    fun eventWaiter(): EventWaiter {
        return EventWaiter()
    }

    @Bean
    fun guildSettingsManager(): GuildSettingsManager<*> {
        return object : GuildSettingsManager<Any> {
            override fun getSettings(guild: Guild): Any? {
                return null
            }
        }
    }

    @Bean
    fun threadpool(): ExecutorService {
        return newCachedThreadPool()
    }

    @Bean
    fun sessionControllerAdapter(): SessionControllerAdapter {
        return object : SessionControllerAdapter() {
            override fun runWorker() {
                if (workerHandle == null) {
                    workerHandle = QueueWorker(20)
                    gc()
                    workerHandle.start()
                }
            }
        }
    }

    @Bean
    fun commandListener(): CommandListener {
        return object : CommandListener {

            override fun onCommand(event: CommandEvent?, command: Command?) {
                // TODO
            }

            override fun onCompletedCommand(event: CommandEvent?, command: Command?) {
                event!!.reactSuccess()
            }

            override fun onTerminatedCommand(event: CommandEvent?, command: Command?) {
                // TODO
            }

            override fun onNonCommandMessage(event: MessageReceivedEvent?) {
                // TODO
            }

            override fun onCommandException(event: CommandEvent?, command: Command?, throwable: Throwable) {
                logger.error(throwable)
                event!!.reactError()
            }
        }
    }

    @Bean
    @Autowired
    fun commandClient(@Qualifier("rootCommand") commands: List<Command>,
                      settingsManager: GuildSettingsManager<*>,
                      listener: CommandListener): CommandClient {
        return CommandClientBuilder()
                .setPrefix(PREFIX)
                .setGame(playing("In Development"))
                .setOwnerId(ownerId)
                .setServerInvite(serverInvite)
                .setEmojis(CHECK_MARK, QUESTION_MARK, CROSS_X)
                .setLinkedCacheSize(0)
                .setGuildSettingsManager(settingsManager)
                .setListener(listener)
                .setShutdownAutomatically(false)
                .addCommands(*commands.toTypedArray())
                //                .setHelpConsumer(event -> event.replyInDm(event, this), m -> {})
                .setDiscordBotsKey(botToken)
                .build()
    }
}
