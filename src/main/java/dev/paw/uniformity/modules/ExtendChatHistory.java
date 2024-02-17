package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.ChatHistoryLengthEvent;
import org.dizitart.jbus.Subscribe;

public class ExtendChatHistory extends Module {
    public ExtendChatHistory() {
        super("ChatHistory");
    }

    @Subscribe
    public void onChatHistory(ChatHistoryLengthEvent evt) {
        if (isEnabled()) evt.setChatLength(65535);
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.chatHistoryToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.chatHistoryToggle = value;
    }
}
