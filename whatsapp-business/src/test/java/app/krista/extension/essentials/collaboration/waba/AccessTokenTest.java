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

import app.krista.extension.essentials.collaboration.waba.client.AccessToken;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;

public class AccessTokenTest {

    @Test
    public void testGetError() {
        AccessToken.Error error = new AccessToken.Error("Error message", "Error type", "Error code", "Error subcode", "FB Trace ID");

        AccessToken accessToken = new AccessToken();
        setErrorField(accessToken, error);

        assertEquals(error, accessToken.getError());
    }

    @Test
    public void testGetError_Null() {
        AccessToken accessToken = new AccessToken();
        setErrorField(accessToken, null);

        assertNull(accessToken.getError());
    }

    @Test
    public void testAccess_Token() {
        String token = "access_token_value";

        AccessToken accessToken = new AccessToken();
        accessToken.access_Token = token;

        assertEquals(token, accessToken.access_Token);
    }

    @Test
    public void testTokenType() {
        String tokenType = "Bearer";

        AccessToken accessToken = new AccessToken();
        accessToken.tokenType = tokenType;

        assertEquals(tokenType, accessToken.tokenType);
    }

    @Test
    public void testExpiresIn() {
        String expiresIn = "3600";

        AccessToken accessToken = new AccessToken();
        accessToken.expiresIn = expiresIn;

        assertEquals(expiresIn, accessToken.expiresIn);
    }

    private void setErrorField(AccessToken accessToken, AccessToken.Error error) {
        try {
            Field errorField = AccessToken.class.getDeclaredField("error");
            errorField.setAccessible(true);
            errorField.set(accessToken, error);
        } catch (Exception e) {
            throw new RuntimeException("Error setting AccessToken.Error field", e);
        }
    }
}
