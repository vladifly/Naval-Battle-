import java.util.HashMap;

public class OurShips {
    public int[][] quartet = new int[4][3];
    public int[][] trio1 = new int[3][3];
    public int[][] trio2 = new int[3][3];
    public int[][] duo1 = new int[2][3];
    public int[][] duo2 = new int[2][3];
    public int[] uno1 = new int[3];
    public int[] uno2 = new int[3];

    public OurShips(int[][] quartet, int[][] trio1, int[][] trio2, int[][] duo1, int[][] duo2, int[] uno1, int[] uno2) {
        this.quartet = quartet;
        this.trio1 = trio1;
        this.trio2 = trio2;
        this.duo1 = duo1;
        this.duo2 = duo2;
        this.uno1 = uno1;
        this.uno2 = uno2;
    }
    // метод для получения четверехпалубного судна
    public static int[][] getQuartet(String[][] yourMap) {
        boolean isAvailable = false;
        /* создаем массив, который мы потом вернем, он 
           будет хранить в себе 4 координаты четверного
           корабля */
        int[][] quartet = new int[4][3];
        /* HashMap и переменную direction для хранения направления
           и будущих возможный направлений*/
        HashMap<String, int[]> checkMap = new HashMap<>();
        String[] direction = {""};

        System.out.println("Enter the coordinates where your four-deck ship will be located");
        Game.printMap(yourMap);
        // используем метод getCoordinate для получения первого поля судна
        quartet[0] = Game.getCoordinate(yourMap);
        // проверка на то, ввел ли пользователь /clear в getCoordinate
        if (quartet[0] == null) {return getQuartet(yourMap);}
        yourMap[quartet[0][0]][quartet[0][1]] = "S";
        Game.printMap(yourMap);
        // проверка на то, ввел ли пользователь /clear в getCoordinate
        while(!isAvailable) {
            // используем метод getCoordinate для получения второго поля судна
            quartet[1] = Game.getCoordinate(yourMap);
            if (quartet[1] == null) {
                // устанавливаем все установленные до этого значения обратно на D
                yourMap[quartet[0][0]][quartet[0][1]] = "D";
                return getQuartet(yourMap);
            }
            isAvailable = checkField(yourMap, direction, checkMap, quartet, 0, 1);
            if (isAvailable) {
                yourMap[quartet[1][0]][quartet[1][1]] = "S";
            } else {
                System.out.println("Please enter correct coordinates of ship");
            }
        }
        Game.printMap(yourMap);
        isAvailable = false;
        while(!isAvailable) {
            // используем метод getCoordinate для получения второго поля судна
            quartet[2] = Game.getCoordinate(yourMap);
            if (quartet[2] == null) {
                // устанавливаем все установленные до этого значения обратно на D
                yourMap[quartet[0][0]][quartet[0][1]] = "D";
                yourMap[quartet[1][0]][quartet[1][1]] = "D";
                return getQuartet(yourMap);
            }
            isAvailable = checkField(yourMap, direction, checkMap, quartet, 1, 2);
            if (isAvailable) {
                yourMap[quartet[2][0]][quartet[2][1]] = "S";
            } else {
                System.out.println("Please enter correct coordinates of ship");
            }
        }
        Game.printMap(yourMap);
        isAvailable = false;
        while(!isAvailable) {
            // используем метод getCoordinate для получения второго поля судна
            quartet[3] = Game.getCoordinate(yourMap);
            if (quartet[3] == null) {
                // устанавливаем все установленные до этого значения обратно на D
                yourMap[quartet[0][0]][quartet[0][1]] = "D";
                yourMap[quartet[1][0]][quartet[1][1]] = "D";
                yourMap[quartet[2][0]][quartet[2][1]] = "D";
                return getQuartet(yourMap);
            }
            isAvailable = checkField(yourMap, direction, checkMap, quartet, 2, 3);
            if (isAvailable) {
                yourMap[quartet[3][0]][quartet[3][1]] = "S";
            } else {
                System.out.println("Please enter correct coordinates of ship");
            }
        }
        System.out.println("Your four-deck ship: ");
        Game.printMap(yourMap);
        return quartet;
    }


