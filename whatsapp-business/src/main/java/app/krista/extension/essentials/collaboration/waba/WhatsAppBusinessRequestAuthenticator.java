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

import app.krista.extension.authorization.MustAuthenticateException;
import app.krista.extension.authorization.MustAuthorizeException;
import app.krista.extension.authorization.RequestAuthenticator;
import app.krista.extension.essentials.collaboration.waba.client.WhatsAppClient;
import app.krista.extension.essentials.collaboration.waba.store.InvokerParameterStore;
import app.krista.extension.essentials.collaboration.waba.util.Constants;
import app.krista.extension.essentials.collaboration.waba.util.InvokerParameters;
import app.krista.extension.essentials.collaboration.waba.whatsapp.WhatsAppReceivedMessage;
import app.krista.extension.request.ProtoRequest;
import app.krista.extension.request.ProtoResponse;
import app.krista.extension.request.protos.http.HttpRequest;
import app.krista.model.field.NamedField;
import app.krista.model.field.NamedValuedField;
import app.krista.omni.sdk.complex_fields.impl.FromLinkRequestAuthenticator;
import app.krista.omni.sdk.medium.api.MediumService;
import app.krista.omni.sdk.medium.text.TextMediumMessageIn;
import app.krista.omni.sdk.medium.text.util.Strings;
import com.kristasoft.common.io.Unicodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static app.krista.extension.essentials.collaboration.waba.util.Constants.GSON_JSON_MAPPER;

/**
 * Request authenticator for WhatsApp Business Extension.
 * <p>
 * This authenticator handles multiple authentication scenarios:
 * <ul>
 *   <li><b>WhatsApp Messages</b>: Identifies users from incoming WhatsApp webhook messages</li>
 *   <li><b>OAuth Callbacks</b>: Processes OAuth 2.0 callback requests from Meta</li>
 *   <li><b>Form Requests</b>: Delegates form-based authentication to FromLinkRequestAuthenticator</li>
 *   <li><b>Entity Requests</b>: Handles entity-related authentication</li>
 * </ul>
 * </p>
 * <p>
 * The authenticator extracts user identity from different request types and returns the
 * corresponding Krista account ID. For WhatsApp messages, it uses the sender's phone number
 * to look up or create a user session.
 * </p>
 *
 * @see RequestAuthenticator
 * @see WhatsAppReceivedMessage
 * @see FromLinkRequestAuthenticator
 */
public class WhatsAppBusinessRequestAuthenticator implements RequestAuthenticator {
    private static final Logger logger = LoggerFactory.getLogger(WhatsAppBusinessRequestAuthenticator.class);
    private final MediumService service;
    private final WhatsAppClient whatsAppClient;
    private final InvokerParameterStore invokerParameterStore;
    private final FromLinkRequestAuthenticator fromLinkRequestAuthenticator;

