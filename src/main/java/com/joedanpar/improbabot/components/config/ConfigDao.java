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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Log4j2
@Repository
@Transactional
public class ConfigDao extends GenericDao<Config> {

    @Autowired
    public ConfigDao(final EntityManager entityManager) {
        super(entityManager);
    }

    List<String> getValuesByName(final String serverId, final String configName) {
        log.debug("Getting config values for {} {}", serverId, configName);
        return getAllObjectsByName(serverId, configName)
                .stream()
                .map(Config::getValue)
                .collect(toList());
    }

    List<Config> getAllObjectsByName(final String serverId, final String configName) {
        log.debug("Getting configs for {} {}", serverId, configName);
        val query = entityManager.createQuery("FROM Config where serverId = :serverId and name = :configName", getType());
        query.setParameter("serverId", serverId);
        query.setParameter("configName", configName);
        return query.getResultList();
    }

    void removeObject(final String serverId, final String configName, final String configValue) {
        removeObject(new ConfigBuilder().setServerId(serverId).setName(configName).setValue(configValue).build());
    }
}
