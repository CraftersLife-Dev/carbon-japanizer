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
package io.github.crafterslife.dev.carbonjapanizer.platform.paper.event;

import io.github.crafterslife.dev.carbonjapanizer.CarbonJapanizer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.jspecify.annotations.NullMarked;

/**
 * {@code Paper} のログインイベントの操作を担う。
 */
@NullMarked
public final class PaperLoginEventHandler implements Listener {

    private final CarbonJapanizer carbonJapanizer;

    /**
     * このクラスのインスタンスを生成する
     *
     * @param carbonJapanizer {@link CarbonJapanizer}
     */
    public PaperLoginEventHandler(final CarbonJapanizer carbonJapanizer) {
        this.carbonJapanizer = carbonJapanizer;
    }

    /**
     * ログインしたプレイヤーをユーザーサービスでロードする。
     *
     * @param event ログインイベント
     */
    @EventHandler
    private void onLogin(final AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            final var uuid = event.getUniqueId();
            this.carbonJapanizer.handleLogin(uuid);
        }
    }
}
