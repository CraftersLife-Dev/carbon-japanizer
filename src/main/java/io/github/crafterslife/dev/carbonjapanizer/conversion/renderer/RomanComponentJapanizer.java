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

import io.github.crafterslife.dev.carbonjapanizer.resource.configuration.configurations.PrimaryConfig;
import java.util.regex.Matcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jspecify.annotations.NullMarked;

/**
 * {@link Component} のローマ字をかな漢字へ変換するクラス。
 */
@NullMarked
public final class RomanComponentJapanizer {

    private final PrimaryConfig primaryConfig;
    private final TextReplacementConfig preventPrefixRemover;
    private final TextReplacementConfig forcePrefixReplace;
    private final TextReplacementConfig romajiToHiragana;
    private final TextReplacementConfig hiraganaToKanji;

    /**
     * このクラスのインスタンスを生成する。
     *
     * @param primaryConfig        メイン設定
     * @param preventPrefixRemover かな漢字変換を防止するプレフィクスを削除するための {@link TextReplacementConfig}
     * @param forcePrefixReplace   かな漢字変換を強制するプレフィクスを削除するための {@link TextReplacementConfig}
     * @param romajiToHiragana     ローマ字を日本語へ変換するための {@link TextReplacementConfig}
     * @param hiraganaToKanji      ひらがなを漢字へ変換するための {@link TextReplacementConfig}
     */
    public RomanComponentJapanizer(
            final PrimaryConfig primaryConfig,
            final TextReplacementConfig preventPrefixRemover,
            final TextReplacementConfig forcePrefixReplace,
            final TextReplacementConfig romajiToHiragana,
            final TextReplacementConfig hiraganaToKanji
    ) {
        this.primaryConfig = primaryConfig;
        this.preventPrefixRemover = preventPrefixRemover;
        this.forcePrefixReplace = forcePrefixReplace;
        this.romajiToHiragana = romajiToHiragana;
        this.hiraganaToKanji = hiraganaToKanji;
    }

    /**
     * {@link Component} 内のローマ字文字列をかな漢字へ変換する。
     *
     * @param input 変換対象の {@link Component}
     * @return 変換後の {@link Component}
     */
    public Component render(final Component input) {

        // メッセージが変換防止プレフィクスから始まっている場合はプレフィクスを削除した結果を返す
        final String plainMessage = PlainTextComponentSerializer.plainText().serialize(input);
        final String preventPrefix = this.primaryConfig.preventPrefix();
        if (plainMessage.startsWith(preventPrefix)) {
            return input.replaceText(this.preventPrefixRemover);
        }

        // メッセージが変換強制プレフィクスから始まっている、または変換条件を満たしている場合は、
        // 「プレフィクス削除 -> ローマ字 -> ひらがな -> 漢字」 の順番で変換を試行した結果を返す
        final String forcePrefix = this.primaryConfig.forcePrefix();
        final Matcher japanizeMatcher = this.primaryConfig.convertCondition().matcher(plainMessage);
        if (plainMessage.startsWith(forcePrefix) || japanizeMatcher.matches()) {
            return input.replaceText(this.forcePrefixReplace).compact()
                    .replaceText(this.romajiToHiragana).compact()
                    .replaceText(this.hiraganaToKanji).compact();
        }

        // どの条件にも合致しない場合はそのまま返す
        return input;
    }
}
