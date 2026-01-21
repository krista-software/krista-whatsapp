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

import app.krista.extension.authorization.MustAuthorizeException;
import app.krista.extension.authorization.RequestAuthenticator;
import app.krista.extension.essentials.collaboration.waba.catalog.MediumArea;
import app.krista.extension.essentials.collaboration.waba.client.TextMediumMessageSenderImpl;
import app.krista.extension.essentials.collaboration.waba.client.WhatsAppClient;
import app.krista.extension.essentials.collaboration.waba.listener.InboxMessageListenerImpl;
import app.krista.extension.essentials.collaboration.waba.store.AccessTokenStore;
import app.krista.extension.essentials.collaboration.waba.store.InvokerParameterStore;
import app.krista.extension.essentials.collaboration.waba.util.Constants;
import app.krista.extension.essentials.collaboration.waba.util.ErrorMessages;
import app.krista.extension.essentials.collaboration.waba.util.InvokerParameters;
import app.krista.extension.executor.Invoker;
import app.krista.extension.impl.anno.*;
import app.krista.extension.util.InvokerAttributeProvider;
import app.krista.extension.util.ServiceRegistry;
import app.krista.extensions.util.KeyValueStore;
import app.krista.ksdk.authentication.AuthenticationSettings;
import app.krista.ksdk.context.AuthorizationContext;
import app.krista.ksdk.context.ExecutionMode;
import app.krista.ksdk.context.RequestContext;
import app.krista.ksdk.workspace.Conversation;
import app.krista.model.field.NamedValuedField;
import app.krista.omni.sdk.complex_fields.impl.FromLinkRequestAuthenticator;
import app.krista.omni.sdk.complex_fields.impl.monitor.FormExpiryMonitorThread;
import app.krista.omni.sdk.medium.api.MediumService;
import app.krista.omni.sdk.medium.api.impl.InvInbox;
import app.krista.omni.sdk.medium.text.TextMediumService;
import app.krista.omni.sdk.medium.text.util.CommonInvokerParameters;
import app.krista.omni.sdk.spi.OmniChannelService;
import app.krista.omni.sdk.spi.impl.monitor.SessionTimeoutMonitor;
import app.krista.omni.sdk.spi.impl.services.ConversationsSearch;
import app.krista.omni.sdk.spi.impl.store.SessionStore;
import app.krista.omni.sdk.spi.util.Logger;
import com.kristasoft.common.holders.ThreadLocalProxy;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * WhatsApp Business Extension for Krista Platform.
 * <p>
 * This extension enables seamless integration between Krista and WhatsApp Business Cloud API,
 * allowing organizations to communicate with users through WhatsApp messaging. The extension
 * handles message sending, receiving, session management, and OAuth authentication with Meta's
 * WhatsApp Business platform.
 * </p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Send and receive WhatsApp messages through Krista conversations</li>
 *   <li>OAuth 2.0 authentication with Meta WhatsApp Business API</li>
 *   <li>Automatic session management with configurable timeouts</li>
 *   <li>Webhook integration for real-time message delivery</li>
 *   <li>Support for text messages, interactive buttons, and list messages</li>
 *   <li>NLP-powered conversation routing and fallback handling</li>
 *   <li>Agent handoff capabilities for human intervention</li>
 * </ul>
 *
 * <h2>Configuration Requirements:</h2>
 * <ul>
 *   <li><b>USER_IDENTIFIER</b>: WhatsApp Business phone number in E.164 format</li>
 *   <li><b>PHONE_NUMBER_ID</b>: Unique identifier from Meta Developer Portal</li>
 *   <li><b>CLIENT_ID</b>: OAuth 2.0 client identifier from Meta app</li>
 *   <li><b>CLIENT_SECRET</b>: OAuth 2.0 client secret (secured)</li>
 *   <li><b>VERIFY_TOKEN</b>: Custom token for webhook verification (secured)</li>
 *   <li><b>WELCOME_CONVERSATION</b>: Conversation to execute when new user starts session</li>
 *   <li><b>CONFIG_JSON</b>: Optional JSON configuration for advanced customization</li>
 *   <li><b>GUEST_ROLE</b>: Optional role assigned to external WhatsApp users</li>
 *   <li><b>FALLBACK_CONVERSATION</b>: Optional conversation for unmatched queries</li>
 *   <li><b>SESSION_TIMEOUT</b>: Optional session timeout in minutes (default: 30)</li>
 * </ul>
 *
 * <h2>Execution Mode:</h2>
 * <p>
 * This extension only supports <b>LIVE</b> execution mode. All conversations referenced
 * (Welcome, Fallback) must also be in Live mode.
 * </p>
 *
 * @version 1.0.23
 * @since 1.0.0
 * @see WhatsAppBusinessRequestAuthenticator
 * @see WhatsAppClient
 * @see MediumArea
 */
