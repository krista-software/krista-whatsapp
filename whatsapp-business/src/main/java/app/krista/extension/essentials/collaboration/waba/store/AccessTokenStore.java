/*
 * Whatsapp Extension for Krista
 * Copyright (C) 2025 Krista Software
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

package app.krista.extension.essentials.collaboration.waba.store;

import app.krista.extensions.util.KeyValueStore;

import java.util.Objects;

public final class AccessTokenStore {

    private final KeyValueStore keyValueStore;

    private AccessTokenStore(KeyValueStore keyValueStore) {
        this.keyValueStore = keyValueStore;
    }

    public static AccessTokenStore create(KeyValueStore keyValueStore) {
        return new AccessTokenStore(keyValueStore);
    }

    public void put(String key, String refToken) {
        keyValueStore.put(toKey(key), refToken);
    }

    public String toKey(String key) {
        return key + "_access_token";
    }

    public String get(String key) {
        Object accessToken = keyValueStore.get(toKey(key));
        return Objects.isNull(accessToken) ? null : accessToken.toString();
    }

    public String getAsString(String key) {
        return keyValueStore.get(toKey(key), String.class);
    }

    public void remove(String key) {
        keyValueStore.remove(key);
    }

}
