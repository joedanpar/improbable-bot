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
package com.joedanpar.improbabot.components.dialog

import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import com.jagrosh.jdautilities.menu.Menu
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User
import java.util.*
import java.util.Arrays.asList
import java.util.concurrent.TimeUnit

open class WizardDialog protected constructor(waiter: EventWaiter, users: Set<User>, roles: Set<Role>, timeout: Long,
                                              unit: TimeUnit) : Menu(waiter, users, roles, timeout, unit) {

    protected val dialogs: MutableList<Menu> = LinkedList()
    protected var currentMenu = dialogs.listIterator()

    protected fun reset() {
        currentMenu = dialogs.listIterator()
    }

    private fun safeReset() {
        if (!(currentMenu.hasPrevious() || currentMenu.hasNext())) {
            reset()
        }
    }

    override fun display(channel: MessageChannel) {
        safeReset()

        dialogs[currentMenu.nextIndex()].display(channel)
        currentMenu.next()
    }

    override fun display(message: Message) {
        safeReset()

        dialogs[currentMenu.nextIndex()].display(message)
        currentMenu.next()
    }

    abstract class Builder<B : Builder<B, D>, D : WizardDialog> : Menu.Builder<B, D>() {

        protected val dialogs: MutableList<Menu> = LinkedList()

        fun addDialog(dialog: Menu): B {
            this.dialogs.add(dialog)
            return this as B
        }

        fun setDialogs(vararg dialogs: Menu): B {
            this.dialogs.clear()
            this.dialogs.addAll(asList(*dialogs))
            return this as B
        }
    }
}
