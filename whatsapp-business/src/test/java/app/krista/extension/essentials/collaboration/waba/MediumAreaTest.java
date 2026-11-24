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

import app.krista.extension.essentials.collaboration.waba.catalog.MediumArea;
import app.krista.extension.util.InvokerAttributeProvider;
import app.krista.ksdk.accounts.Account;
import app.krista.ksdk.context.AuthorizationContext;
import app.krista.ksdk.inboxes.Inbox;
import app.krista.ksdk.inboxes.InboxManager;
import app.krista.omni.sdk.medium.api.KristaMessageReceiver;
import app.krista.omni.sdk.medium.api.MediumService;
import app.krista.omni.sdk.medium.text.TextMediumService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class MediumAreaTest {
    @Mock
    private AuthorizationContext authorizationContext;
    @Mock
    private Account account;
    @Mock
    private Inbox inbox;
    @Mock
    private InvokerAttributeProvider<String> fromAddress;
    @Mock
    private KristaMessageReceiver kristaMessageReceiver;
    @Mock
    private InboxManager inboxManager;
    @Mock
    private MediumService mediumService;
    private MediumArea mediumArea;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetSessionIdForActiveUser_WithValidData_ReturnsSessionId() {
        // Arrange
        String email = "user@example.com";
        String sessionId = "session123";

        // Set up the mocks and dependencies
        Mockito.when(authorizationContext.getAuthorizedAccount()).thenReturn(account);
        Mockito.when(account.getPrimaryEmailAddress()).thenReturn(email);
        Mockito.when(fromAddress.get()).thenReturn("from@example.com");

        try (MockedStatic<KristaMessageReceiver> kristaMessageReceiverMockedStatic = Mockito.mockStatic(KristaMessageReceiver.class);
             MockedStatic<TextMediumService> textMediumServiceMockedStatic = Mockito.mockStatic(TextMediumService.class)) {
            kristaMessageReceiverMockedStatic.when(KristaMessageReceiver::getInstance).thenReturn(kristaMessageReceiver);
            // Mocking TextMediumService.getInstance() to return mediumService
            textMediumServiceMockedStatic.when(TextMediumService::getInstance).thenReturn(mediumService);
            // Creating MediumArea instance
            mediumArea = new MediumArea(authorizationContext, fromAddress, inboxManager);
            // Mocking KristaMessageReceiver.getSessionInbox(account) to return inbox
            Mockito.when(KristaMessageReceiver.getInstance().getSessionInbox(account)).thenReturn(inbox);
        }

        Mockito.when(inbox.getInboxId()).thenReturn("session123");

        // Act
        String result = mediumArea.getSessionIdForActiveUser();

        // Assert
        Assert.assertEquals(sessionId, result);
        Mockito.verify(fromAddress).get();
    }

    @Test
    public void testGetSessionIdForActiveUser_WithNullAccount_ReturnsNull() {
        // Arrange
        Mockito.when(authorizationContext.getAuthorizedAccount()).thenReturn(null);

        try (MockedStatic<KristaMessageReceiver> kristaMessageReceiverMockedStatic = Mockito.mockStatic(KristaMessageReceiver.class);
             MockedStatic<TextMediumService> textMediumServiceMockedStatic = Mockito.mockStatic(TextMediumService.class)){
            kristaMessageReceiverMockedStatic.when(KristaMessageReceiver::getInstance).thenReturn(kristaMessageReceiver);
            kristaMessageReceiverMockedStatic.when(KristaMessageReceiver::getInstance).thenReturn(kristaMessageReceiver);
            // Mocking TextMediumService.getInstance() to return mediumService
            textMediumServiceMockedStatic.when(TextMediumService::getInstance).thenReturn(mediumService);
            // Creating MediumArea instance
            mediumArea = new MediumArea(authorizationContext, fromAddress, inboxManager);
            // Mocking KristaMessageReceiver.getSessionInbox(account) to return inbox
            Mockito.when(KristaMessageReceiver.getInstance().getSessionInbox(account)).thenReturn(inbox);
        }

        // Act and Assert
        Assert.assertThrows(NullPointerException.class, () -> {
            mediumArea.getSessionIdForActiveUser();
        });

        Mockito.verifyNoInteractions(fromAddress);
    }

    @Test
    public void testGetSessionIdForActiveUser_WithInvalidEmailAddress_ReturnsNull() {
        // Arrange
        String email = "invalid.email";

        // Set up the mocks and dependencies
        Mockito.when(authorizationContext.getAuthorizedAccount()).thenReturn(account);
        Mockito.when(account.getPrimaryEmailAddress()).thenReturn(email);

        try (MockedStatic<KristaMessageReceiver> kristaMessageReceiverMockedStatic = Mockito.mockStatic(KristaMessageReceiver.class);
             MockedStatic<TextMediumService> textMediumServiceMockedStatic = Mockito.mockStatic(TextMediumService.class)) {
            kristaMessageReceiverMockedStatic.when(KristaMessageReceiver::getInstance).thenReturn(kristaMessageReceiver);
            // Mocking TextMediumService.getInstance() to return mediumService
            textMediumServiceMockedStatic.when(TextMediumService::getInstance).thenReturn(mediumService);
            // Creating MediumArea instance
            mediumArea = new MediumArea(authorizationContext, fromAddress, inboxManager);
            // Mocking KristaMessageReceiver.getSessionInbox(account) to return inbox
            Mockito.when(KristaMessageReceiver.getInstance().getSessionInbox(account)).thenReturn(inbox);
        }

        // Act
        String result = mediumArea.getSessionIdForActiveUser();

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testGetSessionIdForActiveUser_WithMissingSessionInbox_ReturnsNull() {
        // Arrange
        String email = "user@example.com";

        // Set up the mocks and dependencies
        Mockito.when(authorizationContext.getAuthorizedAccount()).thenReturn(account);
        Mockito.when(account.getPrimaryEmailAddress()).thenReturn(email);
        Mockito.when(fromAddress.get()).thenReturn("from@example.com");
        Mockito.when(kristaMessageReceiver.getSessionInbox(account)).thenReturn(null);

        try (MockedStatic<KristaMessageReceiver> kristaMessageReceiverMockedStatic = Mockito.mockStatic(KristaMessageReceiver.class);
             MockedStatic<TextMediumService> textMediumServiceMockedStatic = Mockito.mockStatic(TextMediumService.class)) {
            kristaMessageReceiverMockedStatic.when(KristaMessageReceiver::getInstance).thenReturn(kristaMessageReceiver);
            // Mocking TextMediumService.getInstance() to return mediumService
            textMediumServiceMockedStatic.when(TextMediumService::getInstance).thenReturn(mediumService);
            // Creating MediumArea instance
            mediumArea = new MediumArea(authorizationContext, fromAddress, inboxManager);
            // Mocking KristaMessageReceiver.getSessionInbox(account) to return inbox
            Mockito.when(KristaMessageReceiver.getInstance().getSessionInbox(account)).thenReturn(inbox);
        }

        // Act
        String result = mediumArea.getSessionIdForActiveUser();

        // Assert
        Assert.assertNull(result);
        Mockito.verify(fromAddress).get();
    }
}

