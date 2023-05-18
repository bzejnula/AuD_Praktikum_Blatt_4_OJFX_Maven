# Inhalt

Dies ist eine Schablone zur Realisierung von Wege-Algorithmen mit Java. 

# Build-Umgebung

Es handelt sich um ein Java-Projekt, welches verwendet **OpenJFX** fuer das GUI verwendet. 
JFX ist im Oracle-JDK /-JRE enthalten, nicht aber in den offenen JDKs. Deshalb wird 
als Build-Umgebung **Maven** verwendet und dort eine Abhängigkeit gesetzt. 

Die Schablone kann mit _IntelliJ_ oder _Netbeans_ geöffnet werden. Die SDK-Version
sollte unter den `Project Settings` auf Version > 11 eingestellt werden. 
Alternativ kann das Projekt auch per `mvn install` auf der Konsole gebildet 
werden. 

Das Projekt besitzt Abhängigkeiten zu: 
* `javafx.fxml`
* `java.xml`
* `java.logging`
* `java.desktop`
* `javafx.swing`

Diese sollten bei einem _Build_ aufgelöst werden.

# Nutzung der Schablone

Das GUI befindet sich in der Klasse `UIApplication.java` im Paket
`de.hsos.aud.routing.gui`). Die Routing-Algorithmen werden vom GUI aus aufgerufen werden.

Die zu editierenden Stellen sind im Quellcode kenntlich gemacht: 

`// ACHTUNG SCHABLONE: HIER EIGENEN CODE REALISIEREN  `
