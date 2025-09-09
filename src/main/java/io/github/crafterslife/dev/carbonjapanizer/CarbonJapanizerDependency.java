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

import java.nio.file.Path;
import java.util.Set;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jspecify.annotations.NullMarked;
import xyz.jpenilla.gremlin.runtime.DependencyCache;
import xyz.jpenilla.gremlin.runtime.DependencyResolver;
import xyz.jpenilla.gremlin.runtime.DependencySet;

/**
 * {@code CarbonJapanizer} の依存関係を解決を担う。
 */
@NullMarked
public final class CarbonJapanizerDependency {

    private CarbonJapanizerDependency() {
    }

    /**
     * {@code CarbonJapanizer} の依存関係を解決する。
     *
     * @param logger 例外発生時に使用するロガー
     * @param cacheDirectory 依存関係をキャッシュするディレクトリ
     * @return 解決された依存関係のパス
     */
    public static Set<Path> resolve(final ComponentLogger logger, final Path cacheDirectory) {
        final DependencySet dependencies = DependencySet.readFromClasspathResource(
            CarbonJapanizerDependency.class.getClassLoader(), "dependencies.txt");
        final DependencyCache cache = new DependencyCache(cacheDirectory);
        final Set<Path> files;
        try (DependencyResolver downloader = new DependencyResolver(logger)) {
            files = downloader.resolve(dependencies, cache).jarFiles();
        }
        cache.cleanup();
        return files;
    }
}
