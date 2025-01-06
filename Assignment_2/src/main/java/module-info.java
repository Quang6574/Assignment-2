module rmit_hanoi.assignment {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.desktop;
    requires java.sql;
    requires org.postgresql.jdbc;


    opens edu.rmit_hanoi.assignment2 to javafx.fxml;
    exports edu.rmit_hanoi.assignment2;
    exports edu.rmit_hanoi.assignment2.controller;
    exports edu.rmit_hanoi.assignment2.controller.register_user.manager;
    exports edu.rmit_hanoi.assignment2.controller.visitor;
    exports edu.rmit_hanoi.assignment2.model;
    exports edu.rmit_hanoi.assignment2.view;

}