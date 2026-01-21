# Authentication

## Overview

The WhatsApp Business Extension uses OAuth 2.0 for secure authentication with Meta's WhatsApp Business Cloud API. This document explains the authentication mechanisms, token management, and security best practices for the extension.

## Authentication Mode

The WhatsApp Business Extension uses **User Account Authentication** through Meta's OAuth 2.0 framework. Unlike some extensions that support both service account and user authentication, this extension exclusively uses OAuth 2.0 user authentication to access WhatsApp Business accounts.

### Why User Account Authentication?

- **User Context**: WhatsApp Business API requires user-level permissions
- **Security**: OAuth 2.0 provides secure, delegated access without sharing credentials
- **Token Management**: Automatic token refresh ensures continuous operation
- **Revocation**: Users can revoke access at any time through Meta settings

## OAuth 2.0 Scopes and Permissions

### Required Scopes

The WhatsApp Business Extension requires the following OAuth 2.0 scopes:

| Scope | Permission Level | Description |
|-------|------------------|-------------|
| `whatsapp_business_management` | Read/Write | Manage WhatsApp Business account settings and phone numbers |
| `whatsapp_business_messaging` | Read/Write | Send and receive WhatsApp messages |
| `business_management` | Read | Access business portfolio information |

### Scope Details

#### whatsapp_business_management
- **Full URL**: `https://graph.facebook.com/whatsapp_business_management`
- **Purpose**: Allows the extension to:
  - Access WhatsApp Business phone number details
  - Retrieve phone number configuration
  - Verify phone number status
  - Manage webhook subscriptions

#### whatsapp_business_messaging
- **Full URL**: `https://graph.facebook.com/whatsapp_business_messaging`
- **Purpose**: Allows the extension to:
  - Send messages to WhatsApp users
  - Receive incoming messages via webhooks
  - Send interactive messages (buttons, lists)
  - Send media messages (images, documents)
  - Mark messages as read

#### business_management
- **Full URL**: `https://graph.facebook.com/business_management`
- **Purpose**: Allows the extension to:
  - Access business portfolio information
  - Verify business account status
  - Retrieve business details for authentication

### Why These Scopes?

These scopes provide the minimum permissions required for the extension to function. Alternative scopes like `public_profile` or `email` are not used because:
- They provide unnecessary access to user data
- WhatsApp Business API doesn't require them
- Following principle of least privilege

## OAuth 2.0 Grant Types

The WhatsApp Business Extension uses two OAuth 2.0 grant types:

### Authorization Code Grant

- **Grant Type**: `authorization_code`
- **Used For**: Initial authentication when user authorizes the extension
- **Flow**: User is redirected to Meta login, authorizes the app, and returns with an authorization code

**Request Example**:
```http
POST https://graph.facebook.com/v18.0/oauth/access_token
Content-Type: application/x-www-form-urlencoded

client_id={client_id}
&client_secret={client_secret}
&code={authorization_code}
&redirect_uri={redirect_uri}
```

**Response Example**:
```json
{
  "access_token": "EAABwzLixnjYBO...",
  "token_type": "bearer",
  "expires_in": 5184000
}
```

### Refresh Token Grant

Meta's OAuth 2.0 implementation for WhatsApp Business uses **long-lived access tokens** instead of refresh tokens. Access tokens are valid for 60 days and can be exchanged for new long-lived tokens before expiration.

**Token Exchange Request**:
```http
GET https://graph.facebook.com/v18.0/oauth/access_token
  ?grant_type=fb_exchange_token
  &client_id={client_id}
  &client_secret={client_secret}
  &fb_exchange_token={current_access_token}
```

**Response Example**:
```json
{
  "access_token": "EAABwzLixnjYBO...",
  "token_type": "bearer",
  "expires_in": 5184000
}
```

## Authentication Flow

### Initial Authentication

The following diagram illustrates the OAuth 2.0 authentication flow:

```
User → Krista Extension → Meta OAuth → User Authorization → Meta → Access Token → Krista
```

