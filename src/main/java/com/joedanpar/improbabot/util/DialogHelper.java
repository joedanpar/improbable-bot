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
package com.joedanpar.improbabot.util;

import com.jagrosh.jdautilities.menu.*;
import com.jagrosh.jdautilities.menu.Menu.Builder;
import com.joedanpar.improbabot.handlers.*;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.Map;

@Log4j2
@UtilityClass
public class DialogHelper {
    public static void showDialog(final MessageChannel channel, final MenuType menuType,
                                  final Map<String, Object> parameters) {
        configureBuilder(menuType, getMenuBuilder(menuType), parameters).build().display(channel);
    }

    private static Builder configureBuilder(final MenuType menuType, final Builder builder,
                                            final Map<String, Object> parameters) {
        if (builder != null) {
            switch (menuType) {
                case BUTTON:
                    return new ButtonMenuHandler().configureBuilder(builder, parameters);
                case ORDERED:
                    return new OrderedMenuHandler().configureBuilder(builder, parameters);
                case PAGINATED:
                    return new PaginatedMenuHandler().configureBuilder(builder, parameters);
                case DIALOG:
                    return new DialogMenuHandler().configureBuilder(builder, parameters);
                case SLIDESHOW:
                    return new SlideshowMenuHandler().configureBuilder(builder, parameters);
                default:
                    log.warn("Unable to configure Menu Builder, unknown Menu Type: {}", menuType);
            }
        }

        return null;
    }

    private static Builder getMenuBuilder(final MenuType menuType) {
        switch (menuType) {
            case BUTTON:
                return new ButtonMenu.Builder();
            case ORDERED:
                return new OrderedMenu.Builder();
            case PAGINATED:
                return new Paginator.Builder();
            case DIALOG:
                return new SelectionDialog.Builder();
            case SLIDESHOW:
                return new Slideshow.Builder();
            default:
                log.error("Unknown Menu Type: {}", menuType);
                return null;
        }
    }
}
