# Get Last User Message

## Overview

Retrieves the most recent message sent by the user to Krista in the current WhatsApp conversation. This is useful for workflows that need to reference or process the user's last input.

## Request Details

- **Area**: Medium
- **Type**: QUERY_SYSTEM
- **Retry Support**: ❌ No (Query operations are idempotent and don't require retry)

## Input Parameters

This catalog request does not require any input parameters. It automatically retrieves the last message from the currently authenticated user's session.

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| _(None)_ | - | - | No input parameters required | - |

## Output Parameters

| Parameter Name | Type | Description | Example |
|----------------|------|-------------|---------|
| Message | Text | The text content of the last message sent by the user | "Hello Krista" |

### Output Details

**Message**: The text content of the most recent message sent by the user to Krista. This includes:
- Plain text messages
- Text extracted from complex messages
- User responses to prompts
- Commands or queries sent by the user

**Special Cases**:
- If no messages are found, returns an empty string ("")
- Only returns messages sent by the user (not Krista's responses)
- Returns the most recent message in chronological order

## Validation Rules

This catalog request performs minimal validation as it operates on the authenticated user's message history:

| Validation | Error Message | Resolution |
|------------|---------------|------------|
| User not authenticated | "Authentication required. Please complete the OAuth authentication process..." | Ensure the user has completed OAuth authentication |
| No active session | "No active session found for this user. A new session will be created..." | User must initiate a conversation through WhatsApp first |
| Inbox not accessible | "Unable to connect to WhatsApp Business API..." | Verify session is active and inbox exists |

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

**Cause**: Internal system error while retrieving message history

**Common Scenarios**:
- Inbox service unavailable
- Database connection failure
- Message retrieval timeout
- Internal service error

**Resolution**:
1. Check system logs for detailed error information
2. Verify inbox service is running
3. Retry the operation
4. Contact support if issue persists

### Logic Errors (LOGIC_ERROR)

**Cause**: No messages found in the user's conversation history

**Common Scenarios**:
- User has not sent any messages yet
- Message history has been cleared
- Session is new with no messages

**Resolution**:
1. Verify user has sent at least one message
2. Check that session is active
3. Handle empty message case in workflow logic
4. Prompt user to send a message if needed

## Usage Examples

### Example 1: Retrieve Last User Message

**Scenario**: A workflow needs to reference what the user just asked to provide context-aware responses.

**Input**:
```
(No input parameters required)
```

**Output**:
```json
{
  "Message": "What is my account balance?"
}
```

**Result**: The workflow retrieves the user's last question and can use it for logging, analysis, or context.

### Example 2: Confirmation Workflow

**Scenario**: After a user confirms an action, the workflow retrieves the confirmation message.

**Input**:
```
(No input parameters required)
```

**Output**:
```json
{
  "Message": "Yes, proceed with the transfer"
}
```

**Result**: The workflow validates the user's confirmation and proceeds with the requested action.

### Example 3: Empty Message History

**Scenario**: A new session where the user hasn't sent any messages yet.

**Input**:
```
(No input parameters required)
```

**Output**:
```json
{
  "Message": ""
}
```

**Result**: An empty string is returned, indicating no messages are available. The workflow handles this gracefully.

### Example 4: Command Processing

**Scenario**: A workflow needs to process the user's command for analytics or logging.

**Input**:
```
(No input parameters required)
```

**Output**:
```json
{
  "Message": "Help"
}
```

**Result**: The workflow logs the command and provides appropriate help information.

## Business Rules

1. **User Messages Only**: Only retrieves messages sent by the user, not Krista's responses
2. **Most Recent Message**: Always returns the chronologically last message sent by the user
3. **Session Scoped**: Only searches within the current active session
4. **Text Content Only**: Returns the text content of the message, not metadata or attachments
5. **Empty String Default**: Returns empty string if no messages are found (not null or error)
6. **Backward Search**: Searches message history in reverse chronological order for efficiency

## Limitations

1. **Authentication Required**: User must be authenticated through OAuth before calling this request
2. **Active Session Required**: User must have an active WhatsApp session with message history
3. **Text Only**: Does not return media attachments, only text content
4. **No Message Metadata**: Does not return timestamp, message ID, or other metadata
5. **Single Message**: Only returns the last message, not message history
6. **No Filtering**: Cannot filter by message type, date, or other criteria
7. **Performance**: May be slower for users with very long message histories

## Best Practices

### 1. Handle Empty Messages
Always check for empty string responses and handle them appropriately in your workflow logic.

```
If Message is empty:
  Prompt user to send a message
Else:
  Process the message
```

### 2. Use for Context Awareness
Leverage this request to make workflows more context-aware by referencing what the user just said.

### 3. Combine with Session Management
Use in conjunction with session management to ensure you're retrieving messages from the correct session.

### 4. Avoid Excessive Calls
Cache the message value within a workflow execution to avoid repeated calls for the same message.

### 5. Validate Message Content
Always validate and sanitize the message content before using it in business logic or displaying it.

## Common Use Cases

### 1. Confirmation Validation
```
Scenario: User is asked to confirm an action
Action: Retrieve last message to verify confirmation response
Result: Workflow validates "Yes" or "No" and proceeds accordingly
```

### 2. Input Logging
```
Scenario: System needs to log user inputs for audit or analytics
Action: Call "Get Last User Message" after each user interaction
Result: User inputs are logged for compliance and analysis
```

### 3. Context-Aware Responses
```
Scenario: Workflow needs to reference user's question in the response
Action: Retrieve last message and include it in the response
Result: "Regarding your question about [last message], here's the answer..."
```

### 4. Error Recovery
```
Scenario: Workflow encounters an error and needs to show what the user asked
Action: Retrieve last message to display in error message
Result: "Sorry, I couldn't process your request: [last message]"
```

### 5. Intent Verification
```
Scenario: NLP system needs to verify the user's intent
Action: Retrieve last message for intent classification
Result: Message is analyzed and appropriate conversation is triggered
```

## Related Catalog Requests

- [Get Session Id for Active User](pages/GetSessionIdForActiveUser.md) - Retrieve the session ID for the current user

## Technical Implementation

### Helper Class
- **Class**: MediumArea
- **Package**: app.krista.extension.essentials.collaboration.waba.catalog
- **Method**: getLastUserMessage()

### Implementation Details

The catalog request:
1. Retrieves the authorized account from AuthorizationContext
2. Gets the session inbox ID for the user
3. Retrieves the inbox using InboxManager
4. Iterates through messages in reverse chronological order (batches of 20)
5. Finds the first message sent by the user (matching account ID)
6. Converts the message to MessageDto and extracts text content
7. Returns the message text or empty string if no messages found

### Dependencies
- **AuthorizationContext**: Provides the authenticated user's account information
- **MediumService**: Service for retrieving session inbox
- **InboxManager**: Service for accessing inbox and messages
- **MessageDtoFactory**: Factory for converting Message to MessageDto

### Code Flow
```
1. Get authorized account from context
2. Retrieve session inbox ID
3. Get inbox from InboxManager
4. Loop through messages (batches of 20, newest first):
   a. For each message in reverse order:
      - Check if sender matches authorized account
      - If match found, convert to MessageDto
      - Return message text
   b. If no match in batch, get next batch
5. Return empty string if no user messages found
```

### Performance Considerations
- Messages are retrieved in batches of 20 for efficiency
- Search stops as soon as a user message is found
- For users with long message histories, this may take longer
- Consider caching the result within a workflow execution

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

#### Issue: Empty String Returned

**Symptoms**:
- Empty string returned when messages exist
- "No active session found for this user..."

**Quick Solution**:
1. Verify user has actually sent messages (not just received them)
2. Check that the account ID matching logic is correct
3. Review message history in the inbox
4. Ensure session is the correct one for the user

**Detailed Guide**: [Session Errors](pages/ErrorMessages.md#session-errors)

---

#### Issue: Slow Response Time

**Cause**: User has very long message history requiring multiple batch retrievals

**Solution**:
1. Consider caching the result within workflow execution
2. Optimize message retrieval batch size if possible
3. Review session timeout settings to limit history size
4. Consider implementing message history limits

---

#### Issue: Wrong Message Returned

**Cause**: Message history includes messages from multiple users or sessions

**Solution**:
1. Verify session ID is correct for the user
2. Check that account ID matching is working correctly
3. Ensure session isolation is properly configured
4. Review message sender information in logs

---

#### Issue: API Communication Errors

**Symptoms**:
- "Unable to connect to WhatsApp Business API..."
- Cannot access inbox

**Quick Solution**:
1. Verify inbox service is running
2. Check that session is active and valid
3. Ensure user has proper permissions
4. Review system logs for detailed error information

**Detailed Guide**: [API Communication Errors](pages/ErrorMessages.md#api-communication-errors)

## See Also

- [Error Messages and Troubleshooting](pages/ErrorMessages.md) - Comprehensive error message guide
- [Extension Configuration](pages/ExtensionConfiguration.md) - Configure session and messaging settings
- [Authentication](pages/Authentication.md) - OAuth authentication required for this request
- [Get Session Id for Active User](pages/GetSessionIdForActiveUser.md) - Related session operation
- [Workflow](pages/Workflow.md) - Examples of using messages in workflows

