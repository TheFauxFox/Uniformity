package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.SplashColorEvent;
import dev.paw.uniformity.utils.Color;
import org.dizitart.jbus.Subscribe;

public class DarkLoadingScreen extends Module {
    public DarkLoadingScreen() {
        super("DarkLoadingScreen");
    }

    @Subscribe
    public void onLoadingScreen(SplashColorEvent evt) {
        if (isEnabled()) evt.setColor(Color.hex(Uniformity.config.darkLoadingScreen.loadingScreenHex));
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.darkLoadingScreenToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.darkLoadingScreenToggle = value;
    }
}
