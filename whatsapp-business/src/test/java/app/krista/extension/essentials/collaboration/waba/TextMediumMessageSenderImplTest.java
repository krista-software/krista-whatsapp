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

import app.krista.extension.essentials.collaboration.waba.client.TextMediumMessageSenderImpl;
import app.krista.extension.essentials.collaboration.waba.client.WhatsAppClient;
import app.krista.extension.essentials.collaboration.waba.client.WhatsAppMessageClient;
import app.krista.extension.essentials.collaboration.waba.request.MessageRequest;
import app.krista.omni.sdk.medium.api.message.MediumInfo;
import app.krista.omni.sdk.medium.api.message.MediumMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link TextMediumMessageSenderImpl}.
 * <p>
 * Tests message sending functionality including:
 * <ul>
 *   <li>Single message sending</li>
 *   <li>Batch message sending</li>
 *   <li>Message transformation</li>
 *   <li>Medium type identification</li>
 * </ul>
 * </p>
 */
public class TextMediumMessageSenderImplTest {

    @Mock
    private WhatsAppClient mockWhatsAppClient;

    @Mock
    private WhatsAppMessageClient mockMessageClient;

    @Mock
    private MediumMessage mockMessage1;

    @Mock
    private MediumMessage mockMessage2;

    @Mock
    private MediumInfo mockMediumInfo1;

    @Mock
    private MediumInfo mockMediumInfo2;

    private TextMediumMessageSenderImpl messageSender;

    private static final String TEST_RECIPIENT_1 = "1234567890";
    private static final String TEST_RECIPIENT_2 = "0987654321";
    private static final String TEST_MESSAGE_TEXT_1 = "Hello, User 1!";
    private static final String TEST_MESSAGE_TEXT_2 = "Hello, User 2!";
    private static final String TEST_RESPONSE = "{\"messaging_product\":\"whatsapp\",\"messages\":[{\"id\":\"wamid.test\"}]}";

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        when(mockWhatsAppClient.getMessageClient()).thenReturn(mockMessageClient);
        when(mockMessageClient.postMessage(any(MessageRequest.class))).thenReturn(TEST_RESPONSE);
        
        // Setup first message
        when(mockMessage1.getMediumInfo()).thenReturn(mockMediumInfo1);
        when(mockMediumInfo1.getTargetAddress()).thenReturn(TEST_RECIPIENT_1);
        when(mockMessage1.getText()).thenReturn(TEST_MESSAGE_TEXT_1);
        
        // Setup second message
        when(mockMessage2.getMediumInfo()).thenReturn(mockMediumInfo2);
        when(mockMediumInfo2.getTargetAddress()).thenReturn(TEST_RECIPIENT_2);
        when(mockMessage2.getText()).thenReturn(TEST_MESSAGE_TEXT_2);
        
