import java.util.HashMap;

// Before reading this code you have to understand that:
// row + 1 = forward direction
// row - 1 = backward direction
// column + 1 = right direction
// column - 1 = left direction

// also read info in superclass Ships.java

public class OurShips extends Ships {
    public int[][] quartet = new int[4][3];
    public int[][] trio1 = new int[3][3];
    public int[][] trio2 = new int[3][3];
    public int[][] duo1 = new int[2][3];
    public int[][] duo2 = new int[2][3];
    public int[] uno1 = new int[3];
    public int[] uno2 = new int[3];

    public OurShips(int[][] quartet, int[][] trio1, int[][] trio2, int[][] duo1, int[][] duo2, int[] uno1, int[] uno2) {
        super(quartet, trio1, trio2, duo1, duo2, uno1, uno2);
    }

    // a method to get four-decked ship
    public static int[][] getQuartet(String[][] yourMap) {
        // this boolean variable will store checkField method result
        boolean isAvailable = false;
        /* 
        We create an array, which we will return later.
        It will keep four four-decked ship's coordinates.
        */
        int[][] quartet = new int[4][3];
        /* 
        Also we create a HashMap and direction variable for storing
        direction and future directions
        */
        HashMap<String, int[]> checkMap = new HashMap<>();
        // this variable also will store the direction
        String[] direction = {""};

        System.out.println("Enter the coordinates where your four-deck ship will be located");
        Game.printMap(yourMap, Game.mod);
        // we use getCoordinate method for getting first ship's coordinate
        quartet[0] = Game.getCoordinate(yourMap);
        // checking if the user entered /clear in getCoordinate method
        if (quartet[0] == null) {return getQuartet(yourMap);}
        yourMap[quartet[0][0]][quartet[0][1]] = "S";
        Game.printMap(yourMap, Game.mod);
        while(!isAvailable) {
            // we use getCoordinate method for getting second ship's coordinate
            quartet[1] = Game.getCoordinate(yourMap);
             // checking if the user entered /clear in getCoordinate method
            if (quartet[1] == null) {
                // if player entered /clear (quartet[1] is null), then we return all previously set values ​​to value D
                yourMap[quartet[0][0]][quartet[0][1]] = "D";
                return getQuartet(yourMap);
            }
            // we're checking field which player entered with checkField method
            isAvailable = checkField(yourMap, direction, checkMap, quartet, 0, 1);
            if (isAvailable) {
                // if possible to locate than we locate it to our map
                yourMap[quartet[1][0]][quartet[1][1]] = "S";
            } else {
                // else we ask player for enter correct coordinates
                System.out.println("Please enter correct ship's coordinates");
            }
        }
        Game.printMap(yourMap, Game.mod);
        // we reset isAvailable
        isAvailable = false;
        while(!isAvailable) {
            // we use getCoordinate method to get second ship's coorinate
            quartet[2] = Game.getCoordinate(yourMap);
             // checking if the user entered /clear in getCoordinate method
            if (quartet[2] == null) {
                // if player entered /clear (quartet[1] is null), then we return all previously set values to value D
                yourMap[quartet[0][0]][quartet[0][1]] = "D";
                yourMap[quartet[1][0]][quartet[1][1]] = "D";
                // we return this same one method if user entered /clear
                return getQuartet(yourMap);
            }
            isAvailable = checkField(yourMap, direction, checkMap, quartet, 1, 2);
            if (isAvailable) {
                // if possible to locate than we locate it to our map
                // and if player didn't enter /clear, then we locate second ship's deck
                yourMap[quartet[2][0]][quartet[2][1]] = "S";
            } else {
                // else we ask player for enter correct coordinates
                System.out.println("Please enter correct coordinates of ship");
            }
        }
        Game.printMap(yourMap, Game.mod);
        // we reset isAvailable
        isAvailable = false;
        while(!isAvailable) {
            // we use getCoordinate method to get coordinates of third ship's deck
            quartet[3] = Game.getCoordinate(yourMap);
            // checking if the user entered /clear
            if (quartet[3] == null) {
                // if player entered /clear (quartet[1] is null), then we return all previously set values to value D
                yourMap[quartet[0][0]][quartet[0][1]] = "D";
                yourMap[quartet[1][0]][quartet[1][1]] = "D";
                yourMap[quartet[2][0]][quartet[2][1]] = "D";
                // we return this same one method if player entered /clear
                return getQuartet(yourMap);
            }
            isAvailable = checkField(yourMap, direction, checkMap, quartet, 2, 3);
            if (isAvailable) {
                // if player
                yourMap[quartet[3][0]][quartet[3][1]] = "S";
            } else {
                // if possible to locate than we locate it to our map
                // and if player didn't enter /clear, then we locate second ship's deck
                System.out.println("Please enter correct coordinates of ship");
            }
        }
        System.out.println("Your four-deck ship: ");
        Game.printMap(yourMap, Game.mod);
        // return result
        return quartet;
    }


