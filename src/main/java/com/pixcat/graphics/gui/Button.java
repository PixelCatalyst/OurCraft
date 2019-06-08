package com.pixcat.graphics.gui;

import com.pixcat.core.MouseAction;
import com.pixcat.mesh.Mesh;

public class Button extends StaticImage {

    public Button(Mesh mesh, int width, int height) {
        super(mesh, width, height);
    }

    public boolean wasTouched(MouseAction action) {
        boolean inLeftTop = (action.getX() >= super.getX()) && (action.getY() >= super.getY());
        boolean inRight = (action.getX() <= (super.getX() + super.getWidth()));
        boolean inBottom = (action.getY() <= (super.getY() + super.getHeight()));
        return inLeftTop && inRight && inBottom;
    }

    public boolean wasClicked(MouseAction action) {
        return wasTouched(action) && action.getEvent() == MouseAction.Event.RELEASE;
    }

    public boolean wasClicked(MouseAction action, MouseAction.Button mouseButton) {
        return wasClicked(action) && action.getButton() == mouseButton;
    }
}
