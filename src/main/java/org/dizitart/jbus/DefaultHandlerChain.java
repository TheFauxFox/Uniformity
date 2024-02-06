package org.dizitart.jbus;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class DefaultHandlerChain extends CopyOnWriteArrayList<ListenerMethod> {
    volatile boolean interrupt;

    DefaultHandlerChain(List<ListenerMethod> subscribers) {
        super(subscribers);
    }
}