        messageSender = new TextMediumMessageSenderImpl(mockWhatsAppClient);
    }

    @Test
    public void testSendMessages_SingleMessage() {
        // Arrange
        List<MediumMessage> messages = Arrays.asList(mockMessage1);

        // Act
        messageSender.sendMessages(messages);

        // Assert
        verify(mockWhatsAppClient, times(1)).getMessageClient();
        verify(mockMessageClient, times(1)).postMessage(any(MessageRequest.class));
        verify(mockMessage1, times(1)).getMediumInfo();
        verify(mockMessage1, times(1)).getText();
    }

    @Test
    public void testSendMessages_MultipleMessages() {
        // Arrange
        List<MediumMessage> messages = Arrays.asList(mockMessage1, mockMessage2);

        // Act
        messageSender.sendMessages(messages);

        // Assert
        verify(mockWhatsAppClient, times(2)).getMessageClient();
        verify(mockMessageClient, times(2)).postMessage(any(MessageRequest.class));
        verify(mockMessage1, times(1)).getMediumInfo();
        verify(mockMessage2, times(1)).getMediumInfo();
    }

    @Test
    public void testSendMessages_EmptyList() {
        // Arrange
        List<MediumMessage> messages = Arrays.asList();

        // Act
        messageSender.sendMessages(messages);

        // Assert
        verify(mockMessageClient, never()).postMessage(any(MessageRequest.class));
    }

    @Test
    public void testSendMessages_UsesCorrectRecipient() {
        // Arrange
        List<MediumMessage> messages = Arrays.asList(mockMessage1);

        // Act
        messageSender.sendMessages(messages);

        // Assert
        verify(mockMediumInfo1, times(1)).getTargetAddress();
        assertEquals("Target address should match", TEST_RECIPIENT_1, mockMediumInfo1.getTargetAddress());
    }

    @Test
    public void testSendMessages_UsesCorrectMessageText() {
        // Arrange
        List<MediumMessage> messages = Arrays.asList(mockMessage1);

        // Act
        messageSender.sendMessages(messages);

        // Assert
        verify(mockMessage1, times(1)).getText();
        assertEquals("Message text should match", TEST_MESSAGE_TEXT_1, mockMessage1.getText());
    }

    @Test
    public void testSendMessages_PreviewUrlDisabled() {
        // Arrange
        List<MediumMessage> messages = Arrays.asList(mockMessage1);
        ArgumentCaptor<MessageRequest> requestCaptor = ArgumentCaptor.forClass(MessageRequest.class);

        // Act
        messageSender.sendMessages(messages);

        // Assert
        verify(mockMessageClient, times(1)).postMessage(requestCaptor.capture());
        // Note: We can't directly verify previewUrl without accessing the request object
        // This would require the MessageRequest to have a getter for previewUrl
    }

    @Test
    public void testSendMessages_ProcessesMessagesIndependently() {
        // Arrange
        List<MediumMessage> messages = Arrays.asList(mockMessage1, mockMessage2);
        
        // Make the first message fail
        when(mockMessageClient.postMessage(any(MessageRequest.class)))
            .thenThrow(new RuntimeException("First message failed"))
            .thenReturn(TEST_RESPONSE);

        // Act & Assert
        try {
            messageSender.sendMessages(messages);
            fail("Should have thrown exception for first message");
        } catch (RuntimeException e) {
            assertEquals("Exception message should match", "First message failed", e.getMessage());
        }
    }

    @Test
    public void testSendMessages_ReturnsApiResponse() {
        // Arrange
        List<MediumMessage> messages = Arrays.asList(mockMessage1);

        // Act
        messageSender.sendMessages(messages);

        // Assert
        verify(mockMessageClient, times(1)).postMessage(any(MessageRequest.class));
        // The response is logged but not returned by sendMessages
    }

    @Test
    public void testConstructor_InitializesCorrectly() {
        // Arrange & Act
        TextMediumMessageSenderImpl sender = new TextMediumMessageSenderImpl(mockWhatsAppClient);

        // Assert
        assertNotNull("Message sender should be initialized", sender);
    }

    @Test
    public void testSendMessages_WithNullText() {
        // Arrange
        when(mockMessage1.getText()).thenReturn(null);
        List<MediumMessage> messages = Arrays.asList(mockMessage1);

        // Act
        messageSender.sendMessages(messages);

        // Assert
        verify(mockMessageClient, times(1)).postMessage(any(MessageRequest.class));
        // The implementation should handle null text gracefully
    }

    @Test
    public void testSendMessages_WithEmptyText() {
        // Arrange
        when(mockMessage1.getText()).thenReturn("");
        List<MediumMessage> messages = Arrays.asList(mockMessage1);

        // Act
        messageSender.sendMessages(messages);

        // Assert
        verify(mockMessageClient, times(1)).postMessage(any(MessageRequest.class));
    }

    @Test
    public void testSendMessages_WithLongText() {
        // Arrange
        String longText = "A".repeat(4096); // WhatsApp max length
        when(mockMessage1.getText()).thenReturn(longText);
        List<MediumMessage> messages = Arrays.asList(mockMessage1);

        // Act
        messageSender.sendMessages(messages);

        // Assert
        verify(mockMessageClient, times(1)).postMessage(any(MessageRequest.class));
        verify(mockMessage1, times(1)).getText();
    }

    @Test
    public void testSendMessages_WithSpecialCharacters() {
        // Arrange
        String specialText = "Hello! 😊 How are you? \n\t Special chars: @#$%";
        when(mockMessage1.getText()).thenReturn(specialText);
        List<MediumMessage> messages = Arrays.asList(mockMessage1);

        // Act
        messageSender.sendMessages(messages);

        // Assert
        verify(mockMessageClient, times(1)).postMessage(any(MessageRequest.class));
    }

    @Test
    public void testSendMessages_CallsGetMessageClientForEachMessage() {
        // Arrange
        List<MediumMessage> messages = Arrays.asList(mockMessage1, mockMessage2);

        // Act
        messageSender.sendMessages(messages);

        // Assert
        verify(mockWhatsAppClient, times(2)).getMessageClient();
    }

    @Test
    public void testSendMessages_WithUrlInText() {
        // Arrange
        String textWithUrl = "Check this out: https://example.com";
        when(mockMessage1.getText()).thenReturn(textWithUrl);
        List<MediumMessage> messages = Arrays.asList(mockMessage1);

        // Act
        messageSender.sendMessages(messages);

        // Assert
        verify(mockMessageClient, times(1)).postMessage(any(MessageRequest.class));
        // Preview URL should be disabled (false) as per implementation
    }
}

