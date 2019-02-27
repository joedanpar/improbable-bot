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
package com.joedanpar.improbabot.components.common

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.IOException
import java.net.URL
import javax.imageio.ImageIO

class UserHelper {

    companion object {

        fun getAverageColor(avatarUrl: String?): Color {
            return if (avatarUrl.isNullOrEmpty()) {
                Color.BLUE
            } else {
                try {
                    val connection = URL(avatarUrl).openConnection()

                    connection.setRequestProperty("User-Agent", "NING/1.0")
                    val image = ImageIO.read(connection.getInputStream())
                    UserHelper.getAverageColor(image)
                } catch (e: IOException) {
                    Color.BLUE
                }
            }
        }

        private fun getAverageColor(image: BufferedImage): Color {
            val width = image.width
            val height = image.height

            var redSum = 0
            var greenSum = 0
            var blueSum = 0

            for (x in 0 until width step 1) {
                for (y in 0 until height step 1) {
                    val pixel = Color(image.getRGB(x, y))
                    redSum += pixel.red
                    greenSum += pixel.green
                    blueSum += pixel.blue
                }
            }

            val total = width * height

            return Color(redSum / total, greenSum / total, blueSum / total)
        }
    }
}