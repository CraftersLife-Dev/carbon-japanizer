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
package io.github.crafterslife.dev.papertemplate.minecraft.paper.utility;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jspecify.annotations.NullMarked;

/**
 * <p>同期タスクの実行を簡素化するためのユーティリティクラスです。</p>
 *
 * <p>このクラスは、Bukkitの{@link BukkitScheduler}をラップし、
 * メインサーバーティックで実行されるタスクのスケジューリングを簡潔なメソッドで
 * 提供します。</p>
 */
@NullMarked
public final class TemplateScheduler {

    private final Plugin plugin;
    private final BukkitScheduler scheduler;

    /**
     * 新しい{@code TemplateScheduler}インスタンスを構築します。
     *
     * @param plugin このスケジューラがタスクを実行するプラグイン
     */
    public TemplateScheduler(final Plugin plugin) {
        this.plugin = plugin;
        this.scheduler = Bukkit.getScheduler();
    }

    /**
     * 次のサーバーティックでタスクをメインスレッドで実行します。
     *
     * @param task 実行するタスク
     */
    public void runTask(final Runnable task) {
        this.scheduler.runTask(this.plugin, task);
    }

    /**
     * 指定された遅延後にタスクをメインスレッドで実行します。
     *
     * @param task  実行するタスク
     * @param delay タスクを実行するまでの遅延時間（ティック単位）
     */
    public void runTaskLater(final Runnable task, final long delay) {
        this.scheduler.runTaskLater(this.plugin, task, delay);
    }

    /**
     * 指定された遅延後にタスクをメインスレッドで繰り返し実行します。
     *
     * @param task   実行するタスク
     * @param delay  タスクを最初に実行するまでの遅延時間（ティック単位）
     * @param period タスクを繰り返し実行する間隔（ティック単位）
     */
    public void runTaskTimer(final Runnable task, final long delay, final long period) {
        this.scheduler.runTaskTimer(this.plugin, task, delay, period);
    }
}
