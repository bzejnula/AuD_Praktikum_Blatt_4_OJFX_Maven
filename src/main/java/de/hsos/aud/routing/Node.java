package de.hsos.aud.routing;

import java.util.LinkedList;

/**
 * Klasse fuer Knoten in einem Graphen.
 *
 * Teilweise übernommen aus
 * https://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
 *
 *
 * @author Simonr Balzer
 * @author Heinz-Josef Eikerling
 */
public class Node {

    final private String name;

    private int id;

    public double distance;
    public Node predecessor;
    public boolean marked;

    private final LinkedList<Edge> out_edges;

    public LinkedList<Edge> getOutEdges() {
        return out_edges;
    }



    public final void reset () {
        distance = Double.MAX_VALUE;
        predecessor = null;
        marked = false;
    }

    public Node(int id, String name) {
        out_edges = new LinkedList<>();
        this.id = id;
        this.name = name;
        reset();
    }

    public void setId(int id_) {
        id = id_;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isMarked () {
        return marked;
    }

    public void setMarked () {
        marked = true;
    }

    @Override
    public String toString() {
        return name;
    }


}
