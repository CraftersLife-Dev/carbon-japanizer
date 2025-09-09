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
package io.github.crafterslife.dev.carbonjapanizer.platform.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github.crafterslife.dev.carbonjapanizer.CarbonJapanizer;
import io.github.crafterslife.dev.carbonjapanizer.CarbonJapanizerDependency;
import io.github.crafterslife.dev.carbonjapanizer.platform.velocity.command.VelocityCommandManagerFactory;
import io.github.crafterslife.dev.carbonjapanizer.platform.velocity.event.VelocityLoginEventHandler;
import java.nio.file.Path;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jspecify.annotations.NullMarked;
import xyz.jpenilla.gremlin.runtime.platformsupport.VelocityClasspathAppender;

/**
 * Velocityプラグインのライフサイクルを管理する。
 */
@NullMarked
public final class VelocityCarbonJapanizer {

    private final ProxyServer server;
    private final PluginContainer pluginContainer;
    private final ComponentLogger logger;
    private final Path dataDirectory;

    /**
     * このクラスのインスタンスを生成する。
     *
     * @param server プロキシサーバー
     * @param pluginContainer プラグインコンテナ
     * @param logger ロガー
     * @param dataDirectory プラグインディレクトリ
     */
    @Inject
    public VelocityCarbonJapanizer(
            final ProxyServer server,
            final PluginContainer pluginContainer,
            final ComponentLogger logger,
            final @DataDirectory Path dataDirectory
    ) {
        this.server = server;
        this.pluginContainer = pluginContainer;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    private void onInitialize(final ProxyInitializeEvent event) {
        // 依存関係の解決
        final var classpathAppender = new VelocityClasspathAppender(this.server, this);
        final var dependencies = CarbonJapanizerDependency.resolve(this.logger, this.dataDirectory.resolve("libraries"));
        classpathAppender.append(dependencies);

        final var carbonJapanizer = new CarbonJapanizer(this.logger, this.dataDirectory);

        // ログインイベントハンドラーを登録
        final var loginHandler = new VelocityLoginEventHandler(carbonJapanizer);
        this.server.getEventManager().register(this, LoginEvent.class, loginHandler);

        // Carbonイベントハンドラーを登録
        carbonJapanizer.registerChatEvent();

        // コマンドを登録
        final var commandManagerFactory = new VelocityCommandManagerFactory(this.pluginContainer, this.server);
        carbonJapanizer.registerCommands(commandManagerFactory);
    }
}
