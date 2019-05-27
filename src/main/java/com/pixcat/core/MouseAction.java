package com.pixcat.core;

import org.joml.Vector3f;

public class MouseAction {

    public enum Button {
        LEFT,
        RIGHT,
        NONE
    }

    public enum Event {
        PRESS,
        RELEASE,
        NONE
    }

    private Button button;
    private Event event;
    private double x;
    private double y;

    public MouseAction() {
        button = Button.NONE;
        event = Event.NONE;
        x = y = -1.0;
    }

    public MouseAction(Button button, Event event, double x, double y) {
        this.button = button;
        this.event = event;
        this.x = x;
        this.y = y;
    }

    public Button getButton() {
        return button;
    }

    public Event getEvent() {
        return event;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void translateCoords(Vector3f offset) {
        this.x -= offset.x;
        this.y -= offset.y;
    }
}