**Step-by-Step Process**:

1. **User Initiates Authentication**
   - User configures the extension in Krista
   - Clicks "Validate Attributes"
   - Extension detects no access token exists

2. **Authorization Request**
   - Extension throws `MustAuthorizeException`
   - User is redirected to Meta OAuth authorization URL
   - URL includes: client_id, redirect_uri, scope, state

3. **User Authorization**
   - User logs in to Meta account (if not already logged in)
   - Reviews requested permissions
   - Clicks "Continue" to authorize the app

4. **Authorization Code Exchange**
   - Meta redirects user back to Krista with authorization code
   - Extension receives the code at the callback endpoint
   - Extension exchanges code for access token

5. **Token Storage**
   - Extension stores access token securely in KeyValueStore
   - Token is encrypted at rest
   - Token is associated with Phone Number ID

6. **Validation Complete**
   - Extension validates the token by calling WhatsApp Business API
   - Configuration validation succeeds
   - Extension is ready to use

### Authorization URL Format

```
https://www.facebook.com/v18.0/dialog/oauth
  ?client_id={client_id}
  &redirect_uri={redirect_uri}
  &scope=whatsapp_business_management,whatsapp_business_messaging,business_management
  &state={state_parameter}
```

### Callback Endpoint

The extension provides a callback endpoint to receive the authorization code:

```
https://[your-krista-domain]/waba/callback
```

This endpoint:
- Receives the authorization code from Meta
- Validates the state parameter
- Exchanges code for access token
- Stores the token securely
- Redirects user back to configuration page

## Token Management

### Access Token Lifecycle

**Token Duration**: 60 days (5,184,000 seconds)

**Token Storage**:
- Stored in Krista's KeyValueStore
- Encrypted at rest
- Key format: `{phone_number_id}_access_token`
- Associated with specific Phone Number ID

**Token Retrieval**:
```java
String accessToken = accessTokenStore.get(phoneNumberId);
```

### Token Expiration Scenarios

#### 1. Normal Expiration (60 Days)

**Scenario**: Access token reaches its 60-day expiration

**Handling**:
- Extension should exchange token for new long-lived token before expiration
- Recommended: Exchange token every 30 days
- Current implementation: Manual re-authentication required

**User Impact**: User must re-authenticate after 60 days

#### 2. User Revocation

**Scenario**: User revokes app access through Meta settings

**Handling**:
- API calls return authentication error
- Extension throws `MustAuthorizeException`
- User must re-authenticate

**Common Causes**:
- User manually revokes access in Meta settings
- User removes app from authorized apps list

#### 3. Password Change

**Scenario**: User changes their Meta account password

**Handling**:
- All access tokens are automatically revoked by Meta
- Extension detects authentication failure
- User must re-authenticate

#### 4. Security Events

**Scenario**: Meta detects suspicious activity or security issues

**Handling**:
- Meta may revoke access tokens
- Extension detects authentication failure
- User must re-authenticate

**Common Causes**:
- Unusual login patterns
- Compromised account detection
- Policy violations

#### 5. Token Limit Exceeded

**Scenario**: Too many access tokens issued for the same app/user combination

**Handling**:
- Older tokens are automatically invalidated
- Extension may need re-authentication
- Limit: Approximately 20 tokens per app/user

### Token Refresh Flow

```
Extension → Check Token Expiration → Token Expiring Soon? → Exchange Token → Store New Token
                                    ↓
                                    No → Continue Using Current Token
```

**Automatic Refresh** (Recommended Implementation):
1. Check token expiration before each API call
2. If token expires within 7 days, exchange for new token
3. Store new token in KeyValueStore
4. Continue with API call

**Current Implementation**:
- Manual re-authentication required when token expires
- User receives `MustAuthorizeException`
- User must complete OAuth flow again

## Security Best Practices

### 1. Secure Credential Storage

