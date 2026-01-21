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

package app.krista.extension.essentials.collaboration.waba;

import app.krista.extension.essentials.collaboration.waba.request.parameters.*;
import app.krista.extension.essentials.collaboration.waba.whatsapp.WhatsAppReceivedMessage;
import org.junit.Test;

import static app.krista.extension.essentials.collaboration.waba.util.Constants.GSON_JSON_MAPPER;
import static org.junit.Assert.*;

public class WhatsAppReceivedMessageTest {

    @Test
    public void testFromAddress() {
        WhatsAppReceivedMessage receivedMessage = createWhatsAppReceivedMessage();
        String fromAddress = receivedMessage.fromAddress();

        assertEquals("918468823135", fromAddress);
    }

    @Test
    public void testFromAddress_NoMessages() {
        WhatsAppReceivedMessage receivedMessage = createWhatsAppReceivedMessage();
        String text = receivedMessage.getText();

        assertNull(text);
    }

    @Test
    public void testToAddress() {
        WhatsAppReceivedMessage receivedMessage = createWhatsAppReceivedMessage();
        String toAddress = receivedMessage.toAddress();

        assertEquals("15550975514", toAddress);
    }

    @Test
    public void testGetText() {
        WhatsAppReceivedMessage receivedMessage = createWhatsAppReceivedMessage();
        String text = receivedMessage.getText();

        assertNull(text);
    }

    @Test
    public void testGetText_MessagePresent() {
        WhatsAppReceivedMessage receivedMessage = createWhatsAppReceivedMessage();
        String text = receivedMessage.getText();

        // As whatsApp message is interactive, then there isn't any text message
        assertNull(text);
    }

    @Test
    public void testSenderName() {
        WhatsAppReceivedMessage receivedMessage = createWhatsAppReceivedMessage();
        String senderName = receivedMessage.senderName();

        assertEquals("Harshad Panmand", senderName);
    }

    @Test
    public void testGetReplyTitle() {
        WhatsAppReceivedMessage receivedMessage = createWhatsAppReceivedMessage();
        String replyTitle = receivedMessage.getReplyTitle();

        assertEquals("SECTION_1_ROW_1_TITLE", replyTitle);
    }


    @Test
    public void testGetReplyId() {
        WhatsAppReceivedMessage receivedMessage = createWhatsAppReceivedMessage();
        String replyId = receivedMessage.getReplyId();

        assertEquals("SECTION_1_ROW_1_ID", replyId);
    }

    @Test
    public void testGetProfile() {
        WhatsAppReceivedMessage receivedMessage = createWhatsAppReceivedMessage();
        Profile profile = receivedMessage.getProfile();

        assertNotNull(profile);
        assertEquals("Harshad Panmand", profile.name);
    }


    private WhatsAppReceivedMessage createWhatsAppReceivedMessage() {
        String jsonString =
                "{\n" +
                        "  \"object\": \"whatsapp_business_account\",\n" +
                        "  \"entry\": [\n" +
                        "    {\n" +
                        "      \"id\": \"114149291599657\",\n" +
                        "      \"changes\": [\n" +
                        "        {\n" +
                        "          \"value\": {\n" +
                        "            \"messaging_product\": \"whatsapp\",\n" +
                        "            \"metadata\": {\n" +
                        "              \"display_phone_number\": \"15550975514\",\n" +
                        "              \"phone_number_id\": \"105967012428678\"\n" +
                        "            },\n" +
                        "            \"contacts\": [\n" +
                        "              {\n" +
                        "                \"profile\": {\n" +
                        "                  \"name\": \"Harshad Panmand\"\n" +
                        "                },\n" +
                        "                \"wa_id\": \"918468823135\"\n" +
                        "              }\n" +
                        "            ],\n" +
                        "            \"messages\": [\n" +
                        "              {\n" +
                        "                \"context\": {\n" +
                        "                  \"from\": \"15550975514\",\n" +
                        "                  \"id\": \"wamid.HBgMOTE4NDY4ODIzMTM1FQIAERgSRDU0OUNDMTc5M0IyMDRDRTY0AA==\"\n" +
                        "                },\n" +
                        "                \"from\": \"918468823135\",\n" +
                        "                \"id\": \"wamid.HBgMOTE4NDY4ODIzMTM1FQIAEhgUM0VCMDlDRTIxNTAwOTU1NDY0QTIA\",\n" +
                        "                \"timestamp\": \"1678687358\",\n" +
                        "                \"type\": \"interactive\",\n" +
                        "                \"interactive\": {\n" +
                        "                  \"type\": \"list_reply\",\n" +
                        "                  \"list_reply\": {\n" +
                        "                    \"id\": \"SECTION_1_ROW_1_ID\",\n" +
                        "                    \"title\": \"SECTION_1_ROW_1_TITLE\",\n" +
                        "                    \"description\": \"SECTION_1_ROW_1_DESCRIPTION\"\n" +
                        "                  }\n" +
                        "                }\n" +
                        "              }\n" +
                        "            ]\n" +
                        "          },\n" +
                        "          \"field\": \"messages\"\n" +
                        "        }\n" +
                        "      ]\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}";


        return GSON_JSON_MAPPER.fromString(jsonString, WhatsAppReceivedMessage.class);
    }
}