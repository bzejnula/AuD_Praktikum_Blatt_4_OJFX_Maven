/* 
 * Copyright 2019 Hochschule Osnabrueck
 */
package de.hsos.aud.routing.gui;

/**
 *
 * @author root
 */
import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author root
 */
public class UIBlockStatusEvent extends Event {

    /**
     *
     */
    public static final EventType<UIBlockStatusEvent> BLOCKSTATUS
            = new EventType<>(Event.ANY, "BLOCKSTATUS");

    /**
     * Start der Kante.
     */
    public final String from;

    /**
     * Ende der Kante.
     */
    public final String to;

    /**
     * Durchfahrtsstatus der Kante.
     */
    public final boolean blocked;

    /**
     * Konstruktor
     *
     * @param eventType Eventtyp
     * @param from Start der Kante
     * @param to Ende der Kante
     * @param blocked Durchfahrtsstatus der Kante
     */
    public UIBlockStatusEvent(EventType<? extends Event> eventType, String from, String to, boolean blocked) {
        super(eventType);
        this.from = from;
        this.to = to;
        this.blocked = blocked;
    }
}
