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
package io.github.crafterslife.dev.papertemplate;

import io.github.crafterslife.dev.papertemplate.configuration.ConfigurateHelper;
import io.github.crafterslife.dev.papertemplate.configuration.Header;
import io.github.crafterslife.dev.papertemplate.configuration.UncheckedConfigurateException;
import io.github.crafterslife.dev.papertemplate.configuration.annotations.ConfigName;
import io.github.crafterslife.dev.papertemplate.configuration.configurations.PrimaryConfig;
import io.github.crafterslife.dev.papertemplate.integration.MiniPlaceholdersExpansion;
import io.github.crafterslife.dev.papertemplate.translation.DynamicResourceBundleControl;
import io.github.crafterslife.dev.papertemplate.translation.Message;
import io.github.crafterslife.dev.papertemplate.translation.TranslationStoreInitializer;
import io.github.crafterslife.dev.papertemplate.translation.annotations.LogLevel;
import io.github.crafterslife.dev.papertemplate.translation.services.LoggingService;
import io.github.crafterslife.dev.papertemplate.translation.services.MessageService;
import io.github.namiuni.doburoku.standard.DoburokuStandard;
import io.github.namiuni.doburoku.standard.argument.MiniMessageArgumentTransformer;
import io.leangen.geantyref.TypeToken;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.kyori.adventure.util.UTF8ResourceBundleControl;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.slf4j.event.Level;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

/**
 * 設定や翻訳などのリソースをまとめたコンテナ。
 *
 * @param primaryConfig  プラグインの設定
 * @param messageService メッセージサービス
 * @param loggingService ロギングサービス
 */
@NullMarked
@SuppressWarnings("UnstableApiUsage")
public record ResourceContainer(
        PrimaryConfig primaryConfig,
        MessageService messageService,
        LoggingService loggingService
) {

    /**
     * このレコードのインスタンスを生成する。
     *
     * @param context リソースの初期化に必要なプラグインコンテキスト
     * @return このレコードのインスタンス
     */
    public static ResourceContainer create(final PluginProviderContext context) {
        final LoggingService loggingService = ResourceContainer.createLoggingService(context);
        final MessageService messageService = ResourceContainer.createMessageService(context, loggingService);
        final PrimaryConfig primaryConfig = ResourceContainer.createConfig(context, loggingService, PrimaryConfig.class, PrimaryConfig.DEFAULT);

        return new ResourceContainer(primaryConfig, messageService, loggingService);
    }

    private static <T extends Record> T createConfig(
            final PluginProviderContext context,
            final LoggingService loggingService,
            final Class<T> model,
            final T base
    ) throws UncheckedConfigurateException {
        try {
            final T config = ConfigurateHelper.builder(model)
                    .defaultConfiguration(base)
                    .configurationLoader(YamlConfigurationLoader.builder()
                            .nodeStyle(NodeStyle.BLOCK)
                            .defaultOptions(options -> options
                                    .shouldCopyDefaults(true)
                                    .header(model.getAnnotation(Header.class).value())
                                    .serializers(ConfigurateComponentSerializer.configurate().serializers()))
                            .path(context.getDataDirectory().resolve(model.getAnnotation(ConfigName.class).value()))
                            .build())
                    .build();

            loggingService.configurationLoaded(PrimaryConfig.class.getAnnotation(ConfigName.class).value());

            return config;
        } catch (final ConfigurateException exception) {
            throw new UncheckedConfigurateException(exception);
        }
    }

    private static LoggingService createLoggingService(final PluginProviderContext context) {

        TranslationStoreInitializer.initialize(
                context,
                LoggingService.class,
                UTF8ResourceBundleControl.utf8ResourceBundleControl()
        );

        final ComponentLogger logger = context.getLogger();
        return DoburokuStandard.of(LoggingService.class)
                .argument(registry -> {
                    registry.plus(new TypeToken<Collection<Locale>>() {
                    }, (parameter, locales) -> {
                        final List<TextComponent> components = locales.stream()
                                .map(Locale::getDisplayName)
                                .map(Component::text)
                                .toList();
                        final JoinConfiguration joinConfig = JoinConfiguration.arrayLike();
                        return Component.join(joinConfig, components);
                    });
                }, MiniMessageArgumentTransformer.create())
                .result(registry -> registry
                        .plus(void.class, (method, component) -> {
                            final Level level = method.getAnnotation(LogLevel.class).value();
                            switch (level) {
                                case INFO -> logger.info(component);
                                case WARN -> logger.warn(component);
                                case ERROR -> logger.error(component);
                                case DEBUG -> logger.debug(component);
                                case TRACE -> logger.trace(component);
                            }
                            return null;
                        }))
                .brew();
    }

    private static MessageService createMessageService(final PluginProviderContext context, final LoggingService loggingService) {
        final ResourceBundle.Control control = new DynamicResourceBundleControl(context.getDataDirectory());
        final Collection<Locale> installedLocales = TranslationStoreInitializer.initialize(
                context,
                MessageService.class,
                control
        );
        loggingService.translationLoaded(installedLocales.size(), installedLocales);

        return DoburokuStandard.of(MessageService.class)
                .argument(registry -> registry.plus(Player.class, (parameter, player) -> player.displayName()),
                        MiniMessageArgumentTransformer.create())
                .result(registry -> registry
                        .plus(Message.class, (method, component) -> audience -> {
                            final List<ComponentLike> arguments = new ArrayList<>(component.arguments());
                            arguments.add(Argument.tagResolver(MiniPlaceholdersExpansion.audiencePlaceholders()));
                            arguments.add(Argument.target(audience));

                            final TranslatableComponent result = Component.translatable(component.key(), arguments);
                            audience.sendMessage(result);
                        }))
                .brew();
    }
}
