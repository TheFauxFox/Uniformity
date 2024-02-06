package org.dizitart.jbus;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @since 1.0
 * @author Anindya Chatterjee.
 */
class ListenerMethod {
    Object target;

    Method method;
    Class<?> eventType;

    ListenerMethod(Method method, Class<?> eventType) {
        this.method = method;
        this.eventType = eventType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ListenerMethod other = (ListenerMethod) obj;

        return other.method.getName().equals(method.getName())
                && other.method.getModifiers() != Modifier.PRIVATE
                && method.getModifiers() != Modifier.PRIVATE
                && eventType.equals(other.eventType)
                && target != null
                && other.target != null
                && target.equals(other.target)
                && (target.getClass().isAssignableFrom(other.target.getClass())
                || other.target.getClass().isAssignableFrom(target.getClass()));
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(method)
                .append(target)
                .append(eventType)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "[" +
                "method = " +
                method.getName() +
                ", target = " +
                (target) +
                ", event = " +
                eventType.getName() +
                "]";
    }
}
