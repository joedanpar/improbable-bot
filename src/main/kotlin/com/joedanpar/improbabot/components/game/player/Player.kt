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

}
