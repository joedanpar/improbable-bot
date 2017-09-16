/*******************************************************************************
 * This file is part of Improbable Bot.
 *
 *    Improbable Bot is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Improbable Bot is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with Improbable Bot.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.joedanpar.improbabot;

import com.joedanpar.improbabot.listeners.CommandListener;
import com.joedanpar.improbabot.listeners.ReactionListener;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.flywaydb.core.Flyway;

import javax.security.auth.login.LoginException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.joedanpar.improbabot.util.Reference.BOT_PROPERTIES;
import static com.joedanpar.improbabot.util.Reference.SQLITE_DB;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Thread.currentThread;
import static net.dv8tion.jda.core.AccountType.BOT;

@Log4j2
@UtilityClass
public class Improbabot {
    @Getter
    JDA jda;

    @Getter
    private boolean debug;

    public static void main(final String[] args) throws IOException, InterruptedException {
        val properties = initProperties();

        try {
            jda = new JDABuilder(BOT).setToken(properties.getProperty("botToken")).buildBlocking();
        } catch (LoginException e) {
            log.error("Failed to login", e);
        } catch (RateLimitedException e) {
            log.warn("Rate limit exceeded", e);
        }

        assert jda != null;

        debug = parseBoolean(properties.getProperty("debugEnabled"));
        jda.addEventListener(new CommandListener(debug));
        jda.addEventListener(new ReactionListener());

//        migrateDb();
    }

    private static Properties initProperties() throws IOException {
        val properties = new Properties();

        val classLoader = currentThread().getContextClassLoader();

        InputStream inputStream = classLoader.getResourceAsStream(BOT_PROPERTIES);
        if (inputStream == null) {
            log.warn("Unable to load properties from classpath, retrying from current directory.");
            try {
                inputStream = new FileInputStream(BOT_PROPERTIES);
            } catch (FileNotFoundException e) {
                log.error("Could not find properties file.", e);
                throw e;
            }
        }

        try {
            properties.load(inputStream);
            log.info("Properties file loaded.");
        } catch (IOException e) {
            log.error("Unable to load properties file.", e);
            throw e;
        }

        return properties;
    }

    private static void migrateDb() {
        val flyway = new Flyway();

        flyway.setDataSource(SQLITE_DB, null, null);
        flyway.migrate();
    }
}
