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

import javax.persistence.CascadeType.ALL
import javax.persistence.FetchType.LAZY
import javax.persistence.ManyToOne
import javax.persistence.MappedSuperclass
import javax.persistence.OneToMany

@MappedSuperclass
abstract class PointOfInterest(

        override val name: String,

        override val description: String,

        override val size: Int,

        override val traversable: Boolean = true,

        @ManyToOne(
                targetEntity = LocalArea::class,
                fetch = LAZY,
                cascade = [ALL])
        override val parentLocation: LocalArea,

        @OneToMany(
                mappedBy = "start",
                targetEntity = PointOfInterest::class,
                fetch = LAZY,
                cascade = [ALL])
        override val adjacentLocations: MutableCollection<Distance>? = null
) : Location() {
    constructor() : this("", "", 0, true, LocalArea(), mutableSetOf())

    abstract class Builder<T : PointOfInterest> : Location.Builder<T, LocalArea, Location>()
}