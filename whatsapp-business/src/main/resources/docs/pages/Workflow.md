# Workflow

## Overview

This document provides common use cases and workflow examples for the WhatsApp Business Extension. These examples demonstrate how users interact with Krista through WhatsApp and how the extension handles various scenarios.

## Getting Started Workflow

### How to Start a Conversation

1. **User Initiates Contact**
   - User sends a message to the WhatsApp Business number
   - Message can be any text query or greeting

2. **Session Creation**
   - Extension automatically creates a new session for the user
   - Session is associated with the user's phone number
   - Welcome Conversation is triggered (if configured)

3. **NLP Processing**
   - User's message is processed by Krista's Natural Language Processing (NLP)
   - NLP identifies the user's intent
   - Matching conversations are found based on intent

4. **Response Delivery**
   - Krista sends appropriate response through WhatsApp
   - Interactive buttons or lists may be presented
   - User can select from available options

### Example: First-Time User

```
User: "Hello"
↓
System: Creates new session
↓
System: Triggers Welcome Conversation
↓
Krista: "Welcome to Krista! How can I help you today?"
        [Button: Account Info]
        [Button: Support]
        [Button: Help]
↓
User: Clicks "Account Info"
↓
Krista: Executes Account Info conversation
```

## Common User Interactions

### Query and Response Flow

**Scenario**: User asks a question

```
User: "What is my account balance?"
↓
NLP: Identifies "account balance" intent
↓
System: Finds matching conversation
↓
Krista: Executes conversation to retrieve balance
↓
Krista: "Your current account balance is $1,234.56"
```

### Selection from Options

**Scenario**: User selects from multiple options

```
Krista: "What would you like to do?"
        [Button: Check Balance]
        [Button: Transfer Money]
        [Button: View History]
↓
User: Clicks "Check Balance"
↓
Krista: Executes balance check conversation
↓
Krista: "Your balance is $1,234.56"
```

### Handling Incorrect Input

**Scenario**: User provides invalid input

```
Krista: "Please enter the amount to transfer:"
↓
User: "abc"
↓
System: Validates input (fails)
↓
Krista: "Invalid amount. Please enter a numeric value."
↓
User: "100"
↓
System: Validates input (succeeds)
↓
Krista: Proceeds with transfer
```

### Canceling a Flow

**Scenario**: User wants to cancel current operation

```
Krista: "Please enter the recipient's account number:"
↓
User: "Exit"
↓
System: Detects cancellation keyword
↓
Krista: "Operation cancelled. How else can I help you?"
```

## Agent Handoff Workflow

### Customer Support Escalation

**Scenario**: User needs to talk to a human agent

```
User: "I need to speak with someone"
↓
NLP: Identifies agent request intent
↓
System: Creates agent request
↓
System: Routes request to appropriate queue
↓
Krista: "Connecting you with an agent. Please wait..."
↓
System: Notifies available agents
↓
Agent: Accepts the task
↓
Krista: "Agent [Name] has joined the conversation"
↓
Agent ↔ User: Direct conversation
↓
Agent: Completes task
↓
Krista: "Agent [Name] has left the conversation. Is there anything else I can help with?"
```

### Detailed Agent Workflow

1. **Request Creation**
   - User requests agent assistance
   - Session creates an agent request
   - Request is sent to Router

2. **Queue Assignment**
   - Router sends request to appropriate Queue
   - Queue is determined by:
     - User's query type
     - Agent availability
     - Business rules

3. **Task Distribution**
   - Queue creates a Task
   - Task is added to Task List for eligible agents
   - Agents receive notification in Krista Web Client

4. **Agent Acceptance**
   - Agent accepts the task
   - Task is removed from other agents' lists
   - Agent is connected to user's session

5. **Conversation**
   - Agent communicates with user through WhatsApp
   - Agent has access to conversation history
   - Agent can use Krista tools to assist user

6. **Task Completion**
   - Agent completes the task
   - Agent leaves the conversation
   - Queue is notified of completion
   - Task is removed from Task List

7. **Session Continuation**
   - User can continue with automated conversations
   - Or request another agent if needed
   - Session remains active until timeout

## Session Management Workflow

### Session Lifecycle

```
User sends first message
↓
New session created
↓
Welcome Conversation triggered
↓
User interacts with Krista
↓
Session remains active
↓
[After timeout period]
↓
Session expires
↓
User sends new message
↓
New session created
```

### Session Timeout Handling

**Scenario**: User's session times out

```
User: Active conversation
↓
[30 minutes of inactivity]
↓
System: Session timeout triggered
↓
System: Session marked as expired
↓
User: Sends new message
↓
System: Creates new session
↓
Krista: "Welcome back! How can I help you?"
```

