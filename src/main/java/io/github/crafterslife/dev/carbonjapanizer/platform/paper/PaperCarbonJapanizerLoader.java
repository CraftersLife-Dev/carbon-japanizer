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

import io.github.crafterslife.dev.carbonjapanizer.CarbonJapanizerDependency;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import org.jspecify.annotations.NullMarked;
import xyz.jpenilla.gremlin.runtime.platformsupport.PaperClasspathAppender;

/**
 * <p>依存関係の解決を担う{@link PluginLoader}の実装。</p>
 *
 * <p>このクラスは、プラグインが起動する前に、必要なライブラリや依存関係が
 * 正しくクラスパスに追加されることを保証しする。</p>
 */
@NullMarked
@SuppressWarnings({"UnstableApiUsage", "unused"})
public final class PaperCarbonJapanizerLoader implements PluginLoader {

    @Override
    public void classloader(final PluginClasspathBuilder classpathBuilder) {
        final var logger = classpathBuilder.getContext().getLogger();
        final var dataDirectory = classpathBuilder.getContext().getDataDirectory();

        final var classpathAppender = new PaperClasspathAppender(classpathBuilder);
        final var dependencies = CarbonJapanizerDependency.resolve(logger, dataDirectory.resolve("libraries"));
        classpathAppender.append(dependencies);
    }
}
