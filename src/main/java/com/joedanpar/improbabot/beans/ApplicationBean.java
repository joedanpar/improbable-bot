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
package com.joedanpar.improbabot.beans;

import com.jagrosh.jdautilities.command.*;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.nincodedo.recast.RecastAPI;
import com.nincodedo.recast.RecastAPIBuilder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.utils.SessionControllerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nullable;
import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static com.joedanpar.improbabot.Constants.PREFIX;
import static com.joedanpar.improbabot.components.common.Emojis.*;
import static java.lang.System.gc;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static net.dv8tion.jda.core.AccountType.BOT;
import static net.dv8tion.jda.core.OnlineStatus.DO_NOT_DISTURB;
import static net.dv8tion.jda.core.entities.Game.playing;

@Configuration
@ComponentScan({"com.joedanpar.improbabot"})
@Log4j2
public class ApplicationBean {

    @Getter
    @Value("${debugEnabled}")
    private boolean debug;

    @Getter
    @Value("${ownerId}")
    private String ownerId;

    @Value("${botToken}")
    private String botToken;

    @Value("${serverInvite}")
    private String serverInvite;

    @Value("${recastToken}")
    private String recastToken;

    @Value("${shardToken:}")
    private String shardToken;

    @Value("${totalShards:-1}")
    private int totalShards;

    @Bean
    @Autowired
    public JDA jda(final EventWaiter waiter, final CommandClient client) throws InterruptedException {
        JDA jda = null;

        try {
            jda = new JDABuilder(BOT)
                    .setToken(botToken)
                    .setStatus(DO_NOT_DISTURB)
                    .setGame(playing("Loading..."))
                    .addEventListener(waiter, client)
                    .build();
        } catch (LoginException e) {
            log.error("Failed to login", e);
        }

        assert jda != null;
        jda.awaitReady();
        return jda;
    }

    @Bean
    public RecastAPI recastAPI() {
        return new RecastAPIBuilder(recastToken).build();
    }

    @Bean
    public EventWaiter eventWaiter() {
        return new EventWaiter();
    }

    @Bean
    public GuildSettingsManager guildSettingsManager() {
        return new GuildSettingsManager() {
            @Nullable
            @Override
            public Object getSettings(final Guild guild) {
                return null;
            }
        };
    }

    @Bean
    public ExecutorService threadpool() {
        return newCachedThreadPool();
    }

    @Bean
    public SessionControllerAdapter sessionControllerAdapter() {
        return new SessionControllerAdapter() {
            @Override
            protected void runWorker() {
                if (workerHandle == null) {
                    workerHandle = new SessionControllerAdapter.QueueWorker(20);
                    gc();
                    workerHandle.start();
                }
            }
        };
    }

    @Bean
    public CommandListener commandListener() {
        return new CommandListener() {

            @Override
            public void onCommand(final CommandEvent event, final Command command) {
                // TODO
            }

            @Override
            public void onCompletedCommand(final CommandEvent event, final Command command) {
                event.reactSuccess();
            }

            @Override
            public void onTerminatedCommand(final CommandEvent event, final Command command) {
                // TODO
            }

            @Override
            public void onNonCommandMessage(final MessageReceivedEvent event) {
                // TODO
            }

            @Override
            public void onCommandException(final CommandEvent event, final Command command, final Throwable throwable) {
                log.error(throwable);
                event.reactError();
            }
        };
    }

    @Bean
    @Autowired
    public CommandClient commandClient(@Qualifier("rootCommand") final List<Command> commands,
                                       final GuildSettingsManager settingsManager,
                                       final CommandListener listener) {
        return new CommandClientBuilder()
                .setPrefix(PREFIX)
                .setGame(playing("In Development"))
                .setOwnerId(ownerId)
                .setServerInvite(serverInvite)
                .setEmojis(CHECK_MARK, QUESTION_MARK, CROSS_X)
                .setLinkedCacheSize(0)
                .setGuildSettingsManager(settingsManager)
                .setListener(listener)
                .setShutdownAutomatically(false)
                .addCommands(commands.toArray(new Command[0]))
//                .setHelpConsumer(event -> event.replyInDm(event, this), m -> {})
                .setDiscordBotsKey(botToken)
                .build();
    }
}
