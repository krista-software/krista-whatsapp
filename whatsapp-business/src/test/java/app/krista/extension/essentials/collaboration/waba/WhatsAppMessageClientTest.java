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

import app.krista.extension.essentials.collaboration.waba.client.WhatsAppMessageClient;
import app.krista.extension.essentials.collaboration.waba.request.MessageRequest;
import app.krista.extension.essentials.collaboration.waba.request.impl.TextMessageRequest;
import app.krista.extension.essentials.collaboration.waba.request.parameters.Text;
import app.krista.extension.essentials.collaboration.waba.store.AccessTokenStore;
import app.krista.extension.util.InvokerAttributeProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link WhatsAppMessageClient}.
 * <p>
 * Tests message sending functionality including:
 * <ul>
 *   <li>Text message posting</li>
 *   <li>Authentication header inclusion</li>
 *   <li>Error handling for API failures</li>
 *   <li>Request formatting</li>
 * </ul>
 * </p>
 */
public class WhatsAppMessageClientTest {

    @Mock
    private WebTarget mockWebTarget;

    @Mock
    private WebTarget mockPathWebTarget;

    @Mock
    private Invocation.Builder mockBuilder;

    @Mock
    private Response mockResponse;

    @Mock
    private InvokerAttributeProvider<String> mockPhoneNumberIdProvider;

    @Mock
    private AccessTokenStore mockAccessTokenStore;

    private WhatsAppMessageClient messageClient;

    private static final String TEST_PHONE_NUMBER_ID = "123456789";
    private static final String TEST_ACCESS_TOKEN = "test_access_token";
    private static final String TEST_RECIPIENT = "1234567890";
    private static final String TEST_MESSAGE_TEXT = "Hello, World!";

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        when(mockPhoneNumberIdProvider.get()).thenReturn(TEST_PHONE_NUMBER_ID);
        when(mockAccessTokenStore.get(TEST_PHONE_NUMBER_ID)).thenReturn(TEST_ACCESS_TOKEN);
        
        // Setup mock chain for WebTarget
        when(mockWebTarget.path(anyString())).thenReturn(mockPathWebTarget);
        when(mockPathWebTarget.path(anyString())).thenReturn(mockPathWebTarget);
        when(mockPathWebTarget.request(any(MediaType.class))).thenReturn(mockBuilder);
        when(mockBuilder.header(anyString(), anyString())).thenReturn(mockBuilder);
        when(mockBuilder.post(any(Entity.class))).thenReturn(mockResponse);
        when(mockResponse.readEntity(String.class)).thenReturn("{\"success\":true}");
        
