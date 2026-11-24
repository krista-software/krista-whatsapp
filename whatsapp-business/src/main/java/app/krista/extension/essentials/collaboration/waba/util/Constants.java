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

package app.krista.extension.essentials.collaboration.waba.util;

import app.krista.model.field.util.GsonJsonMapper;

public class Constants {
    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String ACCESS_TOKEN = "Access Token";
    public static final String VERIFY_TOKEN = "Verify Token";
    public static final String SUBSCRIBE = "subscribe";
    public static final String AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";
    public static final String GRAPH_FACEBOOK_URL = "https://graph.facebook.com/";
    public static final String OAUTH_ENDPOINT = "oauth/";
    public static final String GRAPH_FACEBOOK_VERSION = "v16.0/";
    public static final String BEARER = "Bearer ";
    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String ENDPOINT_FORM = "/form";
    public static final String ENDPOINT_ENTITY = "/entities";
    public static final String ENDPOINT_WORKSPACE = "/workspace";
    public static final String ENDPOINT_MESSAGES = "/messages";
    public static final String WHATSAPP = "WhatsApp";
    public static final String TYPE = "type";
    public static final String TEXT = "text";
    public static final String WHATS_APP_BUSINESS = "WhatsApp Business";
    public static final String CODE = "code";
    public static final String STATE = "state";
    public static final String FACEBOOK_DIALOG_URL = "https://www.facebook.com/v16.0/dialog/";
    public static final String JAX_RS_ID = "waba";
    public static final String CALLBACK_URI = "/callback";
    public static final String GRANT_TYPE = "client_credentials";
    public static final String ACCOUNT_ID = "account_id";
    public static final String INV_PARAM_KEY = "Inv_Param_Key_";
    public static final GsonJsonMapper GSON_JSON_MAPPER = GsonJsonMapper.create();
}
