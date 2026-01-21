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

import app.krista.extension.essentials.collaboration.waba.request.impl.TextMessageRequest;
import app.krista.extension.essentials.collaboration.waba.request.parameters.Text;
import com.google.gson.Gson;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TextMessageRequestTest {

    @Test
    public void testToString() {
        String to = "recipient@example.com";
        String type = "text";
        Text text = new Text("Hello, World!");
        boolean previewUrl = true;
        String messagingProduct = "product";
        String recipientType = "user";

        TextMessageRequest request = new TextMessageRequest(to, type, text, previewUrl, messagingProduct, recipientType);

        String expectedJson = "{\"to\":\"recipient@example.com\",\"type\":\"text\",\"text\":{\"body\":\"Hello, World!\"},\"preview_url\":true,\"messaging_product\":\"product\",\"recipient_type\":\"user\"}";

        Gson gson = new Gson();
        String actualJson = gson.toJson(request);

        assertEquals(expectedJson, actualJson);
    }
}

