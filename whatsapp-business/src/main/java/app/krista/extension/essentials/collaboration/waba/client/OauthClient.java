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

import app.krista.extension.essentials.collaboration.waba.util.Constants;
import app.krista.extension.essentials.collaboration.waba.util.InvokerParameters;
import app.krista.omni.sdk.medium.text.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static app.krista.extension.essentials.collaboration.waba.util.Constants.GSON_JSON_MAPPER;

/**
 * OAuth 2.0 client for WhatsApp Business authentication.
 * <p>
 * This client handles the OAuth 2.0 authorization flow with Meta's WhatsApp Business
 * platform, including:
 * <ul>
 *   <li>Generating OAuth authorization URLs with required parameters</li>
 *   <li>Exchanging authorization codes for long-lived access tokens</li>
 *   <li>Managing OAuth state parameters for security</li>
 * </ul>
 * </p>
 * <p>
 * <b>OAuth Flow:</b>
 * <ol>
 *   <li>Generate authorization URL using {@link #getOAuthUrl(String, List)}</li>
 *   <li>User is redirected to Meta for authentication</li>
 *   <li>Meta redirects back with authorization code</li>
 *   <li>Exchange code for access token using {@link #getAccessToken(Form)}</li>
 *   <li>Store access token for API requests</li>
 * </ol>
 * </p>
 * <p>
 * <b>Required OAuth Scopes:</b>
 * <ul>
 *   <li>whatsapp_business_management - Manage WhatsApp Business account</li>
 *   <li>whatsapp_business_messaging - Send and receive messages</li>
 *   <li>business_management - Access business portfolio information</li>
 * </ul>
 * </p>
 *
 * @see AccessToken
 * @see WhatsAppClient
 */
public class OauthClient {
    private static final Logger logger = LoggerFactory.getLogger(OauthClient.class);
    private final WebTarget webTarget;
    private final String clientId;
    private final String clientSecret;
    private static final String OAUTH_ENDPOINT = "oauth";
    private static final String ACCESS_POINT_END_POINT = "access_token";

    /**
     * Constructs a new OAuth client for WhatsApp Business authentication.
     *
     * @param clientId the OAuth 2.0 client ID from Meta Developer Portal
     * @param clientSecret the OAuth 2.0 client secret from Meta Developer Portal
     */
    public OauthClient(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        webTarget = ClientBuilder.newClient()
                .target(Constants.FACEBOOK_DIALOG_URL);
    }

    /**
     * Generates the OAuth authorization URL for user authentication.
     * <p>
     * Constructs a URL that redirects users to Meta's OAuth authorization page.
     * The URL includes:
     * <ul>
     *   <li>Client ID for identifying the application</li>
     *   <li>Redirect URI for OAuth callback</li>
     *   <li>State parameter containing phone number ID and account ID (for CSRF protection)</li>
     *   <li>Required OAuth scopes</li>
     * </ul>
     * </p>
     * <p>
     * The state parameter is constructed by concatenating all state values with a
     * hash (#) separator. This allows multiple pieces of information to be preserved
     * through the OAuth flow.
     * </p>
     *
     * @param redirectUri the callback URL where Meta will redirect after authentication
     * @param state list of state values to preserve (e.g., phone number ID, account ID)
     * @return the complete OAuth authorization URL
     */
    public String getOAuthUrl(String redirectUri, List<String> state) {
        WebTarget authorize = webTarget.path(OAUTH_ENDPOINT);
        authorize = authorize.queryParam(InvokerParameters.CLIENT_ID, clientId);
        authorize = authorize.queryParam(InvokerParameters.REDIRECT_URI, redirectUri);
        authorize = authorize.queryParam(Constants.STATE, getStateString(state));
        return authorize.getUri().toString();
    }

    /**
     * Constructs the state parameter string from a list of state values.
     * <p>
     * Concatenates all state values with a hash (#) separator. The state parameter
     * is used to maintain context during the OAuth flow and prevent CSRF attacks.
     * </p>
     *
     * @param states list of state values to concatenate
     * @return the concatenated state string with hash separators
     */
    private String getStateString(List<String> states) {
        StringBuilder stateBuilder = new StringBuilder();
        states.forEach(state -> stateBuilder
                .append(state)
                .append(Strings.HASH));
        logger.debug("OAuth state parameter: {}", stateBuilder);
        return stateBuilder.toString();
    }

    /**
     * Exchanges an authorization code for a long-lived access token.
     * <p>
     * Makes a POST request to Meta's token endpoint to exchange the authorization
     * code (received from OAuth callback) for an access token. The access token
     * is valid for 60 days and can be used to authenticate API requests.
     * </p>
     * <p>
     * The request includes:
     * <ul>
     *   <li>Client ID and Client Secret for application authentication</li>
     *   <li>Authorization code from OAuth callback</li>
     *   <li>Redirect URI (must match the one used in authorization request)</li>
     * </ul>
     * </p>
     * <p>
     * <b>Performance:</b> This operation typically takes 1-2 seconds as it involves
     * a network request to Meta's servers.
     * </p>
     *
     * @param body form data containing the authorization code and redirect URI
     * @return an {@link AccessToken} object containing the access token and metadata
     */
    public AccessToken getAccessToken(Form body) {
        body.param(InvokerParameters.CLIENT_ID, clientId);
        body.param(InvokerParameters.CLIENT_SECRET, clientSecret);
        Response response = ClientBuilder.newClient()
                .target(Constants.GRAPH_FACEBOOK_URL + Constants.GRAPH_FACEBOOK_VERSION)
                .path(OAUTH_ENDPOINT)
                .path(ACCESS_POINT_END_POINT)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.form(body));
        String accessTokenResponse = response.readEntity(String.class);
        return GSON_JSON_MAPPER.fromString(accessTokenResponse, AccessToken.class);
    }

}
