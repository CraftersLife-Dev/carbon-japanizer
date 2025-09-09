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
package io.github.crafterslife.dev.carbonjapanizer.resource.configuration;

import io.github.crafterslife.dev.carbonjapanizer.resource.configuration.annotations.ConfigName;
import java.nio.file.Path;
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

/**
 * 設定のファクトリクラス。
 */
@NullMarked
public final class ConfigFactory {

    private final Path dataDirectory;

    /**
     * このクラスのインスタンスを生成する。
     *
     * @param dataDirectory 設定ファイルを保存するためのディレクトリ
     */
    public ConfigFactory(final Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    /**
     * 設定クラスのインスタンスを生成する。
     *
     * @param <T>   設定クラスのモデルクラス
     * @param model 設定クラスのモデルクラス
     * @param base  設定のデフォルト値を持つインスタンス
     * @return 設定クラスのインスタンス
     * @throws UncheckedConfigurateException 設定クラスのインスタンスの生成に失敗した場合
     */
    public <T> T create(final Class<T> model, final T base) throws UncheckedConfigurateException {
        try {
            return ConfigurateHelper.builder(model)
                    .defaultConfiguration(base)
                    .configurationLoader(HoconConfigurationLoader.builder()
                            .prettyPrinting(true)
                            .defaultOptions(options -> options
                                    .shouldCopyDefaults(true)
                                    .header(model.getAnnotation(Header.class).value())
                                    .serializers(ConfigurateComponentSerializer.configurate().serializers()))
                            .path(this.dataDirectory.resolve(model.getAnnotation(ConfigName.class).value()))
                            .build())
                    .build();
        } catch (final ConfigurateException exception) {
            throw new UncheckedConfigurateException(exception);
        }
    }
}
