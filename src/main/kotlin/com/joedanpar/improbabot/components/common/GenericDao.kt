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
package com.joedanpar.improbabot.components.common

import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Transactional
open class GenericDao<T>(protected var entityManager: EntityManager, protected val type: Class<T>) {

    fun getAllObjects(): List<T> {
        return entityManager.createQuery("FROM ${type.name}", type).resultList
    }

    fun getAllObjectsByServerId(serverId: String): List<T> {
        val query = entityManager.createQuery("FROM ${type.name} where serverId = :serverId", type)
        query.setParameter("serverId", serverId)

        return query.resultList
    }

    fun saveObject(obj: T) {
        entityManager.persist(obj)
    }

    fun removeObject(obj: T) {
        entityManager.remove(if (entityManager.contains(obj)) {
            obj
        } else {
            entityManager.merge(obj)
        })
    }
}