## NLP and Fallback Workflow

### Successful NLP Match

```
User: "Show me my recent transactions"
↓
NLP: Analyzes message
↓
NLP: Finds matching intent (confidence > threshold)
↓
System: Executes matched conversation
↓
Krista: Displays transaction history
```

### No NLP Match - Fallback

```
User: "xyz123"
↓
NLP: Analyzes message
↓
NLP: No matching intent found
↓
System: Triggers Fallback Conversation
↓
Krista: "I'm not sure I understand. Here are some things I can help with:"
        [Button: Account Info]
        [Button: Support]
        [Button: Help]
```

### No NLP Match - No Fallback

```
User: "xyz123"
↓
NLP: Analyzes message
↓
NLP: No matching intent found
↓
System: No Fallback Conversation configured
↓
Krista: Triggers Welcome Conversation
```

## Complex Workflow Example

### Multi-Step Transaction

**Scenario**: User wants to transfer money

```
1. User: "I want to transfer money"
   ↓
   NLP: Identifies transfer intent
   ↓
   Krista: "Transfer Money"
           "Please enter the recipient's account number:"

2. User: "1234567890"
   ↓
   System: Validates account number
   ↓
   Krista: "Please enter the amount to transfer:"

3. User: "100"
   ↓
   System: Validates amount
   ↓
   Krista: "Transfer $100 to account 1234567890?"
           [Button: Confirm]
           [Button: Cancel]

4. User: Clicks "Confirm"
   ↓
   System: Processes transfer
   ↓
   Krista: "Transfer successful! Your new balance is $1,134.56"
           "Transaction ID: TXN123456"

5. User: "Thank you"
   ↓
   Krista: "You're welcome! Anything else I can help with?"
```

## Error Handling Workflows

### Authentication Error

```
User: Sends message
↓
System: Checks authentication
↓
System: Token expired
↓
System: Attempts token refresh
↓
[If refresh fails]
↓
System: Requires re-authentication
↓
Admin: Must re-authenticate extension
```

### System Error

```
User: Requests information
↓
System: Queries database
↓
Database: Connection timeout
↓
System: Catches error
↓
Krista: "I'm having trouble accessing that information right now. Please try again in a moment."
```

### Validation Error

```
User: Enters invalid data
↓
System: Validates input
↓
System: Validation fails
↓
Krista: "Invalid input. [Specific error message]"
        "Please try again."
↓
User: Enters corrected data
↓
System: Validation succeeds
↓
Krista: Continues workflow
```

## Best Practices

### For Users

1. **Be Clear and Specific**: Use clear language in your queries
2. **Follow Prompts**: Respond to Krista's questions appropriately
3. **Use Buttons**: Click buttons instead of typing when available
4. **Type "Exit"**: Cancel operations by typing "Exit" or "Cancel"
5. **Request Help**: Type "Help" or "Support" to get assistance

### For Administrators

1. **Configure Welcome Conversation**: Provide clear initial guidance
2. **Set Up Fallback**: Handle unmatched queries gracefully
3. **Test Workflows**: Thoroughly test all conversation flows
4. **Monitor Sessions**: Review session timeout settings
5. **Train NLP**: Improve NLP matching with training data
6. **Document Flows**: Maintain documentation of conversation flows

### For Developers

1. **Handle Errors Gracefully**: Provide clear error messages
2. **Validate Input**: Always validate user input
3. **Implement Timeouts**: Set appropriate timeout values
4. **Log Interactions**: Log for debugging and analytics
5. **Test Edge Cases**: Test unusual scenarios and inputs
6. **Optimize Performance**: Ensure quick response times

## Troubleshooting Common Workflow Issues

### Issue: User Not Receiving Responses

**Possible Causes**:
- Webhook not configured
- Session expired
- System error

**Resolution**:
1. Verify webhook configuration
2. Check system logs
3. Have user send new message to create new session

### Issue: Wrong Conversation Triggered

**Possible Causes**:
- NLP misidentification
- Multiple matching intents

**Resolution**:
1. Review NLP training data
2. Improve intent specificity
3. Add more training examples

### Issue: Session Expires Too Quickly

**Possible Causes**:
- Session timeout too short
- User taking longer than expected

**Resolution**:
1. Increase session timeout value
2. Send periodic reminders to keep session active
3. Implement session extension logic

## See Also

- [Extension Configuration](pages/ExtensionConfiguration.md) - Configure sessions and conversations
- [Get Session Id for Active User](pages/GetSessionIdForActiveUser.md) - Session management
- [Get Last User Message](pages/GetLastUserMessage.md) - Message retrieval
- [Supported Fields](pages/SupportedFields.md) - Field types and limitations