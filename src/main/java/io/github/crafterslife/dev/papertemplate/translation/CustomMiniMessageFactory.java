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
package io.github.crafterslife.dev.papertemplate.translation;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * カスタムカラータグを含むMiniMessageのインスタンスを提供するファクトリークラス。
 */
@NullMarked
public final class CustomMiniMessageFactory {

    @Contract(pure = true)
    private CustomMiniMessageFactory() {
    }

    /**
     * カスタムタグをサポートするMiniMessageインスタンスを作成します。
     *
     * <p>このインスタンスは、標準的なMiniMessageタグに加えて、特定の意味を持つ
     * カスタムカラータグ（{@code <error>}、{@code <warn>}、{@code <info>}、{@code <debug>}）を
     * 解決します。これらのタグは、JIS安全色に基づいており、どのような環境下でも認識しやすいユニバーサルデザインカラーが採用されています。</p>
     *
     * @return カスタムタグが登録された{@link MiniMessage}インスタンス
     */
    public static MiniMessage create() {

        // JIS Z 9103 https://ja.wikipedia.org/wiki/JIS%E5%AE%89%E5%85%A8%E8%89%B2
        final TextColor red = TextColor.color(Integer.parseInt("ff4b00", 16));
        final TextColor yellow = TextColor.color(Integer.parseInt("f2e700", 16));
        final TextColor green = TextColor.color(Integer.parseInt("00b06b", 16));
        final TextColor blue = TextColor.color(Integer.parseInt("1971ff", 16));

        final TagResolver.Builder tagResolver = TagResolver.builder()
                .resolver(TagResolver.standard())
                .tag("error", Tag.styling(red))
                .tag("warn", Tag.styling(yellow))
                .tag("info", Tag.styling(green))
                .tag("debug", Tag.styling(blue));

        return MiniMessage.builder()
                .tags(tagResolver.build())
                .build();
    }
}
