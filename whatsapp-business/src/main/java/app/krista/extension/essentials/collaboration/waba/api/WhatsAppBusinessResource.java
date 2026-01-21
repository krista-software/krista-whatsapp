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

package app.krista.extension.essentials.collaboration.waba.api;

import app.krista.extension.essentials.collaboration.waba.request.parameters.Metadata;
import app.krista.extension.essentials.collaboration.waba.request.parameters.Value;
import app.krista.extension.essentials.collaboration.waba.util.ErrorMessages;
import app.krista.extension.essentials.collaboration.waba.util.InvokerParameters;
import app.krista.extension.essentials.collaboration.waba.whatsapp.WhatsAppReceivedMessage;
import app.krista.extension.executor.Invoker;
import app.krista.omni.sdk.medium.api.message.MediumMessageReceiver;
import app.krista.omni.sdk.medium.text.TextMediumMessageIn;
import app.krista.omni.sdk.medium.text.TextMediumService;
import com.kristasoft.common.holders.ThreadLocalProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static app.krista.extension.essentials.collaboration.waba.util.Constants.*;
import static app.krista.omni.sdk.medium.text.util.CommonInvokerParameters.USER_IDENTIFIER;

@Path("/")
public final class WhatsAppBusinessResource {
private static final Logger logger= LoggerFactory.getLogger(WhatsAppBusinessResource.class);
    private final Set<String> processedMessages = new HashSet<>();
    private final ExecutorService executorService;
    private final Invoker invoker;
    private final MediumMessageReceiver receiver;

    @Inject
    public WhatsAppBusinessResource(Invoker invoker) {
        this.invoker = invoker;
        this.receiver = TextMediumService.getInstance().getReceiver();
        this.executorService = Executors.newFixedThreadPool(10);
    }

    @POST
    @Path(ENDPOINT_MESSAGES)
    public Response receiveMessage(Object object) {
        String whatsappMessage = GSON_JSON_MAPPER.toString(object);
        WhatsAppReceivedMessage whatsAppReceivedMessage = GSON_JSON_MAPPER.fromString(whatsappMessage, WhatsAppReceivedMessage.class);
        Map<String, Object> attributes = invoker.getAttributes();
        String invokerUserIdentifier = attributes.get(USER_IDENTIFIER).toString();
        String invokerPhoneNumberId = attributes.get(InvokerParameters.PHONE_NUMBER_ID).toString();

        Metadata metadata = whatsAppReceivedMessage.entry.get(0).changes.get(0).value.metadata;
        // Meta is sending client response to multiple webhooks
        // This filter accept the requests which contains phone number & phone number id
        // same as mentioned in invoker setup
        if (invokerPhoneNumberId.equalsIgnoreCase(metadata.phoneNumberId)
                && invokerUserIdentifier.equalsIgnoreCase(metadata.displayPhoneNumber)) {

            Value value = whatsAppReceivedMessage.entry.get(0).changes.get(0).value;
            if (value.messages != null) {
                String messageId = value.messages.get(0).id;
                try {
                    if (!processedMessages.contains(messageId)) {
                        processedMessages.add(messageId);
                        Map<Class<?>, Object> allContexts = ThreadLocalProxy.getAll();

                        this.executorService.submit(() -> {
                            ThreadLocalProxy.setAll(allContexts);
                            TextMediumMessageIn messageIn = new TextMediumMessageIn(whatsAppReceivedMessage.fromAddress(),
                                    whatsAppReceivedMessage.getText(), "WA");
                            receiver.receiveMessage(messageIn);
                        });
                    }
                } catch (RuntimeException cause) {
                    logger.error(ErrorMessages.messageProcessingError(messageId, cause.getMessage()), cause);
                }
            }
            logger.debug(ErrorMessages.debugMessageProcessed(value.messages.get(0).id));
            return Response.ok().build();
        } else {
            logger.warn(ErrorMessages.webhookWrongInvoker(metadata.displayPhoneNumber, metadata.phoneNumberId));
            return Response.status(403).build();
        }
    }

    @GET
    @Path(ENDPOINT_MESSAGES)
    public Response verify(@QueryParam("hub.mode") String mode, @QueryParam("hub.challenge") String challenge,
                           @QueryParam("hub.verify_token") String verifyToken) {

        if ((mode != null && !mode.isBlank()) && (verifyToken != null && !verifyToken.isBlank())) {
            Map<String, Object> attributes = invoker.getAttributes();
            String token = String.valueOf(attributes.get(VERIFY_TOKEN));
            if (SUBSCRIBE.equals(mode) && verifyToken.equals(token)) {
                return Response.ok(challenge).build();
            }
        }
        return Response.status(403).build();
    }
}
