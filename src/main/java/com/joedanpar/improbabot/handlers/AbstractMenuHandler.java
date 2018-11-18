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
package com.joedanpar.improbabot.handlers;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Menu.Builder;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public abstract class AbstractMenuHandler<B extends Builder> {
    B configureBuilder(final Builder builder, final Map<String, Object> parameters) {
//        configureColor(builder, (Color) parameters.get("color"));
        configureWaiter(builder, (EventWaiter) parameters.get("waiter"));
        configureUsers(builder, (Set<User>) parameters.get("users"));
        configureRoles(builder, (Set<Role>) parameters.get("roles"));
        configureTimeout(builder, (Long) parameters.get("timeout"), (TimeUnit) parameters.get("timeUnit"));

        return (B) builder;
    }

    /*private void configureColor(final Builder builder, final Color color) {
        if (color != null) {
            builder.setColor(color);
        }
    }*/

    private void configureWaiter(final Builder builder, final EventWaiter waiter) {
        if (waiter != null) {
            builder.setEventWaiter(waiter);
        }
    }

    private void configureUsers(final Builder builder, final Collection<User> users) {
        if (isNotEmpty(users)) {
            builder.setUsers(users.toArray(new User[0]));
        }
    }

    private void configureRoles(final Builder builder, final Collection<Role> roles) {
        if (isNotEmpty(roles)) {
            builder.setRoles(roles.toArray(new Role[0]));
        }
    }

    private void configureTimeout(final Builder builder, final Long timeout, final TimeUnit unit) {
        if (timeout != null && unit != null) {
            builder.setTimeout(timeout, unit);
        }
    }
}