        messageClient = new WhatsAppMessageClient(mockWebTarget, mockPhoneNumberIdProvider, mockAccessTokenStore);
    }

    @Test
    public void testPostMessage_Success() {
        // Arrange
        TextMessageRequest messageRequest = new TextMessageRequest(
            TEST_RECIPIENT,
            "text",
            new Text(TEST_MESSAGE_TEXT),
            true,
            "whatsapp",
            "individual"
        );

        // Act
        String response = messageClient.postMessage(messageRequest);

        // Assert
        assertNotNull("Response should not be null", response);
        assertEquals("Response should match expected", "{\"success\":true}", response);
        verify(mockWebTarget, times(1)).path(TEST_PHONE_NUMBER_ID);
        verify(mockAccessTokenStore, times(1)).get(TEST_PHONE_NUMBER_ID);
    }

    @Test
    public void testPostMessage_UsesCorrectAccessToken() {
        // Arrange
        TextMessageRequest messageRequest = new TextMessageRequest(
            TEST_RECIPIENT,
            "text",
            new Text(TEST_MESSAGE_TEXT),
            false,
            "whatsapp",
            "individual"
        );

        // Act
        messageClient.postMessage(messageRequest);

        // Assert
        verify(mockAccessTokenStore, times(1)).get(TEST_PHONE_NUMBER_ID);
        verify(mockBuilder, times(1)).header(eq("Authorization"), eq("Bearer " + TEST_ACCESS_TOKEN));
    }

    @Test
    public void testPostMessage_UsesCorrectPhoneNumberId() {
        // Arrange
        TextMessageRequest messageRequest = new TextMessageRequest(
            TEST_RECIPIENT,
            "text",
            new Text(TEST_MESSAGE_TEXT),
            false,
            "whatsapp",
            "individual"
        );

        // Act
        messageClient.postMessage(messageRequest);

        // Assert
        verify(mockPhoneNumberIdProvider, atLeastOnce()).get();
        verify(mockWebTarget, times(1)).path(TEST_PHONE_NUMBER_ID);
    }

    @Test
    public void testPostMessage_UsesMessagesEndpoint() {
        // Arrange
        TextMessageRequest messageRequest = new TextMessageRequest(
            TEST_RECIPIENT,
            "text",
            new Text(TEST_MESSAGE_TEXT),
            false,
            "whatsapp",
            "individual"
        );

        // Act
        messageClient.postMessage(messageRequest);

        // Assert
        verify(mockPathWebTarget, times(1)).path("messages");
    }

    @Test
    public void testPostMessage_UsesJsonMediaType() {
        // Arrange
        TextMessageRequest messageRequest = new TextMessageRequest(
            TEST_RECIPIENT,
            "text",
            new Text(TEST_MESSAGE_TEXT),
            false,
            "whatsapp",
            "individual"
        );

        // Act
        messageClient.postMessage(messageRequest);

        // Assert
        verify(mockPathWebTarget, times(1)).request(MediaType.APPLICATION_JSON_TYPE);
    }

    @Test(expected = IllegalStateException.class)
    public void testPostMessage_ConnectionFailure() {
        // Arrange
        TextMessageRequest messageRequest = new TextMessageRequest(
            TEST_RECIPIENT,
            "text",
            new Text(TEST_MESSAGE_TEXT),
            false,
            "whatsapp",
            "individual"
        );
        
        when(mockBuilder.post(any(Entity.class))).thenThrow(new javax.ws.rs.ProcessingException("Connection failed"));

        // Act
        messageClient.postMessage(messageRequest);

        // Assert - Exception should be thrown
    }

    @Test
    public void testPostMessage_WithPreviewUrl() {
        // Arrange
        TextMessageRequest messageRequest = new TextMessageRequest(
            TEST_RECIPIENT,
            "text",
            new Text("Check this link: https://example.com"),
            true, // preview URL enabled
            "whatsapp",
            "individual"
        );

        // Act
        String response = messageClient.postMessage(messageRequest);

        // Assert
        assertNotNull("Response should not be null", response);
        verify(mockBuilder, times(1)).post(any(Entity.class));
    }

    @Test
    public void testPostMessage_WithoutPreviewUrl() {
        // Arrange
        TextMessageRequest messageRequest = new TextMessageRequest(
            TEST_RECIPIENT,
            "text",
            new Text(TEST_MESSAGE_TEXT),
            false, // preview URL disabled
            "whatsapp",
            "individual"
        );

        // Act
        String response = messageClient.postMessage(messageRequest);

        // Assert
        assertNotNull("Response should not be null", response);
        verify(mockBuilder, times(1)).post(any(Entity.class));
    }

    @Test
    public void testPostMessage_MultipleMessages() {
        // Arrange
        TextMessageRequest message1 = new TextMessageRequest(
            "recipient1",
            "text",
            new Text("Message 1"),
            false,
            "whatsapp",
            "individual"
        );
        
        TextMessageRequest message2 = new TextMessageRequest(
            "recipient2",
            "text",
            new Text("Message 2"),
            false,
            "whatsapp",
            "individual"
        );

        // Act
        String response1 = messageClient.postMessage(message1);
        String response2 = messageClient.postMessage(message2);

        // Assert
        assertNotNull("First response should not be null", response1);
        assertNotNull("Second response should not be null", response2);
        verify(mockBuilder, times(2)).post(any(Entity.class));
    }

    @Test
    public void testConstructor_InitializesCorrectly() {
        // Arrange & Act
        WhatsAppMessageClient client = new WhatsAppMessageClient(
            mockWebTarget,
            mockPhoneNumberIdProvider,
            mockAccessTokenStore
        );

        // Assert
        assertNotNull("Message client should be initialized", client);
    }
}

