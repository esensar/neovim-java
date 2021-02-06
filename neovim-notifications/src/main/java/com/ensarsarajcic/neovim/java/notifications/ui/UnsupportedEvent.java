package com.ensarsarajcic.neovim.java.notifications.ui;

public class UnsupportedEvent implements UiEvent {
    public static final String NAME = "__special_unsupported";

    private final String expectedEventName;

    public UnsupportedEvent(String expectedEventName) {
        this.expectedEventName = expectedEventName;
    }

    public String getExpectedEventName() {
        return expectedEventName;
    }

    @Override
    public String getEventName() {
        return NAME;
    }

    @Override
    public String toString() {
        return "UnsupportedEvent{"
                + "expectedEventName='" + expectedEventName + '\''
                + '}';
    }
}
