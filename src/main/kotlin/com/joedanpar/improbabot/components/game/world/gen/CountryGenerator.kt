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
package com.joedanpar.improbabot.components.game.world.gen

import com.joedanpar.improbabot.components.game.world.location.Continent
import com.joedanpar.improbabot.components.game.world.location.Country
import com.joedanpar.improbabot.components.game.world.location.Region
import org.springframework.stereotype.Service

@Service
class CountryGenerator(
        private val seed: Int,
        override val parent: Continent
) : LocationGenerator<Country, Continent, Region>(seed, parent, 2, 4) {
    override fun generate(): Country {
        val country = Country.Builder()
                .setName("TestCountry")
                .setDescription("Test Country")
                .setParentLocation(parent)
                .build()

        country.childLocations.addAll(generateChildren(country))

        return country
    }

    override fun generateChild(parent: Country): Region {
        return RegionGenerator(random.nextInt(), parent).generate()
    }
}