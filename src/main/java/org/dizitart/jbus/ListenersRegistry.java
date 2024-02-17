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
                logger.error("{} has already been registered.", listener);
                throw new JBusException(listener + " has already been registered.");
            }
            subscriberCache.add(listener);
            logger.debug("{} added to the subscriber cache.", listener);
        }

        List<ListenerMethod> subscribedMethods = findSubscribedMethods(listener.getClass());
        if (subscribedMethods.isEmpty()) {
            logger.error("{} does not have any method marked with @Subscribe.", listener);
            throw new JBusException(listener + " does not have any method marked with @Subscribe.");
        }

        for (ListenerMethod listenerMethod : subscribedMethods) {
            listenerMethod.target = listener;

            Class<?> eventType = listenerMethod.eventType;
            if (registry.containsKey(eventType)) {
                List<ListenerMethod> listenerMethods = registry.get(eventType);

                if (!listenerMethods.contains(listenerMethod)) {
                    listenerMethods.add(listenerMethod);
                    logger.debug("{} has been registered.", listenerMethod);
                } else {
                    logger.debug("{} has already been registered.", listenerMethod);
                }
            } else {
                List<ListenerMethod> listenerMethods = new CopyOnWriteArrayList<>();
                listenerMethods.add(listenerMethod);
                registry.put(listenerMethod.eventType, listenerMethods);
                logger.debug("{} has been registered.", listenerMethod);
            }
        }
    }

    @SuppressWarnings("unused")
    void unregister(Object listener) {
        synchronized (lock) {
            for (Object object : subscriberCache) {
                if (object.equals(listener)) {
                    if (subscriberCache.remove(listener)) {
                        logger.debug("{} removed from the subscriber cache.", listener);
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
                        logger.debug("{} has been un-registered.", listenerMethod);
                    }
                }
            }
        }
    }
}
