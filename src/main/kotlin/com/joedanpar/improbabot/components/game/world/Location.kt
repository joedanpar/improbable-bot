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
import javax.persistence.*
import javax.persistence.CascadeType.ALL
import javax.persistence.DiscriminatorType.STRING
import javax.persistence.FetchType.LAZY
import javax.persistence.InheritanceType.SINGLE_TABLE

@Entity
@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorColumn(name = "location_type", discriminatorType = STRING)
abstract class Location: HasId() {

    abstract val name: String?

    abstract val description: String?

    @ManyToOne
    open val parentLocation: Location? = null

    @OneToMany
    open val childLocations: Collection<Location>? = null
}

@Entity
@DiscriminatorValue("World")
data class World(

        override val name: String,

        override val description: String,

        @OneToMany(
                targetEntity = Continent::class,
                fetch = LAZY,
                cascade = [ALL],
                orphanRemoval = true)
        override val childLocations: Collection<Continent>
) : Location()

@Entity
@DiscriminatorValue("Continent")
data class Continent(

        override val name: String,

        override val description: String,

        @ManyToOne(
                targetEntity = World::class,
                fetch = LAZY,
                cascade = [ALL])
        override val parentLocation: World,

        @OneToMany(
                targetEntity = Country::class,
                fetch = LAZY,
                cascade = [ALL],
                orphanRemoval = true)
        override val childLocations: Collection<Country>
) : Location()

@Entity
@DiscriminatorValue("Country")
data class Country(

        override val name: String,

        override val description: String,

        @ManyToOne(
                targetEntity = Continent::class,
                fetch = LAZY,
                cascade = [ALL])
        override val parentLocation: Continent,

        @OneToMany(
                targetEntity = Region::class,
                fetch = LAZY,
                cascade = [ALL],
                orphanRemoval = true)
        override val childLocations: Collection<Region>
) : Location()

@Entity
@DiscriminatorValue("Region")
data class Region(
        override val name: String,

        override val description: String,

        @ManyToOne(
                targetEntity = Country::class,
                fetch = LAZY,
                cascade = [ALL])
        override val parentLocation: Country,

        @OneToMany(
                targetEntity = LocalArea::class,
                fetch = LAZY,
                cascade = [ALL],
                orphanRemoval = true)
        override val childLocations: Collection<LocalArea>
) : Location()

@Entity
@DiscriminatorValue("LocalArea")
data class LocalArea(
        override val name: String,

        override val description: String,

        @ManyToOne(
                targetEntity = Region::class,
                fetch = LAZY,
                cascade = [ALL])
        override val parentLocation: Region,

        @OneToMany(
                targetEntity = PointOfInterest::class,
                fetch = LAZY,
                cascade = [ALL],
                orphanRemoval = true)
        override val childLocations: Collection<PointOfInterest>
) : Location()

@Entity
@DiscriminatorValue("PointOfInterest")
data class PointOfInterest(
        override val name: String,
        override val description: String,

        @ManyToOne(
                targetEntity = LocalArea::class,
                fetch = LAZY,
                cascade = [ALL])
        override val parentLocation: LocalArea
) : Location()