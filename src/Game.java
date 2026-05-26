import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

// Before reading this code you have to understand that:
// row + 1 = forward direction
// row - 1 = backward direction
// column + 1 = right direction
// column - 1 = left direction

public class Game {
    // constants to storing colors to display colored text in the terminal
    public static final String RESET = "\u001B[0m";
    public static final String GRAY = "\u001B[90m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";

    // player's ship map and bot's ship map
    static String[][] yourMap = new String[10][10];
    static String[][] botMap = new String[10][10];
    // these classes will keep ships and methods to work with it
    static AnotherShips anotherShips; static OurShips ourShips;
    static int mod = 1; // by default 1
    // one scanner per class
    static Scanner scanner = new Scanner(System.in);
    // initialize two fields in constructor: our field and the bot's field
    public Game(int mod) {
        // we set the mode which player entered
        this.mod = mod;
        // filling in the intire field with dots D (sea)
        for (int i = 0; i < yourMap.length; i++) {
            for (int j = 0; j < yourMap[i].length; j++) {
                yourMap[i][j] = "D";
            }
        }
        for (int i = 0; i < botMap.length; i++) {
            for (int j = 0; j < botMap[i].length; j++) {
                botMap[i][j] = "D";
            }
        }
        // read description about constructor in Ships class
        // we create an AnotherShips object where the bot ships will be stored
        anotherShips = new AnotherShips(AnotherShips.getShip(botMap, 4), AnotherShips.getShip(botMap, 3), AnotherShips.getShip(botMap, 3), AnotherShips.getShip(botMap, 2), AnotherShips.getShip(botMap, 2), AnotherShips.getUno(botMap), AnotherShips.getUno(botMap));
        // we create an OurShips object where the our ships will be stored
        ourShips = new OurShips(OurShips.getQuartet(yourMap), OurShips.getTrio(yourMap), OurShips.getTrio(yourMap), OurShips.getDuo(yourMap), OurShips.getDuo(yourMap), OurShips.getUno(yourMap), OurShips.getUno(yourMap));


        for (int i = 0; i < 10; i++) {System.out.println();}
        // print the resulting
        System.out.println("Your map:");
        printMap(yourMap, mod);
        //        printMap(botMap); //
    }

