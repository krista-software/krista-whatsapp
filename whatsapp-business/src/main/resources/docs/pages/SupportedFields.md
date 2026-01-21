# Supported Fields

## Overview

The WhatsApp Business Extension supports various field types for configuration and user interactions. These fields enable rich, interactive conversations through WhatsApp's messaging interface.

## Configuration Fields

The following field types are supported for extension configuration:

### Text Field
- **Description**: Single-line text input
- **Use Cases**:
  - User Identifier (phone number)
  - Phone Number ID
  - Client ID
  - Welcome Conversation name
  - Guest Role name
  - Fallback Conversation name
- **Validation**: String validation, length limits
- **Example**: "+1234567890"

### Secured Text Field
- **Description**: Encrypted text field for sensitive data
- **Use Cases**:
  - Client Secret
  - Verify Token
  - Access tokens
- **Security**: Encrypted at rest, never displayed after entry
- **Example**: "abc123def456..."

### Number Field
- **Description**: Numeric input field
- **Use Cases**:
  - Session Timeout in Minutes
  - Configuration values
- **Validation**: Numeric validation, range limits
- **Example**: 30

### JSON Field
- **Description**: JSON-formatted text field
- **Use Cases**:
  - Configuration JSON (title, icon)
  - Complex configuration objects
- **Validation**: Valid JSON format
- **Example**: `{"title": "Krista", "icon": "https://..."}`

## Interactive Message Fields

The following field types are supported in WhatsApp messages:

### Text
- **Description**: Plain text messages
- **Character Limit**: 4096 characters per message
- **Formatting**: Supports WhatsApp text formatting (bold, italic, strikethrough)
- **Use Cases**:
  - Conversation responses
  - Information display
  - User prompts

### Pick One (Radio Buttons)
- **Description**: Single selection from a list of options
- **Display**: Interactive buttons in WhatsApp
- **Limitations**:
  - Maximum 3 buttons per message
  - Button text limited to 20 characters
- **Use Cases**:
  - Yes/No questions
  - Single choice selections
  - Action confirmations

### Pick List (List Messages)
- **Description**: Single selection from a longer list
- **Display**: Interactive list in WhatsApp
- **Limitations**:
  - Maximum 10 items per list
  - Section headers supported
- **Use Cases**:
  - Menu selections
  - Category choices
  - Product selections

## Field Limitations

### WhatsApp Platform Limitations

| Field Type | Limitation | Details |
|------------|------------|---------|
| Text | 4096 characters | Maximum message length |
| Buttons | 3 buttons max | Per message limitation |
| Button Text | 20 characters | Maximum button label length |
| List Items | 10 items max | Per list message |
| List Title | 60 characters | Maximum list title length |
| List Description | 72 characters | Maximum list description |

### Extension-Specific Limitations

| Configuration Field | Limitation | Details |
|---------------------|------------|---------|
| Phone Number ID | Required | Must be valid WhatsApp Business phone number ID |
| Client ID | Required | Must match Meta app ID |
| Client Secret | Required | Must match Meta app secret |
| Verify Token | Required | Custom token for webhook verification |
| Welcome Conversation | Required | Must exist in workspace |
| Session Timeout | 1-1440 minutes | Range: 1 minute to 24 hours |

## Field Validation

### Configuration Field Validation

All configuration fields are validated during the "Validate Attributes" step:

1. **Required Fields**: Must not be empty
2. **Format Validation**: Must match expected format (e.g., JSON)
3. **Existence Validation**: Referenced resources must exist (e.g., conversations)
4. **Connection Validation**: Credentials must be valid and working

### Interactive Field Validation

Interactive fields in messages are validated by WhatsApp:

1. **Button Count**: Enforced by WhatsApp API
2. **Text Length**: Enforced by WhatsApp API
3. **Format**: Must conform to WhatsApp message structure

## Best Practices

### Configuration Fields

1. **Use Strong Tokens**: Generate secure, random values for Verify Token
2. **Validate Before Saving**: Always use "Validate Attributes" before saving
3. **Document Values**: Keep a secure record of configuration values
4. **Test Thoroughly**: Test configuration in development before production

### Interactive Fields

1. **Keep Button Text Short**: Use concise labels (under 20 characters)
2. **Limit Button Count**: Use 3 or fewer buttons per message
3. **Use Lists for Many Options**: Use list messages for 4+ options
4. **Provide Clear Labels**: Make button/list labels descriptive and clear

## Unsupported Fields

The following field types are **not supported** by WhatsApp Business:

- ❌ File Upload (in messages - use media messages instead)
- ❌ Date Picker (use text input with date format)
- ❌ Time Picker (use text input with time format)
- ❌ Multi-Select (use multiple Pick One messages)
- ❌ Rich Text Editor (use plain text with WhatsApp formatting)
- ❌ Embedded Forms (use conversational flow instead)

## Workarounds for Unsupported Fields

### Date/Time Input
Use text input with format instructions:
```
Please enter the date in format: MM/DD/YYYY
Example: 12/25/2024
```

### Multi-Select
Use multiple sequential Pick One messages:
```
Message 1: "Select your first preference"
Message 2: "Select your second preference"
```

### File Upload
Use WhatsApp media messages:
- Images: JPEG, PNG
- Documents: PDF, DOCX, etc.
- Videos: MP4
- Audio: MP3, OGG

## See Also

- [Extension Configuration](pages/ExtensionConfiguration.md) - Configure extension fields
- [Workflow](pages/Workflow.md) - Examples of using fields in workflows
- [WhatsApp Message Types](https://developers.facebook.com/docs/whatsapp/cloud-api/messages) - Official WhatsApp documentation
