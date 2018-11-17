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

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;

public class PlayerBuilder {

    private String serverId;
    private String name;

    public Player build() {
        if (isEmpty(name)) throw new IllegalArgumentException("A name is required.");
        if (isNull(serverId)) throw new IllegalArgumentException("A serverId is required.");
        return new Player(serverId, name);
    }

    public PlayerBuilder setServerId(final String serverId) {
        this.serverId = serverId;
        return this;
    }

    public PlayerBuilder setName(final String name) {
        this.name = name;
        return this;
    }
}
