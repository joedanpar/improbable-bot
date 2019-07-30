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
import javax.persistence.EntityExistsException
import javax.persistence.TransactionRequiredException

@Service
class ConfigService(private val repository: ConfigRepository) : GenericService<ConfigRepository, Config>(repository), Logging {

    fun findByServerId(serverId: String): List<Config> {
        return repository.findByServerId(serverId)
    }

    fun getConfig(serverId: String, name: String): List<Config> {
        logger.debug("Getting configs for $serverId $name")
        return repository.findByServerIdAndName(serverId, name)
    }

    fun getValueByName(serverId: String, configName: String): String? {
        return getValuesByName(serverId, configName)[0]
    }

    fun getValuesByName(serverId: String, name: String): List<String> {
        logger.debug("Getting config values for $serverId $name")
        return repository.findByServerIdAndName(serverId, name).map { config -> config.value }
    }

    fun removeConfig(config: Config) {
        repository.delete(config)
    }

    fun removeConfig(serverId: String, name: String, value: String) {
        repository.delete(Config.Builder().setServerId(serverId).setName(name).setValue(value).build())
    }

    fun createObject(serverId: String, name: String, value: String): Boolean {
        return createObject(Config.Builder().setServerId(serverId).setName(name).setValue(value).build())
    }

    private fun createObject(config: Config): Boolean {
        try {
            repository.save(config)
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
