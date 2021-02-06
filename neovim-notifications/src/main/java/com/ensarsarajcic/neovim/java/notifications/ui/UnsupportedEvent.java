package com.ensarsarajcic.neovim.java.notifications.ui;

public class UnsupportedEvent implements UiEvent {

    private final String expectedEventName;

    public UnsupportedEvent(String expectedEventName) {
        this.expectedEventName = expectedEventName;
    }

    public String getExpectedEventName() {
        return expectedEventName;
    }

    @Override
    public String getEventName() {
        return "__special_unsupported";
    }

    @Override
    public String toString() {
        return "UnsupportedEvent{"
                + "expectedEventName='" + expectedEventName + '\''
                + '}';
    }
}
