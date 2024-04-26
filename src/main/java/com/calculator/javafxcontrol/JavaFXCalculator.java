package com.calculator.javafxcontrol;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class JavaFXCalculator extends Application {

    /**
     * Keeps track of the user's input
     */
    private TextField userInput;    // display textfield

    /**
     * All calculator buttons
     */
    private Button[] btns;          // 24 buttons

    /**
     * Labels for every button in the calculator
     */
    private String[] btnLabels = {  // Labels of 24 buttons
            "7", "8", "9", "+",
            "4", "5", "6", "-",
            "1", "2", "3", "x",
            ".", "0", "=", "/",
            "C", "←", "^", "√",
            "MR", "MC", "M+", "M-"
    };

    /**
     * Result of computation
     */
    private double result = 0;

    /**
     * Input number as String
     */
    private String inStr = "0";

    /**
     * The user's last inputted operator
     */
    private char lastOperator = ' ';

    /**
     * Test for keeping track of memory and it's values
     */
    private Text memoryText;

    /**
     * Value of the current item in memory
     */
    private double memory;

    /**
     *  Event handler for all 24 buttons
     */
    EventHandler handler = evt -> {
        String currentBtnLabel = ((Button)evt.getSource()).getText();
        switch (currentBtnLabel) {
            // Number buttons
            case "0": case "1": case "2": case "3": case "4":
            case "5": case "6": case "7": case "8": case "9":
                if (inStr.equals("0")) {
                    inStr = currentBtnLabel;  // no leading zero
                } else {
                    inStr += currentBtnLabel; // append input digit
                }
                userInput.setText(inStr);

                // Clear buffer if last operator is '='
                if (lastOperator == '=') {
                    result = 0;
                    lastOperator = ' ';
                }
                break;
            // Operator buttons: '+', '-', 'x', '/' and '='
            case "+":
                compute();
                lastOperator = '+';
                break;
            case "-":
                compute();
                lastOperator = '-';
                break;
            case "x":
                compute();
                lastOperator = '*';
                break;
            case "/":
                compute();
                lastOperator = '/';
                break;
            case "=":
                compute();
                lastOperator = '=';
                break;
            case ".": // Decimal button
                decimal();
                break;
            case "MR": // Memory recall button
                memoryRecall();
                break;
            case "MC": // Memory clear button
                memoryClear();
                break;
            case "M+": // Memory add button
                memoryAdd();
                break;
            case "M-": // Memory subtract button
                memorySubtract();
                break;
            case "^": // Power button
                compute();
                lastOperator = '^';
                break;
            case "√": // Square root button
                squareRoot();
                break;
            case "←": // Backspace button
                backspace();
                break;
            case "C": // Clear button
                result = 0.0;
                inStr = "0";
                lastOperator = ' ';
                userInput.setText("0");
                break;
        }
    };

    /**
     * User pushes '+', '-', '*', '/' '^' or '=' button. Performs computation on the previous result and the current
     * input number, base on the previous operator.
     */
    private void compute() {
        double inNum = Double.parseDouble(inStr);
        inStr = "0";
        if (lastOperator == ' ') {
            result = inNum;
        } else if (lastOperator == '+') {
            result += inNum;
        } else if (lastOperator == '-') {
            result -= inNum;
        } else if (lastOperator == '*') {
            result *= inNum;
        } else if (lastOperator == '/') {
            result /= inNum;
        } else if (lastOperator == '=') {
            // Keep the result for the next operation
        } else if (lastOperator == '^') {
            result = Math.pow(result, inNum);
        }
        userInput.setText(result + "");
    }

    /**
     * Adds the decimal character to the calculator
     */
    private void decimal() {
        if (!inStr.contains(".")) {
            inStr += ".";
        }
        userInput.setText(inStr);
    }

    /**
     * Allows for the memory to be recalled
     */
    private void memoryRecall() {
        this.inStr = String.valueOf(this.memory);
        this.userInput.setText(this.memory + "");
    }

    /**
     * Clears the current value of the memory
     */
    private void memoryClear() {
        this.memory = 0.0;
        memoryText.setText("Memory = " + this.memory);
    }

    /**
     * Allows for the user to add values to the memory
     */
    private void memoryAdd(){
        if (lastOperator != '=') {
            double inValue = Double.parseDouble(inStr);
            memory += inValue;
        } else {
            memory+= result;
        }
        memoryText.setText("Memory = " + memory);
    }

    /**
     * Allows for the user to subtract values to the memory
     */
    private void memorySubtract(){
        if (lastOperator != '='){
            double inValue = Double.parseDouble(inStr);
            this.memory -= inValue;
        } else {
            this.memory -= this.result;
        }
        this.memoryText.setText("Memory = " + this.memory);
    }

    /**
     * Logic for square root math problems
     */
    private void squareRoot() {
        if (lastOperator != '=') {
            result = Math.sqrt(Double.parseDouble(inStr));
            this.result = result;
            inStr = result + "";
            userInput.setText(inStr);
            lastOperator = '=';
        }
    }

    /**
     * Allows the user to backspace in the calculator to delete previous entries
     */
    private void backspace () {
        if (inStr.length() == 1 ) {
            inStr = "0";
        } else {
            inStr = inStr.substring(0, inStr.length() - 1);
        }
        userInput.setText(inStr);
    }

    /**
     * Setup for the calculator's UI
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        // Setup the Display TextField
        userInput = new TextField("0");
        userInput.setEditable(false);
        userInput.setAlignment(Pos.CENTER_RIGHT);

        // Setup a GridPane for 4x6 Buttons
        int numCols = 4;
        int numRows = 6;
        GridPane paneButton = new GridPane();
        paneButton.setPadding(new Insets(15, 0, 15, 0));  // top, right, bottom, left
        paneButton.setVgap(5);  // Vertical gap between nodes
        paneButton.setHgap(5);  // Horizontal gap between nodes
        // Setup 4 columns of equal width, fill parent
        ColumnConstraints[] btns = new ColumnConstraints[numCols];
        for (int i = 0; i < numCols; ++i) {
            btns[i] = new ColumnConstraints();
            btns[i].setHgrow(Priority.ALWAYS) ;  // Allow column to grow
            btns[i].setFillWidth(true);  // Ask nodes to fill space for column
            paneButton.getColumnConstraints().add(btns[i]);
        }

        // Setup 24 Buttons and add to GridPane; and event handler
        this.btns = new Button[24];
        for (int i = 0; i < this.btns.length; ++i) {
            this.btns[i] = new Button(btnLabels[i]);
            this.btns[i].setOnAction(handler);  // Register event handler
            this.btns[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);  // full-width
            paneButton.add(this.btns[i], i % numCols, i / numCols);  // control, col, row
        }

        memoryText = new Text("Memory = 0.0"); // Default memory output

        // Setup up the scene graph rooted at a BorderPane (of 5 zones)
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15, 15, 15, 15));  // top, right, bottom, left
        root.setTop(userInput);     // Top zone contains the TextField
        root.setCenter(paneButton); // Center zone contains the GridPane of Buttons
        root.setBottom(memoryText); // Sets the memory text output to the bottom of the calculator

        // Set up scene and stage
        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.setTitle("JavaFX Calculator");
        primaryStage.show();
    }

    /**
     * Launches the calculator app
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
