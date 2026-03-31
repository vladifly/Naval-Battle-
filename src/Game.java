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
        String[][] botMapHits = new String[10][10];
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
        
        for (int i = 0; i < botMapHits.length; i++) {
            for (int j = 0; j < botMapHits[i].length; j++) {
                botMapHits[i][j] = "O";
                botsMapHit[i][j] = "O";
                availableFields[i * 10 + j] = new int[]{i, j};
            }
        }
        
        boolean isAll = false;
        System.out.println();
        
        while (!isAll) {
            System.out.println("Enter the coordinates where you want to hit");
            int[] coordinates = getCoordinate(yourMap);
            int row = coordinates[0];
            int col = coordinates[1];

            if (botMap[row][col].equals("S")) {
                botMapHits[row][col] = "X";
                shipWrecked = whatsShip(botMap, row, col, "another");
                markShipHit(shipWrecked, row, col, "another");
                shipStatus = isShipKilled(shipWrecked, "another") ? "killed" : "wounded";
                if (shipStatus.equals("killed")) {
                    System.out.println("The enemy ship has been destroyed!");
                    printMap(botMapHits, mod);
                } else {
                    System.out.println("The enemy ship is wounded!");
                    printMap(botMapHits, mod);

                }
            } else {
                System.out.println("A miss!");
                botMapHits[row][col] = "D";
                printMap(botMapHits, mod);
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
                printMap(yourMap, mod);
                System.out.println("The bot hit your ship!");

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

                shipWrecked = whatsShip(yourMap, botRow, botCol, "our");
                markShipHit(shipWrecked, botRow, botCol, "our");
                shipStatus = isShipKilled(shipWrecked, "our") ? "killed" : "wounded";

                if (shipStatus.equals("killed")) {
                    System.out.println("Your ship has been destroyed!");
                    hunting = false;
                    firstHit = null;
                    targetRow = -1;
                    targetCol = -1;
                    directionRow = 0;
                    directionCol = 0;
                } else {
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
                System.out.println("Bot has win");
                isAll = true;
            }
        }
    }

    public static String isGameOver() {
        String res = "false"; boolean isWeWon = true; boolean isBotWon = true;

        // проверка на убитость quartet
        isWeWon = isShipKilled("quartet", "another");
        if (isWeWon) {
            isWeWon = isShipKilled("trio1", "another");
            if (isWeWon) {
                isWeWon = isShipKilled("trio2", "another");
                if (isWeWon) {
                    isWeWon = isShipKilled("duo1", "another");
                    if (isWeWon) {
                        isWeWon = isShipKilled("duo2", "another");
                        if (isWeWon) {
                            isWeWon = isShipKilled("uno1", "another");
                            if (isWeWon) {
                                isWeWon = isShipKilled("uno2", "another");
                            }
                        }
                    }
                }
            }
        }
        isBotWon = isShipKilled("quartet", "our");
        if (isBotWon) {
            isBotWon = isShipKilled("trio1", "our");
            if (isBotWon) {
                isBotWon = isShipKilled("trio2", "our");
                if (isBotWon) {
                    isBotWon = isShipKilled("duo1", "our");
                    if (isBotWon) {
                        isBotWon = isShipKilled("duo2", "our");
                        if (isBotWon) {
                            isBotWon = isShipKilled("uno1", "our");
                            if (isBotWon) {
                                isBotWon = isShipKilled("uno2", "our");
                            }
                        }
                    }
                }
            }
        }

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

        public static boolean isShipKilled(String shipType, String classType) {
        if (shipType.equals("quartet")) {
            for (int i = 0; i < (classType.equals("another") ? anotherShips.quartet.length : ourShips.quartet.length); i++) {
                if ((classType.equals("another") ? anotherShips.quartet[i][2] : ourShips.quartet[i][2]) != -1) return false;
            }
            return true;
        }
        if (shipType.equals("trio1")) {
            for (int i = 0; i < (classType.equals("another") ? anotherShips.trio1.length : ourShips.trio1.length); i++) {
                if ((classType.equals("another") ? anotherShips.trio1[i][2] : ourShips.trio1[i][2]) != -1) return false;
            }
            return true;
        }
        if (shipType.equals("trio2")) {
            for (int i = 0; i < (classType.equals("another") ? anotherShips.trio2.length : ourShips.trio2.length); i++) {
                if ((classType.equals("another") ? anotherShips.trio2[i][2] : ourShips.trio2[i][2]) != -1) return false;
            }
            return true;
        }
        if (shipType.equals("duo1")) {
            for (int i = 0; i < (classType.equals("another") ? anotherShips.duo1.length : ourShips.duo1.length); i++) {
                if ((classType.equals("another") ? anotherShips.duo1[i][2] : ourShips.duo1[i][2]) != -1) return false;
            }
            return true;
        }
        if (shipType.equals("duo2")) {
            for (int i = 0; i < (classType.equals("another") ? anotherShips.duo2.length : ourShips.duo2.length); i++) {
                if ((classType.equals("another") ? anotherShips.duo2[i][2] : ourShips.duo2[i][2]) != -1) return false;
            }
            return true;
        }
        if (shipType.equals("uno1")) {
            if ((classType.equals("another") ? anotherShips.uno1[2] : ourShips.uno1[2]) != -1) return false;
            return true;
        }
        if (shipType.equals("uno2")) {
            if ((classType.equals("another") ? anotherShips.uno2[2] : ourShips.uno2[2]) != -1) return false;
            return true;
        }
        return false;
    }

        public static void markShipHit(String shipType, int row, int col, String classType) {
        if (shipType.equals("quartet")) {
            for (int i = 0; i < (classType.equals("another") ? anotherShips.quartet.length : ourShips.quartet.length); i++) {
                if (anotherShips.quartet[i][0] == row && anotherShips.quartet[i][1] == col) {
                    if (classType.equals("another")) {
                        anotherShips.quartet[i][2] = -1;
                    } else {
                        ourShips.quartet[i][2] = -1;
                    }
                }
            }
        }
        if (shipType.equals("trio1")) {
            for (int i = 0; i < (classType.equals("another") ? anotherShips.trio1.length : ourShips.trio1.length); i++) {
                if (anotherShips.trio1[i][0] == row && anotherShips.trio1[i][1] == col) {
                    if (classType.equals("another")) {
                        anotherShips.trio1[i][2] = -1;
                    } else {
                        ourShips.trio1[i][2] = -1;
                    }
                }
            }
        }
        if (shipType.equals("trio2")) {
            for (int i = 0; i < (classType.equals("another") ? anotherShips.trio2.length : ourShips.trio2.length); i++) {
                if (anotherShips.trio2[i][0] == row && anotherShips.trio2[i][1] == col) {
                    if (classType.equals("another")) {
                        anotherShips.trio2[i][2] = -1;
                    } else {
                        ourShips.trio2[i][2] = -1;
                    }
                }
            }
        }
        if (shipType.equals("duo1")) {
            for (int i = 0; i < (classType.equals("another") ? anotherShips.duo1.length : ourShips.duo1.length); i++) {
                if (anotherShips.duo1[i][0] == row && anotherShips.duo1[i][1] == col) {
                    if (classType.equals("another")) {
                        anotherShips.duo1[i][2] = -1;
                    } else {
                        ourShips.duo1[i][2] = -1;
                    }
                }
            }
        }
        if (shipType.equals("duo2")) {
            for (int i = 0; i < (classType.equals("another") ? anotherShips.duo2.length : ourShips.duo2.length); i++) {
                if (anotherShips.duo2[i][0] == row && anotherShips.duo2[i][1] == col) {
                    if (classType.equals("another")) {
                        anotherShips.duo2[i][2] = -1;
                    } else {
                        ourShips.duo2[i][2] = -1;
                    }
                }
            }
        }
        if (shipType.equals("uno1")) {
            if (classType.equals("another")) {
                anotherShips.uno1[2] = -1;
                } else {
                    ourShips.uno1[2] = -1;
            }
        }
        if (shipType.equals("uno2")) {
            if (classType.equals("another")) {
                anotherShips.uno2[2] = -1;
                } else {
                    ourShips.uno2[2] = -1;
            }
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

    public static String whatsShip(String[][] map, int row, int col, String owner) {
        if (owner.equals("another")) {
            // проверка anotherShips
            if (anotherShips.quartet != null) {
                for (int i = 0; i < anotherShips.quartet.length; i++) {
                    if (anotherShips.quartet[i][0] == row && anotherShips.quartet[i][1] == col) return "quartet";
                }
            }
            if (anotherShips.trio1 != null) {
                for (int i = 0; i < anotherShips.trio1.length; i++) {
                    if (anotherShips.trio1[i][0] == row && anotherShips.trio1[i][1] == col) return "trio1";
                }
            }
            if (anotherShips.trio2 != null) {
                for (int i = 0; i < anotherShips.trio2.length; i++) {
                    if (anotherShips.trio2[i][0] == row && anotherShips.trio2[i][1] == col) return "trio2";
                }
            }
            if (anotherShips.duo1 != null) {
                for (int i = 0; i < anotherShips.duo1.length; i++) {
                    if (anotherShips.duo1[i][0] == row && anotherShips.duo1[i][1] == col) return "duo1";
                }
            }
            if (anotherShips.duo2 != null) {
                for (int i = 0; i < anotherShips.duo2.length; i++) {
                    if (anotherShips.duo2[i][0] == row && anotherShips.duo2[i][1] == col) return "duo2";
                }
            }
            if (anotherShips.uno1 != null && anotherShips.uno1[0] == row && anotherShips.uno1[1] == col) return "uno1";
            if (anotherShips.uno2 != null && anotherShips.uno2[0] == row && anotherShips.uno2[1] == col) return "uno2";
        } else {
            // проверка ourShips
            if (ourShips.quartet != null) {
                for (int i = 0; i < ourShips.quartet.length; i++) {
                    if (ourShips.quartet[i][0] == row && ourShips.quartet[i][1] == col) return "quartet";
                }
            }
            if (ourShips.trio1 != null) {
                for (int i = 0; i < ourShips.trio1.length; i++) {
                    if (ourShips.trio1[i][0] == row && ourShips.trio1[i][1] == col) return "trio1";
                }
            }
            if (ourShips.trio2 != null) {
                for (int i = 0; i < ourShips.trio2.length; i++) {
                    if (ourShips.trio2[i][0] == row && ourShips.trio2[i][1] == col) return "trio2";
                }
            }
            if (ourShips.duo1 != null) {
                for (int i = 0; i < ourShips.duo1.length; i++) {
                    if (ourShips.duo1[i][0] == row && ourShips.duo1[i][1] == col) return "duo1";
                }
            }
            if (ourShips.duo2 != null) {
                for (int i = 0; i < ourShips.duo2.length; i++) {
                    if (ourShips.duo2[i][0] == row && ourShips.duo2[i][1] == col) return "duo2";
                }
            }
            if (ourShips.uno1 != null && ourShips.uno1[0] == row && ourShips.uno1[1] == col) return "uno1";
            if (ourShips.uno2 != null && ourShips.uno2[0] == row && ourShips.uno2[1] == col) return "uno2";
        }
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
                // возвращаем нулл, если пользователь ввел /clear
                return null;
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
