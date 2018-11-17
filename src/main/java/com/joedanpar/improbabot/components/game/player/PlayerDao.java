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
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Repository
@Transactional
public class PlayerDao extends GenericDao<Player> {

    @Autowired
    public PlayerDao(final SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    void removeObjectByName(final String serverId, final String playerName) {
        log.debug("Removing player \"{}\" for {}", playerName, serverId);
        removeObject(getPlayerByName(serverId, playerName));
    }

    Player getPlayerByName(final String serverId, final String playerName) {
        log.debug("Getting player \"{}\" for {} {}", playerName, serverId);
        try (val session = sessionFactory.openSession()) {
            val query = session.createQuery("FROM Config where serverId = :serverId and name = :playerName", getType());
            query.setParameter("serverId", serverId);
            query.setParameter("playerName", playerName);
            return query.getSingleResult();
        }
    }
}
