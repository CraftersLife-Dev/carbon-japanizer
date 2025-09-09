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
package io.github.crafterslife.dev.carbonjapanizer.platform.paper;

import io.github.crafterslife.dev.carbonjapanizer.CarbonJapanizer;
import io.github.crafterslife.dev.carbonjapanizer.platform.paper.event.PaperLoginEventHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

/**
 * Paperプラグインのイベントライフサイクルを管理する。
 */
@NullMarked
public final class PaperCarbonJapanizer extends JavaPlugin {

    private final CarbonJapanizer carbonJapanizer;

    /**
     * このクラスの新しいインスタンスを生成します。
     *
     * @param carbonJapanizer プラグインコンテナ
     */
    public PaperCarbonJapanizer(final CarbonJapanizer carbonJapanizer) {
        this.carbonJapanizer = carbonJapanizer;
    }

    @Override
    public void onEnable() {

        // ログインイベントハンドラーを登録
        final var loginHandler = new PaperLoginEventHandler(this.carbonJapanizer);
        Bukkit.getPluginManager().registerEvents(loginHandler, this);

        // チャットイベントハンドラーを登録
        this.carbonJapanizer.registerChatEvent();
    }
}
