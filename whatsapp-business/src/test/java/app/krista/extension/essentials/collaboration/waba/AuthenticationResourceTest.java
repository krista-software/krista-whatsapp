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

import app.krista.extension.essentials.collaboration.waba.api.AuthenticationResource;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link AuthenticationResource}.
 * <p>
 * Tests OAuth callback handling including:
 * <ul>
 *   <li>Resource class structure</li>
 *   <li>Method signatures</li>
 * </ul>
 * </p>
 * <p>
 * Note: Full integration tests require a running Krista platform instance with OAuth configuration.
 * These tests verify the resource class structure and basic properties.
 * </p>
 */
public class AuthenticationResourceTest {

    @Test
    public void testResourceClass_Exists() {
        // Assert
        assertNotNull("Resource class should exist", AuthenticationResource.class);
    }

    @Test
    public void testResourceClass_IsPublic() {
        // Act
        int modifiers = AuthenticationResource.class.getModifiers();

        // Assert
        assertTrue("Resource class should be public", java.lang.reflect.Modifier.isPublic(modifiers));
    }

    @Test
    public void testResourceClass_HasCorrectPackage() {
        // Act
        String packageName = AuthenticationResource.class.getPackage().getName();

        // Assert
        assertEquals("Resource should be in correct package", 
            "app.krista.extension.essentials.collaboration.waba.api", packageName);
    }

    @Test
    public void testResourceClass_HasConstructor() {
        // Act
        var constructors = AuthenticationResource.class.getConstructors();

        // Assert
        assertTrue("Resource should have at least one public constructor", constructors.length > 0);
    }

    @Test
    public void testResourceClass_HasGetCallBackMethod() throws NoSuchMethodException {
        // Act
        var method = AuthenticationResource.class.getDeclaredMethod("getCallBack", String.class, String.class);

        // Assert
        assertNotNull("Resource should have getCallBack method", method);
        assertEquals("getCallBack should return String", String.class, method.getReturnType());
    }

    @Test
    public void testResourceClass_IsNotAbstract() {
        // Act
        int modifiers = AuthenticationResource.class.getModifiers();

        // Assert
        assertFalse("Resource class should not be abstract", java.lang.reflect.Modifier.isAbstract(modifiers));
    }

    @Test
    public void testResourceClass_IsNotInterface() {
        // Assert
        assertFalse("Resource class should not be an interface", AuthenticationResource.class.isInterface());
    }

    @Test
    public void testResourceClass_HasExpectedMethods() {
        // Act
        var methods = AuthenticationResource.class.getDeclaredMethods();

        // Assert
        assertTrue("Resource should have at least one method", methods.length > 0);
    }

    @Test
    public void testResourceClass_HasExpectedFields() {
        // Act
        var fields = AuthenticationResource.class.getDeclaredFields();

        // Assert
        assertTrue("Resource should have multiple fields", fields.length > 0);
    }

    @Test
    public void testGetCallBackMethod_HasCorrectParameterCount() throws NoSuchMethodException {
        // Act
        var method = AuthenticationResource.class.getDeclaredMethod("getCallBack", String.class, String.class);

        // Assert
        assertEquals("getCallBack should have 2 parameters", 2, method.getParameterCount());
    }

    @Test
    public void testGetCallBackMethod_HasCorrectParameterTypes() throws NoSuchMethodException {
        // Act
        var method = AuthenticationResource.class.getDeclaredMethod("getCallBack", String.class, String.class);
        var parameterTypes = method.getParameterTypes();

        // Assert
        assertEquals("First parameter should be String", String.class, parameterTypes[0]);
        assertEquals("Second parameter should be String", String.class, parameterTypes[1]);
    }
}

