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

package app.krista.extension.essentials.collaboration.waba.util;

/**
 * Centralized error messages for WhatsApp Business Extension.
 * All error messages are user-friendly, actionable, and provide clear guidance for resolution.
 */
public final class ErrorMessages {

    private ErrorMessages() {
        // Utility class - prevent instantiation
    }

    // Authentication Errors
    public static final String AUTH_NOT_AUTHENTICATED = 
        "Authentication required. Please complete the OAuth authentication process by clicking the 'Authorize' button in the extension configuration.";
    
    public static final String AUTH_TOKEN_EXPIRED = 
        "Your authentication token has expired. Please re-authenticate by clicking the 'Authorize' button in the extension configuration.";
    
    public static final String AUTH_INVALID_CREDENTIALS = 
        "Unable to verify your credentials with WhatsApp Business API. Please check that your Client ID and Client Secret are correct and try again.";
    
    public static final String AUTH_MISSING_CODE = 
        "Authentication failed: Missing authorization code. Please try the authentication process again.";
    
    public static final String AUTH_INVALID_STATE = 
        "Authentication failed: Invalid state parameter. This may indicate a security issue or expired authentication request. Please start the authentication process again.";
    
    public static final String AUTH_CALLBACK_SUCCESS = 
        "✅ Authentication successful! Your WhatsApp Business account has been connected. Please click 'Save' to complete the configuration.";

    // Configuration Validation Errors
    public static String conversationNotFound(String conversationName) {
        return String.format(
            "Conversation '%s' not found. Please verify:\n" +
            "  • The conversation exists in your workspace\n" +
            "  • The conversation name is spelled correctly (case-sensitive)\n" +
            "  • The conversation is in Live execution mode\n" +
            "  • There are no leading or trailing spaces in the name",
            conversationName
        );
    }
    
    public static final String CONVERSATION_NOT_UNIQUE = 
        "Multiple conversations with the same name found. Please ensure the Welcome Conversation name is unique. " +
        "Rename duplicate conversations or use a more specific name for your WhatsApp welcome conversation.";
    
    public static final String PHONE_NUMBER_INVALID = 
        "Unable to verify Phone Number ID with WhatsApp Business API. Please check:\n" +
        "  • The Phone Number ID is correct (found in Meta Developer Portal)\n" +
        "  • Your access token is valid\n" +
        "  • The phone number is properly configured in your WhatsApp Business account";

    // Webhook Errors
    public static final String WEBHOOK_VERIFICATION_FAILED = 
        "Webhook verification failed. The verify token does not match. Please ensure:\n" +
        "  • The Verify Token in Krista matches exactly with the token in Meta Developer Portal\n" +
        "  • There are no extra spaces or special characters\n" +
        "  • The token is case-sensitive";
    
    public static String webhookWrongInvoker(String receivedPhoneNumber, String receivedPhoneNumberId) {
        return String.format(
            "Webhook message received for incorrect extension configuration.\n" +
            "Received Phone Number: %s\n" +
            "Received Phone Number ID: %s\n" +
            "This message does not match the configured extension. Please verify your webhook configuration in Meta Developer Portal.",
            receivedPhoneNumber,
            receivedPhoneNumberId
        );
    }

    // API Communication Errors
    public static String apiRequestFailed(String responseDetails) {
        return String.format(
            "WhatsApp Business API request failed. Response details:\n%s\n\n" +
            "Possible causes:\n" +
            "  • Invalid or expired access token\n" +
            "  • Incorrect Phone Number ID\n" +
            "  • API rate limits exceeded\n" +
            "  • Network connectivity issues\n\n" +
            "Please check your configuration and try again. If the issue persists, review the Meta Developer Portal logs.",
            responseDetails
        );
    }
    
    public static String apiConnectionError(String errorMessage) {
        return String.format(
            "Unable to connect to WhatsApp Business API: %s\n\n" +
            "Please check:\n" +
            "  • Your internet connection is stable\n" +
            "  • WhatsApp Business API is accessible (check Meta status page)\n" +
            "  • Your firewall allows outbound connections to graph.facebook.com\n" +
            "  • Your access token is valid and not expired",
            errorMessage
        );
    }

    // Message Processing Errors
    public static final String MESSAGE_PROCESSING_ERROR = 
        "An error occurred while processing the incoming message. The message has been logged for review. " +
        "The user may need to resend their message.";
    
