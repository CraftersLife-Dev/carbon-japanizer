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
package io.github.crafterslife.dev.carbonjapanizer.platform.paper;

import io.github.crafterslife.dev.carbonjapanizer.CarbonJapanizer;
import io.github.crafterslife.dev.carbonjapanizer.platform.paper.command.PaperCommandManagerFactory;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import java.util.function.Supplier;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jspecify.annotations.NullMarked;

/**
 * <p>Paperの{@link PluginBootstrap}を実装し、プラグインのブートストラップと初期化ロジックを管理する。</p>
 */
@NullMarked
@SuppressWarnings({"UnstableApiUsage", "unused"})
public final class PaperCarbonJapanizerBootstrapper implements PluginBootstrap {

    private @MonotonicNonNull Supplier<JavaPlugin> javaPlugin;

    @Override
    public void bootstrap(final BootstrapContext context) {

        // プラグインコンテナを生成
        final var container = new CarbonJapanizer(context.getLogger(), context.getDataDirectory());

        // コマンドを登録
        container.registerCommands(new PaperCommandManagerFactory(context));

        // プラグインサプライヤを生成
        this.javaPlugin = () -> new PaperCarbonJapanizer(container);
    }

    @Override
    public JavaPlugin createPlugin(final PluginProviderContext context) {
        return this.javaPlugin.get();
    }
}
