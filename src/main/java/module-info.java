/**
 * @author Group 18
 */

module rmit_hanoi.assignment {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.desktop;
    requires java.sql;


    opens edu.rmit_hanoi.assignment2 to javafx.fxml;
    opens edu.rmit_hanoi.assignment2.controller.register_user.owner to javafx.fxml;
    exports edu.rmit_hanoi.assignment2;
    exports edu.rmit_hanoi.assignment2.controller;
    exports edu.rmit_hanoi.assignment2.controller.register_user.owner;
    exports edu.rmit_hanoi.assignment2.controller.register_user.tenant;
    exports edu.rmit_hanoi.assignment2.controller.register_user.manager;
    exports edu.rmit_hanoi.assignment2.controller.register_user.host;
    exports edu.rmit_hanoi.assignment2.controller.visitor;
    exports edu.rmit_hanoi.assignment2.Database;

}