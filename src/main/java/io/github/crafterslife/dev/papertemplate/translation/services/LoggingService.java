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

import io.github.crafterslife.dev.papertemplate.translation.annotations.LogLevel;
import io.github.namiuni.doburoku.annotation.Locales;
import io.github.namiuni.doburoku.annotation.annotations.Key;
import io.github.namiuni.doburoku.annotation.annotations.ResourceBundle;
import io.github.namiuni.doburoku.annotation.annotations.Value;
import java.util.Collection;
import java.util.Locale;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.slf4j.event.Level;

/**
 * プラグインのログに記録されるメッセージを管理するためのサービスインターフェース。
 */
@NullMarked
@ApiStatus.NonExtendable
@ResourceBundle(baseName = "translations/logging")
public interface LoggingService {

    /**
     * 設定の読み込みが完了したときにログに記録されるメッセージ
     *
     * @param configName 設定ファイル名
     */
    @LogLevel(Level.INFO)
    @Key("template.info.config.loaded")
    @Value(locale = Locales.EN_US, content = "Loaded configuration: <config_name>")
    @Value(locale = Locales.JA_JP, content = "設定を読み込みました: <config_name>")
    void configurationLoaded(String configName);

    /**
     * 翻訳の読み込みが完了したときにログに記録されるメッセージ
     *
     * @param totalLocale 読み込まれた合計ロケール数
     * @param locales 読み込まれたロケールのコレクション
     */
    @LogLevel(Level.INFO)
    @Key("template.info.translation.loaded")
    @Value(locale = Locales.EN_US, content = "Loaded <total_locale> translations: <locales>")
    @Value(locale = Locales.JA_JP, content = "<total_locale>件の翻訳を読み込みました: <locales>")
    void translationLoaded(int totalLocale, Collection<Locale> locales);
}
