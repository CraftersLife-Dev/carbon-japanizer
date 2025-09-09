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
package io.github.crafterslife.dev.carbonjapanizer.platform.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import io.github.crafterslife.dev.carbonjapanizer.command.Commander;
import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NullMarked;

/**
 * {@code Velocity} のコマンド送信者。
 */
@NullMarked
public interface VelocityCommander extends Commander {

    @Override
    default Audience audience() {
        return this.source();
    }

    /**
     * {@code Velocity} のコマンドソース。
     *
     * @return {@code Velocity} のコマンドソース
     */
    CommandSource source();
}
