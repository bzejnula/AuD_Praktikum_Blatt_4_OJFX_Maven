package de.hsos.aud.routing;

import java.util.LinkedList;
import java.util.List;

/**
 * Klasse fuer gerichteten Graphen. 
 * 
 * @author Simon Balzer
 * @author Heinz-Josef Eikerling
 */
public class DirectedGraph {

    private final List<Node> nodes;
    private final List<Edge> edges;
    private int noNodes = 0;
    private int noEdges = 0;

    DirectedGraph() {
        nodes = new LinkedList<>();
        edges = new LinkedList<>();
    }

    /**
     * Knoten einfuegen.
     * 
     * @param name
     */
    public void addNode(String name) {
        nodes.add(new Node(noNodes++, name));
    }

    /**
     * Knoten mit passendem Namen suchen.
     * 
     * @param name
     * @return
     */
    public Node getNode(String name) {
        for (Node v : nodes) {
            if (v.getName().equals(name)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Kante einfuegen.
     * 
     * @param source
     * @param destination
     * @param weight
     */
    public void addEdge(String source, String destination, double weight, boolean blocked) {
        Node source_n = nodes.stream().filter(v -> v.getName().equals(source)).findFirst().get();
        Node dest_n = nodes.stream().filter(v -> v.getName().equals(destination)).findFirst().get();
        Edge e = new Edge(((Integer) noEdges++).toString(),
                source_n, dest_n, weight, blocked
        );
        edges.add(e);
        source_n.getOutEdges().add(e);
    }

    /**
     *
     * Kante zwischen zwei Knoten suchen.
     * 
     * @param source
     * @param destination
     * @return
     */
    public Edge getEdge(String source, String destination) {
        return edges
                .stream()
                .filter(e -> e.getSource().getName().equals(source))
                .filter(e -> e.getDestination().getName().equals(destination))
                .findFirst().get();
    }

    /**
     *
     * @return
     */
    public List<Node> getNodes() {
        return nodes;
    }

    /**
     * Alle Kanten zurückgeben.
     * 
     * @return
     */
    public List<Edge> getEdges() {
        return edges;
    }
}
