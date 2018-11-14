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
package com.joedanpar.improbabot.components.config;

import com.joedanpar.improbabot.components.common.GenericDao;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.TransactionRequiredException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Log4j2
@Repository
@Transactional
public class ConfigDao extends GenericDao<Config> {
    private static final String CONFIG_NAME = "configName";
    private static final String SERVER_ID   = "serverId";

    @Autowired
    public ConfigDao(final SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    List<String> getValuesByName(final String serverId, final String configName) {
        log.debug("Getting config values for {} {}", serverId, configName);
        try (val session = sessionFactory.openSession()) {
            val query = session.createQuery("FROM Config where serverId = :serverId and key = :configName",
                                            Config.class);
            query.setParameter(SERVER_ID, serverId);
            query.setParameter(CONFIG_NAME, configName);
            return query.getResultList().stream().map(Config::getValue).collect(toList());
        }
    }

    void removeConfig(final String serverId, final String configName, final String configValue) {
        try (val session = sessionFactory.openSession()) {
            session.beginTransaction();
            val list = getConfigByName(serverId, configName);
            list.stream()
                .filter(config -> config.getValue().equals(configValue))
                .forEach(config -> session.delete(session.contains(config)
                                                          ? config
                                                          : session.merge(config)));
            session.getTransaction().commit();
        }
    }

    List<Config> getConfigByName(final String serverId, final String configName) {
        log.debug("Getting configs for {} {}", serverId, configName);
        try (val session = sessionFactory.openSession()) {
            val query = session.createQuery("FROM Config where serverId = :serverId and key = :configName",
                                            Config.class);
            query.setParameter(SERVER_ID, serverId);
            query.setParameter(CONFIG_NAME, configName);
            return query.getResultList();
        }
    }

    boolean addConfig(final String serverId, final String configName, final String configValue) {
        return addConfig(new Config(serverId, configName, configValue));
    }

    boolean addConfig(final Config config) {
        try (val session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(config);
            session.getTransaction().commit();
        } catch (EntityExistsException | TransactionRequiredException e) {
            log.error("Failed to set config {} with value {} for server {}", config.getKey(), config.getValue(),
                      config.getServerId());
            log.error(e);
            return false;
        }
        return true;
    }

    void removeConfig(final Config config) {
        try (val session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(session.contains(config)
                                   ? config
                                   : session.merge(config));
            session.getTransaction().commit();
        }
    }
}
