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

import app.krista.extension.essentials.collaboration.waba.store.AccessTokenStore;
import app.krista.extension.essentials.collaboration.waba.util.Constants;
import app.krista.extension.essentials.collaboration.waba.util.ErrorMessages;
import app.krista.extension.util.InvokerAttributeProvider;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

/**
 * Main client for interacting with WhatsApp Business Cloud API.
 * <p>
 * This client provides a centralized interface for all WhatsApp Business API operations,
 * including:
 * <ul>
 *   <li>OAuth authentication and token management</li>
 *   <li>Message sending and receiving</li>
 *   <li>Business phone number verification</li>
 *   <li>API connection management</li>
 * </ul>
 * </p>
 * <p>
 * The client automatically handles:
 * <ul>
 *   <li>Bearer token authentication for all API requests</li>
 *   <li>Error handling and user-friendly error messages</li>
 *   <li>Connection pooling and resource management</li>
 * </ul>
 * </p>
 *
 * @see WhatsAppMessageClient
 * @see OauthClient
 * @see AccessTokenStore
 */
public class WhatsAppClient {
    private static final String BASE_URL = Constants.GRAPH_FACEBOOK_URL;
    private static final String VER = Constants.GRAPH_FACEBOOK_VERSION;
    private final WebTarget webTarget;
    private final AccessTokenStore accessTokenStore;
    private final InvokerAttributeProvider<String> phoneIdProvider;

    /**
     * Constructs a new WhatsApp API client.
     *
     * @param phoneIdProvider provider for the WhatsApp Business phone number ID
     * @param accessTokenStore store for retrieving OAuth access tokens
     */
    public WhatsAppClient(InvokerAttributeProvider<String> phoneIdProvider, AccessTokenStore accessTokenStore) {
        this.webTarget = ClientBuilder.newClient().target(BASE_URL + VER);
        this.accessTokenStore = accessTokenStore;
        this.phoneIdProvider = phoneIdProvider;
    }

    /**
     * Creates an OAuth client for authentication operations.
     * <p>
     * The OAuth client handles:
     * <ul>
     *   <li>Generating OAuth authorization URLs</li>
     *   <li>Exchanging authorization codes for access tokens</li>
     *   <li>Managing OAuth state parameters</li>
     * </ul>
     * </p>
     *
     * @param clientId the OAuth 2.0 client ID from Meta Developer Portal
     * @param clientSecret the OAuth 2.0 client secret from Meta Developer Portal
     * @return a configured OAuth client instance
     */
    public OauthClient getAuthClient(String clientId, String clientSecret) {
        return new OauthClient(clientId, clientSecret);
    }

    /**
     * Creates a message client for sending WhatsApp messages.
     * <p>
     * The message client handles all message-related API operations including
     * sending text messages, interactive buttons, and list messages.
     * </p>
     *
     * @return a configured message client instance
     */
    public WhatsAppMessageClient getMessageClient() {
        return new WhatsAppMessageClient(webTarget, phoneIdProvider, accessTokenStore);
    }

    /**
     * Retrieves business phone number details from WhatsApp API.
     * <p>
     * This method is used to validate the phone number configuration and verify
     * that the access token is valid. It makes a GET request to the WhatsApp
     * Business API endpoint: {@code /{phone_number_id}}
     * </p>
     * <p>
     * <b>Use Cases:</b>
     * <ul>
     *   <li>Validate extension configuration during setup</li>
     *   <li>Test API connectivity</li>
     *   <li>Verify access token is not expired</li>
     *   <li>Retrieve phone number metadata</li>
     * </ul>
     * </p>
     *
     * @param phoneNumberId the WhatsApp Business phone number ID to query
     * @return JSON response containing phone number details
     * @throws IllegalStateException if the API connection fails
     * @throws ProcessingException if the API returns an error response
     */
    public String businessPhoneNumber(String phoneNumberId) {
        Response response;
        try {
            response = webTarget
                    .path(phoneNumberId)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessTokenStore.get(phoneNumberId))
                    .get();
            requireNonError(response);
            return response.readEntity(String.class);
        } catch (ProcessingException cause) {
            throw new IllegalStateException(ErrorMessages.apiConnectionError(cause.getMessage()), cause);
        }
    }

    /**
     * Validates that the API response indicates success.
     * <p>
     * Checks if the response status is in the 2xx (successful) family. If not,
     * throws a {@link ProcessingException} with a user-friendly error message
     * containing the response details.
     * </p>
     *
     * @param response the HTTP response from WhatsApp API
     * @throws ProcessingException if the response indicates an error
     */
    private void requireNonError(Response response) {
        if (!Objects.equals(response.getStatusInfo().getFamily(), Response.Status.Family.SUCCESSFUL)) {
            String responseString = response.readEntity(String.class);
            throw new ProcessingException(ErrorMessages.apiRequestFailed(responseString));
        }
    }
}
