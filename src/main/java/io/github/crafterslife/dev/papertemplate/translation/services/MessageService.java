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
package io.github.crafterslife.dev.papertemplate.translation.services;

import io.github.crafterslife.dev.papertemplate.translation.Message;
import io.github.namiuni.doburoku.annotation.Locales;
import io.github.namiuni.doburoku.annotation.annotations.Key;
import io.github.namiuni.doburoku.annotation.annotations.ResourceBundle;
import io.github.namiuni.doburoku.annotation.annotations.Value;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * プレイヤーなどに送信するメッセージを管理するためのサービスインターフェース。
 */
@NullMarked
@ApiStatus.NonExtendable
@ResourceBundle(baseName = "translations/message")
public interface MessageService {

    /**
     * サンプルメッセージ
     *
     * @return メッセージインスタンス
     */
    @Key("template.sample")
    @Value(locale = Locales.EN_US, content = "<info>Hello, World!")
    @Value(locale = Locales.JA_JP, content = "<info>こんにちは、世界！")
    Message sample();
}
