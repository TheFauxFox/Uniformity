package org.dizitart.jbus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.dizitart.jbus.ReflectionUtil.findSubscribedMethods;

/**
 * @since 1.0
 * @author Anindya Chatterjee.
 */
class ListenersRegistry {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<Class<?>, List<ListenerMethod>> registry = new ConcurrentHashMap<>();
    private final List<Object> subscriberCache = new CopyOnWriteArrayList<>();
    private final Object lock = new Object();

    void register(Object listener) {
        synchronized (lock) {
            if (subscriberCache.contains(listener)) {
                logger.error(listener + " has already been registered.");
                throw new JBusException(listener + " has already been registered.");
            }
            subscriberCache.add(listener);
            logger.debug(listener + " added to the subscriber cache.");
        }

        List<ListenerMethod> subscribedMethods = findSubscribedMethods(listener.getClass());
        if (subscribedMethods.isEmpty()) {
            logger.error(listener + " does not have any method marked with @Subscribe.");
            throw new JBusException(listener + " does not have any method marked with @Subscribe.");
        }

        for (ListenerMethod listenerMethod : subscribedMethods) {
            listenerMethod.target = listener;

            Class<?> eventType = listenerMethod.eventType;
            if (registry.containsKey(eventType)) {
                List<ListenerMethod> listenerMethods = registry.get(eventType);

                if (!listenerMethods.contains(listenerMethod)) {
                    listenerMethods.add(listenerMethod);
                    logger.debug(listenerMethod + " has been registered.");
                } else {
                    logger.debug(listenerMethod + " has already been registered.");
                }
            } else {
                List<ListenerMethod> listenerMethods = new CopyOnWriteArrayList<>();
                listenerMethods.add(listenerMethod);
                registry.put(listenerMethod.eventType, listenerMethods);
                logger.debug(listenerMethod + " has been registered.");
            }
        }
    }

    @SuppressWarnings("unused")
    void unregister(Object listener) {
        synchronized (lock) {
            for (Object object : subscriberCache) {
                if (object.equals(listener)) {
                    if (subscriberCache.remove(listener)) {
                        logger.debug(listener + " removed from the subscriber cache.");
                    }
                    break;
                }
            }
        }
        removeFromRegistry(listener);
    }

    List<ListenerMethod> getSubscribers(Object event) {
        if (event != null) {
            Class<?> eventType = event.getClass();
            if (registry.containsKey(eventType)) {
                return registry.get(eventType);
            }
        }
        return null;
    }

    private void removeFromRegistry(Object listener) {
        for (Map.Entry<Class<?>, List<ListenerMethod>> entry : registry.entrySet()) {
            List<ListenerMethod> subscribedMethods = entry.getValue();
            for (ListenerMethod listenerMethod : subscribedMethods) {
                if (listenerMethod.target.equals(listener)) {
                    if (subscribedMethods.remove(listenerMethod)) {
                        logger.debug(listenerMethod + " has been un-registered.");
                    }
                }
            }
        }
    }
}