    public static void game(String[][] yourMap, String[][] botMap) {
        /* This list is necessary to save the coordinates that the bot will not access.
         According to the rules cannot set ships which touch another ships
          accordingly, near the killed ship senselessly to hit fields
          because the rules don't allow ships to be placed there. After every
          killed ship deck we will enter coordinates to this list, which
          after total annihilation ship will output and tag as checked */
        ArrayList<Integer> noBotWillHit = new ArrayList<>();

        /* our shots map */
        String[][] ourHitField = new String[10][10];
        /* an array to keep available cells where bot can hit */
        int[][] availableFields = new int[100][2];
        /* bot's shots map */
        String[][] botsMapHit = new String[10][10];

        /* The smart bot when hit into the ship start to groping him to total destroy.
           This groping called hunting mode. */
        boolean hunting = false;
        /* variables which keep  potential ship's decks when hunting mode, by default it equals -1 */
        int targetRow = -1;
        int targetCol = -1;

        /* Very important variables which keep possible direction in which located ship's decks when hunting mode.
        * By game rules we can locate ship only VERTICAL or HORIZONTAL. If we after first hit to ship
        * moved to right field and found there another one ship's deck then there's no meaning in cheking deck located up or down, because
        * we can assuredly say that ship is located horizontal. By default it equals 0 */
        int directionRow = 0;
        int directionCol = 0;

        /* a variable which keep coordinates of the first hit on the ship */
        int[] firstHit = new int[2];
        /* a variable for ship which wounded */
        String shipWrecked;
        /* variable which keep game result (draw, win, loss) */
        String isStrAll;
        /* ship status (destroyed, wounded) */
        String shipStatus;
        Random random = new Random();

        /* we fill our map with emptiness */
        for (int i = 0; i < ourHitField.length; i++) {
            for (int j = 0; j < ourHitField[i].length; j++) {
                ourHitField[i][j] = "O";
                botsMapHit[i][j] = "O";
                availableFields[i * 10 + j] = new int[]{i, j};
            }
        }

        /* if it becomes true then game is over */
        boolean isAll = false;
        System.out.println();

        /* game */
        while (!isAll) {
            /* coordinates where we want to hit */
            int[] coordinates = null;
            /* if user enter /clear, then coordinates will be null and user will can
            * choose coordinates for the hit again */
            while (coordinates == null) {
                coordinates = getCoordinate(yourMap);
            }
            /* hit's row and hit's column */
            int row = coordinates[0];
            int col = coordinates[1];

            /* if ship was located in field where player hit then we tag it on the map */
            if (botMap[row][col].equals("S")) {
                ourHitField[row][col] = "X";
                /* we find the ship which we hited by whatsShip method */
                shipWrecked = whatsShip(botMap, row, col, anotherShips);
                /* tag hit */
                markShipHit(shipWrecked, row, col, anotherShips);
                /* output result to user */
                shipStatus = isShipKilled(shipWrecked, anotherShips) ? "killed" : "wounded";
                if (shipStatus.equals("killed")) {
                    System.out.println("The enemy ship has been destroyed!");
                    printMap(ourHitField, mod);
                } else {
                    System.out.println("The enemy ship is wounded!");
                    printMap(ourHitField, mod);

                }
                /* if we didn't hit the target then tag it and output to user */
            } else {
                System.out.println("A miss!");
                ourHitField[row][col] = "D";
                printMap(ourHitField, mod);
            }

            /* a move transfer to the bot, for imitation that bot is thinking was made delay per second */
            System.out.println("Bot's try...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("interruptedexception");
                e.printStackTrace();
            }

            /* coordinates where bot will hit */
            int[] botsCoordinates = null;

            /* if we're already feeling the ship */
            if (hunting) {
                /* if potential ship's deck in play field borders and isn't O (nothing) */
                if (targetRow >= 0 && targetRow < 10 && targetCol >= 0 && targetCol < 10 && botsMapHit[targetRow][targetCol].equals("O")) {
                    /* check this field */
                    botsCoordinates = new int[]{targetRow, targetCol};/*  */
                } else {
                    /* else we create an array which will keep directions where can be located bot's ship deck */
                    int[][] dirs = {{-1,0}, {1,0}, {0,-1}, {0,1}};
                    /* possible fields where ship's deck can be */
                    ArrayList<int[]> candidates = new ArrayList<>();
                    /* we're filling in this list with possible fields. */
                    for (int[] d : dirs) {
                        int nr = firstHit[0] + d[0];
                        int nc = firstHit[1] + d[1];
                        /* if they're within the play field borders and aren't shot through */
                        if (nr >= 0 && nr < 10 && nc >= 0 && nc < 10 && botsMapHit[nr][nc].equals("O")) {
                            candidates.add(new int[]{nr, nc});
                        }
                    }
                    /* if we found potential fields where can be ship's deck then check one of it */
                    if (!candidates.isEmpty()) {
                        /* choose one of it by random */
                        int[] chosen = candidates.get(random.nextInt(candidates.size()));
                        botsCoordinates = chosen;
                        /* Using this formula, we can determine the direction of the ship's location.
                        *  For example, if firstHit = { 5, 5 } and we checked right field
                        *  chosen = { 5, 6 } then according this formula we can calculate direction.
                        *  Specifically in this case direction row will be 0 and direction column will be 1 */
                        directionRow = chosen[0] - firstHit[0];
                        directionCol = chosen[1] - firstHit[1];
                        /* Now we apply direction and add direction to already chose field and get potential
                        *  field with ship's deck */
                        targetRow = chosen[0] + directionRow;
                        targetCol = chosen[1] + directionCol;
                    } else {
                        /* if no potential fields with ship's decks then we reset all */
                        hunting = false;
                        firstHit = null;
                        targetRow = -1;
                        targetCol = -1;
                        directionRow = 0;
                        directionCol = 0;
                    }
                }
            }

            /* if bot didn't choose field to hit, it choose it random. Generally, this code block is working if hunting = false */
            if (botsCoordinates == null) {
                /* we create list of possible to hit fields based on availableFields. If member of availableFields = null, means bot already used it. */
                ArrayList<int[]> valid = new ArrayList<>();
                for (int i = 0; i < availableFields.length; i++) {
                    if (availableFields[i] != null) valid.add(availableFields[i]);
                }
                /* if the bot didn't have any fields to hit, then the entire field was checked */
                if (valid.isEmpty()) {
                    isAll = true;
                }
                /* we choose any of the possible to hit fields by random */
                int idx = random.nextInt(valid.size());
                botsCoordinates = valid.get(idx);
                /* and tag it as null to avoid hitting it again */
                for (int i = 0; i < availableFields.length; i++) {
                    if (availableFields[i] != null && availableFields[i][0] == botsCoordinates[0] && availableFields[i][1] == botsCoordinates[1]) {
                        availableFields[i] = null;
                        break;
                    }
                }
            }

            /* we devide botsCoordinates to row variable and column variable */
            int botRow = botsCoordinates[0];
            int botCol = botsCoordinates[1];

            /* and tag it as null to avoid hitting it again */
            for (int i = 0; i < availableFields.length; i++) {
                if (availableFields[i] != null && availableFields[i][0] == botRow && availableFields[i][1] == botCol) {
                    availableFields[i] = null;
                    break;
                }
            }

            /* tag this field as checked in bot's shots map */
            botsMapHit[botRow][botCol] = "B";

            /* if bot missed */
            if (yourMap[botRow][botCol].equals("S")) {
                /* we tag it in our map and output result to user */
                yourMap[botRow][botCol] = "X";
                System.out.println("The bot hit your ship!");

                /* we add fields where never will be another ship located to the noBotWillHit list
                because of the rules (read the description this list at beginning the game method) */
                if (botRow + 1 < 10) {
                    if (!yourMap[botRow+1][botCol].equals("X") && !yourMap[botRow+1][botCol].equals("S")) {
                        noBotWillHit.add(botRow+1);
                        noBotWillHit.add(botCol);
                    }
                }
                if (botRow - 1 >= 0) {
                    if (!yourMap[botRow-1][botCol].equals("X") && !yourMap[botRow-1][botCol].equals("S")) {
                        noBotWillHit.add(botRow-1);
                        noBotWillHit.add(botCol);
                    }
                }
                if (botCol + 1 < 10) {
                    if (!yourMap[botRow][botCol+1].equals("X") && !yourMap[botRow][botCol+1].equals("S")) {
                        noBotWillHit.add(botRow);
                        noBotWillHit.add(botCol+1);
                    }
                }
                if (botCol - 1 >= 0) {
                    if (!yourMap[botRow][botCol-1].equals("X") && !yourMap[botRow][botCol-1].equals("S")) {
                        noBotWillHit.add(botRow);
                        noBotWillHit.add(botCol-1);
                    }
                }

                /* one more time reset all if we aren't hunting */
                if (!hunting) {
                    firstHit = new int[]{botRow, botCol};
                    hunting = true;
                    targetRow = -1;
                    targetCol = -1;
                    directionRow = 0;
                    directionCol = 0;
                } else {
                    /* if we're hunting, one more time apply formulas */
                    if (directionRow == 0 && directionCol == 0) {
                        directionRow = botRow - firstHit[0];
                        directionCol = botCol - firstHit[1];
                    }
                    targetRow = botRow + directionRow;
                    targetCol = botCol + directionCol;
                }

                /* we find wounded/destroyed ship */
                shipWrecked = whatsShip(yourMap, botRow, botCol, ourShips);
                /* we tag hit */
                markShipHit(shipWrecked, botRow, botCol, ourShips);
                /* we find the ship status (wounded, destroyed) */
                shipStatus = isShipKilled(shipWrecked, ourShips) ? "killed" : "wounded";

                /* if ship is destroyed, then we tag all fields in noBotWillHit as checked, reset variables and
                   output the result and the map to user */
                if (shipStatus.equals("killed")) {
                    for (int i = 0; i < noBotWillHit.size(); i+=2) {
                        int irow = noBotWillHit.get(i);
                        int icol = noBotWillHit.get(i+1);

                        yourMap[irow][icol] = "B";
                        botsMapHit[irow][icol] = "B";
                    }

                    System.out.println("Your ship has been destroyed!");
                    hunting = false;
                    firstHit = null;
                    targetRow = -1;
                    targetCol = -1;
                    directionRow = 0;
                    directionCol = 0;

                    printMap(yourMap, mod);
                } else {
                    printMap(yourMap, mod);
                    System.out.println("Your ship is wounded!");
                }
            } else {
                /* if bot missed, output result to user */
                yourMap[botRow][botCol] = "B";
                printMap(yourMap, mod);
                System.out.println("The bot missed!");
                /* if we are hunting, have first hit and direction then */
                if (hunting && firstHit != null && (directionRow != 0 || directionCol != 0)) {
                    /* we changing the direction and apply him to targetRow and targetCol */
                    directionRow = -directionRow;
                    directionCol = -directionCol;
                    targetRow = firstHit[0] + directionRow;
                    targetCol = firstHit[1] + directionCol;
                }
            }

            // System.out.println(isProbing + " " + isShipWrecked);
            // printMap(botsMapHit);

            /* check game over */
            isStrAll = isGameOver();
            if (isStrAll.equals("draw")) {
                System.out.println("Draw-Draw");
                isAll = true;
            } else if (isStrAll.equals("ourWin")) {
                System.out.println("You won!");
                isAll = true;
            } else if (isStrAll.equals("botWin")) {
                System.out.println("Bot won");
                isAll = true;
            }
        }
    }

