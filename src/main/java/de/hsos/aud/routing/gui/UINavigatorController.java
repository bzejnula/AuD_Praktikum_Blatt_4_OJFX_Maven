package de.hsos.aud.routing.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Platform.exit;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import static de.hsos.aud.routing.gui.UIConfig.*;
import de.hsos.aud.routing.*;

/**
 * FXML Controller class
 *
 * @author Simon Balzer
 */
public class UINavigatorController implements Initializable {

    @FXML
    UIMap map;
    @FXML
    RoutingInterface routing = ROUTING;
    @FXML
    Text labelSliderNode;
    @FXML
    Slider sliderNode;
    @FXML
    TextField textFieldResult;
    @FXML
    ComboBox comboBoxDestination;
    @FXML
    ComboBox comboBoxStart;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Slider Label aktualisieren
        sliderNode.valueProperty().addListener((obs, oldval, newVal) -> {
            int val = newVal.intValue();
            sliderNode.setValue(val);
            labelSliderNode.setText(Integer.toString(val));
        });
        map.addEventHandler(UIBlockStatusEvent.BLOCKSTATUS, e -> {
            try {
                if (e.blocked) {
                    routing.edgeDisabled(e.from, e.to);
                    Logger.getLogger(Application.class.getName()).log(Level.INFO, "{0} zu {1} ist nun blockiert!", new Object[]{e.from, e.to});
                } else {
                    routing.edgeEnabled(e.from, e.to);
                    Logger.getLogger(Application.class.getName()).log(Level.INFO, "{0} zu {1} ist nun frei!", new Object[]{e.from, e.to});
                }
            } catch (UnsupportedOperationException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.WARNING, ex.getMessage());
            } catch (Exception ex) {
                textFieldResult.setText("Fehler!");
                Logger.getLogger(Application.class.getName()).log(Level.WARNING, "Ein Fehler ist aufgetreten:", ex);
            }
        });
        
        try {
            openFile(DEFAULT_FILE);
            Logger.getLogger(Application.class.getName()).log(Level.INFO, "Die XML-Datei {0} wurde geladen!", DEFAULT_FILE);
        } catch (NullPointerException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.WARNING, "Die XML-Datei wurde nicht gefunden!");
        }
    }

    /**
     * Button Handler "Prüfe Route"
     */
    @FXML
    public void checkRoute() {
        try {
            map.clearHighlights();
            String start = comboBoxStart.getValue().toString();
            String dest = comboBoxDestination.getValue().toString();
            int nodeLimit = (int) sliderNode.getValue();
            if (routing.isReachable(start, dest, nodeLimit)) {
                List<String> route = routing.findShortedPathNoEdges(start, dest, nodeLimit);
                map.highlight(route);
                textFieldResult.setText("Route gefunden!");
                Logger.getLogger(Application.class.getName()).log(Level.INFO, "Route von {0} -> {1} gefunden!", new Object[]{start, dest});
            } else {
                textFieldResult.setText("Keine Route!");
                Logger.getLogger(Application.class.getName()).log(Level.INFO, "Keine Route von {0} -> {1} gefunden!", new Object[]{start, dest});
            }
        } catch (UnsupportedOperationException ex) {
            textFieldResult.setText("Fehler!");
            Logger.getLogger(Application.class.getName()).log(Level.WARNING, ex.getMessage());
        } catch (Exception ex) {
            textFieldResult.setText("Fehler!");
            Logger.getLogger(Application.class.getName()).log(Level.WARNING, "Ein Fehler ist aufgetreten:", ex);
        }
    }

    /**
     * Button Handler "Kürzeste Route"
     */
    @FXML
    public void shortestRoute() {
        try {
            map.clearHighlights();
            String start = comboBoxStart.getValue().toString();
            String dest = comboBoxDestination.getValue().toString();
            double distance = routing.getShortestDistance(start, dest);
            if (distance < Double.MAX_VALUE) {
                String routeLength = String.format("%.1f", distance);
                textFieldResult.setText(routeLength);
                List<String> route = routing.findShortedPath(start, dest);
                map.highlight(route);
                Logger.getLogger(Application.class.getName()).log(Level.INFO, "Berechnete Route: {0} L\u00e4nge: {1}", new Object[]{route.toString(), routeLength});
            } else {
                textFieldResult.setText("Keine Route!");
                Logger.getLogger(Application.class.getName()).log(Level.INFO, "Keine Route von {0} -> {1} gefunden!", new Object[]{start, dest});
            }
        } catch (UnsupportedOperationException ex) {
            textFieldResult.setText("Fehler!");
            Logger.getLogger(Application.class.getName()).log(Level.WARNING, ex.getMessage());
        } catch (Exception ex) {
            textFieldResult.setText("Fehler!");
            Logger.getLogger(Application.class.getName()).log(Level.WARNING, "Ein Fehler ist aufgetreten:", ex);
        }
    }

    /**
     * Button Handler "Öffnen"
     */
    @FXML
    public void open() {
        try {
            Stage primaryStage = (Stage) map.getScene().getWindow(); 
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File("."));
            File file = fc.showOpenDialog(primaryStage);
            String filePath = file.getAbsolutePath();
            openFile(filePath);
            Logger.getLogger(Application.class.getName()).log(Level.INFO, "Die XML-Datei {0} wurde geladen!", filePath);
            primaryStage.setTitle(filePath);
        } catch (NullPointerException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.WARNING, "Die XML-Datei wurde nicht gefunden!");
        }
    }

    /**
     * Button Handler "Beenden"
     */
    @FXML
    public void close() {
        exit();
    }

    /**
     * Parser für XML
     *
     * @param path Pfad der XML-Datei
     */
    private void openFile(String path) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xml = builder.parse(new File(path));

            xml.getDocumentElement().normalize();
            //background

            map.setBackground(((Element) xml.getElementsByTagName("Background").item(0)).getAttribute("file"));
            //Reste aufräumen
            routing.initGraph();
            map.clear();
            // Knoten einlesen
            NodeList nodeList = xml.getElementsByTagName("Node");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element elem = (Element) nodeList.item(i);
                double x = Double.parseDouble(elem.getAttribute("x"));
                double y = Double.parseDouble(elem.getAttribute("y"));
                String name = elem.getAttribute("name");
                //UI Komponente erstellen
                map.addNode(x, y, name);
                //Comboboxen füllen erstellen
                comboBoxStart.getItems().add(name);
                comboBoxDestination.getItems().add(name);

                //Routing Interface benachrichtigen
                routing.nodeAdded(elem.getAttribute("name"));
                Logger.getLogger(Application.class
                        .getName()).log(Level.INFO, "Knoten {0} wurde hinzugef\u00fcgt!", name);
            }
            // Kanten einlesen
            NodeList edgeList = xml.getElementsByTagName("Edge");
            for (int i = 0; i < edgeList.getLength(); i++) {
                Element elem = (Element) edgeList.item(i);
                String from = elem.getAttribute("from");
                String to = elem.getAttribute("to");
                double distance = Double.parseDouble(elem.getAttribute("distance"));
                boolean blocked = Boolean.parseBoolean(elem.getAttribute("blocked"));
                map.addEdge(from, to, distance, blocked);

                //Routing Interface benachrichtigen
                routing.edgeAdded(from, to, distance, blocked);
                Logger.getLogger(Application.class
                        .getName()).log(Level.INFO, "Kante {0} -> {1} wurde hinzugef\u00fcgt!", new Object[]{from, to});
            }

            comboBoxStart.getSelectionModel().selectFirst();
            comboBoxDestination.getSelectionModel().selectLast();

            sliderNode.setMin(0);
            sliderNode.setMax(map.getNodes().size());
            sliderNode.setValue(sliderNode.getMax()); // setValue(0)
            labelSliderNode.setText(Integer.toString((int) sliderNode.getValue()));//getMax()));

        } catch (UnsupportedOperationException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.WARNING, ex.getMessage());
        } catch (IOException | NumberFormatException | ParserConfigurationException | SAXException ex) {
            textFieldResult.setText("Fehler!");
            Logger.getLogger(Application.class.getName()).log(Level.WARNING, "Ein Fehler ist aufgetreten:", ex);
        }
    }
}
