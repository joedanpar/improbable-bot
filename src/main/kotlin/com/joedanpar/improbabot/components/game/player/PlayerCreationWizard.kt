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
package com.joedanpar.improbabot.components.game.player

import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import com.jagrosh.jdautilities.menu.Menu
import com.jagrosh.jdautilities.menu.SelectionDialog
import com.jagrosh.jdautilities.menu.SelectionDialog.SELECT
import com.joedanpar.improbabot.components.common.Emojis.BUTTON
import com.joedanpar.improbabot.components.dialog.TextEntryDialog
import com.joedanpar.improbabot.components.dialog.WizardDialog
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.Role
import net.dv8tion.jda.core.entities.User
import java.awt.Color
import java.util.Arrays.asList
import java.util.concurrent.TimeUnit
import java.util.function.BiConsumer
import java.util.function.Consumer

internal class PlayerCreationWizard private constructor(waiter: EventWaiter, users: Set<User>, roles: Set<Role>,
                                                        timeout: Long, unit: TimeUnit, private val builder: PlayerBuilder,
                                                        private val service: PlayerService,
                                                        val embedColor: Color) : WizardDialog(waiter, users, roles, timeout, unit) {

    init {
        dialogs.addAll(buildDialogs())
        reset()
    }

    private fun buildDialogs(): Collection<Menu> {
        return asList(buildNameDialog(),
                buildGenderDialog(),
                buildRaceDialog())
    }

    private fun buildNameDialog(): Menu {
        return TextEntryDialog.Builder()
                .setEventWaiter(waiter)
                .setUsers(*users.toTypedArray())
                .setRoles(*roles.toTypedArray())
                .setColor(embedColor)
                .setText("Please enter your player's name")
                .setDescription("Name")
                .setReactAction(BiConsumer { message, reaction ->
                    builder.setServerId(message.guild.id)
                            .setUserId(message.author.id)
                            .setName((dialogs[currentMenu.previousIndex()] as TextEntryDialog).value)
                    display(message)
                })
                .setFinalAction(Consumer { message ->
                    message.delete().queue()
                    reset()
                })
                .build()
    }

    private fun buildGenderDialog(): Menu {
        return SelectionDialog.Builder()
                .setEventWaiter(waiter)
                .setUsers(*users.toTypedArray())
                .setRoles(*roles.toTypedArray())
                .setColor(embedColor)
                .setText("Please select your player's gender")
                .setChoices(*genders)
                .setDefaultEnds(BUTTON, "")
                .setSelectedEnds(SELECT, "")
                .useLooping(true)
                .setSelectionConsumer { message, selection ->
                    if (selection != 3) {
                        builder.setGender(genders[selection!! - 1])
                    } else {
                        currentMenu.add(buildGenderEntryDialog(message))
                    }

                    display(message)
                }
                .setCanceled { message -> message.delete().queue() }
                .build()
    }

    private fun buildRaceDialog(): Menu {
        return SelectionDialog.Builder()
                .setEventWaiter(waiter)
                .setUsers(*users.toTypedArray())
                .setRoles(*roles.toTypedArray())
                .setColor(embedColor)
                .setText("Please select your player's Race")
                .setChoices(*races)
                .setDefaultEnds(BUTTON, "")
                .setSelectedEnds(SELECT, "")
                .useLooping(true)
                .setSelectionConsumer { message, selection ->
                    builder.setRace(races[selection!! - 1])
                    display(message)
                }
                .setCanceled { message -> message.delete().queue() }
                .build()
    }

    private fun buildGenderEntryDialog(message: Message): Menu {
        return TextEntryDialog.Builder()
                .setEventWaiter(waiter)
                .setUsers(*users.toTypedArray())
                .setRoles(*roles.toTypedArray())
                .setColor(embedColor)
                .setDescription("Please enter your player's gender")
                .setText("Gender")
                .setReactAction(BiConsumer { response, reaction ->
                    builder.setGender((dialogs[currentMenu.previousIndex()] as TextEntryDialog).value)
                    display(message)
                })
                .setFinalAction(Consumer { response ->
                    message.delete().queue()
                    reset()
                })
                .build()
    }

    class Builder : WizardDialog.Builder<Builder, PlayerCreationWizard>() {

        private var playerBuilder: PlayerBuilder? = null
        private var service: PlayerService? = null
        private var embedColor: Color? = null

        fun setService(service: PlayerService): Builder {
            this.service = service
            return this
        }

        fun setPlayerBuilder(playerBuilder: PlayerBuilder): Builder {
            this.playerBuilder = playerBuilder
            return this
        }

        fun setEmbedColor(color: Color): Builder {
            this.embedColor = color
            return this
        }

        override fun build(): PlayerCreationWizard {
            return PlayerCreationWizard(waiter, users, roles, timeout, unit, playerBuilder!!, service!!, embedColor!!)
        }
    }

    companion object {
        private val genders = arrayOf("Male", "Female", "Other")
        private val races = arrayOf("Human", "Elf", "Dwarf", "Orc")
    }
}
