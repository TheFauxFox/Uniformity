package org.dizitart.jbus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @since 1.0
 * @author Anindya Chatterjee.
 */
class ReflectionUtil {
    private final static Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);

    static List<ListenerMethod> findSubscribedMethods(Class<?> subscribedClass) {
        List<ListenerMethod> listenerMethodList = new ArrayList<>();
        if (subscribedClass != null) {
            Method[] declaredMethods = subscribedClass.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(Subscribe.class) && !method.isBridge() && !method.isSynthetic()) {
                    if (method.getParameterTypes().length != 1) {
                        logger.error(method.getName() + " has @Subscribe annotation, " +
                                "but it should have exactly 1 parameter.");
                        throw new JBusException(method.getName() + " has @Subscribe annotation, " +
                                "but it should have exactly 1 parameter.");
                    }

                    Class<?> parameterType = method.getParameterTypes()[0];
                    if (parameterType.isArray() || method.isVarArgs()) {
                        logger.error(method.getName() + " has @Subscribe annotation, " +
                                "but its parameter should not be an array or varargs.");
                        throw new JBusException(method.getName() + " has @Subscribe annotation, " +
                                "but its parameter should not be an array or varargs.");
                    }

                    method.setAccessible(true);

                    ListenerMethod listenerMethod = new ListenerMethod(method, method.getParameterTypes()[0]);
                    listenerMethodList.add(listenerMethod);
                }
            }

            if (subscribedClass.getSuperclass() != null && !subscribedClass.getSuperclass().equals(Object.class)) {
                if (logger.isDebugEnabled() && !subscribedClass.getSuperclass().equals(Object.class)) {
                    logger.debug("Super class found. searching for listener methods in super class "
                            + subscribedClass.getSuperclass().getName());
                }
                List<ListenerMethod> subscribedMethods = findSubscribedMethods(subscribedClass.getSuperclass());
                listenerMethodList.addAll(subscribedMethods);
            }

            if (subscribedClass.getInterfaces().length > 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Interface found. searching for listener methods in interfaces.");
                }
                for (Class<?> interfaceClass : subscribedClass.getInterfaces()) {
                    List<ListenerMethod> subscribedMethods = findSubscribedMethods(interfaceClass);
                    listenerMethodList.addAll(subscribedMethods);
                }
            }
        }
        return listenerMethodList;
    }
}
