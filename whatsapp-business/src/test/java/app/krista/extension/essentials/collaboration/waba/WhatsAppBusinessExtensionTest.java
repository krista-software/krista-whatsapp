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

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link WhatsAppBusinessExtension}.
 * <p>
 * Tests the main extension functionality including:
 * <ul>
 *   <li>Extension annotations and configuration</li>
 *   <li>Extension class structure</li>
 * </ul>
 * </p>
 * <p>
 * Note: Full integration tests require a running Krista platform instance.
 * These tests verify the extension class structure and basic properties.
 * </p>
 */
public class WhatsAppBusinessExtensionTest {

    @Test
    public void testExtensionClass_Exists() {
        // Assert
        assertNotNull("Extension class should exist", WhatsAppBusinessExtension.class);
    }

    @Test
    public void testExtensionClass_IsPublic() {
        // Act
        int modifiers = WhatsAppBusinessExtension.class.getModifiers();

        // Assert
        assertTrue("Extension class should be public", java.lang.reflect.Modifier.isPublic(modifiers));
    }

    @Test
    public void testExtensionClass_HasCorrectPackage() {
        // Act
        String packageName = WhatsAppBusinessExtension.class.getPackage().getName();

        // Assert
        assertEquals("Extension should be in correct package",
            "app.krista.extension.essentials.collaboration.waba", packageName);
    }

    @Test
    public void testExtensionClass_HasConstructor() {
        // Act
        var constructors = WhatsAppBusinessExtension.class.getConstructors();

        // Assert
        assertTrue("Extension should have at least one public constructor", constructors.length > 0);
    }

    @Test
    public void testExtensionClass_HasCustomTabsMethod() throws NoSuchMethodException {
        // Act
        var method = WhatsAppBusinessExtension.class.getDeclaredMethod("customTabs");

        // Assert
        assertNotNull("Extension should have customTabs method", method);
        assertEquals("customTabs should return Map", java.util.Map.class, method.getReturnType());
    }

    @Test
    public void testExtensionClass_HasTestConnectionMethod() throws NoSuchMethodException {
        // Act
        var method = WhatsAppBusinessExtension.class.getDeclaredMethod("testConnection");

        // Assert
        assertNotNull("Extension should have testConnection method", method);
        assertEquals("testConnection should return void", void.class, method.getReturnType());
    }

    @Test
    public void testExtensionClass_HasOnInvokerLoadMethod() throws NoSuchMethodException {
        // Act
        var method = WhatsAppBusinessExtension.class.getDeclaredMethod("onInvokerLoad");

        // Assert
        assertNotNull("Extension should have onInvokerLoad method", method);
        assertEquals("onInvokerLoad should return void", void.class, method.getReturnType());
    }

    @Test
    public void testExtensionClass_HasOnInvokerUnloadMethod() throws NoSuchMethodException {
        // Act
        var method = WhatsAppBusinessExtension.class.getDeclaredMethod("onInvokerUnload");

        // Assert
        assertNotNull("Extension should have onInvokerUnload method", method);
        assertEquals("onInvokerUnload should return void", void.class, method.getReturnType());
    }

    @Test
    public void testExtensionClass_HasGetAuthenticatedAccountIdMethod() throws NoSuchMethodException {
        // Act
        var method = WhatsAppBusinessExtension.class.getDeclaredMethod("getAuthenticatedAccountId");

        // Assert
        assertNotNull("Extension should have getAuthenticatedAccountId method", method);
        assertEquals("getAuthenticatedAccountId should return RequestAuthenticator",
            app.krista.extension.authorization.RequestAuthenticator.class, method.getReturnType());
    }

    @Test
    public void testExtensionClass_HasValidateAttributeMethod() throws NoSuchMethodException {
        // Act
        var method = WhatsAppBusinessExtension.class.getDeclaredMethod("validateAttribute", java.util.Map.class);

        // Assert
        assertNotNull("Extension should have validateAttribute method", method);
        assertEquals("validateAttribute should return void", void.class, method.getReturnType());
    }

    @Test
    public void testExtensionClass_IsNotAbstract() {
        // Act
        int modifiers = WhatsAppBusinessExtension.class.getModifiers();

        // Assert
        assertFalse("Extension class should not be abstract", java.lang.reflect.Modifier.isAbstract(modifiers));
    }

    @Test
    public void testExtensionClass_IsNotInterface() {
        // Assert
        assertFalse("Extension class should not be an interface", WhatsAppBusinessExtension.class.isInterface());
    }

    @Test
    public void testExtensionClass_HasExpectedMethods() {
        // Act
        var methods = WhatsAppBusinessExtension.class.getDeclaredMethods();

        // Assert
        assertTrue("Extension should have multiple methods", methods.length > 5);
    }

    @Test
    public void testExtensionClass_HasExpectedFields() {
        // Act
        var fields = WhatsAppBusinessExtension.class.getDeclaredFields();

        // Assert
        assertTrue("Extension should have multiple fields", fields.length > 5);
    }
}