    /* a method to check game over */
    public static String isGameOver() {
        String res = "false"; boolean isWeWon = true; boolean isBotWon = true;

        isWeWon = isShipKilled("quartet", anotherShips) && isShipKilled("trio1", anotherShips) &&
                isShipKilled("trio2", anotherShips) && isShipKilled("duo1", anotherShips) &&
                isShipKilled("duo2", anotherShips) && isShipKilled("uno1", anotherShips) &&
                isShipKilled("uno2", anotherShips);

        isBotWon = isShipKilled("quartet", ourShips) && isShipKilled("trio1", ourShips)
        && isShipKilled("trio2", ourShips) && isShipKilled("duo1", ourShips) &&
                isShipKilled("duo2", ourShips) && isShipKilled("uno1", ourShips) &&
                 isShipKilled("uno2", ourShips);

        if (isWeWon && !isBotWon) res = "ourWin";
        if (isWeWon && isBotWon) res = "draw";
        if (!isWeWon && isBotWon) res = "botWin";

        return res;
    }

    // a method to get random bot's coordinates for shot
    public static int[] getBotCoordinate(String[][] map, int[][] fieldMap) {
        int[] resCoordinates = new int[2];
        Random random = new Random();

        // generating random field
        int resRandom = random.nextInt(fieldMap.length);
        resCoordinates[0] = fieldMap[resRandom][0];
        resCoordinates[1] = fieldMap[resRandom][1];
        // we delete chose element
        deleteFromArray(fieldMap, resRandom);

        return resCoordinates;
    }

