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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigService {

    private ConfigDao dao;

    public ConfigService(final ConfigDao dao) {
        this.dao = dao;
    }

    @Transactional
    public List<Config> getConfigByName(final String serverId, final String configName) {
        return dao.getConfigByName(serverId, configName);
    }

    public Optional<String> getSingleValueByName(final String serverId, final String configName) {
        return Optional.ofNullable(getValuesByName(serverId, configName).get(0));
    }

    @Transactional
    public List<String> getValuesByName(final String serverId, final String configName) {
        return dao.getValuesByName(serverId, configName);
    }

    @Transactional
    public void removeConfig(final Config config) {
        dao.removeConfig(config);
    }

    @Transactional
    public void removeConfig(final String serverId, final String configName, final String configValue) {
        dao.removeConfig(serverId, configName, configValue);
    }

    @Transactional
    public boolean addConfig(final Config config) {
        return dao.addConfig(config);
    }

    @Transactional
    public boolean addConfig(final String serverId, final String configName, final String configValue) {
        return dao.addConfig(serverId, configName, configValue);
    }

    @Transactional
    public List<Config> getConfigsByServerId(final String serverId) {
        return dao.getAllObjectsByServerId(serverId);
    }
}
