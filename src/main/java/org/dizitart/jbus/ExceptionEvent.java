package org.dizitart.jbus;

/**
 * @since 1.0
 * @author Anindya Chatterjee.
 */
public class ExceptionEvent {
    private final Throwable exception;
    private final ExceptionContext exceptionContext;

    public ExceptionEvent(Throwable exception, ExceptionContext exceptionContext) {
        this.exception = exception;
        this.exceptionContext = exceptionContext;
    }

    @Override
    public String toString() {
        return String.format("[exception = %s, context = %s]", exception, exceptionContext);
    }
}
