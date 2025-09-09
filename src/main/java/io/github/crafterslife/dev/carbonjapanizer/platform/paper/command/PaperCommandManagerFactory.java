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
package io.github.crafterslife.dev.carbonjapanizer.platform.paper.command;

import io.github.crafterslife.dev.carbonjapanizer.command.CommandManagerFactory;
import io.github.crafterslife.dev.carbonjapanizer.command.Commander;
import io.github.crafterslife.dev.carbonjapanizer.service.UserService;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import org.bukkit.entity.Player;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jspecify.annotations.NullMarked;

/**
 * {@link PaperCommandManager} のファクトリクラス。
 */
@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class PaperCommandManagerFactory implements CommandManagerFactory {

    private final BootstrapContext context;

    /**
     * このクラスのインスタンスを生成する。
     *
     * @param context ブートストラップコンテキスト
     */
    public PaperCommandManagerFactory(final BootstrapContext context) {
        this.context = context;
    }

    @Override
    public CommandManager<Commander> create(final UserService userService) {
        return PaperCommandManager.builder(this.createSenderMapper(userService))
                .executionCoordinator(ExecutionCoordinator.asyncCoordinator())
                .buildBootstrapped(this.context);
    }

    private SenderMapper<CommandSourceStack, Commander> createSenderMapper(final UserService userService) {
        return SenderMapper.create(
                commandSourceStack -> {
                    if (commandSourceStack.getSender() instanceof Player player) {

                        final var user = userService.loadUser(player.getUniqueId()).join();
                        return new PaperPlayerCommander(commandSourceStack, user);
                    } else {
                        return (PaperCommander) () -> commandSourceStack;
                    }
                },
                commander -> ((PaperCommander) commander).source());
    }
}
