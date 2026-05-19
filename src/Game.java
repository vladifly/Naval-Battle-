import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Game {
    public static final String RESET = "\u001B[0m";
    public static final String GRAY = "\u001B[90m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    static String[][] yourMap = new String[10][10];
    static String[][] botMap = new String[10][10];

    static AnotherShips anotherShips; static OurShips ourShips;
    static int mod = 1;
    static Scanner scanner = new Scanner(System.in);
    // в конструкторе инициализируем два поля: врага и наше
    public Game(int mod) {
        this.mod = mod;
        // заполняем все поле точками D (море)
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
        // создаем объект AnotherShips, где будут храниться корабли бота противника
        anotherShips = new AnotherShips(AnotherShips.getShip(botMap, 4), AnotherShips.getShip(botMap, 3), AnotherShips.getShip(botMap, 3), AnotherShips.getShip(botMap, 2), AnotherShips.getShip(botMap, 2), AnotherShips.getUno(botMap), AnotherShips.getUno(botMap));
        // создаем объект OurShips, где будут храниться корабли игрока
        ourShips = new OurShips(OurShips.getQuartet(yourMap), OurShips.getTrio(yourMap), OurShips.getTrio(yourMap), OurShips.getDuo(yourMap), OurShips.getDuo(yourMap), OurShips.getUno(yourMap), OurShips.getUno(yourMap));


        for (int i = 0; i < 10; i++) {System.out.println();}
        System.out.println("Your map:");
        printMap(yourMap, mod);
        //        printMap(botMap); // после теста убрать
    }

    public static void game(String[][] yourMap, String[][] botMap) {
        ArrayList<Integer> noBotWillHit = new ArrayList<>();

        String[][] ourHitField = new String[10][10];
        int[][] availableFields = new int[100][2];
        String[][] botsMapHit = new String[10][10];

        boolean hunting = false;
        int targetRow = -1;
        int targetCol = -1;
        int directionRow = 0;
        int directionCol = 0;

        int[] firstHit = new int[2];
        int counter = 0;
        int step = 1;
        String shipWrecked;
        String isStrAll;
        String shipStatus;
        boolean isShipWrecked = false;
        boolean isProbing = false;
        String mayBeDirection = "";
        Random random = new Random();
        
        for (int i = 0; i < ourHitField.length; i++) {
            for (int j = 0; j < ourHitField[i].length; j++) {
                ourHitField[i][j] = "O";
                botsMapHit[i][j] = "O";
                availableFields[i * 10 + j] = new int[]{i, j};
            }
        }
        
        boolean isAll = false;
        System.out.println();
        
        while (!isAll) {
            int[] coordinates = null;
            while (coordinates == null) {
                coordinates = getCoordinate(yourMap);
            }
            int row = coordinates[0];
            int col = coordinates[1];

            if (botMap[row][col].equals("S")) {
                ourHitField[row][col] = "X";
                shipWrecked = whatsShip(botMap, row, col, anotherShips);
                markShipHit(shipWrecked, row, col, anotherShips);
                shipStatus = isShipKilled(shipWrecked, anotherShips) ? "killed" : "wounded";
                if (shipStatus.equals("killed")) {
                    System.out.println("The enemy ship has been destroyed!");
                    printMap(ourHitField, mod);
                } else {
                    System.out.println("The enemy ship is wounded!");
                    printMap(ourHitField, mod);

                }
            } else {
                System.out.println("A miss!");
                ourHitField[row][col] = "D";
                printMap(ourHitField, mod);
            }

            System.out.println("Bot's try...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("interruptedexception");
                e.printStackTrace();
            }

            int[] botsCoordinates = null;

            if (hunting) {
                if (targetRow >= 0 && targetRow < 10 && targetCol >= 0 && targetCol < 10 && botsMapHit[targetRow][targetCol].equals("O")) {
                    botsCoordinates = new int[]{targetRow, targetCol};
                } else {
                    int[][] dirs = {{-1,0}, {1,0}, {0,-1}, {0,1}};
                    ArrayList<int[]> candidates = new ArrayList<>();
                    for (int[] d : dirs) {
                        int nr = firstHit[0] + d[0];
                        int nc = firstHit[1] + d[1];
                        if (nr >= 0 && nr < 10 && nc >= 0 && nc < 10 && botsMapHit[nr][nc].equals("O")) {
                            candidates.add(new int[]{nr, nc});
                        }
                    }
                    if (!candidates.isEmpty()) {
                        int[] chosen = candidates.get(random.nextInt(candidates.size()));
                        botsCoordinates = chosen;
                        directionRow = chosen[0] - firstHit[0];
                        directionCol = chosen[1] - firstHit[1];
                        targetRow = chosen[0] + directionRow;
                        targetCol = chosen[1] + directionCol;
                    } else {
                        hunting = false;
                        firstHit = null;
                        targetRow = -1;
                        targetCol = -1;
                        directionRow = 0;
                        directionCol = 0;
                    }
                }
            }

            if (botsCoordinates == null) {
                ArrayList<int[]> valid = new ArrayList<>();
                for (int i = 0; i < availableFields.length; i++) {
                    if (availableFields[i] != null) valid.add(availableFields[i]);
                }
                if (valid.isEmpty()) {
                    isAll = true;
                    break;
                }
                int idx = random.nextInt(valid.size());
                botsCoordinates = valid.get(idx);
                for (int i = 0; i < availableFields.length; i++) {
                    if (availableFields[i] != null && availableFields[i][0] == botsCoordinates[0] && availableFields[i][1] == botsCoordinates[1]) {
                        availableFields[i] = null;
                        break;
                    }
                }
            }

            int botRow = botsCoordinates[0];
            int botCol = botsCoordinates[1];

            for (int i = 0; i < availableFields.length; i++) {
                if (availableFields[i] != null && availableFields[i][0] == botRow && availableFields[i][1] == botCol) {
                    availableFields[i] = null;
                    break;
                }
            }

            botsMapHit[botRow][botCol] = "B";

            if (yourMap[botRow][botCol].equals("S")) {
                yourMap[botRow][botCol] = "X";
                System.out.println("The bot hit your ship!");

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

                if (!hunting) {
                    firstHit = new int[]{botRow, botCol};
                    hunting = true;
                    targetRow = -1;
                    targetCol = -1;
                    directionRow = 0;
                    directionCol = 0;
                } else {
                    if (directionRow == 0 && directionCol == 0) {
                        directionRow = botRow - firstHit[0];
                        directionCol = botCol - firstHit[1];
                    }
                    targetRow = botRow + directionRow;
                    targetCol = botCol + directionCol;
                }

                shipWrecked = whatsShip(yourMap, botRow, botCol, ourShips);
                markShipHit(shipWrecked, botRow, botCol, ourShips);
                shipStatus = isShipKilled(shipWrecked, ourShips) ? "killed" : "wounded";

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
                yourMap[botRow][botCol] = "B";
                printMap(yourMap, mod);
                System.out.println("The bot missed!");
                if (hunting && firstHit != null && (directionRow != 0 || directionCol != 0)) {
                    directionRow = -directionRow;
                    directionCol = -directionCol;
                    targetRow = firstHit[0] + directionRow;
                    targetCol = firstHit[1] + directionCol;
                }
            }

//            System.out.println(isProbing + " " + isShipWrecked);
//            printMap(botsMapHit);

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

    public static String isGameOver() {
        String res = "false"; boolean isWeWon = true; boolean isBotWon = true;

        // проверка на убитость quartet
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

    // метод для получения рандомных координат от бота для удара
    public static int[] getBotCoordinate(String[][] map, int[][] fieldMap) {
        int[] resCoordinates = new int[2];
        Random random = new Random();

        // генерация рандомного поля
        int resRandom = random.nextInt(fieldMap.length);
        resCoordinates[0] = fieldMap[resRandom][0];
        resCoordinates[1] = fieldMap[resRandom][1];
        // удаляем выбранный елемент
        deleteFromArray(fieldMap, resRandom);

        return resCoordinates;
    }

    public static void deleteFromArray(int[][] array, int range) {
        // сдвигаем элементы влево начиная с range
        for (int i = range; i < array.length - 1; i++) {
            array[i] = array[i + 1];
        }
        // последний элемент делаем null
        array[array.length - 1] = null;
    }

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

    public static String getShipStatus(String ship, String owner) {
        if (owner.equals("another")) {
            if (ship.equals("quartet")) {
                for (int i = 0; i < anotherShips.quartet.length; i++) {
                    if (anotherShips.quartet[i][2] != -1) return "wounded";
                }
                return "killed";
            }
            if (ship.equals("trio1")) {
                for (int i = 0; i < anotherShips.trio1.length; i++) {
                    if (anotherShips.trio1[i][2] != -1) return "wounded";
                }
                return "killed";
            }
            if (ship.equals("trio2")) {
                for (int i = 0; i < anotherShips.trio2.length; i++) {
                    if (anotherShips.trio2[i][2] != -1) return "wounded";
                }
                return "killed";
            }
            if (ship.equals("duo1")) {
                for (int i = 0; i < anotherShips.duo1.length; i++) {
                    if (anotherShips.duo1[i][2] != -1) return "wounded";
                }
                return "killed";
            }
            if (ship.equals("duo2")) {
                for (int i = 0; i < anotherShips.duo2.length; i++) {
                    if (anotherShips.duo2[i][2] != -1) return "wounded";
                }
                return "killed";
            }
            if (ship.equals("uno1") || ship.equals("uno2")) {
                return "killed";
            }
        } else {
            if (ship.equals("quartet")) {
                for (int i = 0; i < ourShips.quartet.length; i++) {
                    if (ourShips.quartet[i][2] != -1) return "wounded";
                }
                return "killed";
            }
            if (ship.equals("trio1")) {
                for (int i = 0; i < ourShips.trio1.length; i++) {
                    if (ourShips.trio1[i][2] != -1) return "wounded";
                }
                return "killed";
            }
            if (ship.equals("trio2")) {
                for (int i = 0; i < ourShips.trio2.length; i++) {
                    if (ourShips.trio2[i][2] != -1) return "wounded";
                }
                return "killed";
            }
            if (ship.equals("duo1")) {
                for (int i = 0; i < ourShips.duo1.length; i++) {
                    if (ourShips.duo1[i][2] != -1) return "wounded";
                }
                return "killed";
            }
            if (ship.equals("duo2")) {
                for (int i = 0; i < ourShips.duo2.length; i++) {
                    if (ourShips.duo2[i][2] != -1) return "wounded";
                }
                return "killed";
            }
            if (ship.equals("uno1") || ship.equals("uno2")) {
                return "killed";
            }
        }
        return "wounded";
    }

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

    // метод для получения координат одного игрового поля
    public static int[] getCoordinate(String[][] map) {
        /* создаем переменные для хранения координат, которые
           потом будут заключены в массив res и возвращены */
        int firstCoordinate = -1; int secondCoordinate = -1; int[] res;

        // получаем первую цифровую координату
        System.out.println("Numeric coordinate, write /clear in order to enter the ship's coordinates again --> ");
        while (firstCoordinate == -1) {
            firstCoordinate = getInt();
            if (firstCoordinate == -2) {
                // возвращаем этот же метод', если пользователь ввел /clear
                System.out.println("Please write correct numeric coordinate");
                return getCoordinate(map);
            }
        }
        // получаем вторую буквенную координату (уже в индексе)
        System.out.printf("%nLetter coordinate, write /clear in order to enter the ship's coordinates again --> ");
        while (secondCoordinate == -1) {
            secondCoordinate = getLetter();
            if (secondCoordinate == -2) {
                // возвращаем нулл, если пользователь ввел /clear
                return null;
            }
        }
        // заключаем в массив координаты поля в массив и возвращаем его
        res = new int[]{secondCoordinate, firstCoordinate, 0};
        return res;
    }
    
    // метод для получения цифровой координаты поля
    public static int getLetter() {
        /* инициализируем переменную strRes, куда мы
           будем заключать букву, которую вводит поль-
           зователь и переменную res, которую мы будем
           возвращать, в ней будет хранится индекс с
           нуля столбца нашего поля */
        String strRes; int res = 0;
        strRes = scanner.nextLine().trim();
        if (strRes.equalsIgnoreCase("/clear")) {
            return -2;
        }
        // проверяем вписал ли пользователь то букву от A до J
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        boolean isAvailable = false;
        check: for (int i = 0; i < letters.length; i++) {
            if (letters[i].equalsIgnoreCase(strRes)) {
                isAvailable = true;
                break check;
            }
        }
        /* если это не так, возвращаем -1 и выводим текст,
           иначе возвращаем целое число */
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
    
    // метод для получения цифровой координаты поля
    public static int getInt() {
        String strRes;

        synchronized(Game.class) {
            try {
                strRes = scanner.nextLine().trim();

                // проверка на то, вписал ли пользователь /clear
                if (strRes.equalsIgnoreCase("/clear")) {
                    return -2;
                }

                // проверка на то, чтобы результат был в диапазоне от 1 до 10.
                if (strRes.matches("[1-9]|10")) {
                    int res = Integer.parseInt(strRes);
                // уменьшаем res для подсчета массива
                res -= 1;
                    System.out.println();
                    return res;  // сразу возвращаем если да
                } else {
                    System.out.printf("%nPlease write a number from 1 to 10, or /clear to restart --> ");
                    // возвращаем -1, чтобы getCoordinates понял, что нужно повторить метод getInt еще раз
                    return -1;
                }

            } catch (Exception e) {
                System.out.printf("%nError reading input --> ");
                scanner.nextLine();
                // возвращаем -1, чтобы getCoordinates понял, что нужно повторить метод getInt еще раз
                return -1;
            }
        }
    }

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
