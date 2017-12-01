/*
 * Copyright 2017 The Netty Project
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
package io.netty.util.internal;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.security.AccessController;
import java.security.PrivilegedAction;

import java.util.Set;

/**
 * Special {@link PhantomReference} that will be cleaned automatically by a background thread.
 */
public final class AutomaticCleanupReference extends PhantomReference<Object> {
    // This will hold a reference to the AutomaticCleanupReference which will be removed once we called cleanup()
    private static final Set<AutomaticCleanupReference> LIVE_SET = new ConcurrentSet<AutomaticCleanupReference>();
    private static final ReferenceQueue<Object> REFERENCE_QUEUE = new ReferenceQueue<Object>();

    static {
        AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                Thread cleanupThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (;;) {
                            try {
                                AutomaticCleanupReference reference =
                                        (AutomaticCleanupReference) REFERENCE_QUEUE.remove();
                                try {
                                    reference.cleanup();
                                } finally {
                                    LIVE_SET.remove(reference);
                                }
                            } catch (InterruptedException ignore) {
                                // Just consume and move on.
                            }
                        }
                    }
                });
                cleanupThread.setPriority(Thread.MIN_PRIORITY);
                cleanupThread.setName("AutomaticCleanupReference-Thread");
                cleanupThread.setDaemon(true);
                cleanupThread.start();
                return null;
            }
        });
    }

    private final Runnable cleanupTask;

    private AutomaticCleanupReference(Object referent, ReferenceQueue<Object> refQueue, Runnable cleanupTask) {
        super(referent, refQueue);
        this.cleanupTask = cleanupTask;
    }

    @Override
    public void clear() {
        super.clear();
        LIVE_SET.remove(this);
    }

    /**
     * Do any cleanup actions for this {@link AutomaticCleanupReference}.
     */
    private void cleanup() {
        if (cleanupTask != null) {
            cleanupTask.run();
        }
    }

    /**
     * Register the given {@link Object} for which the {@link Runnable} will be executed once there are no references
     * to the object anymore.
     */
    public static void register(Object object, Runnable cleanupTask) {
        AutomaticCleanupReference ref = new AutomaticCleanupReference(object, REFERENCE_QUEUE, cleanupTask);
        LIVE_SET.add(ref);
    }
}
