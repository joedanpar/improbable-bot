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

import lombok.val;
import org.hibernate.SessionFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenericDao<T> {
    private final Class<T>       generic;
    protected     SessionFactory sessionFactory;

    public GenericDao(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.generic = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), GenericDao.class);
    }

    public List<T> getAllObjectsByServerId(final String serverId) {
        try (val session = sessionFactory.openSession()) {
            val query = session.createQuery("FROM " + generic.getName() + " where serverId = :serverId", generic);
            query.setParameter("serverId", serverId);

            return query.getResultList();
        }
    }

    public void saveObject(final T obj) {
        try (val session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(obj);
            session.getTransaction().commit();
        }
    }

    public void removeObject(final T obj) {
        try (val session = sessionFactory.openSession()) {
            session.getTransaction();
            session.delete(session.contains(obj)
                                   ? obj
                                   : session.merge(obj));
            session.getTransaction().commit();
        }
    }

    public List<T> getAllObjects() {
        try (val session = sessionFactory.openSession()) {
            return session.createQuery("FROM " + generic.getName(), generic).getResultList();
        }
    }
}
