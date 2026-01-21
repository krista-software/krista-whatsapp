# Error Messages and Troubleshooting

## Overview

This document provides a comprehensive guide to all error messages you may encounter when using the WhatsApp Business Extension, along with their causes and step-by-step resolution instructions.

## Error Message Categories

- [Authentication Errors](#authentication-errors)
- [Configuration Validation Errors](#configuration-validation-errors)
- [Webhook Errors](#webhook-errors)
- [API Communication Errors](#api-communication-errors)
- [Message Processing Errors](#message-processing-errors)
- [Session Errors](#session-errors)

---

## Authentication Errors

### "Authentication required. Please complete the OAuth authentication process..."

**Full Message**:
```
Authentication required. Please complete the OAuth authentication process by clicking 
the 'Authorize' button in the extension configuration.
```

**Cause**: You haven't completed the OAuth authentication process with Meta.

**Resolution**:
1. Open the WhatsApp Business Extension configuration in Krista
2. Ensure all required fields are filled (Client ID, Client Secret, Phone Number ID, Verify Token)
3. Click **Validate Attributes** to verify your configuration
4. Click the **Authorize** button
5. Complete the OAuth flow in the browser window that opens
6. Return to Krista and click **Save**

**Related Documentation**: [Authentication](pages/Authentication.md)

---

### "Your authentication token has expired..."

**Full Message**:
```
Your authentication token has expired. Please re-authenticate by clicking the 
'Authorize' button in the extension configuration.
```

**Cause**: Your OAuth access token has expired (tokens expire after 60 days).

**Resolution**:
1. Open the WhatsApp Business Extension configuration
2. Click the **Authorize** button to start re-authentication
3. Complete the OAuth flow
4. Click **Save** to update the configuration

**Prevention**: Set up a reminder to re-authenticate every 50-55 days to avoid service interruption.

**Related Documentation**: [Authentication](pages/Authentication.md#token-expiration)

---

### "Unable to verify your credentials with WhatsApp Business API..."

**Full Message**:
```
Unable to verify your credentials with WhatsApp Business API. Please check that 
your Client ID and Client Secret are correct and try again.
```

**Cause**: The Client ID or Client Secret provided doesn't match your Meta app credentials.

**Resolution**:
1. Log in to [Meta Developer Portal](https://developers.facebook.com/)
2. Navigate to your WhatsApp Business app
3. Go to **Settings** > **Basic**
4. Verify your **App ID** (Client ID)
5. Click **Show** next to **App Secret** to reveal your Client Secret
6. Copy the correct values to Krista extension configuration
7. Click **Validate Attributes** and then **Authorize**

**Related Documentation**: [Creating WhatsApp Business App](pages/CreatingWhatsAppBusinessApp.md#step-8-obtain-client-id-and-client-secret)

---

### "Authentication failed: Missing authorization code..."

**Full Message**:
```
Authentication failed: Missing authorization code. Please try the authentication 
process again.
```

**Cause**: The OAuth callback didn't receive the required authorization code from Meta.

**Resolution**:
1. Verify your redirect URI is correctly configured in Meta Developer Portal
2. Ensure the redirect URI is: `https://[your-krista-domain]/waba/callback`
3. Check that your browser isn't blocking the redirect
4. Try the authentication process again
5. If the issue persists, check browser console for errors

**Related Documentation**: [Creating WhatsApp Business App](pages/CreatingWhatsAppBusinessApp.md#step-9-configure-oauth-redirect-uri)

---

### "Authentication failed: Invalid state parameter..."

**Full Message**:
```
Authentication failed: Invalid state parameter. This may indicate a security issue 
or expired authentication request. Please start the authentication process again.
```

**Cause**: The state parameter in the OAuth callback doesn't match or has expired.

**Resolution**:
1. Clear your browser cache and cookies
2. Start a fresh authentication process
3. Complete the OAuth flow without delays
4. Don't navigate away during authentication
5. If the issue persists, restart the Krista extension

**Security Note**: This error can indicate a potential security issue. If it occurs repeatedly, contact your security team.

---

### "✅ Authentication successful!..."

**Full Message**:
```
✅ Authentication successful! Your WhatsApp Business account has been connected. 
Please click 'Save' to complete the configuration.
```

**Type**: Success Message

**Action Required**: Click the **Save** button to persist your configuration.

---

## Configuration Validation Errors

### "Conversation '[name]' not found..."

**Full Message**:
```
Conversation '[name]' not found. Please verify:
  • The conversation exists in your workspace
  • The conversation name is spelled correctly (case-sensitive)
  • The conversation is in Live execution mode
  • There are no leading or trailing spaces in the name
```

**Cause**: The specified conversation doesn't exist or doesn't meet the requirements.

**Resolution**:
1. Go to **Conversations** in Krista
2. Verify the conversation exists
3. Check the exact spelling (names are case-sensitive)
4. Ensure the conversation is in **Live** mode (not Test or Draft)
5. Copy the conversation name directly to avoid typos
6. Remove any extra spaces before or after the name
7. Click **Validate Attributes** again

**Related Documentation**: [Extension Configuration](pages/ExtensionConfiguration.md#welcome-conversation)

---

### "Multiple conversations with the same name found..."

**Full Message**:
```
Multiple conversations with the same name found. Please ensure the Welcome Conversation 
name is unique. Rename duplicate conversations or use a more specific name for your 
WhatsApp welcome conversation.
```

**Cause**: You have multiple conversations with the same name in your workspace.

**Resolution**:
1. Go to **Conversations** in Krista
2. Search for conversations with the specified name
3. Rename duplicate conversations to make them unique
4. Use a specific name like "WhatsApp Welcome" instead of just "Welcome"
5. Update the extension configuration with the unique name
6. Click **Validate Attributes** again

**Best Practice**: Use descriptive, unique names for conversations used by extensions.

---

### "Unable to verify Phone Number ID with WhatsApp Business API..."

**Full Message**:
```
Unable to verify Phone Number ID with WhatsApp Business API. Please check:
  • The Phone Number ID is correct (found in Meta Developer Portal)
  • Your access token is valid
  • The phone number is properly configured in your WhatsApp Business account
```

**Cause**: The Phone Number ID is incorrect or the phone number isn't properly configured.

**Resolution**:
1. Log in to [Meta Developer Portal](https://developers.facebook.com/)
2. Go to your WhatsApp Business app
3. Click **WhatsApp** > **Getting Started**
4. Copy the **Phone Number ID** (not the phone number itself)
5. Paste the correct Phone Number ID in Krista
6. Ensure you've completed authentication
7. Click **Validate Attributes** again

**Common Mistake**: Using the phone number instead of the Phone Number ID. The Phone Number ID is a long numeric string.

**Related Documentation**: [Creating WhatsApp Business App](pages/CreatingWhatsAppBusinessApp.md#step-6-obtain-phone-number-id)

---

## Webhook Errors

### "Webhook verification failed. The verify token does not match..."

**Full Message**:
```
Webhook verification failed. The verify token does not match. Please ensure:
  • The Verify Token in Krista matches exactly with the token in Meta Developer Portal
  • There are no extra spaces or special characters
  • The token is case-sensitive
```

**Cause**: The Verify Token configured in Krista doesn't match the token in Meta Developer Portal.

**Resolution**:
1. Open WhatsApp Business Extension configuration in Krista
2. Note the exact Verify Token value
3. Log in to [Meta Developer Portal](https://developers.facebook.com/)
4. Go to your WhatsApp Business app
5. Click **WhatsApp** > **Configuration**
6. Click **Edit** in the Webhook section
7. Enter the exact same Verify Token from Krista
8. Click **Verify and Save**

**Important**: The token is case-sensitive and must match exactly, including any special characters.

**Related Documentation**: [WhatsApp Webhook Configuration](pages/WhatsAppWebhookConfiguration.md#step-4-configure-verify-token)

---

### "Webhook message received for incorrect extension configuration..."

**Full Message**:
```
Webhook message received for incorrect extension configuration.
Received Phone Number: [number]
Received Phone Number ID: [id]
This message does not match the configured extension. Please verify your webhook 
configuration in Meta Developer Portal.
```

**Cause**: The webhook is receiving messages for a different phone number than configured.

**Resolution**:
1. Verify the Phone Number ID in Krista extension configuration
2. Check the User Identifier (phone number) in the configuration
3. In Meta Developer Portal, verify the webhook is subscribed to the correct phone number
4. Ensure you don't have multiple WhatsApp extensions with conflicting configurations
5. Update the configuration to match the phone number receiving messages

**Related Documentation**: [WhatsApp Webhook Configuration](pages/WhatsAppWebhookConfiguration.md)

---

## API Communication Errors

### "WhatsApp Business API request failed..."

**Full Message**:
```
WhatsApp Business API request failed. Response details:
[error details]

Possible causes:
  • Invalid or expired access token
  • Incorrect Phone Number ID
  • API rate limits exceeded
  • Network connectivity issues

Please check your configuration and try again. If the issue persists, review the 
Meta Developer Portal logs.
```

**Cause**: The API request to WhatsApp Business failed.

**Resolution**:

**For Token Issues**:
1. Re-authenticate the extension
2. Verify your access token is valid

**For Phone Number ID Issues**:
1. Verify the Phone Number ID is correct
2. Check the phone number is active in Meta Developer Portal

**For Rate Limit Issues**:
1. Check your message volume
2. Review Meta's rate limits for your account tier
3. Consider upgrading your business verification status

**For Network Issues**:
1. Check your internet connection
2. Verify firewall allows connections to graph.facebook.com
3. Check Meta's status page for outages

**Related Documentation**: [Troubleshooting](pages/ExtensionConfiguration.md#troubleshooting)

---

### "Unable to connect to WhatsApp Business API..."

**Full Message**:
```
Unable to connect to WhatsApp Business API: [error message]

Please check:
  • Your internet connection is stable
  • WhatsApp Business API is accessible (check Meta status page)
  • Your firewall allows outbound connections to graph.facebook.com
  • Your access token is valid and not expired
```

**Cause**: Network connectivity issue or API unavailability.

**Resolution**:
1. **Check Internet Connection**: Verify your server has internet access
2. **Check Meta Status**: Visit [Meta Status Page](https://metastatus.com/)
3. **Check Firewall**: Ensure outbound HTTPS to graph.facebook.com is allowed
4. **Check DNS**: Verify DNS resolution for graph.facebook.com
5. **Check Proxy**: If using a proxy, ensure it's configured correctly
6. **Retry**: Wait a few minutes and try again

**For System Administrators**:
```bash
# Test connectivity
curl -I https://graph.facebook.com/v16.0/

# Test DNS resolution
nslookup graph.facebook.com
```

---

## Message Processing Errors

### "An error occurred while processing the incoming message..."

**Full Message**:
```
An error occurred while processing the incoming message. The message has been logged 
for review. The user may need to resend their message.
```

**Cause**: An unexpected error occurred while processing a user's message.

**Resolution**:
1. Check Krista system logs for detailed error information
2. Verify the session is still active
3. Check that the Welcome Conversation is properly configured
4. Ensure NLP is functioning correctly
5. Ask the user to resend their message

**For Administrators**:
1. Review application logs for stack traces
2. Check for any system resource issues (memory, CPU)
3. Verify database connectivity
4. Check for any recent configuration changes

---

### "Failed to process message (ID: [id])..."

**Full Message**:
```
Failed to process message (ID: [id]). Error: [error details]
The message has been logged. Please check system logs for more details.
```

**Cause**: Specific error occurred while processing a message.

**Resolution**:
1. Note the message ID for reference
2. Check system logs for the specific error
3. Verify the message format is supported
4. Check if the error is recurring or isolated
5. Contact support if the issue persists

---

## Session Errors

### "No active session found for this user..."

**Full Message**:
```
No active session found for this user. A new session will be created when the user 
sends their next message.
```

**Type**: Informational

**Cause**: The user doesn't have an active session (first-time user or session expired).

**Action**: No action required. A new session will be created automatically when the user sends a message.

**Related Documentation**: [Workflow](pages/Workflow.md#session-management-workflow)

---

### "User session has expired due to inactivity..."

**Full Message**:
```
User session has expired due to inactivity. A new session will be created when the 
user sends their next message.
```

**Type**: Informational

**Cause**: The session timeout period has elapsed without user activity.

**Action**: No action required. A new session will be created automatically.

**Configuration**: Adjust the Session Timeout value in extension configuration if needed (default: 30 minutes).

**Related Documentation**: [Extension Configuration](pages/ExtensionConfiguration.md#session-timeout-in-minutes)

---

## General Errors

### "An unexpected error occurred..."

**Full Message**:
```
An unexpected error occurred. Please try again. If the problem persists, contact 
your system administrator.
```

**Cause**: An unhandled error occurred in the system.

**Resolution**:
1. Try the operation again
2. Check system logs for detailed error information
3. Verify all services are running
4. Check for any recent system changes
5. Contact your system administrator if the issue persists

---

## Success Messages

### "✅ Configuration validated successfully!..."

**Full Message**:
```
✅ Configuration validated successfully! All settings are correct and the connection 
to WhatsApp Business API is working.
```

**Type**: Success

**Next Steps**:
1. Click **Authorize** to complete OAuth authentication (if not already done)
2. Click **Save** to persist your configuration
3. Configure webhooks in Meta Developer Portal
4. Test by sending a message to your WhatsApp Business number

---

### "✅ Configuration saved successfully!..."

**Full Message**:
```
✅ Configuration saved successfully! Your WhatsApp Business Extension is now active.
```

**Type**: Success

**Next Steps**:
1. Configure webhooks if not already done
2. Test the integration by sending a test message
3. Monitor the first few interactions to ensure everything works correctly

---

## Warning Messages

### "⚠️ Warning: WhatsApp Business Extension only supports Live execution mode..."

**Full Message**:
```
⚠️ Warning: WhatsApp Business Extension only supports Live execution mode. Test and 
Draft modes are not available.
```

**Type**: Warning

**Impact**: You cannot use Test or Draft mode conversations with this extension.

**Action**: Ensure all conversations used by the extension are in Live mode.

---

### "⚠️ Warning: Fallback Conversation must be in the same execution mode..."

**Full Message**:
```
⚠️ Warning: Fallback Conversation must be in the same execution mode as the extension 
(Live mode).
```

**Type**: Warning

**Impact**: If the Fallback Conversation is not in Live mode, it won't work correctly.

**Action**: Ensure your Fallback Conversation is in Live mode.

---

## Getting Additional Help

If you encounter an error not listed here or need additional assistance:

1. **Check System Logs**: Review Krista application logs for detailed error information
2. **Review Documentation**: Check the relevant documentation pages linked in each error
3. **Meta Developer Portal**: Check the webhook logs and app dashboard for additional insights
4. **Contact Support**: Reach out to Krista support with:
   - The complete error message
   - Steps to reproduce the issue
   - System logs (if available)
   - Your extension configuration (without sensitive data)

## See Also

- [Extension Configuration](pages/ExtensionConfiguration.md) - Configuration guide and troubleshooting
- [Authentication](pages/Authentication.md) - OAuth authentication details
- [WhatsApp Webhook Configuration](pages/WhatsAppWebhookConfiguration.md) - Webhook setup and troubleshooting
- [Workflow](pages/Workflow.md) - Common workflows and error handling
- [Release Notes](pages/ReleaseNotes.md) - Known issues and limitations

