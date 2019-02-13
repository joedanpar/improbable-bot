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

import static net.dv8tion.jda.core.utils.Checks.notEmpty;
import static net.dv8tion.jda.core.utils.Checks.notNull;

public class PlayerBuilder {

    private String serverId;
    private String userId;
    private String name;
    private String gender;
    private String race;

    public Player build() {
        notEmpty(serverId, "A serverId is required.");
        notEmpty(userId, "A userId is required.");
        notEmpty(name, "A name is required.");
        notNull(gender, "A gender is required.");
        notEmpty(race, "A race is required.");
        return new Player(serverId, userId, name, gender, race);
    }

    public PlayerBuilder setServerId(final String serverId) {
        this.serverId = serverId;
        return this;
    }

    public PlayerBuilder setUserId(final String userId) {
        this.userId = userId;
        return this;
    }

    public PlayerBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    public PlayerBuilder setGender(final String gender) {
        this.gender = gender;
        return this;
    }

    public PlayerBuilder setRace(final String race) {
        this.race = race;
        return this;
    }
}
