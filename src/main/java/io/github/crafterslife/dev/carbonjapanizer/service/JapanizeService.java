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
package io.github.crafterslife.dev.carbonjapanizer.service;

import io.github.crafterslife.dev.carbonjapanizer.conversion.renderer.JapanizedComponentFormatter;
import io.github.crafterslife.dev.carbonjapanizer.conversion.renderer.RomanComponentJapanizer;
import io.github.crafterslife.dev.carbonjapanizer.database.entity.CarbonJapanizerUser;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class JapanizeService {

    private final UserService userService;
    private final RomanComponentJapanizer japanizer;
    private final JapanizedComponentFormatter formatter;

    public JapanizeService(
            final UserService userService,
            final RomanComponentJapanizer japanizer,
            final JapanizedComponentFormatter formatter
    ) {
        this.userService = userService;
        this.japanizer = japanizer;
        this.formatter = formatter;
    }

    public Component japanize(final UUID uuid, final Component input) throws IllegalStateException {
        final CarbonJapanizerUser user = this.userService.loadUser(uuid).join();
        // if (user.hasPermission()) TODO

        // ユーザーが変換機能を有効にしていなければそのまま返す
        if (!user.shouldJapanize()) {
            return input;
        }

        final Component japanized = this.japanizer.render(input);
        return this.formatter.render(input, japanized);
    }
}
