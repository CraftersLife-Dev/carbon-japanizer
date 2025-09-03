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
package io.github.crafterslife.dev.papertemplate.minecraft.paper;

import io.github.crafterslife.dev.papertemplate.ServiceContainer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

/**
 * <p>Paperプラグインのメインクラスです。</p>
 *
 * <p>このクラスは、プラグインのメインのビジネスロジックとライフサイクルを管理します。</p>
 */
@NullMarked
public final class JavaPluginImpl extends JavaPlugin {

    private final ServiceContainer serviceContainer;

    /**
     * このクラスの新しいインスタンスを生成します。
     *
     * @param serviceContainer サービスロジックコンテナ
     */
    public JavaPluginImpl(final ServiceContainer serviceContainer) {
        this.serviceContainer = serviceContainer;
    }

    @Override
    public void onEnable() {
        /* templateServiceをリスナーに渡す */
    }
}
