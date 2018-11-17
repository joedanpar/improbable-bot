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

import com.joedanpar.improbabot.components.common.GenericService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.TransactionRequiredException;

@Log4j2
@Service
public class PlayerService extends GenericService<PlayerDao, Player> {

    @Autowired
    public PlayerService(final PlayerDao dao) {
        super(dao);
    }

    @Transactional
    public Player getObject(final String serverId, final String name) {
        return getDao().getPlayerByName(serverId, name);
    }

    @Transactional
    public void removeObject(final String serverId, final String name) {
        getDao().removeObjectByName(serverId, name);
    }

    @Transactional
    public boolean createObject(final String serverId, final String name) {
        return createObject(new PlayerBuilder()
                                    .setServerId(serverId)
                                    .setName(name)
                                    .build());
    }

    @Transactional
    boolean createObject(final Player player) {
        try {
            saveObject(player);
        } catch (EntityExistsException | TransactionRequiredException e) {
            log.error("Failed to add player \"{}\" for server {}", player.getName(), player.getServerId());
            log.error(e);
            return false;
        }
        return true;
    }
}