@Java(version = Java.Version.JAVA_21)
@Field.Text(name = CommonInvokerParameters.USER_IDENTIFIER)
@Field.Text(name = InvokerParameters.PHONE_NUMBER_ID)
@Field.Text(name = InvokerParameters.CLIENT_ID)
@Field.Text(name = InvokerParameters.CLIENT_SECRET, isSecured = true)
@Field.Text(name = Constants.VERIFY_TOKEN, isSecured = true)
@Field(name = CommonInvokerParameters.WELCOME_CONVERSATION, type = "Text")
@Field(name = CommonInvokerParameters.CONFIG_JSON, type = "Text", required = false)
@Field(name = CommonInvokerParameters.GUEST_ROLE, type = "Text", required = false)
@Field(name = CommonInvokerParameters.FALLBACK_CONVERSATION, type = "Text", required = false)
@Field(name = CommonInvokerParameters.SESSION_TIMEOUT, type = "Number", required = false)
@Extension(implementingDomainIds = "catEntryDomain_5fa2fc97-4b17-44cf-b98f-aa91a459a091",
        name = "WhatsApp Business",
        version = "1.0.23",
        jaxrsId = Constants.JAX_RS_ID,
        requireWorkspaceAdminRights = true,
        supportingModes = app.krista.extension.request.ExecutionMode.LIVE)
@StaticResource(path = "docs", file = "docs")
public class WhatsAppBusinessExtension {
    private final AuthorizationContext authorizationContext;
    private final InvInbox invokerInbox;
    private final InvokerParameterStore invokerParameterStore;
    private final AuthenticationSettings authenticationSettings;
    private final MediumService mediumMessageService;
    private final AccessTokenStore accessTokenStore;
    private final WhatsAppClient whatsAppClient;
    private final InvokerAttributeProvider<String> phoneNumberIdProvider;
    private final Invoker invoker;
    private final ConversationsSearch conversationsSearch;
    private final RequestContext requestContext;

