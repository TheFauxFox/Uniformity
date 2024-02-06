package org.dizitart.jbus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @since 1.0
 * @author Anindya Chatterjee.
 */
@SuppressWarnings("unused")
public class JBus {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ListenersRegistry listenersRegistry;
    private final EventDispatcher eventDispatcher;

    public JBus() {
        listenersRegistry = new ListenersRegistry();
        eventDispatcher = new EventDispatcher(listenersRegistry);
    }

    public void register(Object listener) {
        if (listener == null) {
            logger.error("Null object can not be registered.");
            throw new NullPointerException("Null object can not be registered.");
        }
        logger.info("Registering listener " + listener);
        listenersRegistry.register(listener);
    }

    public void post(Object event) {
        if (event == null) {
            logger.error("Null event posted.");
            throw new NullPointerException("Null event can not be posted.");
        }

        List<ListenerMethod> subscribers = listenersRegistry.getSubscribers(event);
        if (subscribers != null && !subscribers.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Total subscribers found for event " + event + " is = " + subscribers.size());
                logger.debug("Dispatching event " + event);
            }
            DefaultHandlerChain handlerChain = new DefaultHandlerChain(subscribers);
            eventDispatcher.dispatch(event, handlerChain);
        }
    }
}
