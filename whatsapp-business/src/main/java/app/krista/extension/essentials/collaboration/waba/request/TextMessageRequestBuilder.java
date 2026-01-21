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

package app.krista.extension.essentials.collaboration.waba.request;

import app.krista.extension.essentials.collaboration.waba.request.impl.TextMessageRequest;

public interface TextMessageRequestBuilder extends MessageRequestBuilder<TextMessageRequestBuilder, TextMessageRequest> {

    /**
     * @param text represents text message.
     * @return MessageRequestBuilder
     */
    TextMessageRequestBuilder text(String text);

    /**
     * By default, the mobile WhatsApp application recognizes URLs and makes them clickable.
     * To include a URL preview, include "preview_url": true in the message body and
     * make sure the URL begins with http:// or https://. A hostname is required, IP addresses are not matched.
     *
     * @param value i.e true/false
     * @return MessageRequestBuilder
     */
    TextMessageRequestBuilder previewUrl(boolean value);

    /**
     * The type of message you want to send. here the type is text
     * <a href="https://developers.facebook.com/docs/whatsapp/on-premises/reference/messages#text-messages">...</a>
     */
    String TEXT_TYPE = "text";
}
