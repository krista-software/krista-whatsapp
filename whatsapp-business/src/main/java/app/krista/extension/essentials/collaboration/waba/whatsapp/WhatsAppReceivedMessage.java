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

package app.krista.extension.essentials.collaboration.waba.whatsapp;


import app.krista.extension.essentials.collaboration.waba.request.parameters.*;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

// JSON response of whatsapp message example :
/*
{
  "object": "whatsapp_business_account",
  "entry": [
    {
      "id": "114149291599657",
      "changes": [
        {
          "value": {
            "messaging_product": "whatsapp",
            "metadata": {
              "display_phone_number": "15550975514",
              "phone_number_id": "105967012428678"
            },
            "contacts": [
              {
                "profile": {
                  "name": "Harshad Panmand"
                },
                "wa_id": "918468823135"
              }
            ],
            "messages": [
              {
                "context": {
                  "from": "15550975514",
                  "id": "wamid.HBgMOTE4NDY4ODIzMTM1FQIAERgSRDU0OUNDMTc5M0IyMDRDRTY0AA=="
                },
                "from": "918468823135",
                "id": "wamid.HBgMOTE4NDY4ODIzMTM1FQIAEhgUM0VCMDlDRTIxNTAwOTU1NDY0QTIA",
                "timestamp": "1678687358",
                "type": "interactive",
                "interactive": {
                  "type": "list_reply",
                  "list_reply": {
                    "id": "SECTION_1_ROW_1_ID",
                    "title": "SECTION_1_ROW_1_TITLE",
                    "description": "SECTION_1_ROW_1_DESCRIPTION"
                  }
                }
              }
            ]
          },
          "field": "messages"
        }
      ]
    }
  ]
}
 */

public class WhatsAppReceivedMessage {
    @SerializedName("object")
    public String object;
    @SerializedName("entry")
    public List<Entry> entry;

    @Override
    public String toString() {
        return "WhatsAppReceivedMessage{" +
                "object='" + object + '\'' +
                ", entry=" + entry +
                '}';
    }

    public String fromAddress() {
        List<Message> messages = entry.get(0).changes.get(0).value.messages;
        return isMessagePresent(messages) ? messages.get(0).from : null;
    }

    private boolean isMessagePresent(List<Message> messages) {
        return messages != null && !messages.isEmpty() && messages.get(0) != null;
    }

    public String toAddress() {
        return entry.get(0).changes.get(0).value.metadata.displayPhoneNumber;
    }

    public String getText() {
        List<Message> messages = entry.get(0).changes.get(0).value.messages;
        Text text = isMessagePresent(messages) ? messages.get(0).text : null;
        return text != null ? text.getBody() : null;
    }

    public String senderName() {
        return entry.get(0).changes.get(0).value.contacts.get(0).profile.name;
    }


    public String getReplyTitle() {
        List<Message> messages = entry.get(0).changes.get(0).value.messages;
        Interactive interactive = isMessagePresent(messages) ? messages.get(0).interactive : null;
        String replyTitle = null;
        if (interactive != null) {
            replyTitle = !Objects.isNull(interactive.buttonReply) ? interactive.buttonReply.title : interactive.listReply.title;
        }
        return replyTitle;
    }

    public String getReplyId() {
        List<Message> messages = entry.get(0).changes.get(0).value.messages;
        Interactive interactive = isMessagePresent(messages) ? messages.get(0).interactive : null;
        if (interactive == null)
            return null;

        if (interactive.buttonReply != null) {
            return interactive.buttonReply.id;
        } else if (interactive.listReply != null) {
            return interactive.listReply.id;
        }
        return null;
    }

    public Profile getProfile() {
        List<Contact> contacts = entry.get(0).changes.get(0).value.contacts;
        return contacts == null ? null : contacts.get(0).profile;
    }
}

