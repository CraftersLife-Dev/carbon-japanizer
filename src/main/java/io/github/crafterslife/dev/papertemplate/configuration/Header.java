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
package io.github.crafterslife.dev.papertemplate.configuration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.jspecify.annotations.NullMarked;

/**
 * <p>設定モデルのレコードクラスに、生成されるYAML設定ファイルのヘッダーを定義するためのアノテーションです。</p>
 */
@NullMarked
@Retention(RetentionPolicy.RUNTIME)
public @interface Header {

    /**
     * 設定ファイルのヘッダーとして使用される文字列を定義します。
     *
     * @return ヘッダー文字列
     */
    String value();
}