    // a method to get three-deck ship
    public static int[][] getTrio(String[][] yourMap) {
        // this boolean variable will store checkField method result
        boolean isAvailable = false;
        /*
        We create an array, which we will return later.
        It will keep four four-decked ship's coordinates.
        */
        int[][] trio = new int[3][3];
        /*
        Also we create a HashMap and direction variable for storing
        direction and future directions
        */
        HashMap<String, int[]> checkMap = new HashMap<>();
        // this variable also will store the direction
        String[] direction = {""};

        System.out.println("Enter the coordinates where your three-decked ship will be located");
        Game.printMap(yourMap, Game.mod);

        // while isAvailable isn't true (while trio[0] not possible to locate)
        while(!(isAvailable)) {
            // we use getCoordinate method for getting first ship's deck
            trio[0] = Game.getCoordinate(yourMap);
            // if player entered /clear, then trio[0] will be 0
            // we return this same one method if first deck's coordinates is null
            if (trio[0] == null) {return getTrio(yourMap);}
            // we use checkOneField method to check first deck of the ship
            isAvailable = checkOneField(yourMap, trio[0]);
            if (!(isAvailable)) {System.out.println("Please enter correct coordinates of ship");}
        }
        // checking if user entered /clear in getCoordinate method
        if (trio[0] == null) {return getTrio(yourMap);}
        // we locate first ship's deck
        yourMap[trio[0][0]][trio[0][1]] = "S";
        // we reset isAvailable
        isAvailable = false;
        Game.printMap(yourMap, Game.mod);
        while(!isAvailable) {
            // we use getCoordinate method to get second ship deck's coordinates
            trio[1] = Game.getCoordinate(yourMap);
            if (trio[1] == null) {
                // if player entered /clear (quartet[1] is null), then we return all previously set values to value D
                yourMap[trio[0][0]][trio[0][1]] = "D";
                return getTrio(yourMap);
            }
            // we use checkField method to check second ship deck's coordinate
            isAvailable = checkField(yourMap, direction, checkMap, trio, 0, 1);
            if (isAvailable) {
                yourMap[trio[1][0]][trio[1][1]] = "S";
            } else {
                System.out.println("Please enter correct coordinates of ship");
            }
        }

        // AND SO ON for another methods except uno method where we have only first ship deck's coordinate

        Game.printMap(yourMap, Game.mod);
        isAvailable = false;
        while(!isAvailable) {
            trio[2] = Game.getCoordinate(yourMap);
            if (trio[2] == null) {
                yourMap[trio[0][0]][trio[0][1]] = "D";
                yourMap[trio[1][0]][trio[1][1]] = "D";
                return getTrio(yourMap);
            }
            isAvailable = checkField(yourMap, direction, checkMap, trio, 1, 2);
            if (isAvailable) {
                yourMap[trio[2][0]][trio[2][1]] = "S";
            } else {
                System.out.println("Please enter correct coordinates of ship");
            }
        }
        System.out.println("Your three-decked ship: ");
        Game.printMap(yourMap, Game.mod);
        return trio;
    }



    // a method to get two-deck ship
    public static int[][] getDuo(String[][] yourMap) {
        boolean isAvailable = false;
        int[][] duo = new int[2][3];
        HashMap<String, int[]> checkMap = new HashMap<>();
        String[] direction = {""};

        System.out.println("Enter the coordinates where your double-decked ship will be located");
        Game.printMap(yourMap, Game.mod);
        while(!(isAvailable)) {
            duo[0] = Game.getCoordinate(yourMap);
            if (duo[0] == null) {return getDuo(yourMap);}
            isAvailable = checkOneField(yourMap, duo[0]);
            if (!(isAvailable)) {System.out.println("Please enter correct coordinates of ship");}
        }
        if (duo[0] == null) {return getDuo(yourMap);}
        yourMap[duo[0][0]][duo[0][1]] = "S";
        isAvailable = false;
        Game.printMap(yourMap, Game.mod);
        while(!isAvailable) {
            duo[1] = Game.getCoordinate(yourMap);
            if (duo[1] == null) {
                yourMap[duo[0][0]][duo[0][1]] = "D";
                return getDuo(yourMap);
            }
            isAvailable = checkField(yourMap, direction, checkMap, duo, 0, 1);
            if (isAvailable) {
                yourMap[duo[1][0]][duo[1][1]] = "S";
            } else {
                System.out.println("Please enter correct coordinates of ship");
            }
        }
        System.out.println("Your double-decked ship: ");
        Game.printMap(yourMap, Game.mod);
        return duo;
    }

