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

package app.krista.extension.essentials.collaboration.waba.request.impl;

import app.krista.extension.essentials.collaboration.waba.request.MessageRequest;
import app.krista.extension.essentials.collaboration.waba.request.parameters.Text;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class TextMessageRequest implements MessageRequest {
    @SerializedName("to")
    private final String to;
    @SerializedName("type")
    private final String type;
    @SerializedName("text")
    private final Text text;
    @SerializedName("preview_url")
    private final boolean previewUrl;
    @SerializedName("messaging_product")
    private final String messagingProduct;
    @SerializedName("recipient_type")
    private final String recipientType;

    public TextMessageRequest(String to, String type, Text text, boolean previewUrl, String messagingProduct, String recipientType) {
        this.to = to;
        this.type = type;
        this.text = text;
        this.previewUrl = previewUrl;
        this.messagingProduct = messagingProduct;
        this.recipientType = recipientType;
    }

    @Override
    public String toString() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