    /**
     * Constructs a new WhatsApp Business Extension instance with dependency injection.
     * <p>
     * Initializes all required components including:
     * <ul>
     *   <li>Authorization context for user authentication</li>
     *   <li>Token and parameter stores for persistence</li>
     *   <li>WhatsApp API client for communication</li>
     *   <li>Medium service for message handling</li>
     *   <li>Conversation search for NLP routing</li>
     * </ul>
     * </p>
     *
     * @param authorizationContext provides access to the currently authorized user account
     * @param keyValueStore persistent storage for tokens and configuration parameters
     * @param phoneNumberId provider for the WhatsApp Business phone number ID
     * @param authenticationSettings settings for OAuth authentication configuration
     * @param invoker the extension invoker instance
     * @param requestContext provides access to execution mode and request information
     */
    @Inject
    public WhatsAppBusinessExtension(AuthorizationContext authorizationContext, KeyValueStore keyValueStore,
                                     @Named(InvokerParameters.PHONE_NUMBER_ID) InvokerAttributeProvider<String> phoneNumberId,
                                     AuthenticationSettings authenticationSettings, Invoker invoker, RequestContext requestContext) {
        this.authorizationContext = authorizationContext;
        this.phoneNumberIdProvider = phoneNumberId;
        this.invoker = invoker;
        this.requestContext = requestContext;
        this.invokerParameterStore = InvokerParameterStore.create(invoker, keyValueStore);
        this.invokerInbox = new InvInbox();
        this.authenticationSettings = authenticationSettings;
        this.accessTokenStore = AccessTokenStore.create(keyValueStore);
        this.whatsAppClient = new WhatsAppClient(phoneNumberId, accessTokenStore);
        TextMediumMessageSenderImpl textMediumMessageSenderImpl = new TextMediumMessageSenderImpl(whatsAppClient);
        this.mediumMessageService = TextMediumService.getInstance();
        mediumMessageService.registerSender(textMediumMessageSenderImpl);
        this.conversationsSearch = ConversationsSearch.create(ServiceRegistry.getInstance());
    }

    /**
     * Provides custom tabs for the extension configuration interface.
     * <p>
     * Adds a "Documentation" tab that displays the extension's static documentation
     * resources, making it easy for users to access help and configuration guides
     * directly from the extension settings.
     * </p>
     *
     * @return a map containing tab names and their corresponding resource paths
     */
    @InvokerRequest(InvokerRequest.Type.CUSTOM_TABS)
    public Map<String, String> customTabs() {
        return Map.of("Documentation", "static/docs");
    }

    /**
     * Tests the connection to WhatsApp Business API.
     * <p>
     * Verifies that the extension can successfully communicate with WhatsApp Business
     * Cloud API by retrieving the business phone number details. This validates:
     * <ul>
     *   <li>Access token is valid and not expired</li>
     *   <li>Phone Number ID is correct</li>
     *   <li>Network connectivity to WhatsApp API is working</li>
     *   <li>API permissions are properly configured</li>
     * </ul>
     * </p>
     *
     * @throws IllegalStateException if the API request fails or returns an error
     */
    @InvokerRequest(InvokerRequest.Type.TEST_CONNECTION)
    public void testConnection() {
        String response = whatsAppClient.businessPhoneNumber(phoneNumberIdProvider.get());
        Logger.log("response:" + response);
    }

    /**
     * Handles extension initialization when the invoker is loaded.
     * <p>
     * Performs the following initialization tasks:
     * <ul>
     *   <li>Clears cached invoker parameters to ensure fresh configuration</li>
     *   <li>Creates and configures the invoker inbox for message handling</li>
     *   <li>Registers message listener for incoming messages</li>
     *   <li>Adds "omni.com" to supported authentication domains if not already present</li>
     *   <li>Performs one-time session cleanup to remove stale sessions</li>
     *   <li>Starts session timeout monitor in LIVE mode</li>
     *   <li>Initializes form expiry monitoring thread</li>
     * </ul>
     * </p>
     * <p>
     * This method is automatically called by the Krista platform when the extension
     * is loaded or reloaded.
     * </p>
     */
    @InvokerRequest(InvokerRequest.Type.INVOKER_LOADED)
    public void onInvokerLoad() {
        invokerParameterStore.remove(phoneNumberIdProvider.get());
        invokerInbox.ensureExists();
        invokerInbox.setMessageListener(new InboxMessageListenerImpl());
        if (!isDomainSupported()) {
            authenticationSettings.addSupportedDomains("omni.com");
        }

        SessionStore sessionStore = SessionStore.create(ServiceRegistry.getInstance());

        // Check if the cleanup has been done already
        if (!sessionStore.isCleanupDone()) {
            sessionStore.deleteAllSession();
            sessionStore.markCleanupDone(); // Mark the cleanup as done
            Logger.log("Session cleanup completed successfully");
        }
        if (requestContext.getExecutionMode().equals(ExecutionMode.LIVE)) {
            SessionTimeoutMonitor sessionTimeoutMonitor = SessionTimeoutMonitor.create(OmniChannelService.getInstance(), ServiceRegistry.getInstance(), ThreadLocalProxy.getAll());
            sessionTimeoutMonitor.start();
        }
        FormExpiryMonitorThread formExpiryMonitorThread = new FormExpiryMonitorThread();
        formExpiryMonitorThread.run();
    }

