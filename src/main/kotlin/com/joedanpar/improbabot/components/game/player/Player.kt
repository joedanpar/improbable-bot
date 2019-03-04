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

import com.joedanpar.improbabot.components.game.entity.GameEntity
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.Message
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["serverId", "userId"])])
data class Player(

        @Column(nullable = false)
        val serverId: String,

        @Column(nullable = false)
        val userId: String,

        @Column(nullable = false)
        val name: String,

        @Column(nullable = false)
        val gender: String,

        @Column(nullable = false)
        val race: String
) : GameEntity() {

    override fun render(): Message = MessageBuilder().setEmbed(toEmbed().build()).build()

    override fun toEmbed(): EmbedBuilder = super.toEmbed()
            .setTitle(name)
            .addField("Gender", gender, true)
            .addField("Race", race, true)

    class Builder {

        private var serverId: String? = null
        private var userId: String? = null
        private var name: String? = null
        private var gender: String? = null
        private var race: String? = null

        fun setServerId(serverId: String): Builder {
            this.serverId = serverId
            return this
        }

        fun setUserId(userId: String): Builder {
            this.userId = userId
            return this
        }

        fun setName(name: String): Builder {
            this.name = name
            return this
        }

        fun setGender(gender: String): Builder {
            this.gender = gender
            return this
        }

        fun setRace(race: String): Builder {
            this.race = race
            return this
        }

        fun build(): Player {
            return Player(serverId!!, userId!!, name!!, gender!!, race!!)
        }
    }
}