✅ **Do**:
- Store Client Secret encrypted in Krista
- Never log or display Client Secret
- Use Krista's secured field type for sensitive data
- Rotate Client Secret periodically

❌ **Don't**:
- Store credentials in plain text
- Include credentials in error messages
- Share credentials across environments
- Commit credentials to version control

### 2. Token Protection

✅ **Do**:
- Store access tokens encrypted
- Use HTTPS for all API communications
- Validate tokens before use
- Implement token expiration checks

❌ **Don't**:
- Log access tokens
- Send tokens in URL parameters
- Store tokens in cookies or local storage
- Share tokens between users

### 3. Webhook Security

✅ **Do**:
- Use strong, random Verify Token
- Validate webhook signatures
- Use HTTPS for webhook endpoint
- Implement rate limiting

❌ **Don't**:
- Use predictable Verify Tokens
- Accept unsigned webhook requests
- Expose webhook endpoint without authentication
- Process webhooks without validation

### 4. OAuth Flow Security

✅ **Do**:
- Use state parameter to prevent CSRF
- Validate redirect URI matches configuration
- Implement timeout for authorization codes
- Use PKCE for enhanced security (if supported)

❌ **Don't**:
- Skip state parameter validation
- Accept authorization codes multiple times
- Use predictable state values
- Allow open redirects

## Troubleshooting Authentication

### Issue: "User is not Authenticated" Error

**Cause**: No access token found or token is invalid

**Resolution**:
1. Click "Validate Attributes" to trigger authentication
2. Complete OAuth flow in Meta
3. Verify Client ID and Client Secret are correct
4. Check that Phone Number ID is valid

### Issue: Authentication Succeeds but API Calls Fail

**Cause**: Token lacks required permissions

**Resolution**:
1. Verify all required scopes are granted
2. Check app permissions in Meta Developer Portal
3. Re-authenticate to refresh permissions
4. Verify business account has WhatsApp Business access

### Issue: Redirect URI Mismatch Error

**Cause**: Redirect URI in Meta app doesn't match extension callback URL

**Resolution**:
1. Get callback URL from extension configuration
2. Add URL to "Valid OAuth Redirect URIs" in Meta app settings
3. Ensure URL matches exactly (including protocol and path)
4. Save changes in Meta Developer Portal

### Issue: Token Expired Error

**Cause**: Access token has expired (60 days)

**Resolution**:
1. Re-authenticate through extension configuration
2. Complete OAuth flow to get new token
3. Consider implementing automatic token refresh

### Issue: "Invalid Client" Error

**Cause**: Client ID or Client Secret is incorrect

**Resolution**:
1. Verify Client ID matches App ID in Meta Developer Portal
2. Verify Client Secret matches App Secret
3. Check for extra spaces or characters
4. Regenerate App Secret if necessary

## Testing Authentication

### Test Connection

After configuration, test the authentication:

1. Navigate to extension configuration
2. Click "Test Connection" button
3. Extension calls WhatsApp Business API to retrieve phone number details
4. Success indicates authentication is working correctly

**Expected Response**:
```json
{
  "verified_name": "Your Business Name",
  "display_phone_number": "+1234567890",
  "quality_rating": "GREEN",
  "id": "123456789012345"
}
```

### Verify Token Permissions

Check that the token has required permissions:

1. Use Meta's Access Token Debugger: https://developers.facebook.com/tools/debug/accesstoken/
2. Paste your access token
3. Verify:
   - Token is valid
   - Scopes include required permissions
   - Expiration date is in the future
   - App ID matches your configuration

## See Also

- [Extension Configuration](pages/ExtensionConfiguration.md) - Complete configuration guide
- [Creating WhatsApp Business App](pages/CreatingWhatsAppBusinessApp.md) - Setting up your Meta app
- [WhatsApp Webhook Configuration](pages/WhatsAppWebhookConfiguration.md) - Configuring webhooks
- [Meta OAuth Documentation](https://developers.facebook.com/docs/facebook-login/guides/advanced/manual-flow) - Official OAuth documentation

