package PQ;

import de.hsos.aud.routing.Node;

import java.lang.reflect.Array;

/**
 * Vorrangwarteschlange mit Index-Tabelle zur Zuordnung von 
 * IDs zu Indizes im Heap. Zugriff auf Element per ID damit in konstanter Zeit
 * moeglich.
 * 
 * @author heikerli
 * @param <E>  Generischer Datentyp
 */


public class PriorityQueue<E> {
    protected int n;          /* aktuelle Laenge */
    protected int max_size;   /* max. Laenge */

    protected int max_id = 0; /* letzte vergebene ID */

    protected  PQElement<E>[] a = null;  /* Feld zur Speicherung */

    public PriorityQueue(int size, PQElement<E> ... dummy) {
        max_size = size;
        n = 0;
        /* Ansonsten gibt es einen Generic Array Creation Fehler */
        if (dummy.length > 0) {
            throw new IllegalArgumentException("Falscher Datentyp.");
        }
        Class<?> c = dummy.getClass().getComponentType();
        /* Wg. der einfacheren Indexrechnung allokieren wir ein Element mehr */
        a = (PQElement<E>[]) Array.newInstance(c, size + 1);
    }

    public PriorityQueue() {

    }

    public boolean isEmpty() {
        return n == 0;
    }

    protected void swap(PQElement<E> a[], int i, int j) {
        PQElement<E> h = a[i];
        a[i] = a[j];
        a[j] = h;

        /* Update IDs */
        a[i].set_id(i);
        a[j].set_id(j);
    }


    //COMPARE GEAENDERT
    protected int compare(PQElement<E> a, PQElement<E> b) {
        if (a.priority() < b.priority()) {
            return 1; // Kleinste Elemente zuerst
        } else if (a.priority() > b.priority()) {
            return -1;
        } else {
            return 0;
        }
    }

    public PQElement<E> maximum() {
        // ACHTUNG SCHABLONE: HIER EIGENEN CODE REALISIEREN
        if (n == 0) {
            return null; //element gibt es nicht
        }
        return a[1];
    }



    /* Hilfsfunktion */
//DOWN_HEAP GEAENDERT
    protected void down_heap(PQElement<E> a[], int idx) {
        // ACHTUNG SCHABLONE: HIER EIGENEN CODE REALISIEREN
        int smallest = idx;
        int left = 2 * idx;
        int right = 2 * idx + 1;

        if (left <= n && compare(a[left], a[smallest]) > 0) {
            smallest = left;
        }

        if (right <= n && compare(a[right], a[smallest]) > 0) {
            smallest = right;
        }

        if (smallest != idx) {
            swap(a, idx, smallest);
            down_heap(a, smallest);
        }
    }

    /* Hilfsfunktion */
    protected void up_heap(PQElement<E> h[], int idx) {
        // ACHTUNG SCHABLONE: HIER EIGENEN CODE REALISIEREN
        while (idx > 1 && compare(a[idx], a[idx/2]) > 0) {
            swap(a, idx, idx/2);
            idx = idx/2;
        }
    }


    public Node extract_min() {
        if (n == 0) {
            return null;
        }
        Node minNode = (Node) a[1].data;
        a[1] = a[n];
        a[1].set_id(1);
        n--;
        down_heap(a, 1);
        return minNode;
    }

    public void insert(Node e) {
        // ACHTUNG SCHABLONE: HIER EIGENEN CODE REALISIEREN
        if (n >= max_size) {
            System.err.println("PriorityQueue::insert(): zu viele Elemente!");
            return;
        }
        n++;
        max_id++;
        PQElement<Node> pqElement = new PQElement<>(e, e.distance);
        pqElement.set_id(n);
        a[n] = (PQElement<E>) pqElement;
        up_heap(a, n);
    }

    public void update(int id, int delta) {
        // Überprüft, ob die Warteschlange leer ist.
        if (n < 1) {
            return;
        }
        // Überprüft, ob die ID gültig ist.
        if ((id < 1) || (id > this.max_id)) {
            System.err.println("PriorityQueue.insert(): id inkorrekt!");
            return;
        }
        // Sucht das Element mit der angegebenen ID in der Warteschlange.
        int idx = -1;
        for (int i = 1; i <= n; i++) {
            if (a[i].id() == id) {
                idx = i;
                break;
            }
        }
        // Überprüft, ob das Element mit der angegebenen ID in der Warteschlange gefunden wurde.
        if (idx == -1) {
            return;
        }
        // Ändert die Priorität des Elements mit der angegebenen ID um delta.
        a[idx].set_priority(a[idx].priority() + delta);
        // Aktualisiert den Heap entsprechend, indem das Element mit der
        // geänderten Priorität nach oben oder unten verschoben wird.
        if (delta > 0) {
            up_heap(a, idx);
        } else {
            down_heap(a, idx);
        }
    }

    public void remove(int id) {
        // Überprüft, ob die Warteschlange leer ist.
        if (n < 1) {
            return;
        }
        // Überprüft, ob die ID gültig ist.
        if ((id < 1) || (id > this.max_id)) {
            System.err.println("PriorityQueue.insert(): id inkorrekt!");
            return;
        }
        // Sucht das Element mit der angegebenen ID in der Warteschlange.
        int idx = -1;
        for (int i = 1; i <= n; i++) {
            if (a[i].id() == id) {
                idx = i;
                break;
            }
        }
        // Überprüft, ob das Element mit der angegebenen ID in der Warteschlange gefunden wurde.
        if (idx == -1) {
            return;
        }
        // Ersetzt das zu entfernende Element durch das letzte Element im Heap.
        a[idx] = a[n];
        // Aktualisiert die ID des Elements, das an der Stelle des zu entfernenden Elements im Heap steht.
        a[idx].set_id(idx);
        // Verringert die Größe des Heaps.
        n--;
        // Aktualisiert den Heap entsprechend, indem das Element mit der
        // geänderten Priorität nach oben oder unten verschoben wird.
        up_heap(a, idx);
        down_heap(a, idx);
    }
}
