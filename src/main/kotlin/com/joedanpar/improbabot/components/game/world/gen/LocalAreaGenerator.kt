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

import com.joedanpar.improbabot.components.game.world.location.LocalArea
import com.joedanpar.improbabot.components.game.world.location.PointOfInterest
import com.joedanpar.improbabot.components.game.world.location.Region
import org.springframework.stereotype.Service

@Service
class LocalAreaGenerator(
        private val seed: Int,
        override val parent: Region
) : LocationGenerator<LocalArea, Region, PointOfInterest>(seed, parent, 8, 16) {
    override fun generate(): LocalArea {
        val localArea = LocalArea.Builder()
                .setName("TestLocalArea")
                .setDescription("Test Local Area")
                .setParentLocation(parent)
                .build()

        localArea.childLocations.addAll(generateChildren(localArea))

        return localArea
    }

    override fun generateChild(parent: LocalArea): PointOfInterest {
        return RiverGenerator(random.nextInt(), parent).generate()
    }
}