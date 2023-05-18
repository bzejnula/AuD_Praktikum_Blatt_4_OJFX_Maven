package de.hsos.aud.routing;

import java.util.List;

/**
 * Schnittstelle fuer Shortest-Paths-Algorithmus
 * zum UI.
 *
 * @author Heinz-Josef Eikerling
 * @author Simon Balzer
 */
public interface RoutingInterface {

    /**
     * Erstellt einen neuen Graph.
     *
     */
    public abstract void initGraph();

    /**
     * Wird aufgerufen wenn eine Kante hinzugefügt wurde.
     *
     * @param from Start-Knoten
     * @param to Ziel-Knoten
     * @param distance Distanz (Gewichtung)
     * @param blocked Durchfahrtsbeschränkung
     */
    public abstract void edgeAdded(String from, String to, double distance, boolean blocked);

    /**
     * Wird aufgerufen wenn ein Knoten hinzugefügt wurde.
     *
     * @param name Ortsname
     */
    public abstract void nodeAdded(String name);

    /**
     * Ist Ort vorhanden?
     *
     * @param name Ortsname
     * @return 
     */
    public Boolean hasNode (String name);
    
    /**
     * Wird aufgerufen wenn eine Kante wieder frei ist.
     *
     * @param from Start-Knoten
     * @param to Ziel-Knoten
     */
    public abstract void edgeEnabled(String from, String to);

    /**
     * Wird aufgerufen wenn eine Kante blockiert ist.
     *
     * @param from Start-Knoten
     * @param to Ziel-Knoten
     */
    public abstract void edgeDisabled(String from, String to);

    /**
     * Berechnet die Länge des Kürzesten Weges.
     *
     * @param from Start-Knoten
     * @param to Ziel-Knoten
     * @return Länge des Kürzesten Weges
     */
    public abstract double getShortestDistance(String from, String to);

    /**
     * Ermittelt die berechnete Route.
     *
     * @param from Start-Knoten
     * @param to Ziel-Knoten
     * @return Route (Pfad)
     */
    public abstract List<String> findShortedPath(String from, String to);

    /**
     * Überprüft ob ein Knoten erreichbar ist.
     *
     * @param from Start-Knoten
     * @param to Ziel-Knoten
     * @param nodeLimit
     * @return Besteht ein Weg?
     */
    public abstract boolean isReachable(String from, String to, int nodeLimit);
    
    /**
     * Bestimmung des kuerzesten Weges zwischen Startpunkt und Ziel mit der
     * geringsten Anzahl von Kanten.
     * 
     * @param from Startpunkt
     * @param to  Ziel
     * @return Pfad als Liste von Knotenmarkierungen
     */
    public abstract List<String> findShortedPathNoEdges(String from, String to, int nodeLimit);
}
