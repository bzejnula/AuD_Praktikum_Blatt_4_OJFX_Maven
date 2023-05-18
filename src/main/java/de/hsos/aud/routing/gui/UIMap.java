package de.hsos.aud.routing.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javax.imageio.ImageIO;

/**
 * UI Komponete für die Kartendarstellung
 *
 * @author Simon Balzer
 */
public class UIMap extends Pane {

    private final Map<String, UINode> nodes;
    private final Map<String, UIEdge> edges;

    private double imageWidth = 100;
    private double imageHeigth = 100;

    /**
     * Anzeige der Karte.
     */
    public UIMap() {
        super();
        nodes = new HashMap<>();
        edges = new HashMap<>();
        heightProperty().addListener(
                (obs, oldVal, newVal) -> {
                    update();
                });
        widthProperty().addListener(
                (obs, oldVal, newVal) -> {
                    update();
                });
    }

    /**
     * Fügt Knoten hinzu
     *
     * @param x X-Position
     * @param y Y-Position
     * @param nodeName Ortsname
     */
    public void addNode(double x, double y, String nodeName) {
        UINode tmp = new UINode(x, y, nodeName);
        tmp.setXY(x, y);
        nodes.put(nodeName, tmp);
        addUIElement(tmp);
    }

    /**
     * Fügt Kante hinzu
     *
     * @param from Start-Knoten
     * @param to Ziel-Knoten
     * @param distance Distanz (Gewichtung)
     * @param blocked Durchfahrtsstatus
     */
    public void addEdge(String from, String to, double distance, boolean blocked) {
        UINode start = nodes.get(from);
        UINode destination = nodes.get(to);
        UIEdge tmp = new UIEdge(start, destination, distance, blocked);
        edges.put(from + to, tmp);
        addUIElement(tmp);
        tmp.toBack();
    }

    /**
     * Hebt einen Pfad hervor.
     *
     * @param route Pfad
     */
    public void highlight(List<String> route) {
        clearHighlights();
        if (route.size() > 1) {
            Iterator<String> iter = route.iterator();
            try {
                String from = iter.next();
                String to = iter.next();
                while (iter.hasNext()) {
                    nodes.get(from).highlight();
                    edges.get(from + to).highlight();
                    from = to;
                    to = iter.next();
                }
                nodes.get(from).highlight();
                edges.get(from + to).highlight();
                nodes.get(to).highlight();
            } catch (NullPointerException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.WARNING, "Fehlerhafte Route", ex);
            }
        }
    }

    /**
     * Löscht die hervorgehobenen Mapelemente.
     */
    public void clearHighlights() {
        nodes.values().forEach(n -> n.dehighlight());
        edges.values().forEach(n -> n.dehighlight());
    }

    /**
     * Gibt die Knoten-Map zurück.
     *
     * @return
     */
    public Map getNodes() {
        return nodes;
    }

    /**
     * Löscht alle Knoten und Kanten.
     */
    public void clear() {
        this.getChildren().clear();
    }

    /**
     * Setzt eine neue Karte als Hintergrund.
     *
     * @param path Pfad
     * @throws java.io.IOException
     */
    public void setBackground(String path) throws IOException {
        BufferedImage image = ImageIO.read(new File(path));
        BackgroundImage bgImage = new BackgroundImage(
                SwingFXUtils.toFXImage(image, null),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false)
        );
        imageHeigth = image.getHeight();
        imageWidth = image.getWidth();
        this.setBackground(new Background(bgImage));
        this.setPrefSize(imageWidth, imageHeigth);
        this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }

    /**
     * Hilfsfunktion, um UI-Elemente hinzuzufügen.
     *
     * @param element
     */
    private void addUIElement(Node element) {
        this.getChildren().add(element);
        update();
    }

    /**
     * Akualisiert die Kanten und Knoten.
     *
     * @param element
     */
    private void update() {
        nodes.values().stream().forEach(n -> n.setXY(
                n.getX() * getWidth() / imageWidth,
                n.getY() * getHeight() / imageHeigth)
        );
        edges.values().stream().forEach(n -> n.draw());
    }
}
