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
    private TextField userInput;    // display textfield
    private Button[] btns;          // 24 buttons
    private String[] btnLabels = {  // Labels of 24 buttons
            "7", "8", "9", "+",
            "4", "5", "6", "-",
            "1", "2", "3", "x",
            ".", "0", "=", "/",
            "C", "←", "^", "√",
            "MR", "MC", "M+", "M-"
    };
    // For computation
    private double result = 0;      // Result of computation
    private String inStr = "0";  // Input number as String
    // Previous operator: ' '(nothing), '+', '-', '*', '/', '='
    private char lastOperator = ' ';

    private Text memoryText;

    private double memory;

    // Event handler for all the 24 Buttons
    EventHandler handler = evt -> {
        String currentBtnLabel = ((Button)evt.getSource()).getText();
        switch (currentBtnLabel) {
            // Number buttons
            case "0": case "1": case "2": case "3": case "4":
            case "5": case "6": case "7": case "8": case "9":
            case ".":
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
            case "MR":
                memoryRecall();
                break;
            case "MC":
                memoryClear();
                break;
            case "M+":
                memoryAdd();
                break;
            case "M-":
                memorySubtract();
                break;
            case "^":
                compute();
                lastOperator = '^';
                break;



            // Clear button
            case "C":
                result = 0;
                inStr = "0";
                lastOperator = ' ';
                userInput.setText("0");
                break;
        }
    };

    // User pushes '+', '-', '*', '/' or '=' button.
    // Perform computation on the previous result and the current input number,
    // based on the previous operator.
    private void compute() {
        int inNum = Integer.parseInt(inStr);
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
        }
        else if (lastOperator == '^'){
            result = Math.pow(result, inNum);
        }
        userInput.setText(result + "");
    }

    private void memoryRecall() {
        this.inStr = String.valueOf(this.memory);
        this.userInput.setText(this.memory + "");
    }

    private void memoryClear() {
        this.memory = 0.0;
        memoryText.setText("Memory = " + this.memory);
    }

    private void memoryAdd(){
        if (lastOperator != '=') {
            double inValue = Double.parseDouble(inStr);
            memory += inValue;
        } else{
            memory+= result;
        }
        memoryText.setText("Memory = " + memory);
    }

    private void memorySubtract(){
        if (lastOperator != '='){
            double inValue = Double.parseDouble(inStr);
            this.memory -= this.result;
        }else {
            this.memory -= this.result;
        }
        this.memoryText.setText("Memory = " + this.memory);
    }

    // Setup the UI
    @Override
    public void start(Stage primaryStage) {
        // Setup the Display TextField
        userInput = new TextField("0");
        userInput.setEditable(false);
        userInput.setAlignment(Pos.CENTER_RIGHT);

        // Setup a GridPane for 4x4 Buttons
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

        memoryText = new Text("Memory = 0.0");

        // Setup up the scene graph rooted at a BorderPane (of 5 zones)
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15, 15, 15, 15));  // top, right, bottom, left
        root.setTop(userInput);     // Top zone contains the TextField
        //root.setBottom(memoryOutput);
        root.setCenter(paneButton); // Center zone contains the GridPane of Buttons
        root.setBottom(memoryText);

        // Set up scene and stage
        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.setTitle("JavaFX Calculator");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
