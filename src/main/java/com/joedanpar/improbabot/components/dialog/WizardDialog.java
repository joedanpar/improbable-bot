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
package com.joedanpar.improbabot.components.dialog;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Menu;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;

public class WizardDialog extends Menu {

    protected final List<Menu>         dialogs     = new LinkedList<>();
    protected       ListIterator<Menu> currentMenu = dialogs.listIterator();

    protected WizardDialog(final EventWaiter waiter, final Set<User> users, final Set<Role> roles, final long timeout,
                           final TimeUnit unit) {
        super(waiter, users, roles, timeout, unit);
    }

    protected void reset() {
        currentMenu = dialogs.listIterator();
    }

    private void safeReset() {
        if (!(currentMenu.hasPrevious() || currentMenu.hasNext())) {
            reset();
        }
    }

    @Override
    public void display(final MessageChannel channel) {
        safeReset();

        dialogs.get(currentMenu.nextIndex()).display(channel);
        currentMenu.next();
    }

    @Override
    public void display(final Message message) {
        safeReset();

        dialogs.get(currentMenu.nextIndex()).display(message);
        currentMenu.next();
    }

    public static abstract class Builder<B extends Builder<B, D>, D extends WizardDialog> extends Menu.Builder<B, D> {

        protected final List<Menu> dialogs = new LinkedList<>();

        public B addDialog(final Menu dialog) {
            this.dialogs.add(dialog);
            return (B)this;
        }

        public B setDialogs(final Menu... dialogs) {
            this.dialogs.clear();
            this.dialogs.addAll(asList(dialogs));
            return (B)this;
        }
    }
}
