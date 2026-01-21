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

package app.krista.extension.essentials.collaboration.waba.client;

import com.google.gson.annotations.SerializedName;

public class AccessToken {
    private Error error;
    @SerializedName("access_token")
    public String access_Token;
    @SerializedName("token_type")
    public String tokenType;
    @SerializedName("expires_in")
    public String expiresIn;

    public static class Error {
        private final String message;
        private final String type;
        private final String code;
        private final String errorSubCode;
        private final String fbTraceId;

        public Error(String message, String type, String code, String errorSubCode, String fbTraceId) {
            this.message = message;
            this.type = type;
            this.code = code;
            this.errorSubCode = errorSubCode;
            this.fbTraceId = fbTraceId;
        }

        public String getMessage() {
            return message;
        }

        public String getType() {
            return type;
        }

        public String getCode() {
            return code;
        }

        public String getErrorSubCode() {
            return errorSubCode;
        }

        public String getFbTraceId() {
            return fbTraceId;
        }
    }

    public Error getError() {
        return error;
    }
}
