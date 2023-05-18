/**
 * Shell als UI zur Bestimmung kuerzester Wege im Graphen.
 *
 * @Heinz-Josef Eikerling
 */
package de.hsos.aud.routing.shell;

import de.hsos.aud.routing.*;
import static de.hsos.aud.routing.AppConfig.DEFAULT_FILE;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Shell ist eine Klasse.
 *
 * @author Heinz-Josef Eikerling
 */
public class RoutingShell {

    BufferedReader in;

    RoutingInterface routing;

    static PrintStream out = System.out;

    /** 
     * Ctor.
     */
    public RoutingShell() {
        in = new BufferedReader(new InputStreamReader(System.in));
        routing = new ImplRoutingInterface();
        initGraph();
    }

    private void initGraph() {
        try {
            openFile(DEFAULT_FILE);
            Logger.getLogger(Application.class.getName()).log(Level.INFO, "Die XML-Datei {0} wurde geladen!", DEFAULT_FILE);
        } catch (NullPointerException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.WARNING, "Die XML-Datei wurde nicht gefunden!");
        }

    }

    /**
     * Liest Graphen aus Datei (XML) mit Namen path ein. 
     * 
     * @param path 
     */
    private void openFile(String path) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xml = builder.parse(new File(path));

            xml.getDocumentElement().normalize();

            routing.initGraph();
            // Knoten einlesen
            NodeList nodeList = xml.getElementsByTagName("Node");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element elem = (Element) nodeList.item(i);
                double x = Double.parseDouble(elem.getAttribute("x"));
                double y = Double.parseDouble(elem.getAttribute("y"));
                String name = elem.getAttribute("name");

                // Routing Interface benachrichtigen
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

                // Routing Interface benachrichtigen
                routing.edgeAdded(from, to, distance, blocked);
                Logger.getLogger(Application.class
                        .getName()).log(Level.INFO, "Kante {0} -> {1} wurde hinzugef\u00fcgt!", new Object[]{from, to});
            }
        } catch (UnsupportedOperationException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.WARNING, ex.getMessage());
        } catch (IOException | NumberFormatException | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.WARNING, "Ein Fehler ist aufgetreten:", ex);
        }
    }
    
    /**
     * Ausgabe der Hilfe.
     */
    private static void printHelp() {
        out.println("h - Ausgabe Hilfe");
        out.println("q - Beenden der Shell");
        out.println("s - Kuerzesten Weg bestimmen");
        out.println("l - Weg mit max. Anzahl Hops bestimmen");
        out.println("o - Kante sperren");
        out.println("u - Kantensperrung aufheben");
    }

    private static void writePrompt() {
        out.print("Eingabe ('h' fuer Hilfe)> ");
    }

    /**
     * Eingabe lesen (Kommando).
     * @param prompt
     * @return 
     */
    private String readInput(String prompt) {
        out.print(prompt);
        String str = "";
        try {
            str = in.readLine();
        } catch (IOException e) {
            System.out.println(e);
        }
        return str;
    }

    /**
     * Kommando ausfuehren.
     * 
     * @param str
     * @return 
     */
    private Boolean executeCmd(String str) {
        if (str.equals("q")) {
            return false;
        } else if (str.equals("h")) {
            printHelp();
        } else if (str.equals("s")) {
            findShortestPath();
        } else if (str.equals("l")) {
            checkRoute();
        }
        else if (str.equals("o")) {
            lockEdge();
        }
        else if (str.equals("u")) {
            unlockEdge();
        }
        else { 
            out.println("Falsches Kommando.");
        }
        return true;
    }

    /**
     * Start und Zielknoten werden abgefragt, sowie die Anazhl der 
     * maximal zulaessigen Hops. Es wird dann ueberprueft, ob es einen Pfad
     * vom Start zum Ziel mit der Anzahl der Hops gibt.
     */
    private void checkRoute() {
        try {
            String start = readAndCheckLoc("  Start? ");
            String dest = readAndCheckLoc("   Ende? ");
            if (start == null || dest == null) {
                out.println("Fehlerhafte Eingabe!");
                return;
            }
            int nodeLimit = readNoHops();
            if (nodeLimit < 0) {
                out.println("Falschen Wert eingegeben!");
                return;
            }
                
            if (routing.isReachable(start, dest, nodeLimit)) {
                List<String> route = routing.findShortedPathNoEdges(start, dest, nodeLimit);
                out.println("Route gefunden!");
                showRoute(route);
                Logger.getLogger(Application.class.getName()).log(Level.INFO, "Route von {0} -> {1} gefunden!", new Object[]{start, dest});
            } else {
                out.println("Keine Route!");
                Logger.getLogger(Application.class.getName()).log(Level.INFO, "Keine Route von {0} -> {1} gefunden!", new Object[]{start, dest});
            }
        } catch (UnsupportedOperationException ex) {
            out.println("Fehler!");
            Logger.getLogger(Application.class.getName()).log(Level.WARNING, ex.getMessage());
        } catch (Exception ex) {
            out.println("Fehler!");
            Logger.getLogger(Application.class.getName()).log(Level.WARNING, "Ein Fehler ist aufgetreten:", ex);
        }
    }

    /**
     * Start und Zielknoten werden abgefragt und dann der kuerzeste Weg
     * zwischen Start und Zielknoten bestimmt.
     */
    private void findShortestPath() {
        try {
            String start = readAndCheckLoc("  Start? ");
            String dest = readAndCheckLoc("   Ende? ");
            if (start == null || dest == null) {
                out.println("Fehlerhafte Eingabe!");
                return;
            }

            double distance = routing.getShortestDistance(start, dest);
            if (distance < Double.MAX_VALUE) {
                String routeLength = String.format("%.1f", distance);
                out.println("Distanz: " + routeLength);
                List<String> route = routing.findShortedPath(start, dest);
                showRoute(route);
                Logger.getLogger(Application.class.getName()).log(Level.INFO, "Berechnete Route: {0} L\u00e4nge: {1}", new Object[]{route.toString(), routeLength});
            } else {
                out.println("Keine Route!");
                Logger.getLogger(Application.class.getName()).log(Level.INFO, "Keine Route von {0} -> {1} gefunden!", new Object[]{start, dest});
            }
        } catch (UnsupportedOperationException ex) {
            out.println("Fehler!");
            Logger.getLogger(Application.class.getName()).log(Level.WARNING, ex.getMessage());
        } catch (Exception ex) {
            out.println("Fehler!");
            Logger.getLogger(Application.class.getName()).log(Level.WARNING, "Ein Fehler ist aufgetreten:", ex);
        }
    }
    
    /**
     * Kante zwischen zwei Knoten sperren. 
     */
    private void lockEdge() {
        String start = readAndCheckLoc("  Start? ");
        String end = readAndCheckLoc("   Ende? ");
        if (start == null || end == null) {
            out.println("Fehlerhafte Eingabe!");
            return;
        }
        try {
            routing.edgeDisabled(start, end);
            Logger.getLogger(Application.class.getName()).log(Level.INFO, "Sperrung zwischen {0} und {1} aktiv", new Object[]{start, end});
        } catch (Exception ex) {
            Logger.getLogger(Application.class.getName()).log(Level.WARNING, "Sperrung zwischen {0} und {1} fehlgeschlagen", new Object[]{start, end});
        }
    }
    
    /**
     * Kante zwischen zwei Knoten sperren. 
     */
    private void unlockEdge() {
        String start = readAndCheckLoc("  Start? ");
        String end = readAndCheckLoc("   Ende? ");
        if (start == null || end == null) {
            out.println("Fehlerhafte Eingabe!");
            return;
        }
        try {
            routing.edgeEnabled(start, end);
            Logger.getLogger(Application.class.getName()).log(Level.INFO, "Sperrung zwischen {0} und {1} aufgehoben", new Object[]{start, end});
        } catch (Exception ex) {
            Logger.getLogger(Application.class.getName()).log(Level.WARNING, "Aufhebung der Sperrung zwischen {0} und {1} fehlgeschlagen", new Object[]{start, end});
        }
    }

    /** 
     * Hilfsroutinen zum Lesen.
     * 
     * @return 
     */
    private int readNoHops() {
        String hops = readInput("Max. Anzahl Hops? ");
        int val;
        try {
            val = Integer.parseInt(hops);
        } catch (NumberFormatException e) {
            val = -1;
        }
        return val;
    }

    private String readAndCheckLoc(String prompt) {
        String loc = readInput(prompt);
        if (routing.hasNode(loc) == false) {
            out.println("Ort falsch eingegeben!");
            return null;
        }
        return loc;
    }

    /**
     * Route (= Liste von Knoten) ausgeben.
     * 
     * @param route 
     */
    private void showRoute(List<String> route) {
        out.print("Route: ");
        route.forEach((loc) -> {
            out.print(loc + " -> ");
        });
        out.println("ENDE");
    }

    /**
     * Main Funktion: Graph wird eingelesen und Shell gestartet. 
     * 
     * @param args 
     */
    public static void main(String[] args) {
        RoutingShell shell = new RoutingShell();
        do {
            writePrompt();
            String cmd = shell.readInput("");
            if (shell.executeCmd(cmd) != true) {
                break;
            }
        } while (true);
    }
}
