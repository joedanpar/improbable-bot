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

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id

@Entity
data class Config(

        @Id
        @GeneratedValue(strategy = IDENTITY)
        val id: Int?,

        @Column(nullable = false)
        val serverId: String,

        @Column(nullable = false)
        val name: String,

        val value: String) : Serializable {

    constructor(serverId: String, name: String, value: String) : this(null, serverId, name, value)
}