package de.hsos.aud.routing;

import java.util.List;

/**
 * Implementation des Routing Interfaces zur Anbindung 
 * der Algorithmen an das UI. Eigene Algorithmen müssen hier 
 * integriert werden.
 * 
 * @author Simon Balzer
 * @author Heinz-Josef Eikerling
 */
public class ImplRoutingInterface implements de.hsos.aud.routing.RoutingInterface {

    public DirectedGraph graph;
    
    @Override
    public Boolean hasNode(String loc) {
        try {
            Node node = graph.getNodes().stream().filter(v -> v.getName().equals(loc)).findFirst().get();
            return node.getName().equals(loc);
        } catch (java.util.NoSuchElementException e) {
            return false;
        }
    }
    
    @Override
    public void initGraph() {
        graph = new DirectedGraph();
    }

    @Override
    public void edgeAdded(String from, String to, double distance, boolean blocked) {
        graph.addEdge(from, to, distance, blocked);
    }

    @Override
    public void edgeEnabled(String from, String to) {
        graph.getEdge(from, to).setEnabled(true);
    }

    @Override
    public void edgeDisabled(String from, String to) {
        graph.getEdge(from, to).setEnabled(false);
    }

    @Override
    public void nodeAdded(String name) {
        graph.addNode(name);
    }

    /* Hier muessen die Verfahren zur Wegesuche aufgerufen werden */
    /**************************************************************/    

    /**
     * Ermittlung, ob Pfad ueber eine vorgegebene Anzahl von Kanten
     * erreichbar ist.
     * 
     * @param from Startpunkt
     * @param to  Ziel 
     * @param nodeLimit Anzahl der zulässigen Knoten auf Pfad
     * @return  true, falls Pfad der Laenge nodeLimit existiert
     */
    @Override
    public boolean isReachable(String from, String to, int nodeLimit) {
        /* Algorithmus zur Messung des bzgl. der Anzahl Kanten kuerzesten Weges */
        GraphRouting alg = new GraphRouting(graph);
        Node source_n = graph.getNodes().stream().filter(v -> v.getName().equals(from)).findFirst().get();
        Node dest_n = graph.getNodes().stream().filter(v -> v.getName().equals(to)).findFirst().get();
        return alg.find_max_length_path_BFS(source_n, dest_n, nodeLimit);
    }

    /**
     * Bestimmung des kuerzesten Weges zwischen Startpunkt und Ziel mit 
     * geringsten Anzahl von Kanten.
     * 
     * @param from Startpunkt
     * @param to  Ziel
     * @return Pfad als Liste von Knotenmarkierungen
     */
    @Override
    public List<String> findShortedPathNoEdges(String from, String to, int nodeLimit) {
        GraphRouting alg = new GraphRouting(graph);
        Node source_n = graph.getNodes().stream().filter(v -> v.getName().equals(from)).findFirst().get();
        Node dest_n = graph.getNodes().stream().filter(v -> v.getName().equals(to)).findFirst().get();
        /* Breitensuche durchfuehren */
        alg.find_max_length_path_BFS(source_n, dest_n, nodeLimit);
        return alg.get_shortest_path();
    }
    
    /**
     * Bestimmung des kuerzesten Weges zwischen Startpunkt und Ziel.
     * 
     * @param from Startpunkt
     * @param to  Ziel
     * @return Pfad als Liste von Knotenmarkierungen
     */
    @Override
    public List<String> findShortedPath(String from, String to) {
        /* Dijksta-Algorithmus zur Bestimmung des kuerzesten Weges */
        GraphRouting alg = new GraphRouting(graph);
        Node source_n = graph.getNodes().stream().filter(v -> v.getName().equals(from)).findFirst().get();
        Node dest_n = graph.getNodes().stream().filter(v -> v.getName().equals(to)).findFirst().get();
        alg.find_shortest_path_Dijkstra(source_n, dest_n);
        return alg.get_shortest_path();
    }

    /**
     * Berechnung der Distanz zwischen Startpunkt und Ziel.
     * Falls Start und Ziel verbunden sind, muss die Distanz
     * kleiner sein als Double.MAX_VALUE.
     * 
     * @param from Startpunkt
     * @param to  Ziel
     * @return Laenge des kuerzesten Pfades
     */    
    @Override
    public double getShortestDistance(String from, String to) {
        /* Dijksta-Algorithmus zur Bestimmung der Distanz auf kuerzesten Weg */
        GraphRouting alg = new GraphRouting(graph);
        Node source_n = graph.getNodes().stream().filter(v -> v.getName().equals(from)).findFirst().get();
        Node dest_n = graph.getNodes().stream().filter(v -> v.getName().equals(to)).findFirst().get();
        alg.find_shortest_path_Dijkstra(source_n, dest_n);
        return alg.get_shortest_path_distance();
    }
}