    // метод для получения трехпалубного судна
    public static int[][] getTrio(String[][] yourMap) {
        boolean isAvailable = false;
        /* создаем массив, который мы потом вернем, он 
           будет хранить в себе 4 координаты четверного
           корабля */
        int[][] trio = new int[3][3];
        /* HashMap и переменную direction для хранения направления
           и будущих возможный направлений*/
        HashMap<String, int[]> checkMap = new HashMap<>();
        String[] direction = {""};

        System.out.println("Enter the coordinates where your three-decked ship will be located");
        Game.printMap(yourMap);
        // используем метод getCoordinate для получения первого поля судна
        while(!(isAvailable)) {
            trio[0] = Game.getCoordinate(yourMap);
            if (trio[0] == null) {return getTrio(yourMap);}
            isAvailable = checkOneField(yourMap, trio[0]);
            if (!(isAvailable)) {System.out.println("Please enter correct coordinates of ship");}
        }
        // проверка на то, ввел ли пользователь /clear в getCoordinate
        if (trio[0] == null) {return getTrio(yourMap);}
        yourMap[trio[0][0]][trio[0][1]] = "S";
        isAvailable = false;
        Game.printMap(yourMap);
        // проверка на то, ввел ли пользователь /clear в getCoordinate
        while(!isAvailable) {
            // используем метод getCoordinate для получения второго поля судна
            trio[1] = Game.getCoordinate(yourMap);
            if (trio[1] == null) {
                // устанавливаем все установленные до этого значения обратно на D
                yourMap[trio[0][0]][trio[0][1]] = "D";
                return getTrio(yourMap);
            }
            isAvailable = checkField(yourMap, direction, checkMap, trio, 0, 1);
            if (isAvailable) {
                yourMap[trio[1][0]][trio[1][1]] = "S";
            } else {
                System.out.println("Please enter correct coordinates of ship");
            }
        }
        Game.printMap(yourMap);
        isAvailable = false;
        while(!isAvailable) {
            // используем метод getCoordinate для получения второго поля судна
            trio[2] = Game.getCoordinate(yourMap);
            if (trio[2] == null) {
                // устанавливаем все установленные до этого значения обратно на D
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
        Game.printMap(yourMap);
        return trio;
    }



    // метод для получения двухпалубного судна
    public static int[][] getDuo(String[][] yourMap) {
        boolean isAvailable = false;
        /* создаем массив, который мы потом вернем, он 
           будет хранить в себе 4 координаты четверного
           корабля */
        int[][] duo = new int[2][3];
        /* HashMap и переменную direction для хранения направления
           и будущих возможный направлений*/
        HashMap<String, int[]> checkMap = new HashMap<>();
        String[] direction = {""};

        System.out.println("Enter the coordinates where your double-decked ship will be located");
        Game.printMap(yourMap);
        // используем метод getCoordinate для получения первого поля судна
        while(!(isAvailable)) {
            duo[0] = Game.getCoordinate(yourMap);
            if (duo[0] == null) {return getDuo(yourMap);}
            isAvailable = checkOneField(yourMap, duo[0]);
            if (!(isAvailable)) {System.out.println("Please enter correct coordinates of ship");}
        }
        // проверка на то, ввел ли пользователь /clear в getCoordinate
        if (duo[0] == null) {return getDuo(yourMap);}
        yourMap[duo[0][0]][duo[0][1]] = "S";
        isAvailable = false;
        Game.printMap(yourMap);
        // проверка на то, ввел ли пользователь /clear в getCoordinate
        while(!isAvailable) {
            // используем метод getCoordinate для получения второго поля судна
            duo[1] = Game.getCoordinate(yourMap);
            if (duo[1] == null) {
                // устанавливаем все установленные до этого значения обратно на D
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
        Game.printMap(yourMap);
        return duo;
    }

    // метод для получения однопалубного судна
    public static int[] getUno(String[][] yourMap) {
        boolean isAvailable = false;
        /* создаем массив, который мы потом вернем, он 
           будет хранить в себе 4 координаты четверного
           корабля */
        int[] uno = new int[3];
        /* HashMap и переменную direction для хранения направления
           и будущих возможный направлений*/
        HashMap<String, int[]> checkMap = new HashMap<>();
        String[] direction = {""};

        System.out.println("Enter the coordinates where your single-deck ship will be located");
        Game.printMap(yourMap);
        // используем метод getCoordinate для получения первого поля судна
        while(!(isAvailable)) {
            uno = Game.getCoordinate(yourMap);
            if (uno == null) {return getUno(yourMap);}
            isAvailable = checkOneField(yourMap, uno);
            if (isAvailable) {yourMap[uno[0]][uno[1]] = "S";}
            if (!(isAvailable)) {System.out.println("Please enter correct coordinates of ship");}
        }
        System.out.println("Your single-deck ship: ");
        Game.printMap(yourMap);
        return uno;
    }

    // метод для проверки можно ли поставить одну клетку на ее место
    public static boolean checkOneField(String[][] yourMap, int[] array) {
        boolean is = false; boolean res = false;
        int row = array[0];
        int col = array[1];
        // проверка не занято ли само поле
        if (!(yourMap[row][col].equals("S"))) {
            // проверка не занято ли сзади
            if (row - 1 >= 0) {
                if (!(yourMap[row-1][col].equals("S"))) {
                    is = true;
                } else {return false;}
            } else {is = true;}
            if (is == true) {
                is = false;
                if (row + 1 < 10) {
                    // проверка не занято ли спереди
                    if (!(yourMap[row+1][col].equals("S"))) {
                        is = true;
                    } else {return false;}
                } else {is = true;}
            }
            if (is == true) {
                is = false;
                if (col - 1 >= 0) {
                    // проверка не занято ли справа
                    if (!(yourMap[row][col-1].equals("S"))) {
                        is = true;
                    } else {return false;}
                } else {is = true;}
            }
            if (is == true) {
                is = false;
                if (col + 1 < 10) {
                    // проверка не занято ли слева
                    if (!((yourMap[row][col+1].equals("S")))) {
                        is = true;
                    } else {return false;}
                } else {is = true;}
            }
            if (is == true) {res = true;}
        } else {return false;}
        return res;
    }

    // метод для проверки доступности поля
    public static boolean checkField(String[][] yourMap, String[] direction, HashMap<String, int[]> checkMap, int[][] array, int range1, int range2) {
        int counter = 1; boolean isAvailable = false; boolean res = false;
        int newRow = array[range2][0];
        int newCol = array[range2][1];
        int prevRow = array[range1][0];
        int prevCol = array[range1][1];
        // проверяем на то, занято ли само поле
        if (!(yourMap[newRow][newCol].equals("S"))) {
            // проверяем на то, занято ли спереди поля
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
                // проверяем на то, занято ли сзади поля
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
                // проверяем на то, занято ли справа поля
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
                    // проверяем на то, занято ли слева поля
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
                    // если второе поле идентично первому (по координатам) возвращаем false
                    if (newRow != prevRow || newCol != prevCol) {

                        // проверяем можно ли добавить направление вперед
                        if (prevRow + 1 < yourMap.length) {
                            // если да, то добавляем
                            checkMap.put("forward", new int[] {prevRow + 1, prevCol});
                            // если наше поле соответствует этой клетке, то задаем направление fb
                            if (newRow == checkMap.get("forward")[0] && newCol == checkMap.get("forward")[1]) {
                                direction[0] = "fb";
                                if (newRow + 1 < 10) {
                                    checkMap.put("forward", new int[] {newRow + 1, newCol});
                                } else {
                                    checkMap.remove("forward");
                                }
                            }
                        }

                        // проверяем можно ли добавить направление назад
                        if (prevRow - 1 >= 0) {
                            // если да, то добавляем
                            checkMap.put("backward", new int[] {prevRow - 1, prevCol});
                            // если наше поле соответствует этой клетке, то задаем направление fb
                            if (newRow == checkMap.get("backward")[0] && newCol == checkMap.get("backward")[1]) {
                                direction[0] = "fb";
                                if (newRow - 1 >= 0) {
                                    checkMap.put("backward", new int[] {newRow - 1, newCol});
                                } else {
                                    checkMap.remove("backward");
                                }
                            }
                        }

                        // проверяем можно ли добавить направление вправо
                        if (prevCol + 1 < yourMap[prevRow].length) {
                            // если да, то добавляем
                            checkMap.put("right", new int[] {prevRow, prevCol + 1});
                            // если наше поле соответствует этой клетке, то задаем направление rl
                            if (newRow == checkMap.get("right")[0] && newCol == checkMap.get("right")[1]) {
                                direction[0] = "rl";
                                if (newCol + 1 < 10) {
                                    checkMap.put("right", new int[] {newRow, newCol + 1});
                                } else {
                                    checkMap.remove("right");
                                }
                            }
                        }

                        // проверяем можно ли добавить направление влево
                        if (prevCol - 1 >= 0) {
                            // если да, то добавляем
                            checkMap.put("left", new int[] {prevRow, prevCol - 1});
                            // если наше поле соответствует этой клетке, то задаем направление rl
                            if (newRow == checkMap.get("left")[0] && newCol == checkMap.get("left")[1]) {
                                direction[0] = "rl";
                                if (newCol - 1 >= 0) {
                                    checkMap.put("left", new int[] {newRow, newCol - 1});
                                } else {
                                    checkMap.remove("left");
                                }
                            }
                        }

                        /* после проверок на направление, проверяем пуст ли direction, 
                           если да, значит он не вблизи основной точки корабля, значит
                            возвращаем false */
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

                        if (direction[0].equals("fb")) {
                            if (checkMap.containsKey("forward")) {
                                get = checkMap.get("forward");
                                if (get[0] == newRow && get[1] == newCol) {
                                    if (newRow + 1 < 10) {
                                        checkMap.put("forward", new int[]{newRow + 1, newCol});
                                    } else {
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