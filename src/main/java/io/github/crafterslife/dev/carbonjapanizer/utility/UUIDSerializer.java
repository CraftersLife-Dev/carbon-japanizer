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
package io.github.crafterslife.dev.carbonjapanizer.utility;

import java.nio.ByteBuffer;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;

/**
 * UUIDをbyte配列にシリアライズするためのユーティリティー。
 */
@NullMarked
public final class UUIDSerializer {

    private UUIDSerializer() {
    }

    /**
     * {@link UUID} をbyte配列にシリアライズする。
     *
     * @param uuid シリアライズ対象のUUID
     * @return byte配列
     */
    public static byte[] serialize(final UUID uuid) {
        final ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    /**
     * byte配列を {@link UUID} にデシリアライズする。
     *
     * @param bytes デシリアライズ対象のbyte配列
     * @return UUID
     */
    public static UUID deserialize(final byte[] bytes) {
        final ByteBuffer bb = ByteBuffer.wrap(bytes);
        final long firstLong = bb.getLong();
        final long secondLong = bb.getLong();
        return new UUID(firstLong, secondLong);
    }
}
