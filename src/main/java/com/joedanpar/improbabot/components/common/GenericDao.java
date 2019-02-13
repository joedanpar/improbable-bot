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
import lombok.val;
import org.springframework.core.GenericTypeResolver;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Transactional
public class GenericDao<T> {

    @Getter
    private final Class<T>      type;
    @Getter(PROTECTED)
    protected     EntityManager entityManager;

    public GenericDao(final EntityManager sessionFactory) {
        this.entityManager = sessionFactory;
        this.type = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), GenericDao.class);
    }

    public List<T> getAllObjectsByServerId(final String serverId) {
        val query = entityManager.createQuery("FROM " + type.getName() + " where serverId = :serverId", type);
        query.setParameter("serverId", serverId);

        return query.getResultList();
    }

    public void saveObject(final T obj) {
        entityManager.persist(obj);
    }

    public void removeObject(final T obj) {
        entityManager.remove(entityManager.contains(obj)
                                     ? obj
                                     : entityManager.merge(obj));
    }

    public List<T> getAllObjects() {
        return entityManager.createQuery("FROM " + type.getName(), type).getResultList();
    }
}
