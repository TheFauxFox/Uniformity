package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.ServerMetadataEvent;
import org.dizitart.jbus.Subscribe;

public class NoChatReportToast extends Module {
    public NoChatReportToast() {
        super("NoChatReportToast");
    }

    @Subscribe
    public void onChatReportToast(ServerMetadataEvent evt) {
        if (isEnabled()) evt.cancel();
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.noChatReportToastToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.noChatReportToastToggle = value;
    }
}
