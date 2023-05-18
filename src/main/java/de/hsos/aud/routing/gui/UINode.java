package de.hsos.aud.routing.gui;

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * UI-Element fuer Knoten im Graphen.
 * 
 * @author Simon Balzer
 * @author Heinz-Josef Eikerling
 */
public final class UINode extends Parent {

    /**
     * Konstruktor
     *
     * @param x X-Position
     * @param y Y-Position
     * @param nodeName Ortsname
     */
    public UINode(double x, double y, String nodeName) {
        this.x = x;
        this.y = y;
        this.name = nodeName;
        draw();
    }
    private final double x;
    private final double y;
    private final String name;
    private boolean highlighted;

    /**
     * Gibt die X-Position zurück.
     *
     * @return
     */
    public double getX() {
        return x;
    }

    /**
     * Gibt die Y-Position zurück.
     *
     * @return
     */
    public double getY() {
        return y;
    }

    /**
     * Gibt den Namen zurück.
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Hebt sich selber und die übergebende ausgehende Kante hervor. Falls keine
     * Existiert wird null zurückgegeben.
     *
     * @param name Zielknoten der ausgehenden Kante
     * @return
     */

    /**
     * Hebt den Knoten hervor.
     */
    public void highlight() {
        this.highlighted = true;
        update();
    }
    /**
     * Hebt den Knoten nichtmehr hervor.
     */
    public void dehighlight() {
        this.highlighted = false;
        update();
    }
    /////////////////////UI//////////////////
    private Circle circle;

    /**
     * Knoten Zeichen
     */
    public void draw() {
        circle = new Circle(10);
        circle.setFill(Color.TRANSPARENT);
        circle.setStrokeWidth(UIConfig.LINE_WIDTH);
        circle.setStroke(Color.BLACK);
        getChildren().add(circle);
    }

    /**
     * Farbverwaltung
     */
    public void update() {
        if (highlighted) {
            circle.setStroke(Color.BLUE);
        } else {
            circle.setStroke(Color.BLACK);
        }
    }

    /**
     * XY-Position im Raum setzen
     *
     * @param x X-Position
     * @param y Y-Position
     */
    public void setXY(double x, double y) {
        setLayoutX(x);
        setLayoutY(y);
    }
}
