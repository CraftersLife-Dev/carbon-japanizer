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
package io.github.crafterslife.dev.carbonjapanizer.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.crafterslife.dev.carbonjapanizer.database.entity.CarbonJapanizerUser;
import io.github.crafterslife.dev.carbonjapanizer.database.repository.UserRepository;
import io.github.crafterslife.dev.carbonjapanizer.resource.configuration.configurations.PrimaryConfig;
import io.github.crafterslife.dev.carbonjapanizer.resource.translation.messages.LoggingService;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.jdbi.v3.core.Jdbi;
import org.jspecify.annotations.NullMarked;

/**
 * {@link CarbonJapanizerUser} のインスタンスを提供、または保存するサービスクラス。
 */
@NullMarked
public final class UserService {

    public static final String USER_POOL = "%s UserServicePool-%s Thread #%%d";

    private final PrimaryConfig primaryConfig;
    private final LoggingService logger;
    private final UserRepository userRepository;

    private final Executor executor = Executors.newVirtualThreadPerTaskExecutor();
    private final Cache<UUID, CarbonJapanizerUser> userCache;

    /**
     * このクラスのインスタンスを生成する。
     *
     * @param primaryConfig メイン設定
     * @param logger ロギングサービス
     * @param jdbi   ユーザーデータを操作する {@link Jdbi}
     */
    public UserService(final PrimaryConfig primaryConfig, final LoggingService logger, final Jdbi jdbi) {
        this.primaryConfig = primaryConfig;
        this.logger = logger;
        this.userRepository = jdbi.onDemand(UserRepository.class);
        this.userCache = Caffeine.newBuilder()
                .expireAfterAccess(Duration.of(1, ChronoUnit.HOURS))
                .maximumSize(100)
                .build();
    }

    /**
     * ユーザーデータを読み込む。
     *
     * <p>ユーザーデータの読み込み時にI/O処理が発生するため、実行中はスレッドがブロックされる。
     * 読み込んだユーザーデータはキャッシュに保存されるため、このメソッドの呼び出しはユーザーデータがキャッシュに存在する限り高速になる。</p>
     *
     * <p>Note: ユーザーデータが存在しない場合、新しいユーザーデータを生成するがストレージへの保存はしない。</p>
     *
     * @param userUUID 取得対象のユーザーUUID
     * @return 読み込んだユーザーデータ
     */
    public CompletableFuture<CarbonJapanizerUser> loadUser(final UUID userUUID) {
        // First, attempt to retrieve synchronously from the cache.
        final var cachedUser = this.userCache.getIfPresent(userUUID);
        if (cachedUser != null) {
            return CompletableFuture.completedFuture(cachedUser);
        }

        // If not in cache, retrieve asynchronously from DB.
        return CompletableFuture.supplyAsync(() -> {
                    this.setThreadName(userUUID);
                    final var storageUser = this.userRepository.findById(userUUID);

                    if (storageUser.isPresent()) {
                        this.userCache.put(userUUID, storageUser.get());
                        return storageUser.get();
                    } else {
                        return new CarbonJapanizerUser(userUUID, this.primaryConfig.defaultJapanize());
                    }
                },
                this.executor);
    }

    /**
     * ユーザーデータを保存する。
     *
     * @param user 保存対象のユーザーデータ
     * @return {@link CompletableFuture}
     */
    public CompletableFuture<Void> saveUser(final CarbonJapanizerUser user) {
        this.userCache.put(user.uuid(), user);
        return CompletableFuture
                .runAsync(() -> {
                    this.setThreadName(user.uuid());
                    this.userRepository.save(user);
                }, this.executor)
                .exceptionally(exception -> {
                    this.logger.userSaveFailed(user.displayName(), user.uuid()).logging(exception);
                    return null;
                });
    }

    /**
     * キャッシュからユーザーデータを破棄する。
     *
     * @param targetUUID 対象ユーザーのUUID
     */
    public void invalidateFromCache(final UUID targetUUID) {
        this.userCache.invalidate(targetUUID);
    }

    private void setThreadName(final UUID uuid) {
        Thread.currentThread().setName(USER_POOL.formatted("CarbonJapanizer", uuid));
    }
}
