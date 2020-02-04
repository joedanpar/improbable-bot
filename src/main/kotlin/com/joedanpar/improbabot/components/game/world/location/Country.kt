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
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
@DiscriminatorValue("Country")
data class Country(

        override val name: String,

        override val description: String,

        override val size: Int,

        @ManyToOne(
                targetEntity = Continent::class,
                fetch = LAZY,
                cascade = [ALL])
        override val parentLocation: Continent,

        @OneToMany(
                mappedBy = "start",
                targetEntity = Country::class,
                fetch = LAZY,
                cascade = [ALL])
        override val adjacentLocations: MutableCollection<Distance>,

        @OneToMany(
                mappedBy = "parentLocation",
                targetEntity = Region::class,
                fetch = LAZY,
                cascade = [ALL],
                orphanRemoval = true)
        override val childLocations: MutableCollection<Region>
) : Location() {
    constructor() : this("", "", 0, Continent(), mutableSetOf(), mutableSetOf())

    class Builder : Location.Builder<Country, Continent, Region>() {
        override fun build(): Country {
            return Country(name!!, description!!, size!!, parentLocation!!, adjacentLocations, childLocations)
        }
    }
}