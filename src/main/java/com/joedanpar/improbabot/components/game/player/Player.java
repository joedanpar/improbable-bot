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
package com.joedanpar.improbabot.components.game.player;

import lombok.Data;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"serverId", "userId"}))
public class Player implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false)
    private int id;
    @Column(nullable = false)
    private String serverId;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String gender;
    @Column(nullable = false)
    private String race;

    public Player() {
        //no-op
    }

    Player(final String serverId, final String userId, final String name, final String gender, final String race) {
        this.serverId = serverId;
        this.userId = userId;
        this.name = name;
        this.gender = gender;
        this.race = race;
    }

    private MessageEmbed toEmbed() {
        return new EmbedBuilder().setTitle(getName())
                                 .addField("Gender", getGender(), true)
                                 .addField("Race", getRace(), true)
                                 .build();
    }

}
