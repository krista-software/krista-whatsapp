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

import app.krista.extension.essentials.collaboration.waba.store.InvokerParameterStore;
import app.krista.extension.essentials.collaboration.waba.util.Constants;
import app.krista.extension.essentials.collaboration.waba.util.InvokerParameters;
import app.krista.extension.executor.Invoker;
import app.krista.extension.request.RoutingInfo;
import app.krista.extension.request.protos.http.HttpProtocol;
import app.krista.extensions.util.KeyValueStore;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class InvokerParameterStoreTest {

    private static Invoker mockInvoker;

    private KeyValueStore mockKeyValueStore;

    private InvokerParameters mockInvokerParameters;
    private RoutingInfo mockRoutingInfo;

    private InvokerParameterStore parameterStore;

    @Before
    public void setUp() {
        mockInvoker = Mockito.mock(Invoker.class);
        mockKeyValueStore = Mockito.mock(KeyValueStore.class);
        mockInvokerParameters = Mockito.mock(InvokerParameters.class);
        mockRoutingInfo = Mockito.mock(RoutingInfo.class);
        parameterStore = InvokerParameterStore.create(mockInvoker, mockKeyValueStore);
    }

    @Test
    public void testLoad() {
        String phoneNumberId = "123456789";
        String key = Constants.INV_PARAM_KEY + phoneNumberId;
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(InvokerParameters.REDIRECT_URI, "http://example.com");
        when(mockKeyValueStore.get(key)).thenReturn("{'redirectUri':'http://example.com'}");
        when(mockInvokerParameters.getPhoneNumberId()).thenReturn(phoneNumberId);
        when(mockInvokerParameters.load()).thenReturn(attributes);
        when(mockInvoker.getRoutingInfo()).thenReturn(mockRoutingInfo);
        when(mockRoutingInfo.getRoutingURL(HttpProtocol.PROTOCOL_NAME, RoutingInfo.Type.APPLIANCE)).thenReturn("http://example.com");

        InvokerParameters result = parameterStore.load(phoneNumberId);

        Assert.assertNotNull(result);
        Assert.assertEquals("http://example.com/waba/callback", result.getRedirectUri());
        verify(mockKeyValueStore, times(1)).get(key);
        MockedStatic<InvokerParameters> invokerParametersMockedStatic = Mockito.mockStatic(InvokerParameters.class);
        InvokerParameters.createRedirectUrl(mockInvoker);
        invokerParametersMockedStatic.verify(() -> InvokerParameters.createRedirectUrl(mockInvoker), times(1));
    }

    @Test
    public void testRemove() {
        String phoneNumberId = "123456789";
        String key = Constants.INV_PARAM_KEY + phoneNumberId;
        parameterStore.remove(key);
        verify(mockKeyValueStore, times(1)).remove(key);
    }

}