    /**
     * Handles extension cleanup when the invoker is unloaded.
     * <p>
     * Unregisters the message listener from the invoker inbox to prevent memory leaks
     * and ensure clean shutdown. This method is automatically called by the Krista
     * platform when the extension is unloaded or reloaded.
     * </p>
     */
    @InvokerRequest(InvokerRequest.Type.INVOKER_UNLOADED)
    public void onInvokerUnload() {
        this.invokerInbox.unsetMessageListener();
    }

    /**
     * Provides the request authenticator for this extension.
     * <p>
     * Creates and returns a {@link WhatsAppBusinessRequestAuthenticator} that handles:
     * <ul>
     *   <li>Identifying authenticated users from WhatsApp messages</li>
     *   <li>Processing OAuth callback requests</li>
     *   <li>Generating OAuth authorization URLs</li>
     *   <li>Handling form-based authentication requests</li>
     * </ul>
     * </p>
     *
     * @return a configured request authenticator instance
     */
    @InvokerRequest(InvokerRequest.Type.AUTHENTICATOR)
    public RequestAuthenticator getAuthenticatedAccountId() {
        FromLinkRequestAuthenticator fromLinkRequestAuthenticator = new FromLinkRequestAuthenticator();
        return new WhatsAppBusinessRequestAuthenticator(mediumMessageService, whatsAppClient, invokerParameterStore, fromLinkRequestAuthenticator);
    }

    /**
     * Checks if the "omni.com" domain is already in the list of supported authentication domains.
     *
     * @return {@code true} if "omni.com" is supported, {@code false} otherwise
     */
    private boolean isDomainSupported() {
        return authenticationSettings.getSupportedDomains().contains(CommonInvokerParameters.SUPPORTED_DOMAIN);
    }

    /**
     * Validates the extension configuration attributes before saving.
     * <p>
     * Performs comprehensive validation including:
     * <ul>
     *   <li>Verifies OAuth access token exists (triggers authentication if missing)</li>
     *   <li>Validates Phone Number ID by calling WhatsApp Business API</li>
     *   <li>Checks that Welcome Conversation exists and is unique</li>
     *   <li>Checks that Fallback Conversation exists and is unique (if configured)</li>
     *   <li>Ensures conversations are in the correct execution mode (LIVE)</li>
     *   <li>Saves validated parameters to persistent storage</li>
     * </ul>
     * </p>
     *
     * @param connectionAttributes map containing all configuration parameters
     * @throws MustAuthorizeException if access token is missing or expired
     * @throws IllegalArgumentException if conversation validation fails
     */
    @InvokerRequest(InvokerRequest.Type.VALIDATE_ATTRIBUTES)
    public void validateAttribute(Map<String, Object> connectionAttributes) {
        String phoneNumberId = (String) connectionAttributes.get(InvokerParameters.PHONE_NUMBER_ID);
        String accessToken = accessTokenStore.get(phoneNumberId);
        String redirectUrl = InvokerParameters.createRedirectUrl(invoker);
        String welcomeConversation = (String) connectionAttributes.get(CommonInvokerParameters.WELCOME_CONVERSATION);
        String fallbackConversation = (String) connectionAttributes.get(CommonInvokerParameters.FALLBACK_CONVERSATION);
        connectionAttributes.put(InvokerParameters.REDIRECT_URI, redirectUrl);
        InvokerParameters availableParameters = InvokerParameters.create(connectionAttributes);
        invokerParameterStore.save(availableParameters);
        if (Objects.isNull(accessToken)) {
            NamedValuedField phoneNumberIdField = getNamedValuedField(InvokerParameters.PHONE_NUMBER_ID, phoneNumberId);
            NamedValuedField accountIdField = getNamedValuedField(Constants.ACCOUNT_ID, authorizationContext.getAuthorizedAccount().getAccountId());
            throw new MustAuthorizeException(ErrorMessages.AUTH_NOT_AUTHENTICATED, List.of(phoneNumberIdField, accountIdField));
        }
        Logger.log(ErrorMessages.debugPhoneNumberInfo(phoneNumberId));
        String response = getResponse(connectionAttributes, phoneNumberId);
        isConversationFound(welcomeConversation);
        if (fallbackConversation != null && !fallbackConversation.isEmpty()) {
            isConversationFound(fallbackConversation);
        }
        Logger.log("response:" + response);
    }

