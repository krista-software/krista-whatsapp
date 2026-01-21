# WhatsApp Business Extension

## Overview

The Krista WhatsApp Business Extension enables seamless integration between Krista and WhatsApp Business, allowing users to interact with Krista through WhatsApp messaging. This extension leverages the WhatsApp Business Cloud API to provide a conversational interface for accessing Krista's capabilities through one of the world's most popular messaging platforms.

## Key Features

✅ **Real-Time Messaging**: Send and receive messages through WhatsApp Business in real-time
✅ **Session Management**: Automatic session creation and management for WhatsApp users
✅ **OAuth 2.0 Authentication**: Secure authentication using Meta's OAuth 2.0 framework
✅ **Webhook Integration**: Real-time event notifications through WhatsApp webhooks
✅ **NLP Integration**: Natural language processing for intelligent conversation routing
✅ **Complex Field Support**: Support for interactive buttons, forms, and rich media
✅ **Guest User Support**: Allow external users to interact with Krista without full registration
✅ **Fallback Conversations**: Configurable fallback options for unmatched queries
✅ **Session Timeout Management**: Automatic session timeout and cleanup

## Quick Start Guide

### Prerequisites

Before configuring the WhatsApp Business Extension, ensure you have:

1. **Meta Developer Account**: A registered Meta developer account
2. **WhatsApp Business Account**: An active WhatsApp Business account
3. **Business Portfolio**: A Meta business portfolio with complete business details
4. **Krista Workspace**: Admin access to your Krista workspace

### Getting Started Steps

1. **[Create WhatsApp Business App](pages/CreatingWhatsAppBusinessApp.md)**: Set up your WhatsApp Business application in Meta Developer Portal
2. **[Configure Authentication](pages/Authentication.md)**: Set up OAuth 2.0 authentication with Meta
3. **[Configure Extension](pages/ExtensionConfiguration.md)**: Configure the extension in Krista with your WhatsApp Business credentials
4. **[Set Up Webhooks](pages/WhatsAppWebhookConfiguration.md)**: Configure webhooks to receive real-time notifications

## Documentation Structure

### 📋 Getting Started

- **[Extension Configuration](pages/ExtensionConfiguration.md)**: Complete guide to configuring the extension
- **[Authentication](pages/Authentication.md)**: OAuth 2.0 authentication setup and token management
- **[Creating WhatsApp Business App](pages/CreatingWhatsAppBusinessApp.md)**: Step-by-step guide to creating your WhatsApp Business application
- **[WhatsApp Webhook Configuration](pages/WhatsAppWebhookConfiguration.md)**: Configure webhooks for real-time event notifications

### 📨 Catalog Requests

#### Session Management
- **[Get Session Id for Active User](pages/GetSessionIdForActiveUser.md)**: Retrieve the session ID for the currently active user
- **[Get Last User Message](pages/GetLastUserMessage.md)**: Fetch the last message sent by the user to Krista

### 📖 Additional Resources

- **[Error Messages and Troubleshooting](pages/ErrorMessages.md)**: Comprehensive guide to all error messages and their resolutions
- **[Supported Fields](pages/SupportedFields.md)**: Field types supported by the extension
- **[Workflow](pages/Workflow.md)**: Common use cases and workflow examples
- **[Release Notes](pages/ReleaseNotes.md)**: Version history and updates

## Support & Resources

### Extension Information

- **Version**: 1.0.23
- **Ecosystem**: Essentials
- **Domain**: Collaboration
- **Execution Mode**: Live only
- **API**: WhatsApp Business Cloud API

### External Documentation

- [WhatsApp Business Platform Documentation](https://developers.facebook.com/docs/whatsapp)
- [WhatsApp Cloud API Reference](https://developers.facebook.com/docs/whatsapp/cloud-api)
- [Meta OAuth Documentation](https://developers.facebook.com/docs/facebook-login/guides/advanced/manual-flow)
- [Embedded Signup Flow](https://developers.facebook.com/docs/whatsapp/embedded-signup)

### Limitations

⚠️ **Button Limitations**: Maximum of 3 buttons per message
⚠️ **Action Name Length**: Button text must not exceed 20 characters
⚠️ **Execution Mode**: Extension only supports Live mode
⚠️ **Fallback Conversation Mode**: Fallback conversation must match the execution mode (Test/Draft/Live)

## Need Help?

For additional support or questions about the WhatsApp Business Extension, please refer to:

- **[Error Messages and Troubleshooting](pages/ErrorMessages.md)**: Comprehensive guide to all error messages, causes, and resolutions
- Extension configuration troubleshooting sections
- Authentication troubleshooting guide
- Krista support documentation

---

**Ready to get started?** Begin with [Creating WhatsApp Business App](pages/CreatingWhatsAppBusinessApp.md) to set up your WhatsApp Business application.