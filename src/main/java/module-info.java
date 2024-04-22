module com.calculator.javafx_calculator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.calculator.javafxcontrol to javafx.fxml;
    exports com.calculator.javafxcontrol;
}