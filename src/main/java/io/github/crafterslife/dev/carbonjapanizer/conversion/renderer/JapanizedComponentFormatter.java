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
package io.github.crafterslife.dev.carbonjapanizer.conversion.renderer;

import io.github.crafterslife.dev.carbonjapanizer.integration.MiniPlaceholdersExpansion;
import io.github.crafterslife.dev.carbonjapanizer.resource.configuration.configurations.PrimaryConfig;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import org.jspecify.annotations.NullMarked;

/**
 * ローマ字をかな漢字へ変換したあとのフォーマットを担う。
 */
@NullMarked
public final class JapanizedComponentFormatter implements ComponentRenderer<Component> {

    private final PrimaryConfig primaryConfig;
    private final MiniMessage miniMessage;

    /**
     * このクラスのインスタンスを生成する。
     *
     * @param primaryConfig メイン設定
     * @param miniMessage フォーマットに使用する {@link MiniMessage} のインスタンス
     */
    public JapanizedComponentFormatter(
            final PrimaryConfig primaryConfig,
            final MiniMessage miniMessage
    ) {
        this.primaryConfig = primaryConfig;
        this.miniMessage = miniMessage;
    }

    @Override
    public Component render(final Component originalMessage, final Component japanizedMessage) {
        if (Objects.equals(originalMessage, japanizedMessage)) {
            return originalMessage;
        }

        if (this.primaryConfig.messageFormat().isEmpty()) {
            return japanizedMessage;
        }

        final var tagResolver = TagResolver.builder()
                .resolver(Placeholder.component("japanized_message", japanizedMessage))
                .resolver(Placeholder.component("previous_message", originalMessage))
                .resolver(MiniPlaceholdersExpansion.audiencePlaceholders())
                .build();

        return this.miniMessage.deserialize(this.primaryConfig.messageFormat(), tagResolver);
    }
}
