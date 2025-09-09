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
import io.github.crafterslife.dev.carbonjapanizer.command.PlayerCommander;
import io.github.crafterslife.dev.carbonjapanizer.database.entity.CarbonJapanizerUser;
import io.github.crafterslife.dev.carbonjapanizer.permission.CarbonJapanizerPermissions;
import io.github.crafterslife.dev.carbonjapanizer.resource.translation.messages.UserMessage;
import io.github.crafterslife.dev.carbonjapanizer.service.UserService;
import io.github.crafterslife.dev.carbonjapanizer.utility.Status;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.parser.standard.EnumParser;
import org.jspecify.annotations.NullMarked;

/**
 * かな漢字変換の有効/無効の切り替えや、状態を確認するためのコマンド
 */
@NullMarked
public final class JapanizeCommand implements CarbonJapanizerCommand {

    private final UserService userService;
    private final UserMessage messages;

    /**
     * このクラスのインスタンスを生成する。
     *
     * @param userService ユーザーサービス
     * @param messages    メッセージサービス
     */
    public JapanizeCommand(final UserService userService, final UserMessage messages) {
        this.userService = userService;
        this.messages = messages;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Command<Commander> create(final CommandManager<Commander> commandManager) {
        final Command<PlayerCommander> command = commandManager.commandBuilder("japanize", "jp", "nihongo", "日本語", "かな漢字")
                .permission(CarbonJapanizerPermissions.COMMAND_JAPANIZE)
                .senderType(PlayerCommander.class)
                .optional("status", EnumParser.enumParser(Status.class))
                .handler(context -> {
                    final var sender = context.sender();
                    final var user = sender.user();
                    final var currentStatus = user.japanizeStatus();

                    if (context.contains("status")) {
                        // オプション引数の status (enable/disable) を指定していたら
                        // コマンドの送信者のかな漢字変換機能を指定ステータスへ切り替える。
                        final Status newStatus = context.get("status");
                        final var newUser = new CarbonJapanizerUser(user.uuid(), newStatus.toBoolean());

                        this.userService.saveUser(newUser);
                        this.messages.changeJapanizeStatus(newStatus).send(sender);

                    } else {
                        // オプション引数を指定していなければ現在のかな漢字変換ステータスを送信する。
                        this.messages.currentJapanizeStatus(currentStatus).send(sender);
                    }
                })
                .build();

        return (Command<Commander>) (Object) command;
    }
}
