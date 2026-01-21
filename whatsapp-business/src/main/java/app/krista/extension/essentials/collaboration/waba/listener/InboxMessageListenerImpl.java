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

package app.krista.extension.essentials.collaboration.waba.listener;

import app.krista.ksdk.inboxes.Inbox;
import app.krista.ksdk.inboxes.InboxMessageListener;
import app.krista.ksdk.message.Message;
import app.krista.omni.sdk.medium.api.KristaMessageReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.UUID;

public class InboxMessageListenerImpl implements InboxMessageListener {
    private static Logger logger = LoggerFactory.getLogger(InboxMessageListenerImpl.class);
    private static final UUID traceId = UUID.nameUUIDFromBytes(InboxMessageListenerImpl.class.getName().getBytes());
    private final KristaMessageReceiver kristaMessageReceiver;

    public InboxMessageListenerImpl() {
        this.kristaMessageReceiver = KristaMessageReceiver.getInstance();
    }

    @Override
    public void messageReceived(Inbox inbox, Message message) {
        String logMessage = MessageFormat.format("messageID: {0}, listener inboxId: {1}, Message Type: {2}",
                message.getMessageId(), inbox.getInboxId(), message.getClass());

        logger.info("TraceId: " + traceId + " | " + this.getClass().getName() + " | "
                + Thread.currentThread().getStackTrace()[1].getMethodName() + " | " + logMessage);
        kristaMessageReceiver.messageReceived(message);
    }
}
