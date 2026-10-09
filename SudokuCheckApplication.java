//*****************************************************************************
//
// Assignment: Assignment 3 - Sudoku Checker
// Author: Joshua Spencer
// Course: CPT 237
// Section: W38
// Semester: Fall 2026
//
// Description: This JavaFX application accepts a 9x9 Sudoku grid as string 
// input, parses it into a 2D integer array, and checks for valid input data 
// and Sudoku row/column/subgrid rules.
//
//*****************************************************************************

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

public class SudokuCheckApplication extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Create addressable controls
        TextArea taInput = new TextArea("Input your Sudoku answer here:");
        TextArea taOutput = new TextArea("The results will display here:");

        // Create the GridPane pane
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setVgap(5);

        // Place nodes in the GridPane pane
        pane.add(new Label("Input Sudoku:"), 0, 0);
        pane.add(taInput, 0, 1);

        // Create FlowPane pane
        FlowPane btnPane = new FlowPane();
        btnPane.setAlignment(Pos.CENTER);
        btnPane.setPadding(new Insets(10, 10, 10, 10));
        btnPane.setHgap(10);

        // Create buttons and event handlers
        Button btnCheck = new Button("Check");
        btnCheck.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String input = taInput.getText().trim();
                
                // Parse and check the input grid using a 2D Array
                String result = validateSudokuInput(input);
                taOutput.setText(result);
            }
        });

        // Clear Button
        Button btnClear = new Button("Clear");
        btnClear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                taInput.setText("");
                taOutput.setText("");
            }
        });

        // Place Buttons on the FlowPane and place FlowPane on GridPane
        btnPane.getChildren().addAll(btnCheck, btnClear);
        pane.add(btnPane, 0, 2);

        // Place nodes in the GridPane pane
        pane.add(new Label("Check result:"), 0, 3);
        pane.add(taOutput, 0, 4);

        // Create scene and place it on the stage
        Scene scene = new Scene(pane);
        primaryStage.setTitle("CPT 237 Sudoku Checker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Parses input string into a 2D integer array and checks logic.
     */
    private String validateSudokuInput(String input) {
        if (input.isEmpty()) {
            return "ERROR: Input is empty. Please enter a 9x9 Sudoku grid.";
        }

        // Split text by lines (handles both \n and \r\n)
        String[] lines = input.split("\\r?\\n");
        
        // Ensure exactly 9 rows
        if (lines.length != 9) {
            return "ERROR: Bad Input! The grid must have exactly 9 rows. Found: " + lines.length;
        }

        // 2D Array to store the 9x9 grid
        int[][] grid = new int[9][9];

        // Process input string into 2-D Array
        for (int row = 0; row < 9; row++) {
            // Split line by whitespace or commas
            String[] tokens = lines[row].trim().split("\\s+");
            
            if (tokens.length != 9) {
                return "ERROR: Bad Input! Row " + (row + 1) + " must contain exactly 9 numbers. Found: " + tokens.length;
            }

            for (int col = 0; col < 9; col++) {
                try {
                    int val = Integer.parseInt(tokens[col]);
                    
                    // Validate range (1 to 9)
                    if (val < 1 || val > 9) {
                        return "ERROR: Bad Input! Value '" + val + "' at row " + (row + 1) + ", col " + (col + 1) + " is out of range (1-9).";
                    }
                    
                    grid[row][col] = val; // Populate 2D Array
                } catch (NumberFormatException ex) {
                    return "ERROR: Bad Input! Invalid character '" + tokens[col] + "' at row " + (row + 1) + ", col " + (col + 1) + ".";
                }
            }
        }

        // If data parsing passes, check if the grid represents a valid solution
        if (isValidSudokuSolution(grid)) {
            return "SUCCESS: Valid Sudoku Solution!";
        } else {
            return "INVALID: Correct input format, but the solution violates Sudoku rules (duplicate numbers in row, column, or 3x3 box).";
        }
    }

    /**
     * Validates whether the 2D array satisfies Sudoku constraints.
     */
    private boolean isValidSudokuSolution(int[][] grid) {
        // Check rows and columns
        for (int i = 0; i < 9; i++) {
            Set<Integer> rowSet = new HashSet<>();
            Set<Integer> colSet = new HashSet<>();

            for (int j = 0; j < 9; j++) {
                // Row check
                if (!rowSet.add(grid[i][j])) return false;
                // Column check
                if (!colSet.add(grid[j][i])) return false;
            }
        }

        // Check 3x3 subgrids
        for (int boxRow = 0; boxRow < 9; boxRow += 3) {
            for (int boxCol = 0; boxCol < 9; boxCol += 3) {
                Set<Integer> boxSet = new HashSet<>();
                for (int r = 0; r < 3; r++) {
                    for (int c = 0; c < 3; c++) {
                        if (!boxSet.add(grid[boxRow + r][boxCol + c])) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {
        launch(args);
    }
}