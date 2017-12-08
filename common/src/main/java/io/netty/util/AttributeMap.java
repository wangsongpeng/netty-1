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

/**
 * Holds {@link Attribute}s which can be accessed via {@link AttributeKey}.
 *
 * 实现方法必须线程安全
 */
public interface AttributeMap {
    /**
     * 根据参数 AttributeKey 获取 Attribute 的值，这个方法不会返回 null，
     * 但是可能会返回一个没有值的 Attribute
     */
    <T> Attribute<T> attr(AttributeKey<T> key);

    /**
     * 当且仅当提供的 Attribute 在 AttributeMap 中存在时返回 true
     */
    <T> boolean hasAttr(AttributeKey<T> key);
}
