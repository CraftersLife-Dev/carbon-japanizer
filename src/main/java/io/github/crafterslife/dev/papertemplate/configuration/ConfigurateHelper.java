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
package io.github.crafterslife.dev.papertemplate.configuration;

import java.util.Objects;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

/**
 * <p>ビルダーパターンを使用して設定を構成するためのヘルパークラス。</p>
 *
 * <p>このクラスは、提供されたレコードモデルから設定インスタンスをビルドするために、
 * メソッド呼び出しをチェーンすることで、設定ファイルの読み込み、変換、および保存のプロセスを簡素化します。</p>
 */
@NullMarked
public final class ConfigurateHelper {

    /**
     * このユーティリティクラスのインスタンス化を防ぐためのプライベートコンストラクタ。
     */
    private ConfigurateHelper() {
    }

    /**
     * <p>レコードベースの設定モデルを構成するためのビルダーパターンを開始します。</p>
     *
     * <p>これは、提供されたレコードクラスに基づいて新しい設定インスタンスを構成するための最初のステップです。</p>
     *
     * @param model 設定モデルを表すレコードクラス
     * @param <T>   レコードクラスの型
     * @param <L>   {@link ConfigurationLoader}の型
     * @return デフォルト設定を定義するためのビルダーの最初のステップである{@link BasicStep}
     */
    @Contract("_ -> new")
    public static <T extends Record, L extends ConfigurationLoader<?>> BasicStep<T, L> builder(final Class<T> model) {
        Objects.requireNonNull(model, "model");
        return new Builder<>(model);
    }

    /**
     * デフォルト設定を定義するために使用される、ビルダーパターンの最初のステップ。
     *
     * @param <T> レコードクラスの型
     * @param <L> {@link ConfigurationLoader}の型
     */
    public sealed interface BasicStep<T extends Record, L extends ConfigurationLoader<?>> permits Builder {

        /**
         * <p>設定ファイルが存在しない場合に使用されるデフォルト設定インスタンスを設定します。</p>
         *
         * <p>このインスタンスは、ファイルが見つからない場合に最初の実行時にシリアライズされ、設定ファイルに保存されます。</p>
         *
         * @param basic デフォルト設定インスタンス
         * @return ローダーを定義するためのビルダーの次のステップである{@link LoaderStep}
         */
        LoaderStep<T, L> defaultConfiguration(T basic);
    }

    /**
     * 設定ローダーを定義するために使用される、ビルダーパターンの2番目のステップ。
     *
     * @param <T> レコードクラスの型
     * @param <L> {@link ConfigurationLoader}の型
     */
    public sealed interface LoaderStep<T extends Record, L extends ConfigurationLoader<?>> permits Builder {

        /**
         * 設定ファイルを読み込みおよび保存する役割を担う{@link ConfigurationLoader}を設定します。
         *
         * @param loader 設定ローダーインスタンス
         * @return 設定をビルドするためのビルダーの最終ステップである{@link BuildableStep}
         */
        BuildableStep<T, L> configurationLoader(L loader);
    }

    /**
     * 変換を追加し、設定をビルドするために使用される、ビルダーパターンの最終ステップ。
     *
     * @param <T> レコードクラスの型
     * @param <L> {@link ConfigurationLoader}の型
     */
    public sealed interface BuildableStep<T extends Record, L extends ConfigurationLoader<?>> permits Builder {

        /**
         * <p>バージョン管理された設定変換をビルダーに適用します。</p>
         *
         * <p>この変換は、古いバージョンの設定ファイルを最新の形式に移行するために使用されます。</p>
         *
         * @param version {@link ConfigurationTransformation.Versioned}インスタンス
         * @return メソッドチェーンのための現在のビルダーインスタンス
         */
        BuildableStep<T, L> version(ConfigurationTransformation.Versioned version);

        /**
         * <p>ファイルを読み込み、変換を適用し、データをシリアライズして設定インスタンスをビルドします。</p>
         *
         * <p>設定ファイルが存在しない場合、{@link BasicStep#defaultConfiguration(Record)}ステップで提供されたデフォルト値で作成されます。
         * その後、ビルドされた設定が返されます。</p>
         *
         * @return 型{@code T}のビルドされた設定インスタンス
         * @throws ConfigurateException 設定の読み込み、変換、または保存中にエラーが発生した場合
         */
        T build() throws ConfigurateException;
    }

    /**
     * ビルダーパターンの内部実装。
     *
     * @param <T> レコードクラスの型
     * @param <L> {@link ConfigurationLoader}の型
     */
    private static final class Builder<T extends Record, L extends ConfigurationLoader<?>> implements BasicStep<T, L>, LoaderStep<T, L>, BuildableStep<T, L> {

        private final Class<T> model;
        private @MonotonicNonNull T basic;
        private L loader;
        private ConfigurationTransformation.@Nullable Versioned transformation;

        @Contract(pure = true)
        private Builder(final Class<T> model) {
            this.model = model;
        }

        @Contract(value = "_ -> this", mutates = "this")
        @Override
        public LoaderStep<T, L> defaultConfiguration(final T basic) {
            Objects.requireNonNull(basic, "basic");
            this.basic = basic;
            return this;
        }

        @Contract(value = "_ -> this", mutates = "this")
        @Override
        public BuildableStep<T, L> configurationLoader(final L loader) {
            Objects.requireNonNull(loader, "loader");
            this.loader = loader;
            return this;
        }

        @Contract(value = "_ -> this", mutates = "this")
        @Override
        public BuildableStep<T, L> version(final ConfigurationTransformation.Versioned version) {
            this.transformation = version;
            return this;
        }

        @Override
        public T build() throws ConfigurateException {
            final ConfigurationNode node = this.loader.load();

            if (this.transformation != null && !node.virtual()) {
                this.transformation.apply(node);
            }

            final T config = node.get(this.model, this.basic);
            this.loader.save(node);

            return config;
        }
    }
}
