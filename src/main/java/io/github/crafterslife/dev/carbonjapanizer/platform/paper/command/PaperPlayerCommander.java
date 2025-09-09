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
package io.github.crafterslife.dev.carbonjapanizer.platform.paper.command;

import io.github.crafterslife.dev.carbonjapanizer.command.PlayerCommander;
import io.github.crafterslife.dev.carbonjapanizer.database.entity.CarbonJapanizerUser;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jspecify.annotations.NullMarked;

/**
 * {@code Paper} でコマンドを送信するプレイヤー。
 *
 * @param source {@code Paper} のコマンドソース
 * @param user {@code CarbonJapanizer} のユーザー
 */
@NullMarked
@SuppressWarnings("UnstableApiUsage")
public record PaperPlayerCommander(CommandSourceStack source, CarbonJapanizerUser user) implements PaperCommander, PlayerCommander {
}