    public static String messageProcessingError(String messageId, String errorDetails) {
        return String.format(
            "Failed to process message (ID: %s). Error: %s\n" +
            "The message has been logged. Please check system logs for more details.",
            messageId,
            errorDetails
        );
    }

    // Session Errors
    public static final String SESSION_NOT_FOUND = 
        "No active session found for this user. A new session will be created when the user sends their next message.";
    
    public static final String SESSION_EXPIRED = 
        "User session has expired due to inactivity. A new session will be created when the user sends their next message.";

    // General Errors
    public static final String UNEXPECTED_ERROR = 
        "An unexpected error occurred. Please try again. If the problem persists, contact your system administrator.";
    
    public static String unexpectedError(String technicalDetails) {
        return String.format(
            "An unexpected error occurred: %s\n" +
            "Please contact your system administrator with this error message.",
            technicalDetails
        );
    }

    // Validation Errors
    public static final String VALIDATION_REQUIRED_FIELD = 
        "This field is required. Please provide a value.";
    
    public static String validationFieldFormat(String fieldName, String expectedFormat) {
        return String.format(
            "Invalid format for %s. Expected format: %s",
            fieldName,
            expectedFormat
        );
    }
    
    public static final String VALIDATION_PHONE_NUMBER_FORMAT = 
        "Invalid phone number format. Please enter the phone number with country code (e.g., +1234567890).";
    
    public static final String VALIDATION_CLIENT_ID_EMPTY = 
        "Client ID is required. You can find your Client ID (App ID) in the Meta Developer Portal under Settings > Basic.";
    
    public static final String VALIDATION_CLIENT_SECRET_EMPTY = 
        "Client Secret is required. You can find your Client Secret (App Secret) in the Meta Developer Portal under Settings > Basic.";
    
    public static final String VALIDATION_PHONE_NUMBER_ID_EMPTY = 
        "Phone Number ID is required. You can find your Phone Number ID in the Meta Developer Portal under WhatsApp > Getting Started.";
    
    public static final String VALIDATION_VERIFY_TOKEN_EMPTY = 
        "Verify Token is required. Create a secure random token and use the same value in both Krista and Meta Developer Portal webhook configuration.";
    
    public static final String VALIDATION_WELCOME_CONVERSATION_EMPTY = 
        "Welcome Conversation is required. Please specify the name of the conversation to trigger when users first interact with the bot.";

    // Success Messages
    public static final String VALIDATION_SUCCESS = 
        "✅ Configuration validated successfully! All settings are correct and the connection to WhatsApp Business API is working.";
    
    public static final String SAVE_SUCCESS = 
        "✅ Configuration saved successfully! Your WhatsApp Business Extension is now active.";

    // Warning Messages
    public static final String WARNING_TEST_MODE_NOT_SUPPORTED = 
        "⚠️ Warning: WhatsApp Business Extension only supports Live execution mode. Test and Draft modes are not available.";
    
    public static final String WARNING_FALLBACK_MODE_MISMATCH = 
        "⚠️ Warning: Fallback Conversation must be in the same execution mode as the extension (Live mode).";
    
    public static final String WARNING_TOKEN_EXPIRING_SOON = 
        "⚠️ Your access token will expire soon. Please re-authenticate to avoid service interruption.";

    // Info Messages
    public static final String INFO_FIRST_TIME_SETUP = 
        "ℹ️ First time setup: After configuring all fields, click 'Validate Attributes' to verify your connection, then click 'Authorize' to complete OAuth authentication.";
    
    public static final String INFO_WEBHOOK_SETUP_REQUIRED = 
        "ℹ️ Don't forget to configure webhooks in Meta Developer Portal to receive incoming messages. See documentation for details.";
    
    public static final String INFO_PHONE_NUMBER_VERIFICATION = 
        "ℹ️ For production use, verify your business phone number in Meta Developer Portal and complete business verification.";

    // Debug Messages (for logging)
    public static String debugPhoneNumberInfo(String phoneNumberId) {
        return String.format("Verifying business phone number with ID: %s", phoneNumberId);
    }
    
    public static String debugWebhookReceived(String phoneNumber, String phoneNumberId) {
        return String.format(
            "Webhook message received - Phone: %s, Phone ID: %s",
            phoneNumber,
            phoneNumberId
        );
    }
    
    public static String debugMessageProcessed(String messageId) {
        return String.format("Message processed successfully - ID: %s", messageId);
    }
}

