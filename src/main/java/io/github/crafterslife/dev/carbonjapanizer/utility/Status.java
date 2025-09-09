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

/**
 * 有効/無効のステータスを表す列挙型
 */
public enum Status {
    ENABLE(true),
    DISABLE(false);

    private final boolean bool;

    Status(final boolean bool) {
        this.bool = bool;
    }

    /**
     * {@code boolean} から {@code Status} を取得する。
     *
     * @param bool 真偽値
     * @return Status
     */
    public static Status of(final boolean bool) {
        return bool ? ENABLE : DISABLE;
    }

    /**
     * {@code Status} から {@code boolean} を取得する。
     *
     * @return boolean
     */
    public boolean toBoolean() {
        return this.bool;
    }
}
