package PQ;

import de.hsos.aud.routing.Node;

/**
 * Element zur Speicherung in der PQ.
 * @author heikerli
 * @param <E>
 */
public class PQElement<E> {
    private int id;    /* Index des Elementes */
    private double prio;   /* Prioritaet ist eine ganze Zahl */

    /* weitere Daten ... */
    public Node data;

    public PQElement(Node val, double prio) {
        data = val;
        this.prio = prio;
    }


    public int id() { return id; }
    public void set_id (int id) { this.id = id; }

    public double priority () { return prio; }
    public void set_priority (double p) { prio = p; }

    public boolean is_lower(PQElement e) {
        return prio < e.prio;
    }
}
