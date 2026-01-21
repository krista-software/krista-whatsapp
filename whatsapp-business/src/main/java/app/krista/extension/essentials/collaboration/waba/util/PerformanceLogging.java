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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@Aspect
public class PerformanceLogging {

    private static final Logger perfLogger = LoggerFactory.getLogger(PerformanceLogging.class);

    @Around("call(public * app.krista.omni.sdk.medium.api.message.MediumMessageReceiver.receiveMessage(..))")
    public Object logPerformanceForReceivedMessage(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return this.logPerformance(proceedingJoinPoint);
    }

    @Around("execution(public * app.krista.extension.essentials.collaboration.waba.client.TextMediumMessageSenderImpl.sendMessages(..))")
    public Object logWhatsAppSendMessage(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return this.logPerformance(proceedingJoinPoint);
    }

    @Around("execution(public * app.krista.extension.essentials.collaboration.waba.listener.InboxMessageListenerImpl.messageReceived(..))")
    public Object logPlatformReceivedMessage(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return this.logPerformance(proceedingJoinPoint);
    }

    private Object logPerformance(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        if (proceedingJoinPoint.getTarget() == null || proceedingJoinPoint.getSignature() == null) {
            return proceedingJoinPoint.proceed();
        }
        perfLogger.warn("{} -> {}, {} (..) In", proceedingJoinPoint.getThis().getClass().getSimpleName(), proceedingJoinPoint.getTarget().getClass().getSimpleName(), proceedingJoinPoint.getSignature().getName());
        try {
            Object res = proceedingJoinPoint.proceed();
            long end = System.currentTimeMillis();
            perfLogger.warn("{} -> {}, {} (..) - Out | {}", proceedingJoinPoint.getThis().getClass().getSimpleName(), proceedingJoinPoint.getTarget().getClass().getSimpleName(), proceedingJoinPoint.getSignature().getName(), (end - start));
            return res;
        } catch (Throwable t) {
            long end = System.currentTimeMillis();
            perfLogger.warn("{} <- {}.{}(..) - Error | {}", proceedingJoinPoint.getThis().getClass().getSimpleName(), proceedingJoinPoint.getTarget().getClass().getSimpleName(), proceedingJoinPoint.getSignature().getName(), end - start);
            throw t;
        }
    }
}