    public static void deleteFromArray(int[][] array, int range) {
        for (int i = range; i < array.length - 1; i++) {
            array[i] = array[i + 1];
        }
        array[array.length - 1] = null;
    }

    /* a method to check ship's destroy */
    public static boolean isShipKilled(String shipType, Ships classType) {
        if (shipType.equals("quartet")) {
            for (int i = 0; i < classType.quartet.length; i++) {
                if (classType.quartet[i][2] != -1) return false;
            }
            return true;
        }
        if (shipType.equals("trio1")) {
            for (int i = 0; i < classType.trio1.length; i++) {
                if (classType.trio1[i][2] != -1) return false;
            }
            return true;
        }
        if (shipType.equals("trio2")) {
            for (int i = 0; i < classType.trio2.length; i++) {
                if (classType.trio2[i][2] != -1) return false;
            }
            return true;
        }
        if (shipType.equals("duo1")) {
            for (int i = 0; i < classType.duo1.length; i++) {
                if (classType.duo1[i][2] != -1) return false;
            }
            return true;
        }
        if (shipType.equals("duo2")) {
            for (int i = 0; i < classType.duo2.length; i++) {
                if (classType.duo2[i][2] != -1) return false;
            }
            return true;
        }
        if (shipType.equals("uno1")) {
            if (classType.uno1[2] != -1) return false;
            return true;
        }
        if (shipType.equals("uno2")) {
            if (classType.uno2[2] != -1) return false;
            return true;
        }
        return false;
    }

