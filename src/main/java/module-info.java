module org.example.noteapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires jbcrypt;


    opens org.example.noteapp to javafx.fxml;
    exports org.example.noteapp;
    exports org.example.noteapp.controllers;
    opens org.example.noteapp.controllers to javafx.fxml;
}