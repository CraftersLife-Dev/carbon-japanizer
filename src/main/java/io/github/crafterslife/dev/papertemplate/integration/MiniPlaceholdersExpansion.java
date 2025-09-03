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
package io.github.crafterslife.dev.papertemplate.integration;

import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.jspecify.annotations.NullMarked;

/**
 * <p>MiniPlaceholdersプラグインとの連携を管理するためのユーティリティクラスです。</p>
 *
 * <p>このクラスは、MiniPlaceholdersがサーバーにロードされているかどうかを確認し、
 * それに応じて適切な{@link TagResolver}を提供します。これにより、MiniPlaceholdersが
 * 存在しない場合でも安全にプレースホルダーを使用できます。</p>
 */
@NullMarked
public final class MiniPlaceholdersExpansion {

    /**
     * このユーティリティクラスのインスタンス化を防ぐためのプライベートコンストラクタ。
     */
    private MiniPlaceholdersExpansion() {
    }

    /**
     * MiniPlaceholdersプラグインがサーバーにロードされているかを確認します。
     *
     * @return MiniPlaceholdersが有効なプラグインとしてロードされていれば{@code true}
     */
    private static boolean miniPlaceholdersLoaded() {
        return Bukkit.getPluginManager().isPluginEnabled("MiniPlaceholders");
    }

    /**
     * 特定のオーディエンスとグローバルプレースホルダーに基づいて{@link TagResolver}を取得します。
     *
     * <p>MiniPlaceholdersがロードされている場合は、{@link MiniPlaceholders#audienceGlobalPlaceholders()}
     * によって取得されるオーディエンスおよびグローバルプレースホルダーを返します。ロードされていない場合は、
     * 何も解決しない空の{@link TagResolver}を返します。</p>
     *
     * @return オーディエンスおよびグローバルプレースホルダーに基づいた{@link TagResolver}
     */
    public static TagResolver audiencePlaceholders() {
        if (MiniPlaceholdersExpansion.miniPlaceholdersLoaded()) {
            return MiniPlaceholders.audienceGlobalPlaceholders();
        }

        return TagResolver.empty();
    }
}
