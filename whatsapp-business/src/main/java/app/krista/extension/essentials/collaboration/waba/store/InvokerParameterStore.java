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

package app.krista.extension.essentials.collaboration.waba.store;

import app.krista.extension.essentials.collaboration.waba.util.Constants;
import app.krista.extension.essentials.collaboration.waba.util.InvokerParameters;
import app.krista.extension.executor.Invoker;
import app.krista.extensions.util.KeyValueStore;
import app.krista.model.field.util.JavaTypes;

import java.util.Map;
import java.util.Objects;

import static app.krista.extension.essentials.collaboration.waba.util.Constants.GSON_JSON_MAPPER;

public class InvokerParameterStore {

    private final Invoker invoker;
    private final KeyValueStore store;

    private InvokerParameterStore(Invoker invoker, KeyValueStore store) {
        this.invoker = invoker;
        this.store = store;
    }

    public static InvokerParameterStore create(Invoker invoker, KeyValueStore keyValueStore) {
        return new InvokerParameterStore(invoker, keyValueStore);
    }

    public InvokerParameters load(String phoneNumberId) {
        Map<String, Object> attributes = GSON_JSON_MAPPER.fromString(String.valueOf(store.get(toKey(phoneNumberId))),
                JavaTypes.getTypedClass(Map.class, String.class, Object.class));

        if (Objects.isNull(attributes.get(InvokerParameters.REDIRECT_URI))) {
            attributes.put(InvokerParameters.REDIRECT_URI, InvokerParameters.createRedirectUrl(invoker));
        }

        return InvokerParameters.create(attributes);
    }

    private String toKey(String phoneNumberId) {
        return Constants.INV_PARAM_KEY + phoneNumberId;
    }

    public void save(InvokerParameters invokerParameters) {
        store.put(toKey(invokerParameters.getPhoneNumberId()), GSON_JSON_MAPPER.toString(invokerParameters.load()));
    }

    /**
     * Removes given phoneNumber Id id from KeyValueStore
     *
     * @param phoneNumberId phoneNumber id
     */
    public void remove(String phoneNumberId) {
        store.remove(phoneNumberId);
    }

}
