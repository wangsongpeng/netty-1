/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.util;

import java.util.Set;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务 TimerTasks 在后台线程中一次性执行。
 */
public interface Timer {

    /**
     * 在指定的延迟之后安排指定的 TimerTask 执行一次执行
     *
     * 返回一个与指定任务相关的句柄
     *
     * @throws IllegalStateException       如果这个 timer 停止
     * @throws RejectedExecutionException 如果等待中的超时时间太长，创建新的超时会导致系统不稳定。
     */
    Timeout newTimeout(TimerTask task, long delay, TimeUnit unit);

    /**
     *
     * 释放此 Timer 所获取的所有资源，并取消所有已安排但尚未执行的任务。
     *
     * 返回与由此方法取消的任务关联的句柄
     */
    Set<Timeout> stop();
}
