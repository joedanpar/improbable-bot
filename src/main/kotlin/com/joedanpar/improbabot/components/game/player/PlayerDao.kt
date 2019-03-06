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

import com.joedanpar.improbabot.components.common.GenericDao
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Repository
@Transactional
open class PlayerDao(@Autowired entityManager: EntityManager) : GenericDao<Player>(entityManager, Player::class.java), Logging {

    internal fun removePlayerByUser(serverId: String, userId: String) {
        logger.debug("Removing player for $serverId $userId")
        removeObject(getPlayerByUser(serverId, userId))
    }

    internal fun getPlayerByUser(serverId: String, userId: String): Player {
        logger.debug("Getting player for $serverId $userId")
        val query = entityManager.createQuery("FROM Player where serverId = :serverId and userId = :userId", type)
        query.setParameter("serverId", serverId)
        query.setParameter("userId", userId)
        return query.singleResult
    }

    internal fun getPlayersByUser(userId: String): List<Player> {
        logger.debug("Getting players for $userId")
        val query = entityManager.createQuery("FROM Player where userId = :userId", type)
        query.setParameter("userId", userId)
        return query.resultList
    }
}
