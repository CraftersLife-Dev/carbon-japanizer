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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import io.github.crafterslife.dev.carbonjapanizer.resource.translation.messages.LoggingService;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocketHandshakeException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.jspecify.annotations.NullMarked;

/**
 * {@link Component} 内のひらがなを漢字に変換するための
 * {@link TextReplacementConfig} を生成するためのファクトリクラス。
 *
 * <p>ひらがなから漢字への変換には、Google CGI API for Japanese Inputを使用する。
 *
 * @see <a href="https://www.google.co.jp/ime/cgiapi.html">Google CGI APi for Japanese Input</a>
 */
@NullMarked
public final class KanjiReplacementConfigFactory implements TextReplacementConfigFactory {

    private static final String GOOGLE_IME_URL = "https://www.google.com/transliterate?langpair=ja-Hira%7Cja&text=";

    private final LoggingService logger;
    private final Gson gson;

    /**
     * このクラスのインスタンスを生成する。
     *
     * @param logger 例外発生時に使用するロギングサービス
     */
    public KanjiReplacementConfigFactory(final LoggingService logger) {
        this.logger = logger;
        this.gson = new Gson();
    }

    @Override
    public TextReplacementConfig create() {
        return TextReplacementConfig.builder()
                .match(Pattern.compile("^(.*)$"))
                .replacement((matchResult, builder) -> {
                    final String input = matchResult.group();
                    return Component.text(this.requestCGI(input));
                })
                .build();
    }

    /**
     * Google CGI API にリクエストを送信し、結果を取得する。
     *
     * @param input リクエスト対象の文字列
     * @return リクエスト後の文字列。リクエストに失敗した場合は {@code input} の文字列をそのまま返す
     */
    private String requestCGI(final String input) {
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GOOGLE_IME_URL + URLEncoder.encode(input, StandardCharsets.UTF_8)))
                    .GET()
                    .build();

            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() == 200) {
                return this.parseJson(response.body());
            } else {
                throw new WebSocketHandshakeException(response);
            }
        } catch (final IOException | InterruptedException exception) {
            this.logger.japanizeKanaToKanjiFailed(input).logging(exception);
            return input;
        }
    }

    private String parseJson(final String json) {
        final StringBuilder result = new StringBuilder();
        for (final JsonElement response : this.gson.fromJson(json, JsonArray.class)) {
            result.append(response.getAsJsonArray().get(1).getAsJsonArray().get(0).getAsString());
        }
        return result.toString();
    }
}
