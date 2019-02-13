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
package com.joedanpar.improbabot.components.config

import net.dv8tion.jda.core.utils.Checks.notEmpty

class ConfigBuilder {

    private var serverId: String? = null
    private var name: String? = null
    private var value: String? = null

    fun build(): Config {
        notEmpty(serverId!!, "A serverId is required.")
        notEmpty(name!!, "A name is required.")
        notEmpty(value!!, "A value is required.")
        return Config(serverId!!, name!!, value!!)
    }

    fun setServerId(serverId: String): ConfigBuilder {
        this.serverId = serverId
        return this
    }

    fun setName(name: String): ConfigBuilder {
        this.name = name
        return this
    }

    fun setValue(value: String): ConfigBuilder {
        this.value = value
        return this
    }
}
