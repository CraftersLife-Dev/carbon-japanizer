/*
 * PaperTemplate
 *
 * Copyright (c) 2025. Namiu (うにたろう)
 *                     Contributors []
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
package io.github.crafterslife.dev.papertemplate.configuration;

import java.io.Serial;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.ConfigurateException;

/**
 * <p>{@link ConfigurateException}をラップし、チェックされない例外として扱うためのランタイム例外です。</p>
 *
 * <p>このクラスは、{@code ConfigurateException}がチェックされる例外であるために、
 * 呼び出し元で強制的に{@code try-catch}ブロックを記述する必要があるのを避けるために使用されます。
 * これにより、{@code ConfigurateHelper}のようなユーティリティクラスをより簡潔に使用できるようになります。</p>
 */
@NullMarked
public final class UncheckedConfigurateException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2214743499979182898L;

    /**
     * 指定された{@link ConfigurateException}を原因として、新しい{@code UncheckedConfigurateException}を構築します。
     *
     * @param cause この例外の原因となる{@link ConfigurateException}
     */
    public UncheckedConfigurateException(final ConfigurateException cause) {
        super(cause);
    }
}
