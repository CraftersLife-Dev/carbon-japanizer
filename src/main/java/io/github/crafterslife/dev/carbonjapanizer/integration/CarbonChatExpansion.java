/*
 * CarbonJapanizer
 *
 * Copyright (c) 2025. Namiu (うにたろう)
 *                     Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.github.crafterslife.dev.carbonjapanizer.integration;

import java.util.Objects;
import net.draycia.carbon.api.CarbonChat;
import org.jspecify.annotations.NullMarked;

/**
 * {@code Carbon} のラッパー。
 *
 * @see <a href="https://github.com/Hexaoxide/Carbon">Carbon</a>
 */
@NullMarked
public final class CarbonChatExpansion {

    private static byte carbonChatLoaded = -1;

    private CarbonChatExpansion() {
    }

    /**
     * {@code Carbon} がロードされているか確認する。
     *
     * @return {@code Carbon} がロードされている場合はtrue
     */
    public static boolean carbonChatLoaded() {
        if (carbonChatLoaded == -1) {
            try {
                final String name = CarbonChat.class.getName();
                Objects.requireNonNull(name);
                carbonChatLoaded = 1;
            } catch (final NoClassDefFoundError error) {
                carbonChatLoaded = 0;
            }
        }
        return carbonChatLoaded == 1;
    }
}
