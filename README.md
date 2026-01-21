# Whatsapp Extension for Krista

1. [Overview](#overview)
## Overview

The WhatsApp Business Extension is a multi-layered Java application that integrates Krista with WhatsApp Business Cloud API. It follows a clean architecture pattern with clear separation of concerns across presentation, business logic, and data access layers.

### Key Architectural Principles

- **Separation of Concerns**: Clear boundaries between API, business logic, and data layers
- **Dependency Injection**: Uses HK2 for dependency management
- **Stateless Design**: Extension operates in a stateless manner with external state management
- **Event-Driven**: Webhook-based message reception with asynchronous processing
- **OAuth 2.0 Security**: Secure authentication using Meta's OAuth framework

### Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| Language | Java | 21 |
| Framework | JAX-RS (Jersey) | 2.x |
| DI Container | HK2 | 2.x |
| HTTP Client | JAX-RS Client | 2.x |
| JSON Processing | Gson | Custom |
| Logging | SLF4J | 1.x |
| AOP | AspectJ | 1.x |

---



## License

This project is licensed under the **GNU General Public License v3.0**.

```
Whatsapp Extension for Krista
Copyright (C) 2025 Krista Software

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
```

For the full license text, see the [LICENSE](LICENSE) file or visit https://www.gnu.org/licenses/gpl-3.0.html.
