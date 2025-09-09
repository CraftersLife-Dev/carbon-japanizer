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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.kyori.adventure.text.TextReplacementConfig;
import org.jspecify.annotations.NullMarked;

/**
 * {@link net.kyori.adventure.text.Component} 内のローマ字をひらがなに変換するための
 * {@link TextReplacementConfig} を生成するためのファクトリクラス。
 */
// Based on LunaChat design: https://github.com/ucchyocean/LunaChat/blob/master/src/main/java/com/github/ucchyocean/lc3/japanize/YukiKanaConverter.java
@NullMarked
public final class HiraganaReplacementConfigFactory implements TextReplacementConfigFactory {

    /**
     * このクラスのインスタンスを生成する。
     */
    public HiraganaReplacementConfigFactory() {
    }

    @Override
    public TextReplacementConfig create() {
        final Map<String, String> romajis = HiraganaReplacementConfigFactory.defaultRomajiMap();
        final Pattern romajiPattern = romajis.keySet().stream()
                .sorted((s1, s2) -> s2.length() - s1.length()) // キーを文字数の降順でソートする
                .map(this::formatRegex)
                .collect(Collectors.joining("|"))
                .transform(Pattern::compile);

        return TextReplacementConfig.builder()
                .match(romajiPattern)
                .replacement((matchResult, builder) -> {
                    final String input = matchResult.group();
                    final String hiragana = romajis.get(input);
                    return builder.content(hiragana);
                })
                .build();
    }

    private String formatRegex(final String string) {
        final StringBuilder escaped = new StringBuilder();
        final String specialChars = ".^$*+?{}[]\\|()";
        for (final char c : string.toCharArray()) {
            if (specialChars.indexOf(c) != -1) {
                escaped.append('\\');
            }
            escaped.append(c);
        }
        return escaped.toString();
    }

