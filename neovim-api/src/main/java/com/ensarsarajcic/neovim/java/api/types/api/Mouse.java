package com.ensarsarajcic.neovim.java.api.types.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Mouse {
    private Mouse() {
        //no instance
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public enum Button {
        LEFT("left"),
        RIGHT("right"),
        MIDDLE("middle"),
        WHEEL("wheel");


        private static final Logger log = LoggerFactory.getLogger(Button.class);

        private final String value;

        @JsonCreator
        public static Button fromString(String value) {
            for (var button : values()) {
                if (button.value.equals(value)) {
                    return button;
                }
            }

            log.error("Tried to create an invalid mouse button ({})", value);
            throw new IllegalArgumentException(String.format("Mouse.Button (%s) does not exist", value));
        }

        Button(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Button{" + "value='" + value + '\'' + '}';
        }
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public enum Action {
        PRESS("press"),
        DRAG("drag"),
        RELEASE("release"),
        UP("up"),
        DOWN("down"),
        LEFT("left"),
        RIGHT("right");


        private static final Logger log = LoggerFactory.getLogger(Action.class);

        private final String value;

        @JsonCreator
        public static Action fromString(String value) {
            for (var action : values()) {
                if (action.value.equals(value)) {
                    return action;
                }
            }

            log.error("Tried to create an invalid mouse action ({})", value);
            throw new IllegalArgumentException(String.format("Mouse.Action (%s) does not exist", value));
        }

        Action(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Action{" + "value='" + value + '\'' + '}';
        }
    }
}
