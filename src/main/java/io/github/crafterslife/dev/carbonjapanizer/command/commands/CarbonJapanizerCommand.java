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
package io.github.crafterslife.dev.carbonjapanizer.command.commands;

import io.github.crafterslife.dev.carbonjapanizer.command.Commander;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.jspecify.annotations.NullMarked;

/**
 * <p>{@code Cloud} を使用してコマンドを生成するためのインターフェース。</p>
 */
@NullMarked
public interface CarbonJapanizerCommand {

    /**
     * コマンドのルートノードを構築して返す。
     *
     * @param commandManager {@code Cloud} のコマンドマネージャー
     * @return 生成されたコマンド
     */
    Command<Commander> create(CommandManager<Commander> commandManager);
}
