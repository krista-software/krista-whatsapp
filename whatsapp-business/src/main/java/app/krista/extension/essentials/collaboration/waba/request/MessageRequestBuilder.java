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

/**
 * @param <B> MessageRequestBuilder represents input
 * @param <R> MessageRequest represents output
 */
public interface MessageRequestBuilder<B extends MessageRequestBuilder<B, R>, R extends MessageRequest> {

    /**
     * currently, through whatsapp you can only send messages to individuals.
     * hence the default value is individual.
     * <a href="https://developers.facebook.com/docs/whatsapp/on-premises/reference/messages#text-messages">...</a>
     */
    String RECIPIENT_TYPE = "individual";

    /**
     * messaging product is whatsapp so default value is whatsapp.
     * <a href="https://developers.facebook.com/docs/whatsapp/on-premises/reference/messages#text-messages">...</a>
     */
    String MESSAGING_PRODUCT = "whatsapp";

    /**
     * @param to WhatsApp ID or phone number for the person you want to send a message to.
     * @return MessageRequestBuilder
     */
    B to(String to);

    R build();
}
