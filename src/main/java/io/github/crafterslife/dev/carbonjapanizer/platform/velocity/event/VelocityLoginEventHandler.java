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
package io.github.crafterslife.dev.carbonjapanizer.platform.velocity.event;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.connection.LoginEvent;
import io.github.crafterslife.dev.carbonjapanizer.CarbonJapanizer;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;

/**
 * {@code Velocity} のログインイベントの操作を担う。
 */
@NullMarked
public final class VelocityLoginEventHandler implements VelocityEventHandler<LoginEvent> {

    private final CarbonJapanizer carbonJapanizer;

    /**
     * このクラスのインスタンスを生成する
     *
     * @param carbonJapanizer {@link CarbonJapanizer}
     */
    public VelocityLoginEventHandler(final CarbonJapanizer carbonJapanizer) {
        this.carbonJapanizer = carbonJapanizer;
    }

    @Override
    public EventTask executeAsync(final LoginEvent event) {
        return EventTask.async(() -> {
            if (event.getResult().isAllowed()) {
                final UUID playerUUID = event.getPlayer().getUniqueId();
                this.carbonJapanizer.handleLogin(playerUUID);
            }
        });
    }
}
