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
package io.github.crafterslife.dev.carbonjapanizer.event;

import io.github.crafterslife.dev.carbonjapanizer.integration.CarbonChatExpansion;
import io.github.crafterslife.dev.carbonjapanizer.service.JapanizeService;
import net.draycia.carbon.api.CarbonChat;
import net.draycia.carbon.api.CarbonChatProvider;
import net.draycia.carbon.api.event.events.CarbonChatEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CarbonChatHandler {

    private final JapanizeService japanizeService;

    public CarbonChatHandler(final JapanizeService japanizeService) {
        this.japanizeService = japanizeService;
    }

    public void register() throws IllegalStateException {
        if (!CarbonChatExpansion.carbonChatLoaded()) {
            throw new IllegalStateException("Carbon is not loaded!");
        }

        final CarbonChat carbonChat = CarbonChatProvider.carbonChat();
        carbonChat.eventHandler().subscribe(CarbonChatEvent.class, event -> {
            if (!event.cancelled()) {
                final var japanized = this.japanizeService.japanize(event.sender().uuid(), event.message());
                event.message(japanized);
            }
        });
    }
}
