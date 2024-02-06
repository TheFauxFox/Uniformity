package org.dizitart.jbus;

import java.lang.reflect.Method;

/**
 * @author Anindya Chatterjee.
 * @since 1.0
 */
public record ExceptionContext(Object listener, Object event, Method subscribedMethod) {
    @Override
    public String toString() {
        return String.format("[method = %s, listener = %s, event = %s]", subscribedMethod, listener, event);
    }
}
