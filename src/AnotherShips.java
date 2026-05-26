import java.util.Random;

// Before reading this code you have to understand that:
// row + 1 = forward direction
// row - 1 = backward direction
// column + 1 = right direction
// column - 1 = left direction

// also read info in superclass Ships.java

public class AnotherShips extends Ships {
    public int[][] quartet = new int[4][3];
    public int[][] trio1 = new int[3][3];
    public int[][] trio2 = new int[3][3];
    public int[][] duo1 = new int[2][3];
    public int[][] duo2 = new int[2][3];
    public int[] uno1 = new int[3];
    public int[] uno2 = new int[3];
    public AnotherShips(int[][] quartet, int[][] trio1, int[][] trio2, int[][] duo1, int[][] duo2, int[] uno1, int[] uno2) {
        super(quartet, trio1, trio2, duo1, duo2, uno1, uno2);
    }

    /* method to get one-deck ship */
    public static int[] getUno(String[][] botMap) {
        Random random = new Random(); 
        // isAvailable variable will keep values from checkField method and reset after using
        boolean isAvailable = false;
        String[] directions = new String[4];
        // generation a random field
        int row = random.nextInt(10);
        int col = random.nextInt(10);

        // we are checking whether it is possible to start from this direction
        isAvailable = checkField(row, col, botMap);
        if (isAvailable) {isAvailable = false;
        } else {return getUno(botMap);}

        int[] res = {row, col, 0};
        botMap[row][col] = "S";
        return res;
    }

    // Method to get as much as you like deck ship except one-deck.
    // It accepts botMap, where ship will display and range. 
    // The range is a number of ship's decks.
    public static int[][] getShip(String[][] botMap, int range) {
        Random random = new Random(); boolean isAvailable = false;
        // this array is necessary to keep the directions where ship may locate
        String[] directions = new String[4];
        // this variable will count how many directions we added to directions array
        int counter = 0;
        // generation a random field
        int row = random.nextInt(10);
        int col = random.nextInt(10);

        // we are checking whether it's possible to start to locate a ship from this field
        // we check it with checkField method
        isAvailable = checkField(row, col, botMap);
        if (isAvailable) {isAvailable = false;
        } else {return getShip(botMap, range);}

        // if it possible to add forward direction, then we add
        // "for" cycle for field iteration in forward direction
        all1: for (int i = 1; i < range+1; i++) {
            // if row in the play field's borders
            if (row + i < 10) {
                // then we are checking it with checkField method
                isAvailable = checkField(row + i, col, botMap);
                if (isAvailable) {isAvailable = false; 
                } else {break all1;}
            } else {break all1;}
            if (i == range) {
                // if we done cycle, then we add forward direction to array and update counter
                directions[counter] = "forward";
                counter += 1;
            }
        }

        // if it possible to add backward direction, then we add
        // "for" cycle for field iteration in backward direction
        all2: for (int i = 1; i < range+1; i++) {
            // if row in the play field's borders
            if (row - i >= 0) {
                // then we are checking it with checkField method
                isAvailable = checkField(row - i, col, botMap);
                if (isAvailable) {isAvailable = false; 
                } else {break all2;}
            } else {break all2;}
            if (i == range) {
                // if we done cycle, then we add backward direction to array and update counter
                directions[counter] = "backward";
                counter += 1;
            }
        }

        // if it possible to add right direction, then we add
        // "for" cycle for field iteration in right direction
        all3: for (int i = 1; i < range+1; i++) {
            // if row in the play field's borders
            if (col + i < 10) {
                // then we are checking it with checkField method
                isAvailable = checkField(row, col + i, botMap);
                if (isAvailable) {isAvailable = false; 
                } else {break all3;}
            } else {break all3;}
            if (i == range) {
                // if we done cycle, then we add right direction to array and update counter
                directions[counter] = "right";
                counter += 1;
            }
        }

        // if it possible to add left direction, then we add
        // "for" cycle for field iteration in left direction
        all4: for (int i = 1; i < range+1; i++) {
            // if row in the play field's borders
            if (col - i >= 0) {
                // then we are checking it with checkField method
                isAvailable = checkField(row, col - i, botMap);
                if (isAvailable) {isAvailable = false; 
                } else {break all4;}
            } else {break all4;}
            if (i == range) {
                // if we done cycle, then we add left direction to array and update counter
                directions[counter] = "left";
                counter += 1;
            }
        }
        
        // we are choose a random direction from directions array
        int intRes = random.nextInt(counter == 0 ? 1 : counter)+1;
        // because array index is beginning from 0 we subtract 1
        intRes -= 1;
        // This array will keep ship. We will return this array.
        int[][] res = new int[range][3];
        // tag first field of the ship as S (ship mark on the maps)
        botMap[row][col] = "S";
        if (directions[intRes] == null) return getShip(botMap, range);
        // random possible direction
        String strRes = directions[intRes];
        // fill first field to the array
        res[0][0] = row; res[0][1] = col;
        // if random possible direction is forward
        if (strRes.equals("forward")) {
            // than we add relevant fields to the array
            for (int i = 0; i < res.length-1; i++) {
                res[i+1][0] = row + (i+1);
                res[i+1][1] = col;
            }
            // if random possible direction is backward
        } else if (strRes.equals("backward")) {
            // than we add relevant fields to the array
            for (int i = 0; i < res.length-1; i++) {
                res[i+1][0] = row - (i+1);
                res[i+1][1] = col;
            }
            // if random possible direction is right
        } else if (strRes.equals("right")) {
            // than we add relevant fields to the array
            for (int i = 0; i < res.length-1; i++) {
                res[i+1][0] = row;
                res[i+1][1] = col + (i+1);
            }
            // if random possible direction is left
        } else if (strRes.equals("left")) {
            // than we add relevant fields to the array
            for (int i = 0; i < res.length-1; i++) {
                res[i+1][0] = row;
                res[i+1][1] = col - (i+1);
            }
        }

        // we are filling third index in array zeros
        // 0 means that ship's deck doesn't destroyed
        for (int i = 0; i < res.length; i++) {
            res[i][2] = 0;
        }
        // we set a random values to the array
        for (int i = 0; i < res.length; i++) {botMap[res[i][0]][res[i][1]] = "S";}

        // return it
        return res;
    }

    // Method to check fields near a deck for another ships
    // This method accepts deck's coordinates and map where method will find fields near the deck
    public static boolean checkField(int row, int col, String[][] botMap) {
        boolean isAvailable = false; boolean res = false;
        // занято ли само поле
        if (!(botMap[row][col].equals("S"))) {
            // is there an S in front?
            if (row+1 < 10) {
                if (!(botMap[row+1][col].equals("S"))) {
                    isAvailable = true;
                } else {return false;}
            } else {isAvailable = true;}
            if (isAvailable) {
                isAvailable = false;
                if(row-1 >= 0) {
                    // is there an S in back?
                    if (!(botMap[row-1][col].equals("S"))) {
                        isAvailable = true;
                    } else {return false;}
                } else {isAvailable = true;}
            }
            if (isAvailable) {
                isAvailable = false;
                // is there an S in right?
                if (col+1 < 10) {
                    if (!(botMap[row][col+1].equals("S"))) {
                        isAvailable = true;
                    } else {return false;}
                } else {isAvailable = true;}
            }
            if (isAvailable) {
                isAvailable = false;
                // is there an S in left?
                if (col-1 >= 0) {
                    if (!(botMap[row][col-1].equals("S"))) {
                        isAvailable = true;
                    } else {return false;}
                } else {isAvailable = true;}
            }
            if (isAvailable) {res = true;}
        } else {return false;}
        return res;
    }
}
