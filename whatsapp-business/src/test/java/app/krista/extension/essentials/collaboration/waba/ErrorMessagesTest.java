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

import app.krista.extension.essentials.collaboration.waba.util.ErrorMessages;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link ErrorMessages}.
 * <p>
 * Tests error message generation including:
 * <ul>
 *   <li>Static error messages</li>
 *   <li>Dynamic error messages with parameters</li>
 *   <li>Message formatting</li>
 *   <li>User-friendly error descriptions</li>
 * </ul>
 * </p>
 */
public class ErrorMessagesTest {

    @Test
    public void testAuthNotAuthenticated() {
        // Act
        String message = ErrorMessages.AUTH_NOT_AUTHENTICATED;

        // Assert
        assertNotNull("Error message should not be null", message);
        assertFalse("Error message should not be empty", message.isEmpty());
        assertTrue("Error message should mention authentication", 
            message.toLowerCase().contains("authenticat") || message.toLowerCase().contains("authoriz"));
    }

    @Test
    public void testAuthTokenExpired() {
        // Act
        String message = ErrorMessages.AUTH_TOKEN_EXPIRED;

        // Assert
        assertNotNull("Error message should not be null", message);
        assertFalse("Error message should not be empty", message.isEmpty());
        assertTrue("Error message should mention token or expir", 
            message.toLowerCase().contains("token") || message.toLowerCase().contains("expir"));
    }

    @Test
    public void testAuthMissingCode() {
        // Act
        String message = ErrorMessages.AUTH_MISSING_CODE;

        // Assert
        assertNotNull("Error message should not be null", message);
        assertFalse("Error message should not be empty", message.isEmpty());
        assertTrue("Error message should mention code", message.toLowerCase().contains("code"));
    }

    @Test
    public void testAuthInvalidState() {
        // Act
        String message = ErrorMessages.AUTH_INVALID_STATE;

        // Assert
        assertNotNull("Error message should not be null", message);
        assertFalse("Error message should not be empty", message.isEmpty());
        assertTrue("Error message should mention state or invalid", 
            message.toLowerCase().contains("state") || message.toLowerCase().contains("invalid"));
    }

    @Test
    public void testAuthInvalidCredentials() {
        // Act
        String message = ErrorMessages.AUTH_INVALID_CREDENTIALS;

        // Assert
        assertNotNull("Error message should not be null", message);
        assertFalse("Error message should not be empty", message.isEmpty());
        assertTrue("Error message should mention credentials or invalid", 
            message.toLowerCase().contains("credential") || message.toLowerCase().contains("invalid"));
    }

    @Test
    public void testAuthCallbackSuccess() {
        // Act
        String message = ErrorMessages.AUTH_CALLBACK_SUCCESS;

        // Assert
        assertNotNull("Error message should not be null", message);
        assertFalse("Error message should not be empty", message.isEmpty());
        assertTrue("Success message should be positive", 
            message.toLowerCase().contains("success") || message.toLowerCase().contains("complete"));
    }

    @Test
    public void testConversationNotUnique() {
        // Act
        String message = ErrorMessages.CONVERSATION_NOT_UNIQUE;

        // Assert
        assertNotNull("Error message should not be null", message);
        assertFalse("Error message should not be empty", message.isEmpty());
        assertTrue("Error message should mention conversation or unique", 
            message.toLowerCase().contains("conversation") || message.toLowerCase().contains("unique"));
    }

    @Test
    public void testConversationNotFound_WithParameter() {
        // Arrange
        String conversationName = "TestConversation";

        // Act
        String message = ErrorMessages.conversationNotFound(conversationName);

        // Assert
        assertNotNull("Error message should not be null", message);
        assertFalse("Error message should not be empty", message.isEmpty());
        assertTrue("Error message should contain conversation name", message.contains(conversationName));
        assertTrue("Error message should mention not found", message.toLowerCase().contains("not found"));
    }

    @Test
    public void testConversationNotFound_WithNullParameter() {
        // Act
        String message = ErrorMessages.conversationNotFound(null);

        // Assert
        assertNotNull("Error message should not be null", message);
        // Should handle null gracefully
    }

    @Test
    public void testConversationNotFound_WithEmptyParameter() {
        // Act
        String message = ErrorMessages.conversationNotFound("");

        // Assert
        assertNotNull("Error message should not be null", message);
        assertFalse("Error message should not be empty", message.isEmpty());
    }

    @Test
    public void testApiConnectionError_WithParameter() {
        // Arrange
        String errorDetails = "Connection timeout";

        // Act
        String message = ErrorMessages.apiConnectionError(errorDetails);

        // Assert
        assertNotNull("Error message should not be null", message);
        assertFalse("Error message should not be empty", message.isEmpty());
        assertTrue("Error message should contain error details", message.contains(errorDetails));
        assertTrue("Error message should mention connection or API", 
            message.toLowerCase().contains("connection") || message.toLowerCase().contains("api"));
    }

