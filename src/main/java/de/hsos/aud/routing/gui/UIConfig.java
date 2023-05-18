package de.hsos.aud.routing.gui;

import de.hsos.aud.routing.RoutingInterface;
import de.hsos.aud.routing.ImplRoutingInterface;
import de.hsos.aud.routing.AppConfig;

/**
 * Einstellungen fuer das UI.
 * 
 * @author Simon Balzer
 * @author Heinz-Josef Eikerling
 */
public class UIConfig extends AppConfig {

    public final static int MAP_WIDTH = 1300; // 650;

    public final static int MAP_HEIGHT = 980; // 490;

    public final static int ARROW_MARGIN_X = 5;

    public final static int ARROW_MARGIN_Y = 5;

    public final static int ARROW_BOX_WIDTH = 5;
    
    public final static int LINE_WIDTH = 2;
    
    /**
     * Instanz der Routing Klasse
     */
    public final static RoutingInterface ROUTING = new ImplRoutingInterface();
}
