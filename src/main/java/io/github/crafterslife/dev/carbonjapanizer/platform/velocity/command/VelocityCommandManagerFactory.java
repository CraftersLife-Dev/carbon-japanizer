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
package io.github.crafterslife.dev.carbonjapanizer.platform.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github.crafterslife.dev.carbonjapanizer.command.CommandManagerFactory;
import io.github.crafterslife.dev.carbonjapanizer.command.Commander;
import io.github.crafterslife.dev.carbonjapanizer.service.UserService;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.velocity.VelocityCommandManager;
import org.jspecify.annotations.NullMarked;

/**
 * {@code Velocity} の {@link CommandManager} のファクトリクラス。
 */
@NullMarked
public final class VelocityCommandManagerFactory implements CommandManagerFactory {

    private final PluginContainer pluginContainer;
    private final ProxyServer server;

    /**
     * このクラスのインスタンスを生成する。
     *
     * @param pluginContainer プラグインコンテナ
     * @param server          プロキシサーバー
     */
    public VelocityCommandManagerFactory(
            final PluginContainer pluginContainer,
            final ProxyServer server
    ) {
        this.pluginContainer = pluginContainer;
        this.server = server;
    }

    @Override
    public CommandManager<Commander> create(final UserService userService) {
        return new VelocityCommandManager<>(
                this.pluginContainer,
                this.server,
                ExecutionCoordinator.asyncCoordinator(),
                this.createSenderMapper(userService)
        );
    }

    private SenderMapper<CommandSource, Commander> createSenderMapper(final UserService userService) {
        return SenderMapper.create(
                sender -> {
                    if (sender instanceof com.velocitypowered.api.proxy.Player player) {
                        final var user = userService.loadUser(player.getUniqueId()).join();
                        return new VelocityUserCommander(player, user);
                    } else {
                        return (VelocityCommander) () -> sender;
                    }
                },
                commander -> ((VelocityCommander) commander).source()
        );
    }
}
