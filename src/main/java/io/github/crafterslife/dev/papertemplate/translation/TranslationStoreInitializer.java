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
package io.github.crafterslife.dev.papertemplate.translation;

import io.github.crafterslife.dev.papertemplate.minecraft.paper.PluginBootstrapImpl;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslationStore;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationStore;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * {@link TranslationStore}を初期化するためのクラス
 */
@NullMarked
public final class TranslationStoreInitializer {

    private TranslationStoreInitializer() {
    }

    /**
     * {@link TranslationStore}を初期化します。
     *
     * @param context プラグインコンテキスト
     * @param serviceInterface サービスインターフェース
     * @param control リソースバンドルコントロール
     * @return インストールされたロケールのコレクション
     */
    @SuppressWarnings({"PatternValidation", "UnstableApiUsage"})
    public static List<Locale> initialize(
            final PluginProviderContext context,
            final Class<?> serviceInterface,
            final ResourceBundle.Control control
    ) {
        final String baseName = serviceInterface.getAnnotation(io.github.namiuni.doburoku.annotation.annotations.ResourceBundle.class).baseName();
        final Key key = Key.key(context.getConfiguration().getName().toLowerCase(), baseName);
        final MiniMessageTranslationStore store = MiniMessageTranslationStore.create(key, CustomMiniMessageFactory.create());
        store.defaultLocale(Locale.JAPAN);

        final ClassLoader classLoader = PluginBootstrapImpl.class.getClassLoader();
        final Set<ResourceBundle> resourceBundles = Locale.availableLocales()
                .map(locale -> TranslationStoreInitializer.getBundle(baseName, locale, classLoader, control))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        resourceBundles.forEach(bundle -> store.registerAll(bundle.getLocale(), bundle, false));

        GlobalTranslator.translator().addSource(store);

        return resourceBundles.stream().map(ResourceBundle::getLocale).toList();
    }

    private static @Nullable ResourceBundle getBundle(final String baseName, final Locale locale, final ClassLoader classLoader, final ResourceBundle.Control control) {
        try {
            return ResourceBundle.getBundle(baseName, locale, classLoader, control);
        } catch (final MissingResourceException ignore) {
            return null;
        }
    }
}
