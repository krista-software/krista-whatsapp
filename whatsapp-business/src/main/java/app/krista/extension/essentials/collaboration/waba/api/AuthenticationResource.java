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

package app.krista.extension.essentials.collaboration.waba.api;

import app.krista.extension.essentials.collaboration.waba.client.AccessToken;
import app.krista.extension.essentials.collaboration.waba.client.OauthClient;
import app.krista.extension.essentials.collaboration.waba.client.WhatsAppClient;
import app.krista.extension.essentials.collaboration.waba.store.AccessTokenStore;
import app.krista.extension.essentials.collaboration.waba.store.InvokerParameterStore;
import app.krista.extension.essentials.collaboration.waba.util.Constants;
import app.krista.extension.essentials.collaboration.waba.util.ErrorMessages;
import app.krista.extension.essentials.collaboration.waba.util.InvokerParameters;
import app.krista.extension.executor.Invoker;
import app.krista.extension.util.InvokerAttributeProvider;
import app.krista.extensions.util.KeyValueStore;
import app.krista.ksdk.authentication.AuthorizationListener;
import app.krista.omni.sdk.medium.text.util.Strings;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Form;
import java.util.MissingResourceException;
import java.util.Objects;

/**
 * REST resource for handling OAuth authentication callbacks.
 * <p>
 * This resource provides the OAuth callback endpoint that Meta redirects to after
 * user authentication. It:
 * <ul>
 *   <li>Receives the authorization code from Meta OAuth flow</li>
 *   <li>Exchanges the code for a long-lived access token</li>
 *   <li>Stores the access token for future API requests</li>
 *   <li>Notifies the authorization listener of successful authentication</li>
 *   <li>Returns success or error message to the user</li>
 * </ul>
 * </p>
 * <p>
 * <b>OAuth Flow:</b>
 * <ol>
 *   <li>User clicks "Authorize" in extension configuration</li>
 *   <li>User is redirected to Meta for authentication</li>
 *   <li>Meta redirects to this callback endpoint with authorization code</li>
 *   <li>This endpoint exchanges code for access token</li>
 *   <li>Access token is stored and user sees success message</li>
 * </ol>
 * </p>
 * <p>
 * <b>Endpoint:</b> {@code GET /callback?code=...&state=...}
 * </p>
 *
 * @see OauthClient
 * @see AccessTokenStore
 * @see InvokerParameterStore
 * @see AuthorizationListener
 */
@Path("/")
public final class AuthenticationResource {
    private final AuthorizationListener authorizationListener;
    private final WhatsAppClient whatsAppClient;
    private final InvokerParameterStore invokerParameterStore;
    private final AccessTokenStore accessTokenStore;

    /**
     * Constructs a new authentication resource with dependency injection.
     *
     * @param authorizationListener listener to notify when authorization completes
     * @param keyValueStore persistent storage for tokens and configuration
     * @param invoker the extension invoker instance
     * @param businessPhoneNumberId provider for the WhatsApp Business phone number ID
     */
    @Inject
    public AuthenticationResource(AuthorizationListener authorizationListener, KeyValueStore keyValueStore,
                                  Invoker invoker, InvokerAttributeProvider<String> businessPhoneNumberId) {
        this.authorizationListener = authorizationListener;
        this.accessTokenStore = AccessTokenStore.create(keyValueStore);
        this.whatsAppClient = new WhatsAppClient(businessPhoneNumberId, accessTokenStore);
        this.invokerParameterStore = InvokerParameterStore.create(invoker, keyValueStore);
    }

    /**
     * Handles OAuth callback from Meta after user authentication.
     * <p>
     * This endpoint is called by Meta after the user completes the OAuth authorization
     * flow. It performs the following steps:
     * <ol>
     *   <li>Validates that the authorization code is present</li>
     *   <li>Extracts the phone number ID from the state parameter</li>
     *   <li>Retrieves extension configuration (client ID, secret, redirect URI)</li>
     *   <li>Exchanges the authorization code for an access token</li>
     *   <li>Validates the access token response</li>
     *   <li>Stores the access token for the phone number ID</li>
     *   <li>Notifies the authorization listener of success</li>
     *   <li>Returns a success or error message to the user</li>
     * </ol>
     * </p>
     * <p>
     * <b>State Parameter Format:</b> {@code phoneNumberId#accountId#}
     * </p>
     * <p>
     * <b>Performance:</b> This operation takes 1-2 seconds as it involves a network
     * request to Meta's token endpoint.
     * </p>
     *
     * @param code the authorization code from Meta OAuth flow
     * @param state the state parameter containing phone number ID and account ID
     * @return success message if authentication succeeds, error message otherwise
     * @throws MissingResourceException if the authorization code is missing
     * @throws BadRequestException if the state parameter is invalid
     */
    @GET
    @Path("callback")
    public String getCallBack(@QueryParam(Constants.CODE) String code, @QueryParam(Constants.STATE) String state) {
        if (Objects.isNull(code)) {
            throw new MissingResourceException(ErrorMessages.AUTH_MISSING_CODE, AuthenticationResource.class.getName(), "code");
        }
        String businessPhoneNumberId = getBusinessPhoneNumberId(state);
        InvokerParameters invokerParameters = invokerParameterStore.load(businessPhoneNumberId);
        if (Objects.isNull(invokerParameters)) {
            throw new BadRequestException(ErrorMessages.AUTH_INVALID_STATE);
        }
        String redirectUri = invokerParameters.getRedirectUri();
        Form form = new Form();
        form.param(InvokerParameters.REDIRECT_URI, redirectUri);
        form.param(Constants.CODE, code);
        AccessToken accessToken = whatsAppClient
                .getAuthClient(invokerParameters.getClientId(), invokerParameters.getClientSecret())
                .getAccessToken(form);
        if (!Objects.isNull(accessToken) && Objects.isNull(accessToken.getError())
                && !Objects.isNull(accessToken.access_Token)) {
            accessTokenStore.put(getBusinessPhoneNumberId(state), accessToken.access_Token);
            authorizationListener.authorized();
            return ErrorMessages.AUTH_CALLBACK_SUCCESS;
        }
        return ErrorMessages.AUTH_INVALID_CREDENTIALS;
    }

    /**
     * Extracts the business phone number ID from the OAuth state parameter.
     * <p>
     * The state parameter contains multiple values separated by hash (#) characters.
     * The phone number ID is always the first value.
     * </p>
     *
     * @param state the OAuth state parameter (format: {@code phoneNumberId#accountId#})
     * @return the business phone number ID, or empty string if state is null or blank
     */
    private String getBusinessPhoneNumberId(String state) {
        if (state == null || state.isBlank()) {
            return Strings.EMPTY_STRING;
        }
        String[] parts = state.split(Strings.HASH);
        return parts.length > 0 ? parts[0] : Strings.EMPTY_STRING;
    }
}