    /**
     * Constructs a new WhatsApp Business request authenticator.
     *
     * @param service the medium service for handling message authentication
     * @param whatsAppClient the WhatsApp API client for OAuth operations
     * @param invokerParameterStore store for retrieving extension configuration parameters
     * @param fromLinkRequestAuthenticator authenticator for form-based requests
     */
    public WhatsAppBusinessRequestAuthenticator(MediumService service, WhatsAppClient whatsAppClient,
                                                InvokerParameterStore invokerParameterStore, FromLinkRequestAuthenticator fromLinkRequestAuthenticator) {
        this.service = service;
        this.whatsAppClient = whatsAppClient;
        this.invokerParameterStore = invokerParameterStore;
        this.fromLinkRequestAuthenticator = fromLinkRequestAuthenticator;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public Set<String> getSupportedProtocols() {
        return Collections.emptySet();
    }

    /**
     * Extracts the authenticated account ID from the incoming request.
     * <p>
     * Handles different request types:
     * <ul>
     *   <li><b>Form/Entity/Workspace requests</b>: Delegates to FromLinkRequestAuthenticator</li>
     *   <li><b>WhatsApp webhook messages (POST /webhook)</b>: Extracts sender phone number
     *       from message payload and looks up corresponding Krista account</li>
     *   <li><b>OAuth callback (GET /callback)</b>: Extracts account ID from state parameter</li>
     * </ul>
     * </p>
     *
     * @param protoRequest the incoming HTTP request
     * @return the authenticated account ID, or {@code null} if authentication fails
     */
    @Override
    public String getAuthenticatedAccountId(ProtoRequest protoRequest) {
        try {
            HttpRequest httpRequest = (HttpRequest) protoRequest;
            httpRequest.bufferBody();
            String httpRequestMethod = httpRequest.getMethod();
            String uriString = httpRequest.getUri().toString();

            if (uriString.contains(Constants.ENDPOINT_FORM)
                    || (HttpMethod.POST.equalsIgnoreCase(httpRequestMethod) && uriString.contains(Constants.ENDPOINT_ENTITY))
                    || (HttpMethod.GET.equalsIgnoreCase(httpRequestMethod) && uriString.contains(Constants.ENDPOINT_WORKSPACE))) {
                return fromLinkRequestAuthenticator.getAuthenticatedAccountId(protoRequest);
            }

            if ("POST".equals(httpRequestMethod) && uriString.endsWith(Constants.ENDPOINT_MESSAGES)) {
                InputStream body = httpRequest.getBody();
                String requestPayload = Unicodes.fromUTF8(body);
                WhatsAppReceivedMessage whatsAppReceivedMessage = GSON_JSON_MAPPER.fromString(requestPayload, WhatsAppReceivedMessage.class);
                String fromAddress = whatsAppReceivedMessage.fromAddress();
                if (fromAddress == null) {
                    return null;
                }
                TextMediumMessageIn messageIn = new TextMediumMessageIn(fromAddress, whatsAppReceivedMessage.getText(), "WA");
                return service.getAuthenticatedAccount(messageIn).getAccountId();
            }
            if (Objects.equals("GET", httpRequestMethod) && httpRequest.getUri().getPath().endsWith("callback")) {
                MultivaluedMap<String, String> queryParameters = httpRequest.getQueryParameters();
                List<String> states = queryParameters.get("state");
                if (!isStatePresent(states)) {
                    return null;
                }
                String[] stateParts = states.getFirst().split(Strings.HASH);
                return stateParts.length > 1 ? stateParts[1] : null;
            }
            return null;
        } catch (IOException | RuntimeException cause) {
            logger.error("Error authenticating request: " + cause.getMessage(), cause);
            return null;
        }
    }

    /**
     * Checks if the OAuth state parameter is present and not empty.
     *
     * @param states list of state parameter values from the request
     * @return {@code true} if state is present and not empty, {@code false} otherwise
     */
    private boolean isStatePresent(List<String> states) {
        return !Objects.isNull(states) && !states.isEmpty();
    }

    @Override
    public boolean setServiceAuthorization(String s) {
        return false;
    }

    @Override
    public Map<String, NamedField> getAttributeFields() {
        return Collections.emptyMap();
    }

    @Override
    public ProtoResponse getMustAuthenticateResponse(MustAuthenticateException cause, ProtoRequest protoRequest) {
        return null;
    }

    @Override
    public AuthorizationResponse getMustAuthenticateResponse(MustAuthenticateException cause) {
        return null;
    }

    @Override
    public ProtoResponse getMustAuthorizeResponse(MustAuthorizeException cause, ProtoRequest protoRequest) {
        return null;
    }

    /**
     * Generates the OAuth authorization response when authentication is required.
     * <p>
     * Constructs the Meta OAuth authorization URL with the following parameters:
     * <ul>
     *   <li>Client ID from extension configuration</li>
     *   <li>Redirect URI for OAuth callback</li>
     *   <li>State parameter containing phone number ID and account ID</li>
     *   <li>Required OAuth scopes for WhatsApp Business API</li>
     * </ul>
     * </p>
     * <p>
     * The state parameter is used to maintain context during the OAuth flow and
     * includes both the phone number ID and account ID separated by a hash (#).
     * </p>
     *
     * @param cause the exception containing details about why authorization is required
     * @return an {@link AuthorizationResponse} containing the OAuth URL
     */
    @Override
    public AuthorizationResponse getMustAuthorizeResponse(MustAuthorizeException cause) {
        List<NamedValuedField> fields = cause.getDetails();
        Optional<String> phoneNumberId = fields.stream()
                .filter(field -> Objects.equals(InvokerParameters.PHONE_NUMBER_ID, field.getName()))
                .map(field -> (String) field.getValue())
                .findFirst();

        Optional<String> accountId = fields.stream()
                .filter(field -> Objects.equals(Constants.ACCOUNT_ID, field.getName()))
                .map(field -> (String) field.getValue())
                .findFirst();

        InvokerParameters invokerParameters = invokerParameterStore.load(phoneNumberId.orElse(null));
        String clientId = invokerParameters.getClientId();
        String clientSecret = invokerParameters.getClientSecret();
        String oAuthUrl = null;

        if (phoneNumberId.isPresent() && accountId.isPresent()) {
            oAuthUrl = whatsAppClient.getAuthClient(clientId, clientSecret)
                    .getOAuthUrl(invokerParameters.getRedirectUri(), List.of(invokerParameters.getPhoneNumberId(), accountId.get()));
        }
        return new AuthorizationResponse(oAuthUrl, List.of());
    }

}
