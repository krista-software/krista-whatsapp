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


import app.krista.extension.essentials.collaboration.waba.request.TextMessageRequestBuilder;
import app.krista.extension.essentials.collaboration.waba.request.parameters.Text;
import com.google.gson.annotations.SerializedName;

public class TextMessageRequestBuilderImpl implements TextMessageRequestBuilder {
    private String to;
    private Text text;
    @SerializedName("preview_url")
    private boolean previewUrl;

    @Override
    public TextMessageRequestBuilder to(String to) {
        this.to = to;
        return this;
    }

    @Override
    public TextMessageRequestBuilder text(String text) {
        this.text = new Text(text);
        return this;
    }

    @Override
    public TextMessageRequestBuilder previewUrl(boolean value) {
        this.previewUrl = value;
        return this;
    }

    @Override
    public TextMessageRequest build() {
        return new TextMessageRequest(to, TEXT_TYPE, text, previewUrl, MESSAGING_PRODUCT, RECIPIENT_TYPE);
    }
}
