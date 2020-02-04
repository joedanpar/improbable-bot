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
package com.joedanpar.improbabot.components.game.world.location

import com.joedanpar.improbabot.components.common.HasId
import javax.persistence.*
import javax.persistence.CascadeType.ALL
import javax.persistence.DiscriminatorType.STRING
import javax.persistence.FetchType.LAZY
import javax.persistence.InheritanceType.SINGLE_TABLE

@Entity
@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorColumn(name = "location_type", discriminatorType = STRING)
abstract class Location : HasId() {

    abstract val name: String?

    abstract val description: String?

    abstract val size: Int

    open val traversable: Boolean = false

    @ManyToOne(
            fetch = LAZY,
            cascade = [ALL])
    open val parentLocation: Location? = null

    @OneToMany(
            mappedBy = "start",
            fetch = LAZY,
            cascade = [ALL])
    open val adjacentLocations: Collection<Distance>? = null

    @OneToMany(
            mappedBy = "parentLocation",
            fetch = LAZY,
            cascade = [ALL],
            orphanRemoval = true)
    open val childLocations: Collection<Location>? = null

    abstract class Builder<T : Location, P : Location, C : Location> {
        protected var name: String? = null
        protected var description: String? = null
        protected var size: Int? = 0
        protected open var parentLocation: P? = null
        protected val adjacentLocations: MutableCollection<Distance> = mutableSetOf()
        protected open val childLocations: MutableCollection<C> = mutableSetOf()

        fun setName(name: String): Builder<T, P, C> {
            this.name = name
            return this
        }

        fun setDescription(description: String): Builder<T, P, C> {
            this.description = description
            return this
        }

        fun setSize(size: Int): Builder<T, P, C> {
            this.size = size
            return this
        }

        open fun setParentLocation(location: P): Builder<T, P, C> {
            this.parentLocation = location
            return this
        }

        fun addAdjacentLocation(distance: Distance): Builder<T, P, C> {
            adjacentLocations.add(distance)
            return this
        }

        fun addAdjacentLocations(distances: Collection<Distance>): Builder<T, P, C> {
            adjacentLocations.addAll(distances)
            return this
        }

        open fun addChildLocation(location: C): Builder<T, P, C> {
            childLocations.add(location)
            return this
        }

        open fun addChildLocations(locations: Collection<C>): Builder<T, P, C> {
            childLocations.addAll(locations)
            return this
        }

        abstract fun build(): T
    }
}
