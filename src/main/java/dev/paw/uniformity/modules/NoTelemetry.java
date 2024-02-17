package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.TelemetryEvent;
import org.dizitart.jbus.Subscribe;

public class NoTelemetry extends Module {
    public NoTelemetry() {
        super("DisableTelemetry");
    }

    @Subscribe
    public void onTelemetryRequest(TelemetryEvent evt) {
        if (isEnabled()) evt.cancel();
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.disableTelemetryToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.disableTelemetryToggle = value;
    }
}
