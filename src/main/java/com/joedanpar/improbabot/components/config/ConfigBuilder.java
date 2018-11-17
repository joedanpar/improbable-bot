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
package com.joedanpar.improbabot.components.config;

import static org.springframework.util.StringUtils.isEmpty;

public class ConfigBuilder {

    private String serverId;
    private String key;
    private String value;

    public Config build() {
        if (isEmpty(serverId)) throw new IllegalArgumentException("A serverId is required.");
        if (isEmpty(key)) throw new IllegalArgumentException("A key is required.");
        if (isEmpty(value)) throw new IllegalArgumentException("A value is required.");
        return new Config(serverId, key, value);
    }

    public ConfigBuilder setServerId(final String serverId) {
        this.serverId = serverId;
        return this;
    }

    public ConfigBuilder setKey(final String key) {
        this.key = key;
        return this;
    }

    public ConfigBuilder setValue(final String value) {
        this.value = value;
        return this;
    }
}
