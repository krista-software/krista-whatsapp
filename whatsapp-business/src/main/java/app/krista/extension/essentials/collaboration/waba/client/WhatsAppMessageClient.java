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

package app.krista.extension.essentials.collaboration.waba.client;

import app.krista.extension.essentials.collaboration.waba.request.MessageRequest;
import app.krista.extension.essentials.collaboration.waba.store.AccessTokenStore;
import app.krista.extension.essentials.collaboration.waba.util.ErrorMessages;
import app.krista.extension.util.InvokerAttributeProvider;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Client for sending messages through WhatsApp Business Cloud API.
 * <p>
 * This client handles all message-related API operations by making POST requests
 * to the WhatsApp Business API endpoint: {@code /{phone_number_id}/messages}
 * </p>
 * <p>
 * Supported message types:
 * <ul>
 *   <li><b>Text messages</b>: Plain text up to 4096 characters</li>
 *   <li><b>Interactive buttons</b>: Messages with up to 3 clickable buttons</li>
 *   <li><b>Interactive lists</b>: Messages with up to 10 selectable items</li>
 * </ul>
 * </p>
 * <p>
 * All requests are automatically authenticated using the Bearer token from the
 * {@link AccessTokenStore}.
 * </p>
 *
 * @see MessageRequest
 * @see TextMessageRequest
 * @see AccessTokenStore
 */
public class WhatsAppMessageClient {
    private final WebTarget webTarget;
    private final AccessTokenStore accessTokenStore;
    private final InvokerAttributeProvider<String> phoneNumberIdProvider;
    private static final String MESSAGE_END_POINT = "messages";

    /**
     * Constructs a new WhatsApp message client.
     *
     * @param webTarget the JAX-RS web target configured for WhatsApp API base URL
     * @param phoneNumberIdProvider provider for the WhatsApp Business phone number ID
     * @param accessTokenStore store for retrieving OAuth access tokens
     */
    public WhatsAppMessageClient(WebTarget webTarget, InvokerAttributeProvider<String> phoneNumberIdProvider, AccessTokenStore accessTokenStore) {
        this.webTarget = webTarget;
        this.phoneNumberIdProvider = phoneNumberIdProvider;
        this.accessTokenStore = accessTokenStore;
    }

    /**
     * Sends a message through WhatsApp Business API.
     * <p>
     * Makes a POST request to {@code /{phone_number_id}/messages} with the message
     * payload. The request is authenticated using the Bearer token from the access
     * token store.
     * </p>
     * <p>
     * <b>Example Response:</b>
     * <pre>
     * {
     *   "messaging_product": "whatsapp",
     *   "contacts": [{
     *     "input": "1234567890",
     *     "wa_id": "1234567890"
     *   }],
     *   "messages": [{
     *     "id": "wamid.HBgMOTE3QzA4QjBDQTdEAA=="
     *   }]
     * }
     * </pre>
     * </p>
     * <p>
     * <b>Performance:</b> Typical response time is 200-500ms depending on WhatsApp
     * API response time and network latency.
     * </p>
     *
     * @param messageRequest the message request object (e.g., {@link TextMessageRequest})
     * @return JSON response from WhatsApp API containing message ID and delivery status
     * @throws IllegalStateException if the API connection fails
     */
    public String postMessage(MessageRequest messageRequest) {
        Response response;
        try {
            response = webTarget
                    .path(phoneNumberIdProvider.get())
                    .path(MESSAGE_END_POINT)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessTokenStore.get(phoneNumberIdProvider.get()))
                    .post(Entity.entity(messageRequest.toString(), MediaType.APPLICATION_JSON_TYPE));
            return response.readEntity(String.class);
        } catch (ProcessingException cause) {
            throw new IllegalStateException(ErrorMessages.apiConnectionError(cause.getMessage()), cause);
        }
    }
}
