/*
 * PaperTemplate
 *
 * Copyright (c) 2025. Namiu (うにたろう)
 *                     Contributors []
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
package io.github.crafterslife.dev.papertemplate.minecraft.paper.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.crafterslife.dev.papertemplate.ServiceContainer;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * <p>管理者用コマンド{@code /template}を処理する{@link InternalCommand}の実装です。</p>
 *
 * <p>このクラスは、プラグインの設定を再読み込みするなど、管理者向けのタスクを実行するための
 * サブコマンドを定義します。</p>
 */
@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class AdminCommand implements InternalCommand {

    private final ServiceContainer serviceContainer;

    /**
     * 新しい{@code AdminCommand}インスタンスを構築します。
     *
     * @param serviceContainer サービスロジックコンテナ
     */
    @Contract(pure = true)
    public AdminCommand(final ServiceContainer serviceContainer) {
        this.serviceContainer = serviceContainer;
    }

    /**
     * 管理者が使用するためのメインコマンドを構築します。
     *
     * @return 管理者用コマンド
     */
    @Override
    public LiteralCommandNode<CommandSourceStack> command() {
        return Commands.literal("template")
                // .requires(context -> context.getSender().hasPermission("plugin_name.command.admin"))
                // .then(/* サブコマンド */)
                // ...
                .build();
    }
}
