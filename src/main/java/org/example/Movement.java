package org.example;

public class Movement {
    // Position of piece to be moved.
    private final int [] fromPosition;
    private final int [] toPosition;      // Square it is to move to.

    // Constructor.  Just set the values of the instance variables.
    Movement(int row1, int col1, int row2, int col2) {
        fromPosition = new int[2];
        fromPosition[0] = row1;
        fromPosition[1] = col1;

        toPosition = new int[2];
        toPosition[0] = row2;
        toPosition[1] = col2;
    }
    boolean isJump() {
        // Test whether this move is a jump.  It is assumed that
        // the move is legal.  In a jump, the piece moves two
        // rows.  (In a regular move, it only moves one row.)
        return (fromPosition[0] - toPosition[0] == 2 || fromPosition[0] - toPosition[0] == -2);
    }
}
