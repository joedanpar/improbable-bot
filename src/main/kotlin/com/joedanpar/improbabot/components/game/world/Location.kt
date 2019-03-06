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
package com.joedanpar.improbabot.components.game.world

import com.joedanpar.improbabot.components.common.HasId
import java.util.*
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.Inheritance
import javax.persistence.InheritanceType.TABLE_PER_CLASS
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
@Inheritance(strategy = TABLE_PER_CLASS)
abstract class Location(

        open val name: String? = "",

        open val description: String? = "",

        @ManyToOne(targetEntity = Location::class)
        open val outerLocation: Location?,

        @OneToMany(targetEntity = Location::class, mappedBy = "id", fetch = LAZY)
        open val subLocations: Set<Location>?
) : HasId(UUID.randomUUID())

@Entity
data class Continent(
        override val name: String,

        override val description: String,

        @OneToMany(targetEntity = Country::class, mappedBy = "id", fetch = LAZY)
        val countries: Set<Country>
) : Location(name, description, null, countries)

@Entity
data class Country(
        override val name: String,

        override val description: String,

        @ManyToOne(targetEntity = Continent::class)
        val continent: Continent,

        @OneToMany(targetEntity = Region::class, mappedBy = "id", fetch = LAZY)
        val regions: Set<Region>
) : Location(name, description, continent, regions)

@Entity
data class Region(
        override val name: String,

        override val description: String,

        @ManyToOne(targetEntity = Country::class)
        val country: Country,

        @OneToMany(targetEntity = LocalArea::class, mappedBy = "id", fetch = LAZY)
        val localAreas: Set<LocalArea>
) : Location(name, description, country, localAreas)

@Entity
data class LocalArea(
        override val name: String,

        override val description: String,

        @ManyToOne(targetEntity = Region::class)
        val region: Region,

        @OneToMany(targetEntity = PointOfInterest::class, mappedBy = "id", fetch = LAZY)
        val pointsofInterest: Set<PointOfInterest>
) : Location(name, description, region, pointsofInterest)

@Entity
data class PointOfInterest(
        override val name: String,
        override val description: String,

        @ManyToOne(targetEntity = LocalArea::class)
        val localArea: LocalArea
) : Location(name, description, localArea, null)