    /* a method to tag about destroy of the ship's deck */
    public static void markShipHit(String shipType, int row, int col, Ships classType) {
        if (shipType.equals("quartet")) {
            for (int i = 0; i < classType.quartet.length; i++) {
                if (classType.quartet[i][0] == row && classType.quartet[i][1] == col) {
                    classType.quartet[i][2] = -1;
                }
            }
        }
        if (shipType.equals("trio1")) {
            for (int i = 0; i < classType.trio1.length; i++) {
                if (classType.trio1[i][0] == row && classType.trio1[i][1] == col) {
                    classType.trio1[i][2] = -1;
                }
            }
        }
        if (shipType.equals("trio2")) {
            for (int i = 0; i < classType.trio2.length; i++) {
                if (classType.trio2[i][0] == row && classType.trio2[i][1] == col) {
                    classType.trio2[i][2] = -1;
                }
            }
        }
        if (shipType.equals("duo1")) {
            for (int i = 0; i < classType.duo1.length; i++) {
                if (classType.duo1[i][0] == row && classType.duo1[i][1] == col) {
                    classType.duo1[i][2] = -1;
                }
            }
        }
        if (shipType.equals("duo2")) {
            for (int i = 0; i < classType.duo2.length; i++) {
                if (classType.duo2[i][0] == row && classType.duo2[i][1] == col) {
                    classType.duo2[i][2] = -1;
                }
            }
        }
        if (shipType.equals("uno1")) {
            classType.uno1[2] = -1;
        }
        if (shipType.equals("uno2")) {
            classType.uno2[2] = -1;
        }
    }

    /* a method to find name of the ship by field */
    public static String whatsShip(String[][] map, int row, int col, Ships owner) {
        if (owner.quartet != null) {
            for (int i = 0; i < owner.quartet.length; i++) {
                if (owner.quartet[i][0] == row && owner.quartet[i][1] == col) return "quartet";
            }
        }
        if (owner.trio1 != null) {
            for (int i = 0; i < owner.trio1.length; i++) {
                if (owner.trio1[i][0] == row && owner.trio1[i][1] == col) return "trio1";
            }
        }
        if (owner.trio2 != null) {
            for (int i = 0; i < owner.trio2.length; i++) {
                if (owner.trio2[i][0] == row && owner.trio2[i][1] == col) return "trio2";
            }
        }
        if (owner.duo1 != null) {
            for (int i = 0; i < owner.duo1.length; i++) {
                if (owner.duo1[i][0] == row && owner.duo1[i][1] == col) return "duo1";
            }
        }
        if (owner.duo2 != null) {
            for (int i = 0; i < owner.duo2.length; i++) {
                if (owner.duo2[i][0] == row && owner.duo2[i][1] == col) return "duo2";
            }
        }
        if (owner.uno1 != null && owner.uno1[0] == row && owner.uno1[1] == col) return "uno1";
        if (owner.uno2 != null && owner.uno2[0] == row && owner.uno2[1] == col) return "uno2";

        return "unknown";
    }

    // a method to get coordinates
    public static int[] getCoordinate(String[][] map) {
        /* we create variables to store the coordinates, which
            will then be enclosed in the res array and returned */
        int firstCoordinate = -1; int secondCoordinate = -1; int[] res;

        // we get the first digital coordinate
        System.out.println("Numeric coordinate, write /clear in order to enter the ship's coordinates again --> ");
        while (firstCoordinate == -1) {
            firstCoordinate = getInt();
            if (firstCoordinate == -2) {
                // return the same method if the user enters /clear
                System.out.println("Please write correct numeric coordinate");
                return getCoordinate(map);
            }
        }
        // we get the second letter coordinate (already in the index)
        System.out.printf("%nLetter coordinate, write /clear in order to enter the ship's coordinates again --> ");
        while (secondCoordinate == -1) {
            secondCoordinate = getLetter();
            if (secondCoordinate == -2) {
                // return null if the user enters /clear
                return null;
            }
        }
        // we put the field coordinates in an array and return it
        res = new int[]{secondCoordinate, firstCoordinate, 0};
        return res;
    }
    
