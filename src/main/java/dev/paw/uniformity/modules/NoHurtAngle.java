package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.ClientPlayerEvent;
import org.dizitart.jbus.Subscribe;

public class NoHurtAngle extends Module {
    public NoHurtAngle() {
        super("NoHurtAngle");
    }

    @Subscribe
    public void onDamageTilt(ClientPlayerEvent.OnDamageTilt evt) {
        if (isEnabled()) evt.cancel();
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.noHurtAngleToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.noHurtAngleToggle = value;
    }
}
