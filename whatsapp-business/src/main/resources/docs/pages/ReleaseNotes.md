# Release Notes

## WhatsApp Business Extension

### Version 1.0.23

**Release Date**: 2024

**Extension Information**:
- **Ecosystem**: Essentials
- **Domain**: Collaboration
- **Execution Mode**: Live only
- **Java Version**: Java 21

**Dependencies**:
- **Release Calendar Month**: December 2025
- **Krista Service APIs Java**: 1.0.118
- **Global Catalog Version**: GC-2025.12.2
- **Java Version**: 21
- **Gradle Version**: 8.10


#### What's New

- ✅ Enhanced documentation with comprehensive guides
- ✅ Improved error handling and validation
- ✅ Updated OAuth 2.0 authentication flow
- ✅ Enhanced session management
- ✅ Improved webhook configuration

#### Improvements

- Better error messages for configuration validation
- Enhanced security for credential storage
- Improved session timeout handling
- Better NLP integration
- Enhanced agent handoff workflow

#### Bug Fixes

- Fixed session cleanup on extension reload
- Improved token expiration handling
- Enhanced webhook signature validation
- Fixed conversation name validation

---

### Version 1.0.22

**Release Date**: 2024

**Dependencies**:
- **Krista Service APIs Java**: 1.0.112
- **Global Catalog Version**: 1.0.55
- **Appliance Release Version**: 3.4.4

#### Features

- Initial release of WhatsApp Business Extension
- OAuth 2.0 authentication support
- Session management
- Webhook integration
- NLP-based conversation routing
- Agent handoff support
- Complex field support (buttons, lists)

---

## Known Issues

### KR-12472: NLP Search Performance
**Issue**: For Omni-channel extensions, NLP search may take approximately 20 seconds to display results

**Impact**: Users may experience delays when searching for conversations

**Workaround**: 
- Use specific keywords to narrow search results
- Pre-configure common conversations for quick access
- Consider caching frequently accessed conversations

**Status**: Under investigation

---

## Limitations and Caveats

### Execution Mode Limitations

1. **Live Mode Only**
   - Extension only supports Live execution mode
   - Test Mode and Draft Mode are not supported
   - All conversations must be in Live mode

2. **Fallback Conversation Mode Matching**
   - Fallback conversation must match the execution mode
   - For Test Mode configuration, fallback must be in Test mode
   - For Draft Mode configuration, fallback must be in Draft mode
   - For Live Mode configuration, fallback must be in Live mode

### Agent Features

1. **Agent Handoff**
   - "Talk to an agent" feature works only in Live mode
   - Agent must be available in Krista Web Client
   - Queue configuration required for agent routing

### WhatsApp Platform Limitations

1. **Button Limitations**
   - Maximum 3 buttons per message
   - Button text limited to 20 characters
   - Cannot use more than 3 buttons in a single message

2. **Message Limitations**
   - Text messages limited to 4096 characters
   - List messages limited to 10 items
   - Media file size limits apply

3. **Rate Limits**
   - Test phone number: Limited to 5 recipients
   - Unverified business: 250 conversations per 24 hours
   - Verified business: 1,000+ conversations per 24 hours

### Authentication Limitations

1. **Token Expiration**
   - Access tokens expire after 60 days
   - Manual re-authentication required after expiration
   - Automatic token refresh not implemented

2. **OAuth Requirements**
   - Requires Meta Developer account
   - Requires WhatsApp Business account
   - Requires business verification for production use

### Session Management

1. **Session Timeout**
   - Configurable timeout (1-1440 minutes)
   - Sessions automatically expire after inactivity
   - New session created on next user message

2. **Session Cleanup**
   - Automatic cleanup on extension reload
   - Historical sessions are cleared
   - Active sessions are preserved

---

## Upgrade Notes

### Upgrading from 1.0.22 to 1.0.23

1. **Backup Configuration**
   - Export current extension configuration
   - Save Client ID, Client Secret, and Verify Token
   - Document conversation names and settings

2. **Update Extension**
   - Install new extension version
   - Reconfigure extension with saved settings
   - Validate attributes to ensure connectivity

3. **Test Functionality**
   - Send test messages to verify webhook
   - Test NLP conversation routing
   - Verify agent handoff (if used)
   - Check session management

4. **Review Documentation**
   - Review updated documentation
   - Update internal procedures if needed
   - Train users on any new features

---

## Deprecation Notices

No features are currently deprecated.

---

## Future Enhancements

### Planned Features

- 🔄 Automatic token refresh
- 📊 Enhanced analytics and reporting
- 🔔 Improved notification handling
- 🌐 Multi-language support
- 📱 Rich media support enhancements
- 🤖 Advanced NLP training tools

### Under Consideration

- Support for Test and Draft modes
- Enhanced agent dashboard integration
- Custom webhook event handling
- Advanced session management features
- Integration with additional Meta products

---

## Support and Resources

### Documentation

- [Extension Configuration](pages/ExtensionConfiguration.md)
- [Authentication](pages/Authentication.md)
- [Creating WhatsApp Business App](pages/CreatingWhatsAppBusinessApp.md)
- [WhatsApp Webhook Configuration](pages/WhatsAppWebhookConfiguration.md)
- [Workflow](pages/Workflow.md)

### External Resources

- [WhatsApp Business Platform](https://developers.facebook.com/docs/whatsapp)
- [WhatsApp Cloud API](https://developers.facebook.com/docs/whatsapp/cloud-api)
- [Meta OAuth Documentation](https://developers.facebook.com/docs/facebook-login)
- [Meta Developer Portal](https://developers.facebook.com/)

### Getting Help

For support with the WhatsApp Business Extension:

1. Review the documentation
2. Check troubleshooting sections
3. Review Meta Developer Portal logs
4. Contact Krista support

---

## Version History

| Version | Release Date | Key Features |
|---------|--------------|--------------|
| 1.0.23 | 2024 | Enhanced documentation, improved error handling |
| 1.0.22 | 2024 | Initial release with core features |

---

**Last Updated**: 2024-11-28

