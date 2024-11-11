module com.george.chess {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.fxml;  
    requires javafx.media; 
    requires javafx.swing; 
    requires java.logging; 

    // Allow JavaFX to access your window classes
    opens com.chess.window to javafx.graphics;
}
