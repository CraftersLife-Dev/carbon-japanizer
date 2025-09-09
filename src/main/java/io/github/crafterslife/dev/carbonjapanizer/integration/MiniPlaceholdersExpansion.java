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

import io.github.miniplaceholders.api.MiniPlaceholders;
import java.util.Objects;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

/**
 * {@code MiniPlaceholders} プラグインとの連携を管理するためのユーティリティクラスです。
 *
 * <p>このクラスは、MiniPlaceholdersがサーバーにロードされているかどうかを確認し、
 * それに応じて適切な{@link TagResolver}を提供します。これにより、MiniPlaceholdersが
 * 存在しない場合でも安全にプレースホルダーを使用できます。</p>
 *
 * @see <a href="https://github.com/MiniPlaceholders/MiniPlaceholders">MiniPlaceholders</a>
 */
@NullMarked
public final class MiniPlaceholdersExpansion {

    private static byte miniPlaceholdersLoaded = -1;

    private MiniPlaceholdersExpansion() {
    }

    /**
     * {@code MiniPlaceholders} がロードされているか確認する。
     *
     * @return {@code MiniPlaceholders} がロードされている場合はtrue
     */
    private static boolean miniPlaceholdersLoaded() {
        if (miniPlaceholdersLoaded == -1) {
            try {
                final String name = MiniPlaceholders.class.getName();
                Objects.requireNonNull(name);
                miniPlaceholdersLoaded = 1;
            } catch (final NoClassDefFoundError error) {
                miniPlaceholdersLoaded = 0;
            }
        }
        return miniPlaceholdersLoaded == 1;
    }

    /**
     * レンダリング対象のオーディエンスとグローバルプレースホルダーに基づいて{@link TagResolver}を取得する。
     *
     * <p>{@code MiniPlaceholders} がロードされている場合は、{@link MiniPlaceholders#audienceGlobalPlaceholders()}
     * によって取得されるオーディエンスおよびグローバルプレースホルダーを返す。ロードされていない場合は、
     * 空の{@link TagResolver}を返します。</p>
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
