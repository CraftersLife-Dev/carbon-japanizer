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

import io.github.crafterslife.dev.papertemplate.ResourceContainer;
import io.github.crafterslife.dev.papertemplate.ServiceContainer;
import io.github.crafterslife.dev.papertemplate.minecraft.paper.commands.AdminCommand;
import io.github.crafterslife.dev.papertemplate.minecraft.paper.commands.InternalCommand;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import java.util.function.Supplier;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jspecify.annotations.NullMarked;

/**
 * <p>Paperの{@link PluginBootstrap}を実装し、プラグインのブートストラップと初期化ロジックを管理します。</p>
 */
@NullMarked
@SuppressWarnings({"UnstableApiUsage", "unused"})
public final class PluginBootstrapImpl implements PluginBootstrap {

    private @MonotonicNonNull Supplier<JavaPlugin> javaPlugin;

    @Override
    public void bootstrap(final BootstrapContext context) {

        // Create resource container
        final ResourceContainer resourceContainer = ResourceContainer.create(context);
        final ServiceContainer serviceContainer = ServiceContainer.create(resourceContainer);

        // Register commands
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final InternalCommand adminCommand = new AdminCommand(serviceContainer);
            event.registrar().register(adminCommand.command(), adminCommand.description(), adminCommand.aliases());
        });

        // Create plugin supplier
        this.javaPlugin = () -> new JavaPluginImpl(serviceContainer);
    }

    @Override
    public JavaPlugin createPlugin(final PluginProviderContext context) {
        return this.javaPlugin.get();
    }
}
