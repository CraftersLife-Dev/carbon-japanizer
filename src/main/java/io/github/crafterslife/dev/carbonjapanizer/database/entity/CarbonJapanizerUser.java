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
package io.github.crafterslife.dev.carbonjapanizer.database.entity;

import io.github.crafterslife.dev.carbonjapanizer.utility.Status;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jspecify.annotations.NullMarked;

/**
 * {@code CarbonJapanizer} のユーザーデータ
 *
 * @param uuid ユーザーのUUID
 * @param shouldJapanize ローマ字のかな漢字変換が必要かどうか
 */
@NullMarked
public record CarbonJapanizerUser(@ColumnName("id") UUID uuid, @ColumnName("japanize") boolean shouldJapanize) implements Audience, Identified {

    /**
     * ユーザーの名前。
     *
     * @return ユーザーの名前
     */
    public String name() {
        return this.get(Identity.NAME).orElse("Unknown");
    }

    /**
     * ユーザーの表示名。
     *
     * @return ユーザーの表示名
     */
    public Component displayName() {
        return this.get(Identity.DISPLAY_NAME).orElse(Component.text("Unknown"));
    }

    /**
     * ユーザーのかな漢字変換のステータス。
     *
     * @return ユーザーのかな漢字変換のステータス
     */
    public Status japanizeStatus() {
        return Status.of(this.shouldJapanize);
    }

    @Override
    public Identity identity() {
        return Identity.identity(this.uuid);
    }
}
