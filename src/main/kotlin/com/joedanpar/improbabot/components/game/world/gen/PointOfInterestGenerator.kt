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
import com.joedanpar.improbabot.components.game.world.location.Location
import com.joedanpar.improbabot.components.game.world.location.PointOfInterest
import org.springframework.stereotype.Service

@Service
class PointOfInterestGenerator(
        private val seed: Int,
        override val parent: LocalArea
) : LocationGenerator<PointOfInterest, LocalArea, Location>(seed, parent, 16, 32) {
    override fun generate(): PointOfInterest {
        return PointOfInterest.Builder()
                .setName("TestPOI")
                .setDescription("Test Point of Interest")
                .setParentLocation(parent)
                .build()
    }

    override fun generateChild(parent: PointOfInterest): Location {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}