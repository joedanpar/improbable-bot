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

import com.joedanpar.improbabot.components.game.world.location.Location
import kotlin.random.Random

abstract class LocationGenerator<T : Location, P : Location, C : Location>(
        seed: Int,
        protected open val parent: P?,
        private val minChildren: Int,
        private val maxChildren: Int) {
    protected val random = Random(seed)

    abstract fun generate(): T

    protected fun generateChildren(parent: T): Collection<C> {
        val children = mutableListOf<C>()
        for (i in 0..random.nextInt(minChildren, maxChildren)) {
            children.add(generateChild(parent))
        }
        return children
    }

    abstract fun generateChild(parent: T): C
}
