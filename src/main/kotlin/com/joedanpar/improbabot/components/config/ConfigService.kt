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

import com.joedanpar.improbabot.components.common.GenericService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.EntityExistsException
import javax.persistence.TransactionRequiredException

@Service
class ConfigService(dao: ConfigDao) : GenericService<ConfigDao, Config>(dao), Logging {

    fun getConfig(serverId: String, name: String): List<Config> {
        return dao.getAllObjectsByName(serverId, name)
    }

    fun getValueByName(serverId: String, configName: String): Optional<String> {
        return Optional.ofNullable(getValuesByName(serverId, configName)[0])
    }

    private fun getValuesByName(serverId: String, configName: String): List<String> {
        return dao.getValuesByName(serverId, configName)
    }

    fun removeConfig(config: Config) {
        dao.removeObject(config)
    }

    fun removeConfig(serverId: String, configName: String, configValue: String) {
        dao.removeObject(serverId, configName, configValue)
    }

    fun createObject(serverId: String, configName: String, configValue: String): Boolean {
        return createObject(ConfigBuilder().setServerId(serverId).setName(configName).setValue(configValue).build())
    }

    private fun createObject(config: Config): Boolean {
        try {
            dao.saveObject(config)
        } catch (e: EntityExistsException) {
            logger.error("Failed to create config ${config.name} with value ${config.value} for server ${config.serverId}", e)
            return false
        } catch (e: TransactionRequiredException) {
            logger.error("Failed to create config ${config.name} with value ${config.value} for server ${config.serverId}", e)
            return false
        }

        return true
    }
}
