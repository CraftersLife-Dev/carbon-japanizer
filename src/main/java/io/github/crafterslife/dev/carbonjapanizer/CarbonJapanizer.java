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
package io.github.crafterslife.dev.carbonjapanizer;

import com.zaxxer.hikari.HikariDataSource;
import io.github.crafterslife.dev.carbonjapanizer.command.CommandManagerFactory;
import io.github.crafterslife.dev.carbonjapanizer.command.commands.JapanizeCommand;
import io.github.crafterslife.dev.carbonjapanizer.conversion.config.ForceReplacementConfigFactory;
import io.github.crafterslife.dev.carbonjapanizer.conversion.config.HiraganaReplacementConfigFactory;
import io.github.crafterslife.dev.carbonjapanizer.conversion.config.KanjiReplacementConfigFactory;
import io.github.crafterslife.dev.carbonjapanizer.conversion.config.PreventReplacementConfigFactory;
import io.github.crafterslife.dev.carbonjapanizer.conversion.renderer.JapanizedComponentFormatter;
import io.github.crafterslife.dev.carbonjapanizer.conversion.renderer.RomanComponentJapanizer;
import io.github.crafterslife.dev.carbonjapanizer.database.DataSourceFactory;
import io.github.crafterslife.dev.carbonjapanizer.database.JdbiFactory;
import io.github.crafterslife.dev.carbonjapanizer.database.entity.CarbonJapanizerUser;
import io.github.crafterslife.dev.carbonjapanizer.event.CarbonChatHandler;
import io.github.crafterslife.dev.carbonjapanizer.resource.configuration.ConfigFactory;
import io.github.crafterslife.dev.carbonjapanizer.resource.configuration.configurations.PrimaryConfig;
import io.github.crafterslife.dev.carbonjapanizer.resource.translation.DynamicResourceBundleControl;
import io.github.crafterslife.dev.carbonjapanizer.resource.translation.TranslationStoreInitializer;
import io.github.crafterslife.dev.carbonjapanizer.resource.translation.messages.LoggingService;
import io.github.crafterslife.dev.carbonjapanizer.resource.translation.messages.UserMessage;
import io.github.crafterslife.dev.carbonjapanizer.service.JapanizeService;
import io.github.crafterslife.dev.carbonjapanizer.service.UserService;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.draycia.carbon.api.CarbonChat;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.util.UTF8ResourceBundleControl;
import org.jdbi.v3.core.Jdbi;
import org.jspecify.annotations.NullMarked;

/**
 * {@code CarbonJapanizer} のリソースやロジックをまとめたコンテナ
 */
@NullMarked
public final class CarbonJapanizer {

    private final UserMessage translationService;
    private final UserService userService;
    private final JapanizeService japanizeService;

    /**
     * このクラスのインスタンスを生成する。
     *
     * @param logger        プラグインロガー
     * @param dataDirectory プラグインディレクトリ
     */
    public CarbonJapanizer(final ComponentLogger logger, final Path dataDirectory) {

        // メイン設定
        final var configFactory = new ConfigFactory(dataDirectory);
        final var primaryConfig = configFactory.create(PrimaryConfig.class, new PrimaryConfig());

        // ロギングサービス
        final var loggingService = LoggingService.create(logger);
        TranslationStoreInitializer.initialize(LoggingService.class, UTF8ResourceBundleControl.get());

        // ユーザーメッセージ
        final var control = new DynamicResourceBundleControl(dataDirectory);
        final Collection<Locale> installedLocales = TranslationStoreInitializer.initialize(UserMessage.class, control);
        loggingService.translationLoaded(installedLocales.size(), installedLocales);
        this.translationService = UserMessage.create();

        // ユーザーサービス
        final HikariDataSource dataSource = new DataSourceFactory(primaryConfig, loggingService, dataDirectory).create();
        final Jdbi jdbi = JdbiFactory.create(dataSource);
        this.userService = new UserService(primaryConfig, loggingService, jdbi);

        // かな漢字変換サービス
        final var componentJapanizer = new RomanComponentJapanizer(
                primaryConfig,
                new PreventReplacementConfigFactory(primaryConfig).create(),
                new ForceReplacementConfigFactory(primaryConfig).create(),
                new HiraganaReplacementConfigFactory().create(),
                new KanjiReplacementConfigFactory(loggingService).create()
        );
        final var componentFormatter = new JapanizedComponentFormatter(primaryConfig, MiniMessage.miniMessage());
        this.japanizeService = new JapanizeService(this.userService, componentJapanizer, componentFormatter);
    }

    /**
     * コマンドを登録する。
     *
     * @param commandManagerFactory コマンドマネージャーファクトリ
     */
    public void registerCommands(final CommandManagerFactory commandManagerFactory) {
        final var manager = commandManagerFactory.create(this.userService);
        final var commands = Set.of(new JapanizeCommand(this.userService, this.translationService));
        commands.stream()
                .map(command -> command.create(manager))
                .forEach(manager::command);
    }

    /**
     * {@link CarbonChat} のイベントを操作するハンドラーを登録する
     *
     * @throws IllegalStateException {@link CarbonChat} がロードされていない場合
     */
    public void registerChatEvent() throws IllegalStateException {
        final var chatHandler = new CarbonChatHandler(this.japanizeService);
        chatHandler.register();
    }

    /**
     * ログインしたプレイヤーを操作する。
     *
     * @param uuid 対象ユーザーのUUID
     * @return CompletableFuture
     */
    public CompletableFuture<CarbonJapanizerUser> handleLogin(final UUID uuid) {
        return this.userService.loadUser(uuid);
    }
}
