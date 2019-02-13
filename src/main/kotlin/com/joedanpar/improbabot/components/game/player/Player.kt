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

import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.MessageEmbed
import java.io.Serializable
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["serverId", "userId"])])
class Player(

        @Id
        @GeneratedValue(strategy = IDENTITY)
        @Column(nullable = false)
        val id: Int?,
        @Column(nullable = false)
        val serverId: String,
        @Column(nullable = false)
        val userId: String,
        @Column(nullable = false)
        val name: String,
        @Column(nullable = false)
        val gender: String,
        @Column(nullable = false)
        val race: String) : Serializable {

    constructor(serverId: String, userId: String, name: String, gender: String, race: String) : this(null, serverId, userId, name, gender, race)

    private fun toEmbed(): MessageEmbed {
        return EmbedBuilder().setTitle(name)
                .addField("Gender", gender, true)
                .addField("Race", race, true)
                .build()
    }

}
