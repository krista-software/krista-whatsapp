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

import app.krista.extension.essentials.collaboration.waba.api.WhatsAppBusinessResource;
import app.krista.extension.executor.Invoker;
import app.krista.omni.sdk.medium.api.message.MediumMessageReceiver;
import app.krista.omni.sdk.medium.text.TextMediumService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static app.krista.extension.essentials.collaboration.waba.util.Constants.VERIFY_TOKEN;
import static org.junit.Assert.assertEquals;

public class WhatsAppBusinessResourceTest {
    @Mock
    private Invoker mockInvoker;
    @Mock
    private TextMediumService mockTextMediumService;
    @Mock
    private MediumMessageReceiver mockTextMediumMessageReceiver;
    private WhatsAppBusinessResource authenticationResource;
    @Mock
    TextMediumService textMediumService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(mockTextMediumService.getReceiver()).thenReturn(mockTextMediumMessageReceiver);
    }

    @Test
    public void testVerify_SuccessfulVerification() {
        // Arrange
        String mode = "subscribe"; // Set the mode to "subscribe"
        String challenge = "challenge"; // Set the challenge value
        String verifyToken = "verify_token"; // Set the verify token

        // Create a map to hold the attributes
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(VERIFY_TOKEN, verifyToken); // Add the verify token to the attributes map

        // Stub the behavior of the mockInvoker
        Mockito.when(mockInvoker.getAttributes()).thenReturn(attributes);

        // Create a mocked static instance of TextMediumService
        try (MockedStatic<TextMediumService> textMediumServiceMockedStatic = Mockito.mockStatic(TextMediumService.class)) {
            // Configure the behavior of the getInstance() method to return the textMediumService mock
            textMediumServiceMockedStatic.when(TextMediumService::getInstance).thenReturn(textMediumService);

            // Create an instance of WhatsAppBusinessResource using the mockInvoker
            authenticationResource = new WhatsAppBusinessResource(mockInvoker);
        }

        // Act
        Response response = authenticationResource.verify(mode, challenge, verifyToken);

        // Assert
        assertEquals(200, response.getStatus()); // Verify that the response status is 200
        assertEquals(challenge, response.getEntity()); // Verify that the response entity matches the challenge value
    }


    @Test
    public void testVerify_FailedVerification() {
        // Arrange
        String mode = "subscribe"; // Set the mode to "subscribe"
        String challenge = "challenge"; // Set the challenge value
        String verifyToken = "verify_token"; // Set the verify token

        // Create an empty map for the attributes
        Map<String, Object> attributes = new HashMap<>();

        // Stub the behavior of the mockInvoker to return the empty attributes map
        Mockito.when(mockInvoker.getAttributes()).thenReturn(attributes);

        // Create a mocked static instance of TextMediumService
        try (MockedStatic<TextMediumService> textMediumServiceMockedStatic = Mockito.mockStatic(TextMediumService.class)) {
            // Configure the behavior of the getInstance() method to return the textMediumService mock
            textMediumServiceMockedStatic.when(TextMediumService::getInstance).thenReturn(textMediumService);

            // Create an instance of WhatsAppBusinessResource using the mockInvoker
            authenticationResource = new WhatsAppBusinessResource(mockInvoker);
        }

        // Act
        Response response = authenticationResource.verify(mode, challenge, verifyToken);

        // Assert
        assertEquals(403, response.getStatus()); // Verify that the response status is 403
    }


}
