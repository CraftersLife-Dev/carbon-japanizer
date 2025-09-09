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
package io.github.crafterslife.dev.carbonjapanizer.resource.translation.messages;

import io.github.crafterslife.dev.carbonjapanizer.resource.translation.annotations.LogLevel;
import io.github.crafterslife.dev.carbonjapanizer.resource.translation.spi.ThrowableLogger;
import io.github.namiuni.doburoku.annotation.Locales;
import io.github.namiuni.doburoku.annotation.annotations.Key;
import io.github.namiuni.doburoku.annotation.annotations.ResourceBundle;
import io.github.namiuni.doburoku.annotation.annotations.Value;
import io.github.namiuni.doburoku.standard.DoburokuStandard;
import io.github.namiuni.doburoku.standard.argument.MiniMessageArgumentTransformer;
import io.leangen.geantyref.TypeToken;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
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
     * このインターフェースのプロキシインスタンスを生成する。
     *
     * @param logger ロギングに使用する {@link ComponentLogger} のインスタンス
     * @return このインターフェースのプロキシインスタンス
     */
    static LoggingService create(final ComponentLogger logger) {
        return DoburokuStandard.of(LoggingService.class)
                .argument(registry -> registry
                                .plus(new TypeToken<Collection<Locale>>() {
                                }, (parameter, locales) -> {
                                    final List<TextComponent> components = locales.stream()
                                            .map(Locale::getDisplayName)
                                            .map(Component::text)
                                            .toList();
                                    final JoinConfiguration joinConfig = JoinConfiguration.arrayLike();
                                    return Component.join(joinConfig, components);
                                }),
                        MiniMessageArgumentTransformer.create())
                .result(registry -> registry
                        .plus(void.class, (method, component) -> {
                            final Level level = method.getAnnotation(LogLevel.class).value();
                            switch (level) {
                                case INFO -> logger.info(component);
                                case WARN -> logger.warn(component);
                                case ERROR -> logger.error(component);
                                case DEBUG -> logger.debug(component);
                                case TRACE -> logger.trace(component);
                            }
                            return null;
                        })
                        .plus(ThrowableLogger.class, (method, component) -> throwable -> {
                            final Level level = method.getAnnotation(LogLevel.class).value();
                            switch (level) {
                                case INFO -> logger.info(component, throwable);
                                case WARN -> logger.warn(component, throwable);
                                case ERROR -> logger.error(component, throwable);
                                case DEBUG -> logger.debug(component, throwable);
                                case TRACE -> logger.trace(component, throwable);
                            }
                        })
                )
                .brew();
    }

    /**
     * 設定の読み込みが正常に完了したときに記録するメッセージ
     *
     * @param configName 設定ファイル名
     */
    @LogLevel(Level.INFO)
    @Key("carbon_japanizer.config.loaded")
    @Value(locale = Locales.EN_US, content = "Loaded configuration: <config_name>")
    @Value(locale = Locales.JA_JP, content = "設定を読み込みました: <config_name>")
    void configurationLoaded(String configName);

    /**
     * 翻訳の読み込みが正常に完了したときに記録するメッセージ
     *
     * @param totalLocale 読み込まれた合計ロケール数
     * @param locales     読み込まれたロケールのコレクション
     */
    @LogLevel(Level.INFO)
    @Key("carbon_japanizer.translation.loaded")
    @Value(locale = Locales.EN_US, content = "Loaded <total_locale> translations: <locales>")
    @Value(locale = Locales.JA_JP, content = "<total_locale>件の翻訳を読み込みました: <locales>")
    void translationLoaded(int totalLocale, Collection<Locale> locales);

    /**
     * HikariCPのスレッドで未処理の例外が発生したときに記録するメッセージ
     *
     * @param threadName 例外が発生したスレッドの名称
     * @return 例外を記録するロガー
     */
    @LogLevel(Level.WARN)
    @Key("carbon_japanizer.database.thread.uncaught")
    @Value(locale = Locales.EN_US, content = "Uncaught exception on thread <thread_name>.")
    @Value(locale = Locales.JA_JP, content = "スレッド「<thread_name>」で例外が発生しました。")
    ThrowableLogger databaseThreadUncaught(String threadName);

    /**
     * ひらがなから漢字への変換に失敗したときに記録するメッセージ
     *
     * @param text 変換を施行したテキスト
     * @return 例外を記録するロガー
     */
    @LogLevel(Level.WARN)
    @Key("carbon_japanizer.japanize.kana-to-kanji.failed")
    @Value(locale = Locales.EN_US, content = "Failed to convert to kanji text: <text>")
    @Value(locale = Locales.JA_JP, content = "ひらがなから漢字への変換に失敗: <text>")
    ThrowableLogger japanizeKanaToKanjiFailed(String text);

    /**
     * ユーザーデータの保存に失敗したときに記録するメッセージ
     *
     * @param userName ユーザー名
     * @param userId   ユーザーID
     * @return 例外を記録するロガー
     */
    @LogLevel(Level.ERROR)
    @Key("carbon_japanizer.user.save.failed")
    @Value(locale = Locales.EN_US, content = "Failed to save user data: <user_name> (<user_id>)")
    @Value(locale = Locales.JA_JP, content = "ユーザーデータの保存に失敗: <user_name> (<user_id>)")
    ThrowableLogger userSaveFailed(Component userName, UUID userId);
}
