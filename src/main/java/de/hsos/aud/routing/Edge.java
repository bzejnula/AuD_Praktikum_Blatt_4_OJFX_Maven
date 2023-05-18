package de.hsos.aud.routing;

/**
 * Klasse fuer gerichtete Kanten in einem Graphen. 
 * 
 * @author Simon Balzer
 * @author Heinz-Josef Eikerling
 */
public class Edge {

    private final String id;
    private final Node source;
    private final Node target;
    private double weight;
    private boolean enabled;

    public Edge(String id, Node source, Node destination, double weight, boolean blocked) {
        this.id = id;
        this.source = source;
        this.target = destination;
        this.weight = weight;
        this.enabled = !blocked;
    }

    public String getId() {
        return id;
    }

    public Node getDestination() {
        return target;
    }

    public Node getSource() {
        return source;
    }

    public double getWeight() {
        return weight;
    }

    public void setEnabled(boolean status) {
        enabled = status;
        return;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setDistance(double distance) {
        weight = distance;
    }

    @Override
    public String toString() {
        return source + " " + target;
    }
}