    // method to get one-decked ship
    public static int[] getUno(String[][] yourMap) {
        boolean isAvailable = false;
        int[] uno = new int[3];
        HashMap<String, int[]> checkMap = new HashMap<>();
        String[] direction = {""};

        System.out.println("Enter the coordinates where your single-deck ship will be located");
        Game.printMap(yourMap, Game.mod);
        while(!(isAvailable)) {
            uno = Game.getCoordinate(yourMap);
            if (uno == null) {return getUno(yourMap);}
            isAvailable = checkOneField(yourMap, uno);
            if (isAvailable) {yourMap[uno[0]][uno[1]] = "S";}
            if (!(isAvailable)) {System.out.println("Please enter correct coordinates of ship");}
        }
        System.out.println("Your single-deck ship: ");
        Game.printMap(yourMap, Game.mod);
        return uno;
    }

    // a method for checking the possibility of installing one deck in its place
    public static boolean checkOneField(String[][] yourMap, int[] array) {
        boolean is = false; boolean res = false;
        int row = array[0];
        int col = array[1];
        if (!(yourMap[row][col].equals("S"))) {
            if (row - 1 >= 0) {
                if (!(yourMap[row-1][col].equals("S"))) {
                    is = true;
                } else {return false;}
            } else {is = true;}
            if (is == true) {
                is = false;
                if (row + 1 < 10) {
                    if (!(yourMap[row+1][col].equals("S"))) {
                        is = true;
                    } else {return false;}
                } else {is = true;}
            }
            if (is == true) {
                is = false;
                if (col - 1 >= 0) {
                    if (!(yourMap[row][col-1].equals("S"))) {
                        is = true;
                    } else {return false;}
                } else {is = true;}
            }
            if (is == true) {
                is = false;
                if (col + 1 < 10) {
                    if (!((yourMap[row][col+1].equals("S")))) {
                        is = true;
                    } else {return false;}
                } else {is = true;}
            }
            if (is == true) {res = true;}
        } else {return false;}
        return res;
    }

