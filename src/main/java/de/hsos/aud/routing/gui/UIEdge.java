package de.hsos.aud.routing.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;

/**
 * UI-Element fuer Kante im Graphen.
 *
 * @author Simon Balzer
 * @author Heinz-Josef Eikerling
 */
public final class UIEdge extends Parent {

    private final UINode start;
    private final UINode destination;
    private final double distance;
    private boolean blocked;
    private boolean highlighted;

    /**
     * Konstruktor
     *
     * @param from Startknoten
     * @param to Zielknoten
     * @param distance Distanz (Gewichtung)
     * @param blocked Durchfahrtsstatus
     */
    public UIEdge(UINode from, UINode to, double distance, boolean blocked) {
        this.start = from;
        this.destination = to;
        this.distance = distance;
        this.blocked = blocked;
        draw();
        setOnMouseEntered((MouseEvent t) -> {
            hovered = true;
            update();
        });
        setOnMouseExited((MouseEvent t) -> {
            hovered = false;
            update();
        });
        setOnMouseClicked((MouseEvent t) -> {
            toggleBlocked();
            fireEvent(new UIBlockStatusEvent(UIBlockStatusEvent.BLOCKSTATUS, this.start.getName(), this.destination.getName(), this.blocked));
            update();
        });
    }

    /**
     * Wechselt den blocked Status
     */
    public void toggleBlocked() {
        blocked = !blocked;
    }

    /**
     * Kante Hervorheben
     *
     * @return Ziel der Kante zurückgeben
     */
    public UINode highlight() {
        if (blocked) {
            return null;
        } else {
            highlighted = true;
            update();
            return destination;
        }
    }

    /**
     * Kante nichtmehr hervorheben.
     */
    public void dehighlight() {
        this.highlighted = false;
        update();
    }

    ////////////////UI/////////////////
    private boolean hovered;
    private Line center;
    private Polygon arrow;
    private Circle circle;
    private Label label;

    /**
     * Pfeil mit Label Zeichnen sowie Durchfahrtverbotenschild.
     */
    public void draw() {
        int OFFSET = 10;
        //Berechnungen für Länge und diverse Winkel
        double startX = start.getLayoutX();
        double startY = start.getLayoutY();
        double endX = destination.getLayoutX();
        double endY = destination.getLayoutY();

        double dX = endX - startX;
        double dY = endY - startY;

        double length = Math.hypot(Math.abs(dX), Math.abs(dY));

        double wingLength = 15;
        double wingAngle = 35;
        double arrowAngle = Math.toDegrees(Math.atan2(dY, dX));

        double wingLeftX = length + Math.cos(wingAngle) * wingLength;
        double wingLeftY = Math.sin(wingAngle) * wingLength;
        double wingRightX = length + Math.cos(-wingAngle) * wingLength;
        double wingRightY = Math.sin(-wingAngle) * wingLength;

        //Pfeil malen
        center = new Line( length, OFFSET, 0, OFFSET);
        arrow = new Polygon(
                length - 5, OFFSET,
                wingRightX - 5, wingRightY + OFFSET,
                wingLeftX - 5, wingLeftY + OFFSET);

        center.setStrokeWidth(UIConfig.LINE_WIDTH);
        center.setStroke(Color.BLACK);
        arrow.setStroke(Color.BLACK);

        //Durchfahrtverbotenschild
        circle = new Circle(length / 2.5, OFFSET, 12);
        circle.setStroke(Color.RED);
        circle.setFill(Color.WHITE);
        circle.setVisible(blocked);
        circle.setStrokeWidth(3);

        //Distanz-Label
        label = new Label(Double.toString(distance));
        label.setLayoutX(length / 2.5 - 10);
        label.setLayoutY(-7 + OFFSET);
        label.setAlignment(Pos.CENTER);
        label.setFont(Font.font(null, FontWeight.BOLD, 10));
        label.setTextFill(Color.WHITE);
        label.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(3), new Insets(0, -2, 0, -2))));
        label.getTransforms().add(new Rotate(-arrowAngle, 10, 7)); //entgegen der schluss Drehung drehen damit es Horizontal ist

        //drehen und zusammenfügen
        this.getChildren().clear();
        getChildren().addAll(center, arrow, circle, label);
        getTransforms().clear();
        getTransforms().add(new Rotate(arrowAngle, 0, 0));
        setLayoutX(startX);
        setLayoutY(startY);
    }

    /**
     * Farbverhalten
     */
    private void update() {
        circle.setVisible(blocked);
        if (hovered) {
            if (blocked) {
                center.setStroke(Color.CORAL);
                arrow.setStroke(Color.CORAL);
                arrow.setFill(Color.CORAL);
                label.setTextFill(Color.BLACK);
                label.setBackground(Background.EMPTY);
            } else if (highlighted) {
                center.setStroke(Color.LIGHTBLUE);
                arrow.setStroke(Color.LIGHTBLUE);
                arrow.setFill(Color.LIGHTBLUE);
                label.setTextFill(Color.WHITE);
                label.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(3), new Insets(0, -2, 0, -2))));
            } else {
                center.setStroke(Color.DARKSLATEGRAY);
                arrow.setStroke(Color.DARKSLATEGRAY);
                arrow.setFill(Color.DARKSLATEGRAY);
                label.setTextFill(Color.WHITE);
                label.setBackground(new Background(new BackgroundFill(Color.DARKSLATEGRAY, new CornerRadii(3), new Insets(0, -2, 0, -2))));
            }
            setOpacity(0.7);
        } else {
            if (blocked) {
                center.setStroke(Color.RED);
                arrow.setStroke(Color.RED);
                arrow.setFill(Color.RED);
                label.setTextFill(Color.BLACK);
                label.setBackground(Background.EMPTY);
            } else if (highlighted) {
                center.setStroke(Color.BLUE);
                arrow.setStroke(Color.BLUE);
                arrow.setFill(Color.BLUE);
                label.setTextFill(Color.WHITE);
                label.setBackground(new Background(new BackgroundFill(Color.BLUE, new CornerRadii(3), new Insets(0, -2, 0, -2))));
            } else {
                center.setStroke(Color.BLACK);
                arrow.setStroke(Color.BLACK);
                arrow.setFill(Color.BLACK);
                label.setTextFill(Color.WHITE);
                label.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(3), new Insets(0, -2, 0, -2))));
            }
            setOpacity(1);
        }
    }
}
