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

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

/**
 * <p>{@link Component}を{@link Audience}に送信する機能を定義する関数型インターフェースです。</p>
 *
 * <p>このインターフェースは、Adventureの{@link Audience}にメッセージを送信するための
 * 単一のメソッド{@code send}を提供します。これは、Doburokuライブラリによって生成された
 * メッセージサービスの戻り値の型として使用することを目的としています。</p>
 */
@NullMarked
@FunctionalInterface
public interface Message {

    /**
     * 指定されたオーディエンスにメッセージを送信します。
     *
     * @param audience メッセージを受信するオーディエンス
     */
    void send(Audience audience);
}