    // a method for obtaining the digital coordinates of a field
    public static int getLetter() {
        /* we initialize the strRes variable, where we
         will store the letter that the user enters, and the res variable,
            which will store the index of the column in our field. */
        String strRes; int res = 0;
        strRes = scanner.nextLine().trim();
        if (strRes.equalsIgnoreCase("/clear")) {
            return -2;
        }
        // we check if the user has entered a letter from A to J
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        boolean isAvailable = false;
        check: for (int i = 0; i < letters.length; i++) {
            if (letters[i].equalsIgnoreCase(strRes)) {
                isAvailable = true;
                break check;
            }
        }
        /* if this is not the case, return -1 and display the text,
            otherwise return an integer */
        if (!(isAvailable)) {
            System.out.printf("%nPlease enter a correct letter, write /clear in order to enter the ship's coordinates again --> ");
            return -1;
        } else {
            for (int i = 0; i < letters.length; i++) {
                if (strRes.equalsIgnoreCase(letters[i])) {
                    return i;
                }
            }
            return res;
        }
    }
    
    // a method to get a number coordinate of the field
    public static int getInt() {
        String strRes;

        synchronized(Game.class) {
            try {
                strRes = scanner.nextLine().trim();

                // checking whether the user has entered /clear
                if (strRes.equalsIgnoreCase("/clear")) {
                    return -2;
                }

                // check that the result is between 1 and 10
                if (strRes.matches("[1-9]|10")) {
                    int res = Integer.parseInt(strRes);
                // reducing res to count the array
                res -= 1;
                    System.out.println();
                    return res;  // we refund you immediately if yes
                } else {
                    System.out.printf("%nPlease write a number from 1 to 10, or /clear to restart --> ");
                    // we return -1 so that getCoordinates understands that it needs to repeat the getInt method
                    return -1;
                }

            } catch (Exception e) {
                System.out.printf("%nError reading input --> ");
                scanner.nextLine();
                // we return -1 so that getCoordinates understands that it needs to repeat the getInt method
                return -1;
            }
        }
    }

    /* a method to output map in colored mode or uncolored mode */
    public static void printMap(String[][] map, int mod) {
        if (mod == 1) {
            System.out.println("   1  2  3  4  5  6  7  8  9  10");
            for (int i = 0; i < map.length; i++) {
                String rowLetter = switch (i) {
                    case 0 -> "A";
                    case 1 -> "B";
                    case 2 -> "C";
                    case 3 -> "D";
                    case 4 -> "E";
                    case 5 -> "F";
                    case 6 -> "G";
                    case 7 -> "H";
                    case 8 -> "I";
                    default -> "J";
                };
                System.out.print(rowLetter + "  ");
                for (int j = 0; j < map[i].length; j++) {
                    System.out.print(map[i][j] + "  ");
                }
                System.out.println();
            }
        } else {
            System.out.println(GREEN + "   1  2  3  4  5  6  7  8  9  10" + RESET);
            if (map != null) {
                for (int i = 0; i < map.length; i++) {
                    System.out.printf(GREEN + "%s", (i == 0 ? "A  " :
                            (i == 1 ? "B  " :
                                    (i == 2 ? "C  " :
                                            (i == 3 ? "D  " :
                                                    (i == 4 ? "E  " :
                                                            (i == 5 ? "F  " :
                                                                    (i == 6 ? "G  " :
                                                                            (i == 7 ? "H  " :
                                                                                    (i == 8 ? "I  " :
                                                                                            (i == 9 ? "J  " : "K")))))))))) + RESET);
                    for (int j = 0; j < map[i].length; j++) {
                        System.out.print(
                                map[i][j].equals("X") ? RED + "X  " + RESET :
                                        map[i][j].equals("S") ? YELLOW + "S  " + RESET :
                                                map[i][j].equals("D") ? BLUE + "D  " + RESET :
                                                        map[i][j].equals("B") ? GRAY + "B  " + RESET :
                                                                RESET + "O  " + RESET
                        );
                    }
                    System.out.println();
                }
            }
        }
    }
}