    /**
     * ローマ字に対応するひらがなのマップ。
     *
     * @return ローマ字に対応するひらがなのマップ
     */
    private static Map<String, String> defaultRomajiMap() {
        final Map<String, String> romajiMap = new LinkedHashMap<>();

        // ひらがな
        romajiMap.put("a", "あ");
        romajiMap.put("i", "い"); romajiMap.put("yi", "い");
        romajiMap.put("u", "う"); romajiMap.put("wu", "う"); romajiMap.put("whu", "う");
        romajiMap.put("e", "え");
        romajiMap.put("o", "お");

        romajiMap.put("wha", "うぁ");
        romajiMap.put("whi", "うぃ"); romajiMap.put("wi", "うぃ");
        //
        romajiMap.put("whe", "うぇ"); romajiMap.put("we", "うぇ");
        romajiMap.put("who", "うぉ");

        //
        romajiMap.put("wyi", "ゐ");
        //
        romajiMap.put("wye", "ゑ");
        //

        romajiMap.put("la", "ぁ"); romajiMap.put("xa", "ぁ");
        romajiMap.put("li", "ぃ"); romajiMap.put("xi", "ぃ"); romajiMap.put("lyi", "ぃ"); romajiMap.put("xyi", "ぃ");
        romajiMap.put("lu", "ぅ"); romajiMap.put("xu", "ぅ");
        romajiMap.put("le", "ぇ"); romajiMap.put("xe", "ぇ"); romajiMap.put("lye", "ぇ"); romajiMap.put("xye", "ぇ");
        romajiMap.put("lo", "ぉ"); romajiMap.put("xo", "ぉ");

        //
        romajiMap.put("ye", "いぇ");
        //
        //
        //

        romajiMap.put("ka", "か"); romajiMap.put("ca", "か");
        romajiMap.put("ki", "き");
        romajiMap.put("ku", "く"); romajiMap.put("cu", "く"); romajiMap.put("qu", "く");
        romajiMap.put("ke", "け");
        romajiMap.put("ko", "こ"); romajiMap.put("co", "こ");

        romajiMap.put("kya", "きゃ");
        romajiMap.put("kyi", "きぃ");
        romajiMap.put("kyu", "きゅ");
        romajiMap.put("kye", "きぇ");
        romajiMap.put("kyo", "きょ");

        romajiMap.put("qya", "くゃ");
        //
        romajiMap.put("qyu", "くゅ");
        //
        romajiMap.put("qyo", "くょ");

        romajiMap.put("qwa", "くぁ"); romajiMap.put("qa", "くぁ"); romajiMap.put("kwa", "くぁ");
        romajiMap.put("qwi", "くぃ"); romajiMap.put("qi", "くぃ"); romajiMap.put("qyi", "くぃ");
        romajiMap.put("qwu", "くぅ");
        romajiMap.put("qwe", "くぇ"); romajiMap.put("qe", "くぇ"); romajiMap.put("qye", "くぇ");
        romajiMap.put("qwo", "くぉ"); romajiMap.put("qo", "くぉ"); romajiMap.put("kwo", "くぉ");

        romajiMap.put("ga", "が");
        romajiMap.put("gi", "ぎ");
        romajiMap.put("gu", "ぐ");
        romajiMap.put("ge", "げ");
        romajiMap.put("go", "ご");

        romajiMap.put("gya", "ぎゃ");
        romajiMap.put("gyi", "ぎぃ");
        romajiMap.put("gyu", "ぎゅ");
        romajiMap.put("gye", "ぎぇ");
        romajiMap.put("gyo", "ぎょ");

        romajiMap.put("gwa", "ぐぁ");
        romajiMap.put("gwi", "ぐぃ");
        romajiMap.put("gwu", "ぐぅ");
        romajiMap.put("gwe", "ぐぇ");
        romajiMap.put("gwo", "ぐぉ");

        romajiMap.put("lka", "ヵ"); romajiMap.put("xka", "ヵ");
        //
        //
        romajiMap.put("lke", "ヶ"); romajiMap.put("xke", "ヶ");
        //

        romajiMap.put("sa", "さ");
        romajiMap.put("si", "し"); romajiMap.put("ci", "し"); romajiMap.put("shi", "し");
        romajiMap.put("su", "す");
        romajiMap.put("se", "せ"); romajiMap.put("ce", "せ");
        romajiMap.put("so", "そ");

        romajiMap.put("sya", "しゃ"); romajiMap.put("sha", "しゃ");
        romajiMap.put("syi", "しぃ");
        romajiMap.put("syu", "しゅ"); romajiMap.put("shu", "しゅ");
        romajiMap.put("sye", "しぇ"); romajiMap.put("she", "しぇ");
        romajiMap.put("syo", "しょ"); romajiMap.put("sho", "しょ");

        romajiMap.put("swa", "すぁ");
        romajiMap.put("swi", "すぃ");
        romajiMap.put("swu", "すぅ");
        romajiMap.put("swe", "すぇ");
        romajiMap.put("swo", "すぉ");

        romajiMap.put("za", "ざ");
        romajiMap.put("zi", "じ"); romajiMap.put("ji", "じ");
        romajiMap.put("zu", "ず");
        romajiMap.put("ze", "ぜ");
        romajiMap.put("zo", "ぞ");

        romajiMap.put("zya", "じゃ"); romajiMap.put("ja", "じゃ"); romajiMap.put("jya", "じゃ");
        romajiMap.put("zyi", "じぃ")/*                      */; romajiMap.put("jyi", "じぃ");
        romajiMap.put("zyu", "じゅ"); romajiMap.put("ju", "じゅ"); romajiMap.put("jyu", "じゅ");
        romajiMap.put("zye", "じぇ"); romajiMap.put("je", "じぇ"); romajiMap.put("jye", "じぇ");
        romajiMap.put("zyo", "じょ"); romajiMap.put("jo", "じょ"); romajiMap.put("jyo", "じょ");

        romajiMap.put("ta", "た");
        romajiMap.put("ti", "ち"); romajiMap.put("chi", "ち");
        romajiMap.put("tu", "つ"); romajiMap.put("tsu", "つ");
        romajiMap.put("te", "て");
        romajiMap.put("to", "と");

        romajiMap.put("tya", "ちゃ"); romajiMap.put("cha", "ちゃ"); romajiMap.put("cya", "ちゃ");
        romajiMap.put("tyi", "ちぃ")/*                       */; romajiMap.put("cyi", "ちぃ");
        romajiMap.put("tyu", "ちゅ"); romajiMap.put("chu", "ちゅ"); romajiMap.put("cyu", "ちゅ");
        romajiMap.put("tye", "ちぇ"); romajiMap.put("che", "ちぇ"); romajiMap.put("cye", "ちぇ");
        romajiMap.put("tyo", "ちょ"); romajiMap.put("cho", "ちょ"); romajiMap.put("cyo", "ちょ");

        romajiMap.put("tsa", "つぁ");
        romajiMap.put("tsi", "つぃ");
        //
        romajiMap.put("tse", "つぇ");
        romajiMap.put("tso", "つぉ");

        romajiMap.put("tha", "てゃ");
        romajiMap.put("thi", "てぃ");
        romajiMap.put("thu", "てゅ");
        romajiMap.put("the", "てぇ");
        romajiMap.put("tho", "てょ");

        romajiMap.put("twa", "とぁ");
        romajiMap.put("twi", "とぃ");
        romajiMap.put("twu", "とぅ");
        romajiMap.put("twe", "とぇ");
        romajiMap.put("two", "とぉ");

        romajiMap.put("da", "だ");
        romajiMap.put("di", "ぢ");
        romajiMap.put("du", "づ");
        romajiMap.put("de", "で");
        romajiMap.put("do", "ど");

        romajiMap.put("dya", "ぢゃ");
        romajiMap.put("dyi", "ぢぃ");
        romajiMap.put("dyu", "ぢゅ");
        romajiMap.put("dye", "ぢぇ");
        romajiMap.put("dyo", "ぢょ");

        romajiMap.put("dha", "でゃ");
        romajiMap.put("dhi", "でぃ");
        romajiMap.put("dhu", "でゅ");
        romajiMap.put("dhe", "でぇ");
        romajiMap.put("dho", "でょ");

        romajiMap.put("dwa", "どぁ");
        romajiMap.put("dwi", "どぃ");
        romajiMap.put("dwu", "どぅ");
        romajiMap.put("dwe", "どぇ");
        romajiMap.put("dwo", "どぉ");

        //
        //
        romajiMap.put("ltu", "っ"); romajiMap.put("xtu", "っ"); romajiMap.put("ltsu", "っ"); romajiMap.put("xtsu", "っ");
        //
        //

        romajiMap.put("na", "な");
        romajiMap.put("ni", "に");
        romajiMap.put("nu", "ぬ");
        romajiMap.put("ne", "ね");
        romajiMap.put("no", "の");

        romajiMap.put("nya", "にゃ");
        romajiMap.put("nyi", "にぃ");
        romajiMap.put("nyu", "にゅ");
        romajiMap.put("nye", "にぇ");
        romajiMap.put("nyo", "にょ");

        romajiMap.put("ha", "は");
        romajiMap.put("hi", "ひ");
        romajiMap.put("hu", "ふ"); romajiMap.put("fu", "ふ");
        romajiMap.put("he", "へ");
        romajiMap.put("ho", "ほ");

        romajiMap.put("hya", "ひゃ");
        romajiMap.put("hyi", "ひぃ");
        romajiMap.put("hyu", "ひゅ");
        romajiMap.put("hye", "ひぇ");
        romajiMap.put("hyo", "ひょ");

        romajiMap.put("fwa", "ふぁ"); romajiMap.put("fa", "ふぁ");
        romajiMap.put("fwi", "ふぃ"); romajiMap.put("fi", "ふぃ"); romajiMap.put("fyi", "ふぃ");
        romajiMap.put("fwu", "ふぅ");
        romajiMap.put("fwe", "ふぇ"); romajiMap.put("fe", "ふぇ"); romajiMap.put("fye", "ふぇ");
        romajiMap.put("fwo", "ふぉ"); romajiMap.put("fo", "ふぉ");

        romajiMap.put("fya", "ふゃ");
        //
        romajiMap.put("fyu", "ふゅ");
        //
        romajiMap.put("fyo", "ふょ");

        romajiMap.put("ba", "ば");
        romajiMap.put("bi", "び");
        romajiMap.put("bu", "ぶ");
        romajiMap.put("be", "べ");
        romajiMap.put("bo", "ぼ");

        romajiMap.put("bya", "びゃ");
        romajiMap.put("byi", "びぃ");
        romajiMap.put("byu", "びゅ");
        romajiMap.put("bye", "びぇ");
        romajiMap.put("byo", "びょ");

        romajiMap.put("va", "ヴぁ");
        romajiMap.put("vi", "ヴぃ");
        romajiMap.put("vu", "ヴ");
        romajiMap.put("ve", "ヴぇ");
        romajiMap.put("vo", "ヴぉ");

        romajiMap.put("vya", "ヴゃ");
        romajiMap.put("vyi", "ヴぃ");
        romajiMap.put("vyu", "ヴゅ");
        romajiMap.put("vye", "ヴぇ");
        romajiMap.put("vyo", "ヴょ");

        romajiMap.put("pa", "ぱ");
        romajiMap.put("pi", "ぴ");
        romajiMap.put("pu", "ぷ");
        romajiMap.put("pe", "ぺ");
        romajiMap.put("po", "ぽ");

        romajiMap.put("pya", "ぴゃ");
        romajiMap.put("pyi", "ぴぃ");
        romajiMap.put("pyu", "ぴゅ");
        romajiMap.put("pye", "ぴぇ");
        romajiMap.put("pyo", "ぴょ");

        romajiMap.put("ma", "ま");
        romajiMap.put("mi", "み");
        romajiMap.put("mu", "む");
        romajiMap.put("me", "め");
        romajiMap.put("mo", "も");

        romajiMap.put("mya", "みゃ");
        romajiMap.put("myi", "みぃ");
        romajiMap.put("myu", "みゅ");
        romajiMap.put("mye", "みぇ");
        romajiMap.put("myo", "みょ");

        romajiMap.put("ya", "や");
        //
        romajiMap.put("yu", "ゆ");
        //
        romajiMap.put("yo", "よ");

        romajiMap.put("lya", "ゃ"); romajiMap.put("xya", "ゃ");
        //
        romajiMap.put("lyu", "ゅ"); romajiMap.put("xyu", "ゅ");
        //
        romajiMap.put("lyo", "ょ"); romajiMap.put("xyo", "ょ");

        romajiMap.put("ra", "ら");
        romajiMap.put("ri", "り");
        romajiMap.put("ru", "る");
        romajiMap.put("re", "れ");
        romajiMap.put("ro", "ろ");

        romajiMap.put("rya", "りゃ");
        romajiMap.put("ryi", "りぃ");
        romajiMap.put("ryu", "りゅ");
        romajiMap.put("rye", "りぇ");
        romajiMap.put("ryo", "りょ");

        romajiMap.put("wa", "わ");
        //
        //
        //
        romajiMap.put("wo", "を");

        romajiMap.put("lwa", "ゎ"); romajiMap.put("xwa", "ゎ");
        //
        //
        //
        //

        romajiMap.put("n", "ん"); romajiMap.put("nn", "ん"); romajiMap.put("n'", "ん"); romajiMap.put("xn", "ん");

        // 促音を追加する
        Set.copyOf(romajiMap.entrySet()).stream()
                .filter(entry -> entry.getKey().matches("^[^aiueon].*"))
                .forEach(entry -> {
                    final var romaji = entry.getKey();
                    final var hiragana = entry.getValue();
                    romajiMap.put(romaji.charAt(0) + romaji, "っ" + hiragana);
                });

        // 記号とか
        romajiMap.put("-", "ー");
        romajiMap.put(",", "、");
        romajiMap.put(".", "。");
        romajiMap.put("?", "？");
        romajiMap.put("!", "！");
        romajiMap.put("[", "「"); romajiMap.put("]", "」");
        romajiMap.put("<", "＜"); romajiMap.put(">", "＞");
        romajiMap.put("&", "＆");
        romajiMap.put("\"", "”");
        romajiMap.put("(", "（"); romajiMap.put(")", "）");

        return romajiMap;
    }
}
