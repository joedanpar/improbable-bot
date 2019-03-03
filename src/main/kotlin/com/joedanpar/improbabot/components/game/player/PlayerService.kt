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
class PlayerService @Autowired
constructor(dao: PlayerDao) : GenericService<PlayerDao, Player>(dao), Logging {

    fun getObjectByUser(serverId: String, userId: String): Player {
        return dao.getPlayerByUser(serverId, userId)
    }

    fun getObjectsByUser(userId: String): List<Player> {
        return dao.getPlayersByUser(userId)
    }

    fun removeObject(serverId: String, userId: String) {
        dao.removePlayerByUser(serverId, userId)
    }

    fun createObject(serverId: String, userId: String, name: String, gender: String): Boolean {
        return createObject(PlayerBuilder()
                .setServerId(serverId)
                .setUserId(userId)
                .setName(name)
                .setGender(gender))
    }

    private fun createObject(builder: PlayerBuilder): Boolean {
        return createObject(builder.build())
    }

    private fun createObject(player: Player): Boolean {
        try {
            saveObject(player)
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
