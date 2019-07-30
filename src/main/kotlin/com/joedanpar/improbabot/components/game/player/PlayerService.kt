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
package com.joedanpar.improbabot.components.game.player

import com.joedanpar.improbabot.components.common.GenericService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.persistence.EntityExistsException
import javax.persistence.TransactionRequiredException

@Service
class PlayerService @Autowired constructor(
        private val repository: PlayerRepository
) : GenericService<PlayerRepository, Player>(repository), Logging {

    fun findByServerAndUser(serverId: String, userId: String): Player {
        logger.debug("Getting player for $serverId $userId")
        return repository.findByServerIdAndUserId(serverId, userId)
    }

    fun findByUser(userId: String): List<Player> {
        logger.debug("Getting players for $userId")
        return repository.findByUserId(userId)
    }

    fun delete(serverId: String, userId: String) {
        logger.debug("Removing player for $serverId $userId")
        repository.deleteByServerIdAndUserId(serverId, userId)
    }

    fun createObject(serverId: String, userId: String, name: String, gender: String): Boolean {
        return createObject(Player.Builder()
                .setServerId(serverId)
                .setUserId(userId)
                .setName(name)
                .setGender(gender))
    }

    private fun createObject(builder: Player.Builder): Boolean {
        return createObject(builder.build())
    }

    private fun createObject(player: Player): Boolean {
        try {
            repository.save(player)
        } catch (e: EntityExistsException) {
            logger.error("Failed to add player \"${player.name}\" for server ${player.serverId}", e)
            return false
        } catch (e: TransactionRequiredException) {
            logger.error("Failed to add player \"${player.name}\" for server ${player.serverId}", e)
            return false
        }

        return true
    }
}
