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

import app.krista.extension.executor.Invoker;
import app.krista.extension.request.RoutingInfo;
import app.krista.extension.request.protos.http.HttpProtocol;
import app.krista.omni.sdk.medium.text.util.Strings;

import java.util.HashMap;
import java.util.Map;

public class InvokerParameters {
    public static final String PHONE_NUMBER_ID = "Phone Number ID";
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String REDIRECT_URI = "redirect_uri";
    private final Map<String, Object> invokerParametersMap = new HashMap<>();

    private InvokerParameters(Map<String, Object> attributes) {
        invokerParametersMap.putAll(attributes);
    }

    public static InvokerParameters create(Map<String, Object> attributes) {
        return new InvokerParameters(attributes);
    }

    public String getClientId() {
        return (String) invokerParametersMap.get(CLIENT_ID);
    }

    public String getClientSecret() {
        return (String) invokerParametersMap.get(CLIENT_SECRET);
    }

    public String getPhoneNumberId() {
        return (String) invokerParametersMap.get(PHONE_NUMBER_ID);
    }

    public String getRedirectUri() {
        return (String) invokerParametersMap.get(REDIRECT_URI);
    }

    public Map<String, Object> load() {
        return invokerParametersMap;
    }

    public static String createRedirectUrl(Invoker invoker) {
        String redirectUrl = invoker
                .getRoutingInfo()
                .getRoutingURL(HttpProtocol.PROTOCOL_NAME, RoutingInfo.Type.APPLIANCE)
                .concat(Strings.PATH_SEPARATOR)
                .concat(Constants.JAX_RS_ID)
                .concat(Constants.CALLBACK_URI);
        System.out.println("Redirect Url :" + redirectUrl);
        return redirectUrl;//.replace("https://local.eng.krista.app", "https://0685-115-160-209-186.ngrok-free.app");
    }
}
