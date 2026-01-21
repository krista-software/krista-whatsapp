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

import app.krista.extension.essentials.collaboration.waba.client.WhatsAppClient;
import app.krista.extension.essentials.collaboration.waba.store.InvokerParameterStore;
import app.krista.omni.sdk.complex_fields.impl.FromLinkRequestAuthenticator;
import app.krista.omni.sdk.medium.api.MediumService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link WhatsAppBusinessRequestAuthenticator}.
 * <p>
 * Tests request authentication including:
 * <ul>
 *   <li>Constructor initialization</li>
 *   <li>Scheme and protocol support</li>
 *   <li>Attribute fields</li>
 *   <li>Service authorization</li>
 * </ul>
 * </p>
 */
public class WhatsAppBusinessRequestAuthenticatorTest {

    @Mock
    private MediumService mockMediumService;

    @Mock
    private WhatsAppClient mockWhatsAppClient;

    @Mock
    private InvokerParameterStore mockInvokerParameterStore;

    @Mock
    private FromLinkRequestAuthenticator mockFromLinkAuthenticator;

    private WhatsAppBusinessRequestAuthenticator authenticator;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        authenticator = new WhatsAppBusinessRequestAuthenticator(
            mockMediumService,
            mockWhatsAppClient,
            mockInvokerParameterStore,
            mockFromLinkAuthenticator
        );
    }

    @Test
    public void testConstructor_InitializesCorrectly() {
        // Arrange & Act
        WhatsAppBusinessRequestAuthenticator auth = new WhatsAppBusinessRequestAuthenticator(
            mockMediumService,
            mockWhatsAppClient,
            mockInvokerParameterStore,
            mockFromLinkAuthenticator
        );

        // Assert
        assertNotNull("Authenticator should be initialized", auth);
    }

    @Test
    public void testGetScheme_ReturnsNull() {
        // Act
        String scheme = authenticator.getScheme();

        // Assert
        assertNull("Scheme should be null", scheme);
    }

    @Test
    public void testGetSupportedProtocols_ReturnsEmptySet() {
        // Act
        var protocols = authenticator.getSupportedProtocols();

        // Assert
        assertNotNull("Supported protocols should not be null", protocols);
        assertTrue("Supported protocols should be empty", protocols.isEmpty());
    }

    @Test
    public void testSetServiceAuthorization_ReturnsFalse() {
        // Act
        boolean result = authenticator.setServiceAuthorization("test");

        // Assert
        assertFalse("Service authorization should return false", result);
    }

    @Test
    public void testGetAttributeFields_ReturnsEmptyMap() {
        // Act
        var fields = authenticator.getAttributeFields();

        // Assert
        assertNotNull("Attribute fields should not be null", fields);
        assertTrue("Attribute fields should be empty", fields.isEmpty());
    }

    @Test
    public void testConstructor_WithNullMediumService() {
        // Arrange & Act
        WhatsAppBusinessRequestAuthenticator auth = new WhatsAppBusinessRequestAuthenticator(
            null,
            mockWhatsAppClient,
            mockInvokerParameterStore,
            mockFromLinkAuthenticator
        );

        // Assert
        assertNotNull("Authenticator should be initialized even with null medium service", auth);
    }

    @Test
    public void testConstructor_WithNullWhatsAppClient() {
        // Arrange & Act
        WhatsAppBusinessRequestAuthenticator auth = new WhatsAppBusinessRequestAuthenticator(
            mockMediumService,
            null,
            mockInvokerParameterStore,
            mockFromLinkAuthenticator
        );

        // Assert
        assertNotNull("Authenticator should be initialized even with null WhatsApp client", auth);
    }

    @Test
    public void testConstructor_WithNullInvokerParameterStore() {
        // Arrange & Act
        WhatsAppBusinessRequestAuthenticator auth = new WhatsAppBusinessRequestAuthenticator(
            mockMediumService,
            mockWhatsAppClient,
            null,
            mockFromLinkAuthenticator
        );

        // Assert
        assertNotNull("Authenticator should be initialized even with null invoker parameter store", auth);
    }

    @Test
    public void testConstructor_WithNullFromLinkAuthenticator() {
        // Arrange & Act
        WhatsAppBusinessRequestAuthenticator auth = new WhatsAppBusinessRequestAuthenticator(
            mockMediumService,
            mockWhatsAppClient,
            mockInvokerParameterStore,
            null
        );

        // Assert
        assertNotNull("Authenticator should be initialized even with null from link authenticator", auth);
    }

    @Test
    public void testConstructor_WithAllNullParameters() {
        // Arrange & Act
        WhatsAppBusinessRequestAuthenticator auth = new WhatsAppBusinessRequestAuthenticator(
            null,
            null,
            null,
            null
        );

        // Assert
        assertNotNull("Authenticator should be initialized even with all null parameters", auth);
    }

    @Test
    public void testGetScheme_ConsistentlyReturnsNull() {
        // Act
        String scheme1 = authenticator.getScheme();
        String scheme2 = authenticator.getScheme();

        // Assert
        assertNull("First call should return null", scheme1);
        assertNull("Second call should return null", scheme2);
    }

    @Test
    public void testGetSupportedProtocols_ConsistentlyReturnsEmptySet() {
        // Act
        var protocols1 = authenticator.getSupportedProtocols();
        var protocols2 = authenticator.getSupportedProtocols();

        // Assert
        assertNotNull("First call should not return null", protocols1);
        assertNotNull("Second call should not return null", protocols2);
        assertTrue("First call should return empty set", protocols1.isEmpty());
        assertTrue("Second call should return empty set", protocols2.isEmpty());
    }

    @Test
    public void testSetServiceAuthorization_WithNullParameter() {
        // Act
        boolean result = authenticator.setServiceAuthorization(null);

        // Assert
        assertFalse("Service authorization with null should return false", result);
    }

    @Test
    public void testSetServiceAuthorization_WithEmptyString() {
        // Act
        boolean result = authenticator.setServiceAuthorization("");

        // Assert
        assertFalse("Service authorization with empty string should return false", result);
    }

    @Test
    public void testSetServiceAuthorization_WithValidString() {
        // Act
        boolean result = authenticator.setServiceAuthorization("valid_auth_string");

        // Assert
        assertFalse("Service authorization with valid string should return false", result);
    }

    @Test
    public void testGetAttributeFields_ConsistentlyReturnsEmptyMap() {
        // Act
        var fields1 = authenticator.getAttributeFields();
        var fields2 = authenticator.getAttributeFields();

        // Assert
        assertNotNull("First call should not return null", fields1);
        assertNotNull("Second call should not return null", fields2);
        assertTrue("First call should return empty map", fields1.isEmpty());
        assertTrue("Second call should return empty map", fields2.isEmpty());
    }
}

