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

import com.joedanpar.improbabot.components.common.GenericService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.TransactionRequiredException;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class ConfigService extends GenericService<ConfigDao, Config> {

    public ConfigService(final ConfigDao dao) {
        super(dao);
    }

    public List<Config> getConfig(final String serverId, final String name) {
        return getDao().getAllObjectsByName(serverId, name);
    }

    public Optional<String> getValueByName(final String serverId, final String configName) {
        return Optional.ofNullable(getValuesByName(serverId, configName).get(0));
    }

    public List<String> getValuesByName(final String serverId, final String configName) {
        return getDao().getValuesByName(serverId, configName);
    }

    public void removeConfig(final Config config) {
        getDao().removeObject(config);
    }

    public void removeConfig(final String serverId, final String configName, final String configValue) {
        getDao().removeObject(serverId, configName, configValue);
    }

    public boolean createObject(final String serverId, final String configName, final String configValue) {
        return createObject(new ConfigBuilder().setServerId(serverId).setName(configName).setValue(configValue).build());
    }

    public boolean createObject(final Config config) {
        try {
            getDao().saveObject(config);
        } catch (EntityExistsException | TransactionRequiredException e) {
            log.error("Failed to create config {} with value {} for server {}", config.getName(), config.getValue(),
                      config.getServerId());
            log.error(e);
            return false;
        }
        return true;
    }
}
