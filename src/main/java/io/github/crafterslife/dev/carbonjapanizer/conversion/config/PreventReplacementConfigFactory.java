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
package io.github.crafterslife.dev.carbonjapanizer.conversion.config;

import io.github.crafterslife.dev.carbonjapanizer.resource.configuration.configurations.PrimaryConfig;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.jspecify.annotations.NullMarked;

/**
 * かな漢字変換を防止するプレフィクスを {@link Component} から削除するための {@link TextReplacementConfig} を生成するためのファクトリクラス。
 */
@NullMarked
public final class PreventReplacementConfigFactory implements TextReplacementConfigFactory {

    private final PrimaryConfig primaryConfig;

    /**
     * このクラスのインスタンスを生成する。
     *
     * @param primaryConfig メイン設定
     */
    public PreventReplacementConfigFactory(final PrimaryConfig primaryConfig) {
        this.primaryConfig = primaryConfig;
    }

    @Override
    public TextReplacementConfig create() {
        return TextReplacementConfig.builder()
                .once()
                .match("^" + Pattern.quote(this.primaryConfig.preventPrefix()))
                .replacement(Component.empty())
                .build();
    }
}
