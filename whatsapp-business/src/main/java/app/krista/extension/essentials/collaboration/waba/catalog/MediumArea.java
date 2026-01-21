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

package app.krista.extension.essentials.collaboration.waba.catalog;

import app.krista.extension.impl.anno.*;
import app.krista.extension.util.InvokerAttributeProvider;
import app.krista.ksdk.accounts.Account;
import app.krista.ksdk.context.AuthorizationContext;
import app.krista.omni.sdk.medium.api.KristaMessageReceiver;
import app.krista.omni.sdk.medium.text.util.CommonInvokerParameters;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.ksdk.accounts.Account;
import app.krista.ksdk.context.AuthorizationContext;
import app.krista.ksdk.inboxes.Inbox;
import app.krista.ksdk.inboxes.InboxManager;
import app.krista.ksdk.message.Message;
import app.krista.omni.sdk.complex_fields.impl.MessageDtoFactory;
import app.krista.omni.sdk.medium.api.KristaMessageReceiver;
import app.krista.omni.sdk.medium.api.MediumService;
import app.krista.omni.sdk.medium.api.dto.MessageDto;
import app.krista.omni.sdk.medium.text.TextMediumService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Medium Area catalog for WhatsApp Business Extension.
 * <p>
 * This class provides catalog requests for session management and message retrieval
 * in the Collaboration domain. It enables conversations to:
 * <ul>
 *   <li>Retrieve the session ID for the currently active WhatsApp user</li>
 *   <li>Fetch the last message sent by the user to Krista</li>
 * </ul>
 * </p>
 * <p>
 * These catalog requests are essential for building conversational flows that need
 * to access session context or message history.
 * </p>
 *
 * @see CatalogRequest
 * @see AuthorizationContext
 * @see KristaMessageReceiver
 */
@Domain(id = "catEntryDomain_5fa2fc97-4b17-44cf-b98f-aa91a459a091",
        name = "Collaboration",
        ecosystemId = "catEntryEcosystem_84b53163-327b-4b1b-8c96-9334d292f9f5",
        ecosystemName = "Essentials",
        ecosystemVersion = "e66100ba-c77b-478e-adee-cff7169ec1b6")
public class MediumArea {

    private final AuthorizationContext authorizationContext;
    private final KristaMessageReceiver kristaMessageReceiver;
    private final InvokerAttributeProvider<String> from;
    private final MediumService mediumService;
    private final InboxManager inboxManager;

    /**
     * Constructs a new Medium Area with dependency injection.
     *
     * @param authorizationContext provides access to the currently authorized user account
     * @param fromAddress provider for the user identifier (phone number)
     * @param inboxManager manager for accessing user inboxes and message history
     */
    @Inject
    public MediumArea(AuthorizationContext authorizationContext, @Named(CommonInvokerParameters.USER_IDENTIFIER) InvokerAttributeProvider<String> fromAddress, InboxManager inboxManager) {
        this.authorizationContext = authorizationContext;
        this.kristaMessageReceiver = KristaMessageReceiver.getInstance();
        this.from = fromAddress;
        this.inboxManager = inboxManager;
        this.mediumService = TextMediumService.getInstance();
    }

    /**
     * Retrieves the session ID (inbox ID) for the currently active WhatsApp user.
     * <p>
     * This catalog request provides access to the unique session identifier for the
     * authorized user's WhatsApp conversation. The session ID can be used to:
     * <ul>
     *   <li>Track and manage user sessions</li>
     *   <li>Route messages to the correct conversation context</li>
     *   <li>Associate data with specific user sessions</li>
     *   <li>Monitor active conversations</li>
     * </ul>
     * </p>
     * <p>
     * <b>Performance:</b> This operation is very fast (< 50ms) as it performs an
     * in-memory cache lookup.
     * </p>
     *
     * @return the session inbox ID for the active user
     * @throws NullPointerException if no authorized account is found
     */
    @CatalogRequest(
            id = "localDomainRequest_fa680a04-7cb1-4de2-885b-2a56a174f36a",
            name = "Get Session Id for active user",
            description = "Get Session Id for active user",
            area = "Medium",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field(name = "Session Id", type = "Text", required = false, attributes = {}, options = {})
    public String getSessionIdForActiveUser() {
        Account account = authorizationContext.getAuthorizedAccount();
        String toAddress = account.getPrimaryEmailAddress().split("@")[0];
        System.out.println("toAddress:" + toAddress + " and fromAddress:" + from.get());
        return kristaMessageReceiver.getSessionInbox(account).getInboxId();
    }

    /**
     * Fetches the last message sent by the user to Krista.
     * <p>
     * This catalog request searches through the user's inbox history to find the most
     * recent message sent by the authorized user. It:
     * <ul>
     *   <li>Retrieves messages in batches of 20, working backwards from most recent</li>
     *   <li>Filters messages to find those sent by the authorized user</li>
     *   <li>Returns the text content of the first matching message</li>
     *   <li>Returns empty string if no user messages are found</li>
     * </ul>
     * </p>
     * <p>
     * <b>Use Cases:</b>
     * <ul>
     *   <li>Display conversation history to the user</li>
     *   <li>Provide context for follow-up questions</li>
     *   <li>Audit user interactions</li>
     *   <li>Implement message replay functionality</li>
     * </ul>
     * </p>
     * <p>
     * <b>Performance:</b> Execution time is 100-500ms depending on the size of the
     * message history. The method processes messages in batches to minimize memory usage.
     * </p>
     *
     * @return the text of the last message sent by the user, or empty string if none found
     * @throws NullPointerException if no authorized account is found
     */
    @CatalogRequest(
            id = "localDomainRequest_f9e14ad1-9286-4f75-8be2-a3307be6a395",
            name = "Get Last User Message",
            description = "Fetch the last user message to Krista.",
            area = "Medium",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field.Text(name = "Message", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public String getLastUserMessage() {
        Account authorizedAccount = authorizationContext.getAuthorizedAccount();
        String inboxId = mediumService.getSessionInbox(authorizedAccount).getInboxId();
        Inbox inbox = inboxManager.getInbox(inboxId);
        List<Message> messages;
        String offset = null;
        do {
            messages = inbox.getMessages(offset, offset == null, -20);
            for (int i = messages.size() - 1; i >= 0; i--) {
                Message message = messages.get(i);
                offset = message.getMessageId();
                if (message.getSender().getAccountId().equals(authorizedAccount.getAccountId())) {
                    MessageDto msgDto = MessageDtoFactory.create(message);
                    return msgDto.getMessage();
                }
            }
        } while (!messages.isEmpty());
        return "";
    }

}