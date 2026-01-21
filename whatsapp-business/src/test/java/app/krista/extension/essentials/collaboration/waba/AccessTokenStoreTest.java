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

package app.krista.extension.essentials.collaboration.waba;

import app.krista.extension.essentials.collaboration.waba.store.AccessTokenStore;
import app.krista.extensions.util.KeyValueStore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AccessTokenStoreTest {

    private KeyValueStore mockKeyValueStore;

    private AccessTokenStore accessTokenStore;

    @Before
    public void setUp() {
        mockKeyValueStore = Mockito.mock(KeyValueStore.class);
        accessTokenStore = AccessTokenStore.create(mockKeyValueStore);
    }

    @Test
    public void testPut() {
        String key = "test_key";
        String refToken = "test_ref_token";
        accessTokenStore.put(key, refToken);
        verify(mockKeyValueStore, times(1)).put(key + "_access_token", refToken);
    }

    @Test
    public void testToKey() {
        String key = "test_key";
        String expected = "test_key_access_token";
        String result = accessTokenStore.toKey(key);
        assertEquals(expected, result);
    }

    @Test
    public void testGet() {
        String key = "test_key";
        String accessToken = "test_access_token";
        when(mockKeyValueStore.get(key + "_access_token")).thenReturn(accessToken);
        String result = accessTokenStore.get(key);
        assertEquals(accessToken, result);
    }

    @Test
    public void testGetAsString() {
        String key = "test_key";
        String accessToken = "test_access_token";
        when(mockKeyValueStore.get(key + "_access_token", String.class)).thenReturn(accessToken);
        String result = accessTokenStore.getAsString(key);
        assertEquals(accessToken, result);
    }

    @Test
    public void testRemove() {
        String key = "test_key";
        accessTokenStore.remove(key);
        verify(mockKeyValueStore, times(1)).remove(key);
    }
}