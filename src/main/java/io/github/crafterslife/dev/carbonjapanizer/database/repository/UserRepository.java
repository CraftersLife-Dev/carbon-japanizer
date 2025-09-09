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
package io.github.crafterslife.dev.carbonjapanizer.database.repository;

import io.github.crafterslife.dev.carbonjapanizer.database.jdbi.argument.BinaryUUIDArgumentFactory;
import io.github.crafterslife.dev.carbonjapanizer.database.entity.CarbonJapanizerUser;
import java.util.Optional;
import java.util.UUID;
import org.jdbi.v3.sqlobject.config.RegisterArgumentFactory;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jspecify.annotations.NullMarked;

/**
 * {@code CarbonJapanizer} のユーザーリポジトリ
 */
@NullMarked
public interface UserRepository {

    /**
     * UUIDからユーザーデータを検索する。
     *
     * @param uuid 検索対象のユーザーのUUID
     * @return {@link Optional} でラップしたユーザーインスタンス
     */
    @SqlQuery("SELECT id, japanize FROM carbon_japanizer_users WHERE id = :uuid")
    @RegisterConstructorMapper(CarbonJapanizerUser.class)
    Optional<CarbonJapanizerUser> findById(UUID uuid);

    /**
     * ユーザーデータを保存する。対象ユーザーのレコードがすでに存在する場合は更新する。
     *
     * @param user 保存対象のユーザー
     */
    @SqlUpdate("""
            INSERT INTO carbon_japanizer_users (id, name, japanize)
            VALUES (:uuid, :name, :shouldJapanize)
            ON DUPLICATE KEY UPDATE name = :name, japanize = :shouldJapanize
            """)
    @RegisterArgumentFactory(BinaryUUIDArgumentFactory.class)
    void save(@BindMethods CarbonJapanizerUser user);
}