    /**
     * Retrieves business phone number details from WhatsApp API to validate configuration.
     * <p>
     * Makes an API call to verify that the Phone Number ID is valid and accessible
     * with the current access token. If the token is expired, throws a
     * {@link MustAuthorizeException} to trigger re-authentication.
     * </p>
     *
     * @param connectionAttributes map containing configuration parameters
     * @param phoneNumberId the WhatsApp Business phone number ID to validate
     * @return API response containing phone number details
     * @throws MustAuthorizeException if the access token is expired or invalid
     */
    private String getResponse(Map<String, Object> connectionAttributes, String phoneNumberId) {
        String response;
        try {
            response = whatsAppClient.businessPhoneNumber((String) connectionAttributes.get(InvokerParameters.PHONE_NUMBER_ID));
        } catch (IllegalStateException cause) {
            NamedValuedField phoneNumberIdField = getNamedValuedField(InvokerParameters.PHONE_NUMBER_ID, phoneNumberId);
            NamedValuedField accountIdField = getNamedValuedField(Constants.ACCOUNT_ID, authorizationContext.getAuthorizedAccount().getAccountId());
            throw new MustAuthorizeException(ErrorMessages.AUTH_TOKEN_EXPIRED, List.of(phoneNumberIdField, accountIdField));
        }
        return response;
    }

    /**
     * Creates a named-valued field for use in exception details.
     *
     * @param fieldName the name of the field
     * @param fieldValue the value of the field
     * @return a {@link NamedValuedField} instance with the specified name and value
     */
    private NamedValuedField getNamedValuedField(String fieldName, String fieldValue) {
        return new NamedValuedField(fieldName, "Text", fieldValue, Map.of(), Map.of());
    }

    /**
     * Validates that a conversation exists, is unique, and matches the current execution mode.
     * <p>
     * Searches for conversations by name and filters them to match the current execution mode
     * (LIVE). Throws an exception if:
     * <ul>
     *   <li>No conversation found with the given name</li>
     *   <li>Multiple conversations found with the same name</li>
     *   <li>Conversation exists but is not in the correct execution mode</li>
     * </ul>
     * </p>
     *
     * @param conversationName the name of the conversation to validate
     * @throws IllegalArgumentException if conversation is not found or not unique
     */
    private void isConversationFound(String conversationName) {
        List<Conversation> conversations = conversationsSearch.getConversations(conversationName);
        List<Conversation> filteredConversations = conversations
                .stream()
                .filter(conversation -> Objects.equals(conversation.getMode().name(), requestContext.getExecutionMode().name()))
                .collect(Collectors.toList());
        if (filteredConversations.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.conversationNotFound(conversationName));
        }
        if (filteredConversations.size() > 1) {
            throw new IllegalArgumentException(ErrorMessages.CONVERSATION_NOT_UNIQUE);
        }
    }

}
