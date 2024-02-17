package org.dizitart.jbus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Anindya Chatterjee.
 */
class ErrorHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ListenersRegistry listenersRegistry;
    private final EventDispatcher eventDispatcher;

    ErrorHandler(ListenersRegistry listenersRegistry, EventDispatcher eventDispatcher) {
        this.listenersRegistry = listenersRegistry;
        this.eventDispatcher = eventDispatcher;
    }

    void handle(Object event, ListenerMethod listenerMethod, Throwable error) {
        ExceptionContext exceptionContext = new ExceptionContext(listenerMethod.target, event, listenerMethod.method);
        ExceptionEvent exceptionEvent = new ExceptionEvent(error, exceptionContext);

        List<ListenerMethod> errorSubscribers = listenersRegistry.getSubscribers(exceptionEvent);
        if (errorSubscribers != null && !errorSubscribers.isEmpty()) {
            logger.debug("Total error handler found for error {} is = {}", error, errorSubscribers.size());
            logger.info("Dispatching error event {}", exceptionEvent);
            DefaultHandlerChain errorHandlerChain = new DefaultHandlerChain(errorSubscribers);
            eventDispatcher.dispatch(exceptionEvent, errorHandlerChain);
        } else {
            logger.error("No error handler found for {}", error.toString());
        }
    }
}
