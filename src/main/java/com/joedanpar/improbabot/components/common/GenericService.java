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
package com.joedanpar.improbabot.components.common;

import lombok.Getter;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class GenericService<D extends GenericDao<T>, T> {

    @Getter
    private D dao;

    public GenericService(final D dao) {
        this.dao = dao;
    }

    @Transactional
    public List<T> getAllObjectsByServerId(final String serverId) {
        return dao.getAllObjectsByServerId(serverId);
    }

    @Transactional
    public List<T> getAllObjects() {
        return dao.getAllObjects();
    }

    @Transactional
    public void saveObject(final T obj) {
        dao.saveObject(obj);
    }

    @Transactional
    public void removeObject(final T obj) {
        dao.removeObject(obj);
    }
}
