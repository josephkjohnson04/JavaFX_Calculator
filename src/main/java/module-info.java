module com.calculator.javafx_calculator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.calculator.javafx_calculator to javafx.fxml;
    exports com.calculator.javafx_calculator;
}