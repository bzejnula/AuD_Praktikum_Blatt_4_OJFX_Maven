package de.hsos.aud.routing;

// ACHTUNG SCHABLONE: HIER EIGENE IMPORTS EINFUEDEN
// z.B. fuer PQ ....


import PQ.PQElement;
import PQ.PriorityQueue;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Klasse mit Algorithmen zur Wegesuche.
 * ACHTUNG: SCHABLONE
 * 
 * @author heikerli
 */
public class GraphRouting {
    DirectedGraph g;
    List<String> path;
    double distance;
    
    public GraphRouting(DirectedGraph graph) {
        path = new ArrayList<>();
        g = graph;
    }

    /**
     * Vorher muss Dijkstra aufgerufen worden sein. 
     * @return Pfad vom Start zum Ziel bei vorherigem Dijkstra-Durchlauf.
     */
    public List<String> get_shortest_path() {
        return path;
    };
    
    /**
     * Vorher muss Dijkstra aufgerufen worden sein. 
     * @return Distanz auf kürzestem Weg nach Dijstra.
     */
    public double get_shortest_path_distance() {
        return distance;
    };
    
    /**
     * Finden des kuerzesten Weges.
     * @param s Startknoten
     * @param t Endknoten
     */
    public void find_shortest_path_Dijkstra(Node s, Node t) {
        // ACHTUNG SCHABLONE: HIER EIGENEN CODE REALISIEREN
        // Setze die Distanz aller Knoten auf Unendlich (außer dem Startknoten s)
        // Setze die Distanz aller Knoten auf Unendlich (außer dem Startknoten s)
        DirectedGraph G = new DirectedGraph();
        for (Node v : G.getNodes()) {
            v.reset();
        }

        // Erzeuge eine PriorityQueue, um die Knoten nach ihrer Distanz zu verwalten
        PriorityQueue<Node> pq = new PriorityQueue<>();
        s.distance = 0.0;
        pq.insert(s);

        while (!pq.isEmpty()) {
            Node u = pq.extract_min(); // Extrahiere den Knoten mit der kleinsten Distanz
            u.setMarked();

            for (Edge e : u.getOutEdges()) {
                Node v = e.getDestination();

                if (!v.isMarked()) {
                    double new_dist = u.distance + e.getWeight();

                    if (new_dist < v.distance) {
                        v.distance = new_dist;
                        v.predecessor = u;

                        // Aktualisiere den Knoten in der PriorityQueue
                        pq.update(v.getId(), (int) new_dist); // Verwenden Sie hier die ID des Knotens als Index
                    }
                }
            }
        }

        // Ggf. Ausgabe des kürzesten Weges zu einem Zielknoten (in umgekehrter Reihenfolge)
        if (t.distance == Double.MAX_VALUE) {
            System.out.println("Es existiert kein Pfad von s nach t.");
        } else {
            System.out.println("Kürzester Weg von s nach t: ");
            Node current = t;
            while (current != null) {
                System.out.println(current);
                current = current.predecessor;
            }
        }
    }
   
    /** 
     * Ueberpruefung, ob kuerzester Weg bzgl. Anzahl der Kanten 
     * maximal die Laenge limit hat.
     * @param s Startknoten
     * @param t Zielknoten
     * @param limit max. zulaessige Anzahl Kanten
     * @return true, falls Anzahl Kanten < als limit
     */    
    public boolean find_max_length_path_BFS(Node s, Node t, int limit) {
        // Setze die Distanz aller Knoten auf -1 (außer dem Startknoten s)
        DirectedGraph G = new DirectedGraph();
        for (Node v : G.getNodes()) {
            v.reset();
        }

        // Füge den Startknoten zur Warteschlange hinzu
        Queue<Node> queue = new LinkedList<>();
        s.distance = 0;
        queue.add(s);

        while (!queue.isEmpty()) {
            Node u = queue.poll(); // Entferne den ersten Knoten aus der Warteschlange
            u.setMarked();

            for (Edge e : u.getOutEdges()) {
                Node v = e.getDestination();

                if (!v.isMarked()) {
                    v.distance = u.distance + 1; // Erhöhe die Distanz um 1 (Anzahl der Kanten)

                    if (v.distance <= limit) {
                        if (v == t) {
                            return true; // Der Zielknoten wurde erreicht
                        }
                        queue.add(v); // Füge den Knoten zur Warteschlange hinzu
                    }
                }
            }
        }

        return false; // Es wurde kein Pfad gefunden, der die Längenbeschränkung erfüllt
    }
    }
    
    
    // ACHTUNG SCHABLONE: HIER EIGENEN CODE REALISIEREN FUER
    // HILFSFUNKTIONEN / -METHODEN
    

