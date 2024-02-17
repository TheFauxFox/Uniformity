package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.ShowOperatorTabEvent;
import org.dizitart.jbus.Subscribe;

public class CreativeTools extends Module {
    public CreativeTools() {
        super("CreativeTools");
    }

    @Subscribe
    public void onCreativeToolsCheck(ShowOperatorTabEvent evt) {
        if (!isEnabled()) evt.cancel();
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.creativeToolsToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.creativeToolsToggle = value;
    }
}
