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
package io.github.crafterslife.dev.carbonjapanizer.resource.configuration.configurations;

import io.github.crafterslife.dev.carbonjapanizer.resource.configuration.Header;
import io.github.crafterslife.dev.carbonjapanizer.resource.configuration.annotations.ConfigName;
import java.util.regex.Pattern;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@NullMarked
@ConfigSerializable
@ConfigName("config.conf")
@Header("""
        チャットメッセージかな漢字変換プラグインのメイン設定です。
        """)
@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal", "CheckStyle"})
public final class PrimaryConfig {

    @ConfigSerializable
    public record DatabaseSettings(

            @Comment("""
                    JDBC URL. e.g.:
                    MySQL: jdbc:mysql://localhost:3306/carbon_japanizer
                    H2: jdbc:h2:{plugin_directory}/carbon-japanizer-h2;MODE=MySQL
                    """)
            String url,

            @Comment("The connection username.")
            String username,

            @Comment("The connection password.")
            String password
    ) {
    }

    @Comment("""
            かな漢字変換の標準設定値
            この値がtrueの場合は標準でかな漢字変換が有効になります。
            有効な値: boolean (true|false)
            """)

    private boolean defaultJapanize = true;

    @Comment("""
            かな漢字変換を試行する条件となる正規表現パターン
            送信メッセージがこの正規表現に合致した場合に限りかな漢字変換が試行されます。
            初期値は、空文字、ひらがな、カタカナ、漢字を含まない事を前提とした３文字以上のローマ字表記を検索する正規表現です。
            初期値で合致しない例: hi! | un | ¯\\_(ツ)_/¯ | watashiha unitarou desu.
            有効な値: 正規表現
            """)
    private Pattern convertCondition = Pattern.compile("^(?!.*[\\t\\p{IsHiragana}\\p{IsKatakana}\\p{IsHan}])(?:a|i|u|e|o|ka|ki|ku|ke|ko|sa|shi|su|se|so|ta|chi|tsu|te|to|na|ni|nu|ne|no|ha|hi|fu|he|ho|ma|mi|mu|me|mo|ya|yu|yo|ra|ri|ru|re|ro|wa|wo|ga|gi|gu|ge|go|za|ji|zu|ze|zo|da|de|do|ba|bi|bu|be|bo|pa|pi|pu|pe|po|kya|kyu|kyo|sha|shu|sho|cha|chu|cho|nya|nyu|nyo|hya|hyu|hyo|mya|myu|myo|rya|ryu|ryo|gya|gyu|gyo|ja|ju|jo|bya|byu|byo|pya|pyu|pyo|n(?![aiueoyn])|nn|([ckstnhmyrwgzjdbp])\\1){3,}.*$");

    @Comment("""
            かな漢字変換を強制するプレフィックス
            この値をメッセージの先頭に付けて送信すると`conversion-condition`の合致に関わらずかな漢字変換が強制されます。
            かな漢字変換機能が有効に設定されている必要があります。
            初期値で強制変換される例: !hello -> へっぉ
            有効な値: 文字 (一文字に限らず)
            """)
    private String forcePrefix = String.valueOf('!');

    @Comment("""
            かな漢字変換を回避するプレフィックス
            この値をメッセージの先頭に付けて送信すると`conversion-condition`の合致に関わらずかな漢字変換は試行されません。
            かな漢字変換機能が有効に設定されている必要があります。
            初期値で変換阻止される例: ?konnnichiha -> konnnichiha
            有効な値: 文字 (一文字に限らず)
            """)
    private String preventPrefix = String.valueOf('?');

    @Comment("""
            メッセージのかな漢字変換を試行した後のメッセージフォーマット
            MiniMessage形式で記述する必要があります。カラーコード(&や§で装飾された文字)には対応していません。
            サンプル: https://webui.advntr.dev/?x=Vc6P87LjCK
            有効な値: MiniMessage形式の文字列
            有効なタグ: japanized_message (変換後) | previous_message (変換前) | original_message (オリジナル)
            """)
    private String messageFormat = "<japanized_message><hover:show_text:'<previous_message>'><#1E88E5>🔄</#1E88E5></hover>";

    @Comment("データベース設定")
    private DatabaseSettings database = new DatabaseSettings(
            "jdbc:h2:{plugin_directory}/carbon-japanizer-h2;MODE=MySQL",
            "user_name",
            "password");

    public boolean defaultJapanize() {
        return this.defaultJapanize;
    }

    public Pattern convertCondition() {
        return this.convertCondition;
    }

    public String forcePrefix() {
        return this.forcePrefix;
    }

    public String preventPrefix() {
        return this.preventPrefix;
    }

    public String messageFormat() {
        return this.messageFormat;
    }

    public DatabaseSettings database() {
        return this.database;
    }
}
