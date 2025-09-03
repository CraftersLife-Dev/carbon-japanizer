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
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import java.util.List;
import org.jspecify.annotations.NullMarked;

/**
 * <p>PaperのBrigadier APIを使用してコマンドを定義するためのインターフェースです。</p>
 *
 * <p>このインターフェースは、プラグインの各コマンドが実装すべき基本メソッドを定義します。
 * 主な目的は、コマンドのルートノードを提供し、エイリアスや説明などのメタデータを
 * 統一された方法で管理することです。</p>
 */
@NullMarked
public interface InternalCommand {

    /**
     * コマンドのルートノードを構築し、返します。
     *
     * <p>このメソッドは、{@link LiteralCommandNode}を返す必要があります。
     * Paperの{@link Commands#literal(String)}ヘルパーメソッドを使用すると、
     * このメソッド内で簡単にコマンド構造を定義できます。</p>
     *
     * @return コマンドのルートノード。
     */
    @SuppressWarnings("UnstableApiUsage")
    LiteralCommandNode<CommandSourceStack> command();

    /**
     * このコマンドのエイリアスのリストを取得します。
     * <p>デフォルトでは空のリストを返します。</p>
     *
     * @return このコマンドのエイリアスの文字列リスト。
     */
    default List<String> aliases() {
        return List.of();
    }

    /**
     * このコマンドの説明を取得します。
     * <p>デフォルトでは空の文字列を返します。</p>
     *
     * @return コマンドの説明。
     */
    default String description() {
        return "";
    }
}
