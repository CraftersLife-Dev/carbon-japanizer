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
package io.github.crafterslife.dev.carbonjapanizer.resource.translation.messages;

import io.github.crafterslife.dev.carbonjapanizer.database.entity.CarbonJapanizerUser;
import io.github.crafterslife.dev.carbonjapanizer.integration.MiniPlaceholdersExpansion;
import io.github.crafterslife.dev.carbonjapanizer.resource.translation.spi.Message;
import io.github.crafterslife.dev.carbonjapanizer.utility.Status;
import io.github.namiuni.doburoku.annotation.Locales;
import io.github.namiuni.doburoku.annotation.annotations.Key;
import io.github.namiuni.doburoku.annotation.annotations.ResourceBundle;
import io.github.namiuni.doburoku.annotation.annotations.Value;
import io.github.namiuni.doburoku.standard.DoburokuStandard;
import io.github.namiuni.doburoku.standard.argument.MiniMessageArgumentTransformer;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * プレイヤーなどに送信するメッセージを管理するためのサービスインターフェース。
 */
@NullMarked
@ApiStatus.NonExtendable
@ResourceBundle(baseName = "translations/message")
public interface UserMessage {

    /**
     * このインターフェースのプロキシインスタンスを生成する。
     *
     * @return このインターフェースのプロキシインスタンス
     */
    static UserMessage create() {
        return DoburokuStandard.of(UserMessage.class)
                .argument(registry -> registry
                                .plus(CarbonJapanizerUser.class, (parameter, user) -> user.displayName())
                                .plus(Status.class, (parameter, status) -> {
                                    if (status == Status.ENABLE) {
                                        return Component.translatable("carbon_japanizer.status.enable");
                                    } else {
                                        return Component.translatable("carbon_japanizer.status.disable");
                                    }
                                }),
                        MiniMessageArgumentTransformer.create())
                .result(registry -> registry
                        .plus(Message.class, (method, component) -> audience -> {
                            final List<ComponentLike> arguments = new ArrayList<>(component.arguments());
                            arguments.add(Argument.tagResolver(MiniPlaceholdersExpansion.audiencePlaceholders()));
                            arguments.add(Argument.target(audience));

                            final TranslatableComponent result = Component.translatable(component.key(), arguments);
                            audience.sendMessage(result);
                        }))
                .brew();
    }

    /**
     * 有効を表す文字列
     *
     * @return メッセージコンポーネント
     */
    @Key("carbon_japanizer.status.enable")
    @Value(locale = Locales.EN_US, content = "<white>enabled</white>")
    @Value(locale = Locales.JA_JP, content = "<white>有効</white>")
    Component statusEnable();

    /**
     * 無効を表す文字列
     *
     * @return メッセージコンポーネント
     */
    @Key("carbon_japanizer.status.disable")
    @Value(locale = Locales.EN_US, content = "<white>disabled</white>")
    @Value(locale = Locales.JA_JP, content = "<white>無効</white>")
    Component statusDisable();

    /**
     * ユーザーが現在のかな漢字変換ステータスを取得した際に送信するメッセージ
     *
     * @param japanizeStatus かな漢字変換のステータス
     * @return メッセージ
     */
    @Key("carbon_japanizer.command.japanize.status.current")
    @Value(locale = Locales.EN_US, content = "<info>You have changed '<japanize_status>' kana-kanji conversion for chat.")
    @Value(locale = Locales.JA_JP, content = "<info>チャットのかな漢字変換機能は「<japanize_status>」になっています。")
    Message currentJapanizeStatus(Status japanizeStatus);

    /**
     * ユーザーがかな漢字変換ステータスを変更した際に送信するメッセージ
     *
     * @param japanizeStatus 変更後のステータス
     * @return メッセージ
     */
    @Key("carbon_japanizer.command.japanize.status.changed")
    @Value(locale = Locales.EN_US, content = "<info>You have changed the kana-kanji conversion status for chat to “<japanize_status>”.")
    @Value(locale = Locales.JA_JP, content = "<info>チャットのかな漢字変換機能を「<japanize_status>」に変更しました。")
    Message changeJapanizeStatus(Status japanizeStatus);
}
