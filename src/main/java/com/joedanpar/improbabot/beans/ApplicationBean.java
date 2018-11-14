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

import com.nincodedo.recast.RecastAPI;
import com.nincodedo.recast.RecastAPIBuilder;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;
import java.util.List;

@Configuration
@ComponentScan(basePackages = {"com.joedanpar.improbabot"})
@Log4j2
public class ApplicationBean {

    @Value("${botToken}")
    private String botToken;

    @Value("${recastToken}")
    private String recastToken;

    @Autowired
    @Bean
    public JDA jda(final List<ListenerAdapter> listenerAdapters) throws InterruptedException {
        JDA jda = null;

        try {
            jda = new JDABuilder(AccountType.BOT).setToken(botToken).build();
        } catch (LoginException e) {
            log.error("Failed to login", e);
        }

        assert jda != null;
        jda.awaitReady();
        jda.addEventListener(listenerAdapters.toArray());
        return jda;
    }

    @Bean
    public RecastAPI recastAPI() {
        return new RecastAPIBuilder(recastToken).build();
    }
}
