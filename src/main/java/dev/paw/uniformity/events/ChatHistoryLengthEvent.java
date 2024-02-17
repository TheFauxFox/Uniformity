package dev.paw.uniformity.events;

public class ChatHistoryLengthEvent extends Event {
    private int chatLength;

    public ChatHistoryLengthEvent(int current) {
        chatLength = current;
    }

    public int getChatLength() {
        return chatLength;
    }

    public void setChatLength(int chatLength) {
        this.chatLength = chatLength;
    }
}
