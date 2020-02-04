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
import com.joedanpar.improbabot.components.game.world.location.World
import org.springframework.stereotype.Service

@Service
class ContinentGenerator(
        private val seed: Int,
        override val parent: World
) : LocationGenerator<Continent, World, Country>(seed, parent, 1, 2) {
    override fun generate(): Continent {
        val continent = Continent.Builder()
                .setName("TestContinent")
                .setDescription("Test Continent")
                .setParentLocation(parent)
                .build()

        continent.childLocations.addAll(generateChildren(continent))

        return continent
    }

    override fun generateChild(parent: Continent): Country {
        return CountryGenerator(random.nextInt(), parent).generate()
    }
}