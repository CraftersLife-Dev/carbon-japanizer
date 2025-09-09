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
package io.github.crafterslife.dev.carbonjapanizer.database;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.crafterslife.dev.carbonjapanizer.resource.configuration.configurations.PrimaryConfig;
import io.github.crafterslife.dev.carbonjapanizer.resource.translation.messages.LoggingService;
import java.nio.file.Path;
import java.util.concurrent.ThreadFactory;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.jspecify.annotations.NullMarked;

/**
 * データソースのファクトリ。
 */
@NullMarked
public final class DataSourceFactory {

    private final PrimaryConfig primaryConfig;
    private final LoggingService logger;
    private final Path dataDirectory;

    /**
     * このクラスのインスタンスを生成する。
     *
     * @param primaryConfig メイン設定
     * @param logger 例外発生時に使用するロギングサービス
     * @param dataDirectory H2データベースを保存するプラグインディレクトリ
     */
    public DataSourceFactory(
            final PrimaryConfig primaryConfig,
            final LoggingService logger,
            final Path dataDirectory
    ) {
        this.primaryConfig = primaryConfig;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    /**
     * データソースを生成する。
     *
     * <p>データソースの生成時には {@link Flyway} を使用してマイグレーション処理も実行する。
     * マイグレーションのソースはresourcesに存在する。</p>
     *
     * @return 生成されたデータソース
     */
    public HikariDataSource create() {

        // HikariCPの設定
        final HikariConfig hikariConfig = new HikariConfig();
        final String storageType = this.primaryConfig.database().url().split(":")[1];

        hikariConfig.setJdbcUrl(this.url(storageType));
        hikariConfig.setDriverClassName(this.driverClassName(storageType));
        hikariConfig.setUsername(this.primaryConfig.database().username());
        hikariConfig.setPassword(this.primaryConfig.database().password());
        hikariConfig.setPoolName("CarbonJapanizer-HikariPool");
        hikariConfig.setThreadFactory(this.threadFactory());

        // データソースの生成
        final HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        // Flywayによるバージョン管理
        DataSourceFactory.migrate(dataSource, storageType);

        return dataSource;
    }

    private String url(final String storageType) {
        if ("h2".equals(storageType)) {
            return this.primaryConfig.database().url().replace("{plugin_directory}", this.dataDirectory.toAbsolutePath().toString());
        }

        return this.primaryConfig.database().url();
    }

    private String driverClassName(final String storageType) {
        if ("h2".equals(storageType)) {
            return org.h2.Driver.class.getName();
        } else if ("mysql".equals(storageType)) {
            return com.mysql.cj.jdbc.Driver.class.getName();
        }

        throw new UnsupportedOperationException();
    }

    private ThreadFactory threadFactory() {
        return new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat("%s %s Thread #%%d".formatted("CarbonJapanizer", "HikariPool"))
                .setUncaughtExceptionHandler((thread, throwable) -> this.logger.databaseThreadUncaught(thread.getName()).logging(throwable))
                .build();
    }

    private static void migrate(final DataSource dataSource, final String storageType) {
        final Flyway flyway = Flyway.configure(DataSourceFactory.class.getClassLoader())
                .baselineVersion("0")
                .baselineOnMigrate(true)
                .dataSource(dataSource)
                .locations("queries/migrations/" + storageType)
                .validateMigrationNaming(true)
                .validateOnMigrate(true)
                .load();
        flyway.repair();
        flyway.migrate();
    }
}
