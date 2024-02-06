package org.dizitart.jbus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @since 1.0
 * @author Anindya Chatterjee.
 */
class EventDispatcher {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ErrorHandler errorHandler;

    EventDispatcher(ListenersRegistry listenersRegistry) {
        errorHandler = new ErrorHandler(listenersRegistry, this);
    }

    void dispatch(Object event, DefaultHandlerChain handlerChain) {
        for (ListenerMethod listenerMethod : handlerChain) {
            if (!handlerChain.interrupt) {
                logger.debug("Executing listener " + listenerMethod);
                dispatchSingle(event, listenerMethod);
            }
        }
    }

    private void dispatchSingle(Object event, ListenerMethod listenerMethod) {
        try {
            listenerMethod.method.invoke(listenerMethod.target, event);
        } catch (Exception e) {
            if (e.getCause() != null) {
                logger.error("Error occurred while invoking " + listenerMethod, e.getCause());
                errorHandler.handle(event, listenerMethod, e.getCause());
            } else {
                logger.error("Error occurred while invoking " + listenerMethod, e);
                errorHandler.handle(event, listenerMethod, e);
            }
        }
    }
}
