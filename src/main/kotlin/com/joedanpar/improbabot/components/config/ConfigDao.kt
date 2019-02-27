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

import com.joedanpar.improbabot.components.common.GenericDao
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Repository
@Transactional
class ConfigDao @Autowired
constructor(entityManager: EntityManager) : GenericDao<Config>(entityManager, Config::class.java), Logging {

    fun getValuesByName(serverId: String, configName: String): List<String> {
        logger.debug("Getting config values for $serverId $configName")
        return getAllObjectsByName(serverId, configName).map { config -> config.value }
    }

    fun getAllObjectsByName(serverId: String, configName: String): List<Config> {
        logger.debug("Getting configs for $serverId $configName")
        val query = entityManager.createQuery("FROM Config where serverId = :serverId and name = :configName", type)
        query.setParameter("serverId", serverId)
        query.setParameter("configName", configName)
        return query.resultList
    }

    fun removeObject(serverId: String, configName: String, configValue: String) {
        removeObject(ConfigBuilder().setServerId(serverId).setName(configName).setValue(configValue).build())
    }
}