    @Test
    public void testApiConnectionError_WithNullParameter() {
        // Act
        String message = ErrorMessages.apiConnectionError(null);

        // Assert
        assertNotNull("Error message should not be null", message);
        // Should handle null gracefully
    }

    @Test
    public void testApiRequestFailed_WithParameter() {
        // Arrange
        String responseDetails = "{\"error\":\"Invalid request\"}";

        // Act
        String message = ErrorMessages.apiRequestFailed(responseDetails);

        // Assert
        assertNotNull("Error message should not be null", message);
        assertFalse("Error message should not be empty", message.isEmpty());
        assertTrue("Error message should contain response details", message.contains(responseDetails));
        assertTrue("Error message should mention failed or error", 
            message.toLowerCase().contains("fail") || message.toLowerCase().contains("error"));
    }

    @Test
    public void testApiRequestFailed_WithNullParameter() {
        // Act
        String message = ErrorMessages.apiRequestFailed(null);

        // Assert
        assertNotNull("Error message should not be null", message);
        // Should handle null gracefully
    }

    @Test
    public void testDebugPhoneNumberInfo_WithParameter() {
        // Arrange
        String phoneNumberId = "123456789";

        // Act
        String message = ErrorMessages.debugPhoneNumberInfo(phoneNumberId);

        // Assert
        assertNotNull("Error message should not be null", message);
        assertFalse("Error message should not be empty", message.isEmpty());
        assertTrue("Error message should contain phone number ID", message.contains(phoneNumberId));
    }

    @Test
    public void testDebugPhoneNumberInfo_WithNullParameter() {
        // Act
        String message = ErrorMessages.debugPhoneNumberInfo(null);

        // Assert
        assertNotNull("Error message should not be null", message);
        // Should handle null gracefully
    }

    @Test
    public void testErrorMessages_AreUserFriendly() {
        // Test that error messages are user-friendly (not too technical)
        String[] messages = {
            ErrorMessages.AUTH_NOT_AUTHENTICATED,
            ErrorMessages.AUTH_TOKEN_EXPIRED,
            ErrorMessages.AUTH_MISSING_CODE,
            ErrorMessages.AUTH_INVALID_STATE,
            ErrorMessages.AUTH_INVALID_CREDENTIALS,
            ErrorMessages.CONVERSATION_NOT_UNIQUE
        };

        for (String message : messages) {
            assertNotNull("Message should not be null", message);
            assertFalse("Message should not be empty", message.isEmpty());
            assertTrue("Message should be reasonably long", message.length() > 10);
            // User-friendly messages typically don't contain stack traces or technical jargon
            assertFalse("Message should not contain stack trace", message.contains("Exception"));
        }
    }

    @Test
    public void testConversationNotFound_DifferentNames() {
        // Arrange
        String name1 = "Conversation1";
        String name2 = "Conversation2";

        // Act
        String message1 = ErrorMessages.conversationNotFound(name1);
        String message2 = ErrorMessages.conversationNotFound(name2);

        // Assert
        assertNotEquals("Messages should be different for different names", message1, message2);
        assertTrue("First message should contain first name", message1.contains(name1));
        assertTrue("Second message should contain second name", message2.contains(name2));
    }

    @Test
    public void testApiConnectionError_DifferentErrors() {
        // Arrange
        String error1 = "Timeout";
        String error2 = "Connection refused";

        // Act
        String message1 = ErrorMessages.apiConnectionError(error1);
        String message2 = ErrorMessages.apiConnectionError(error2);

        // Assert
        assertNotEquals("Messages should be different for different errors", message1, message2);
        assertTrue("First message should contain first error", message1.contains(error1));
        assertTrue("Second message should contain second error", message2.contains(error2));
    }

    @Test
    public void testApiRequestFailed_DifferentResponses() {
        // Arrange
        String response1 = "Error 400";
        String response2 = "Error 500";

        // Act
        String message1 = ErrorMessages.apiRequestFailed(response1);
        String message2 = ErrorMessages.apiRequestFailed(response2);

        // Assert
        assertNotEquals("Messages should be different for different responses", message1, message2);
        assertTrue("First message should contain first response", message1.contains(response1));
        assertTrue("Second message should contain second response", message2.contains(response2));
    }

    @Test
    public void testErrorMessages_NoHardcodedValues() {
        // Verify that error messages don't contain hardcoded test values
        String[] messages = {
            ErrorMessages.AUTH_NOT_AUTHENTICATED,
            ErrorMessages.AUTH_TOKEN_EXPIRED,
            ErrorMessages.CONVERSATION_NOT_UNIQUE
        };

        for (String message : messages) {
            assertFalse("Message should not contain 'test'", message.toLowerCase().contains("test"));
            assertFalse("Message should not contain 'TODO'", message.contains("TODO"));
            assertFalse("Message should not contain 'FIXME'", message.contains("FIXME"));
        }
    }
}

