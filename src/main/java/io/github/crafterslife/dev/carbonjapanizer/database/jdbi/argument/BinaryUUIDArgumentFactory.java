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
package io.github.crafterslife.dev.carbonjapanizer.database.jdbi.argument;

import io.github.crafterslife.dev.carbonjapanizer.utility.UUIDSerializer;
import java.sql.Types;
import java.util.UUID;
import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;
import org.jspecify.annotations.NullMarked;

/**
 * 引数に受け取ったUUIDをバイナリ形式でSQLとやり取りするためのファクトリー。
 */
@NullMarked
public final class BinaryUUIDArgumentFactory extends AbstractArgumentFactory<UUID> {

    /**
     * このクラスのインスタンスを生成する。
     */
    public BinaryUUIDArgumentFactory() {
        super(Types.BINARY);
    }

    @Override
    public Argument build(final UUID value, final ConfigRegistry config) {
        return (position, statement, ctx) -> statement.setBytes(position, UUIDSerializer.serialize(value));
    }
}
