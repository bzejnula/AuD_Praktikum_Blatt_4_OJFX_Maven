module de.hsos.aud.routing.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.logging;
    requires java.desktop;
    requires javafx.swing;
    
    opens de.hsos.aud.routing.gui to javafx.fxml;
    exports de.hsos.aud.routing.gui;
}