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
package com.joedanpar.improbabot.components.game.player;

import com.joedanpar.improbabot.components.common.GenericDao;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Log4j2
@Repository
@Transactional
public class PlayerDao extends GenericDao<Player> {

    @Autowired
    public PlayerDao(final EntityManager entityManager) {
        super(entityManager);
    }

    void removePlayerByUser(final String serverId, final String userId) {
        log.debug("Removing player for {} {}", serverId, userId);
        removeObject(getPlayerByUser(serverId, userId));
    }

    Player getPlayerByUser(final String serverId, final String userId) {
        log.debug("Getting player for {} {}", serverId, userId);
        val query = entityManager.createQuery("FROM Player where serverId = :serverId and userId = :userId", getType());
        query.setParameter("serverId", serverId);
        query.setParameter("userId", userId);
        return query.getSingleResult();
    }

    List<Player> getPlayersByUser(final String userId) {
        log.debug("Getting players for {}", userId);
        val query = entityManager.createQuery("FROM Player where userId = :userId", getType());
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}
