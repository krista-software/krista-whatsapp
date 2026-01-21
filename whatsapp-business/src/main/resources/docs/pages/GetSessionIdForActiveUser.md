# Get Session Id for Active User

## Overview

Retrieves the unique session inbox ID for the currently active user in a WhatsApp conversation. This session ID is used to identify and manage the user's conversation context within Krista.

## Request Details

- **Area**: Medium
- **Type**: QUERY_SYSTEM
- **Retry Support**: ❌ No (Query operations are idempotent and don't require retry)

## Input Parameters

This catalog request does not require any input parameters. It automatically uses the context of the currently authenticated user.

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| _(None)_ | - | - | No input parameters required | - |

## Output Parameters

| Parameter Name | Type | Description | Example |
|----------------|------|-------------|---------|
| Session Id | Text | Unique identifier for the user's session inbox | "inbox_abc123def456" |

### Output Details

**Session Id**: A unique string identifier that represents the user's active session inbox in Krista. This ID is used internally by the platform to:
- Route messages to the correct conversation
- Maintain conversation context
- Track user interactions
- Manage session state

## Validation Rules

This catalog request performs minimal validation as it operates on the authenticated user's context:

| Validation | Error Message | Resolution |
|------------|---------------|------------|
| User not authenticated | "Authentication required. Please complete the OAuth authentication process..." | Ensure the user has completed OAuth authentication |
| No active session | "No active session found for this user. A new session will be created..." | User must initiate a conversation through WhatsApp first |

## Error Handling

### Authorization Errors

**Cause**: User is not authenticated or authorization context is missing

**Common Scenarios**:
- OAuth token has expired
- User has not completed authentication
- Authorization context is not available

**Resolution**:
1. Verify user has completed OAuth authentication
2. Check that access token is valid
3. Ensure user has an active WhatsApp session
4. Re-authenticate if necessary

### System Errors (SYSTEM_ERROR)

**Cause**: Internal system error while retrieving session information

**Common Scenarios**:
- Database connection failure
- Session service unavailable
- Internal service error

**Resolution**:
1. Check system logs for detailed error information
2. Verify all required services are running
3. Retry the operation
4. Contact support if issue persists

## Usage Examples

### Example 1: Retrieve Session ID for Current User

**Scenario**: A workflow needs to identify the current user's session to route messages or track conversation state.

**Input**:
```
(No input parameters required)
```

**Output**:
```json
{
  "Session Id": "inbox_5fa2fc97-4b17-44cf-b98f-aa91a459a091"
}
```

**Result**: The session ID is successfully retrieved and can be used for message routing or session management.

### Example 2: Use Session ID in Workflow

**Scenario**: A conversation workflow retrieves the session ID to send a follow-up message to the user.

**Input**:
```
(No input parameters required)
```

**Output**:
```json
{
  "Session Id": "inbox_abc123def456789"
}
```

**Result**: The workflow uses this session ID to send targeted messages to the specific user's WhatsApp conversation.

### Example 3: Session Management

**Scenario**: An agent dashboard needs to identify which session a user is currently active in.

**Input**:
```
(No input parameters required)
```

**Output**:
```json
{
  "Session Id": "inbox_user_session_12345"
}
```

**Result**: The dashboard displays the correct session information for the active user.

## Business Rules

1. **Single Active Session**: Each user can have only one active session at a time
2. **Automatic Session Creation**: Sessions are automatically created when a user initiates a WhatsApp conversation
3. **Session Persistence**: Session IDs remain constant throughout the session lifecycle
4. **User Context Required**: The request operates on the currently authenticated user's context
5. **No Cross-User Access**: Users cannot retrieve session IDs for other users

## Limitations

1. **Authentication Required**: User must be authenticated through OAuth before calling this request
2. **Active Session Required**: User must have an active WhatsApp session
3. **Read-Only Operation**: This request only retrieves information; it does not create or modify sessions
4. **No Historical Data**: Only returns the current active session ID, not historical session information

## Best Practices

### 1. Cache Session IDs Appropriately
Cache the session ID within a workflow or conversation to avoid repeated calls. However, be aware that session IDs may change if the session expires or is recreated.

### 2. Handle Missing Sessions Gracefully
Always implement error handling for cases where no active session exists. Provide clear feedback to users or workflows when a session cannot be found.

### 3. Use for Message Routing
Leverage session IDs to ensure messages are routed to the correct user conversation, especially in multi-user or agent scenarios.

### 4. Combine with Session Management
Use this request in conjunction with session timeout and session management features to maintain proper conversation state.

## Common Use Cases

### 1. Agent Dashboard Integration
```
Scenario: An agent needs to view the active session for a WhatsApp user
Action: Call "Get Session Id for active user" to retrieve the session identifier
Result: Agent dashboard displays the correct session and conversation history
```

### 2. Workflow Message Routing
```
Scenario: A workflow needs to send a follow-up message to a specific user
Action: Retrieve session ID and use it to route the message to the correct inbox
Result: Message is delivered to the user's active WhatsApp conversation
```

### 3. Session State Tracking
```
Scenario: System needs to track which users have active sessions
Action: Call this request for each authenticated user
Result: System maintains accurate session state information
```

### 4. Multi-Channel Integration
```
Scenario: User switches between WhatsApp and web chat
Action: Retrieve session ID to maintain conversation context across channels
Result: Seamless conversation experience across multiple channels
```

## Related Catalog Requests

- [Get Last User Message](pages/GetLastUserMessage.md) - Retrieve the most recent message from the user

## Technical Implementation

### Helper Class
- **Class**: MediumArea
- **Package**: app.krista.extension.essentials.collaboration.waba.catalog
- **Method**: getSessionIdForActiveUser()

### Implementation Details

The catalog request:
1. Retrieves the authorized account from AuthorizationContext
2. Extracts the user identifier from the account's primary email address
3. Calls KristaMessageReceiver.getSessionInbox() to get the session inbox
4. Returns the inbox ID as the session identifier

### Dependencies
- **AuthorizationContext**: Provides the authenticated user's account information
- **KristaMessageReceiver**: Service for retrieving session inbox information
- **InvokerAttributeProvider**: Provides the user identifier from extension configuration

### Code Flow
```
1. Get authorized account from context
2. Extract user identifier (email prefix)
3. Retrieve session inbox for the account
4. Return inbox ID as session identifier
```

## Troubleshooting

> 📖 **For comprehensive error messages and troubleshooting**: See the [Error Messages and Troubleshooting](pages/ErrorMessages.md) guide for detailed information about all error messages, their causes, and step-by-step resolution instructions.

### Common Issues

#### Issue: Authentication Errors

**Symptoms**:
- "Authentication required. Please complete the OAuth authentication process..."
- "Your authentication token has expired..."

**Quick Solution**:
1. Verify user has completed OAuth authentication flow
2. Check that access token is valid and not expired
3. Ensure extension configuration is correct
4. Re-authenticate user if necessary

**Detailed Guide**: [Authentication Errors](pages/ErrorMessages.md#authentication-errors)

---

#### Issue: Session Not Found

**Symptoms**:
- "No active session found for this user..."
- "User session has expired due to inactivity..."
- Empty or null session ID returned

**Quick Solution**:
1. Verify user has initiated a WhatsApp conversation
2. Check that session has not expired
3. Ensure session timeout settings are appropriate
4. Have user send a message to create a new session

**Detailed Guide**: [Session Errors](pages/ErrorMessages.md#session-errors)

---

#### Issue: Session ID Changes Unexpectedly

**Cause**: Session expired or was recreated

**Solution**:
1. Review session timeout configuration
2. Implement session expiration handling in workflows
3. Re-retrieve session ID when needed
4. Consider increasing session timeout if appropriate

---

#### Issue: Cannot Use Session ID for Message Routing

**Cause**: Session ID format or usage is incorrect

**Solution**:
1. Verify session ID is being passed correctly to message routing logic
2. Check that session still exists and is active
3. Ensure message routing service is properly configured
4. Review logs for detailed error information

## See Also

- [Error Messages and Troubleshooting](pages/ErrorMessages.md) - Comprehensive error message guide
- [Extension Configuration](pages/ExtensionConfiguration.md) - Configure session timeout and other settings
- [Authentication](pages/Authentication.md) - OAuth authentication required for this request
- [Get Last User Message](pages/GetLastUserMessage.md) - Related session operation
- [Workflow](pages/Workflow.md) - Examples of using session IDs in workflows