    // a method to check possibility of installing the ship's deck
    public static boolean checkField(String[][] yourMap, String[] direction, HashMap<String, int[]> checkMap, int[][] array, int range1, int range2) {
        int counter = 1; boolean isAvailable = false; boolean res = false;
        int newRow = array[range2][0];
        int newCol = array[range2][1];
        int prevRow = array[range1][0];
        int prevCol = array[range1][1];
        if (!(yourMap[newRow][newCol].equals("S"))) {
            // we check if there are fields in front
            if (newRow + 1 < yourMap.length) {
                if (!(yourMap[newRow + 1][newCol].equals("S"))) {
                    isAvailable = true;
                } else {
                    if (counter <= 1) {
                        isAvailable = true;
                        counter += 1;
                    } else {return false;}
                }
            } else {
                isAvailable = true;
            }
            if (isAvailable) {
                isAvailable = false;
                // we check if there are fields in back
                if (newRow - 1 >= 0) {
                    if (!(yourMap[newRow - 1][newCol].equals("S"))) {
                        isAvailable = true;
                    } else {
                        if (counter <= 1) {
                            isAvailable = true;
                            counter += 1;
                        } else {return false;}
                    }
                } else {isAvailable = true;}
            } else {return false;}

            if (isAvailable) {
                isAvailable = false;
                // we check if there are fields in right
                if (newCol + 1 < yourMap[newRow].length) {
                    if (!(yourMap[newRow][newCol + 1].equals("S"))) {
                        isAvailable = true;
                    } else {
                        if (counter <= 1) {
                            isAvailable = true;
                            counter += 1;
                        } else {return false;}
                    }
                } else {isAvailable = true;} 
            } else {return false;}

            if (isAvailable) {
                isAvailable = false;
                    // we check if there are fields in left
                    if (newCol - 1 >= 0) {
                        if (!(yourMap[newRow][newCol - 1].equals("S"))) {
                            isAvailable = true;
                        } else {
                            if (counter <= 1) {
                                isAvailable = true;
                                counter += 1;
                            } else {return false;}
                        }
                    } else {isAvailable = true;}
            } else {return false;}

            if (isAvailable) {
                if (direction[0].isEmpty()) {
                    // if second field is equals first field (coordinates), then we return false
                    if (newRow != prevRow || newCol != prevCol) {

                        // we check if add forward direction
                        if (prevRow + 1 < yourMap.length) {
                            // if yes, then we add
                            checkMap.put("forward", new int[] {prevRow + 1, prevCol});
                            // if our field corresponds to this cell, then we set the direction fb
                            if (newRow == checkMap.get("forward")[0] && newCol == checkMap.get("forward")[1]) {
                                direction[0] = "fb";
                                if (newRow + 1 < 10) {
                                    checkMap.put("forward", new int[] {newRow + 1, newCol});
                                } else {
                                    checkMap.remove("forward");
                                }
                            }
                        }

                        // we check if add backward direction
                        if (prevRow - 1 >= 0) {
                            // if yes, then we add
                            checkMap.put("backward", new int[] {prevRow - 1, prevCol});
                            // if our field corresponds to this cell, then we set the direction fb
                            if (newRow == checkMap.get("backward")[0] && newCol == checkMap.get("backward")[1]) {
                                direction[0] = "fb";
                                if (newRow - 1 >= 0) {
                                    checkMap.put("backward", new int[] {newRow - 1, newCol});
                                } else {
                                    checkMap.remove("backward");
                                }
                            }
                        }

                        // we check if add right direction
                        if (prevCol + 1 < yourMap[prevRow].length) {
                            // if yes, then we add
                            checkMap.put("right", new int[] {prevRow, prevCol + 1});
                            // if our field corresponds to this cell, then we set the direction rl
                            if (newRow == checkMap.get("right")[0] && newCol == checkMap.get("right")[1]) {
                                direction[0] = "rl";
                                if (newCol + 1 < 10) {
                                    checkMap.put("right", new int[] {newRow, newCol + 1});
                                } else {
                                    checkMap.remove("right");
                                }
                            }
                        }

                        // we check if add left direction
                        if (prevCol - 1 >= 0) {
                            // if yes, then we add
                            checkMap.put("left", new int[] {prevRow, prevCol - 1});
                            // if our field corresponds to this cell, then we set the direction rl
                            if (newRow == checkMap.get("left")[0] && newCol == checkMap.get("left")[1]) {
                                direction[0] = "rl";
                                if (newCol - 1 >= 0) {
                                    checkMap.put("left", new int[] {newRow, newCol - 1});
                                } else {
                                    checkMap.remove("left");
                                }
                            }
                        }

                        // After checks for direction we check if direction is empty.
                        // If yes, then it not near first ship's deck and we return false
                        if (!(direction[0].isEmpty())) {
                            if (direction[0].equals("fb")) {
                                checkMap.remove("right");
                                checkMap.remove("left");
                                res = true;
                            } else if (direction[0].equals("rl")) {
                                checkMap.remove("forward");
                                checkMap.remove("backward");
                                res = true;
                            } else {return false;}
                        } else {return false;}
                    } else {return false;}
                } else {
                        int[] get;

                        // if direction is forward or backward (fb), then we check our HashMap with possible directions
                        // if there is forward direction, then we put forward to checkMap
                        if (direction[0].equals("fb")) {
                            if (checkMap.containsKey("forward")) {
                                get = checkMap.get("forward");
                                if (get[0] == newRow && get[1] == newCol) {
                                    if (newRow + 1 < 10) {
                                        checkMap.put("forward", new int[]{newRow + 1, newCol});
                                    } else {
                                        // if forward direction out of play field borders, then we remove it from our checkMap
                                        checkMap.remove("forward");
                                    }
                                    res = true;
                                }
                            }
                            if (!res && checkMap.containsKey("backward")) {
                                get = checkMap.get("backward");
                                if (get[0] == newRow && get[1] == newCol) {
                                    if (newRow - 1 >= 0) {
                                        checkMap.put("backward", new int[]{newRow - 1, newCol});
                                    } else {
                                        // if backward direction out of play field borders, then we remove it from our checkMap
                                        checkMap.remove("backward");
                                    }
                                    res = true;
                                }
                            }
                        } else if (direction[0].equals("rl")) {
                            if (checkMap.containsKey("right")) {
                                get = checkMap.get("right");
                                if (get[0] == newRow && get[1] == newCol) {
                                    if (newCol + 1 < 10) {
                                        checkMap.put("right", new int[]{newRow, newCol + 1});
                                    } else {
                                        // if right direction out of play field borders, then we remove it from our checkMap
                                        checkMap.remove("right");
                                    }
                                    res = true;
                                }
                            }
                            if (!res && checkMap.containsKey("left")) {
                                get = checkMap.get("left");
                                if (get[0] == newRow && get[1] == newCol) {
                                    if (newCol - 1 >= 0) {
                                        checkMap.put("left", new int[]{newRow, newCol - 1});
                                    } else {
                                        // if left direction out of play field borders, then we remove it from our checkMap
                                        checkMap.remove("left");
                                    }
                                    res = true;
                                }
                            }
                        }
                    }
            } else {return false;}
        } else {return false;}
        return res;
    }
}