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
package com.joedanpar.improbabot.components.game.player

import net.dv8tion.jda.core.utils.Checks.notEmpty
import net.dv8tion.jda.core.utils.Checks.notNull

class PlayerBuilder {

    private var serverId: String? = null
    private var userId: String? = null
    private var name: String? = null
    private var gender: String? = null
    private var race: String? = null

    fun build(): Player {
        notEmpty(serverId!!, "A serverId is required.")
        notEmpty(userId!!, "A userId is required.")
        notEmpty(name!!, "A name is required.")
        notNull(gender!!, "A gender is required.")
        notEmpty(race!!, "A race is required.")
        return Player(serverId!!, userId!!, name!!, gender!!, race!!)
    }

    fun setServerId(serverId: String): PlayerBuilder {
        this.serverId = serverId
        return this
    }

    fun setUserId(userId: String): PlayerBuilder {
        this.userId = userId
        return this
    }

    fun setName(name: String): PlayerBuilder {
        this.name = name
        return this
    }

    fun setGender(gender: String): PlayerBuilder {
        this.gender = gender
        return this
    }

    fun setRace(race: String): PlayerBuilder {
        this.race = race
        return this
    }
}
