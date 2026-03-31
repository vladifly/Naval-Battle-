import java.util.Random;
public class AnotherShips {
    public int[][] quartet = new int[4][3];
    public int[][] trio1 = new int[3][3];
    public int[][] trio2 = new int[3][3];
    public int[][] duo1 = new int[2][3];
    public int[][] duo2 = new int[2][3];
    public int[] uno1 = new int[3];
    public int[] uno2 = new int[3];
    public AnotherShips(int[][] quartet, int[][] trio1, int[][] trio2, int[][] duo1, int[][] duo2, int[] uno1, int[] uno2) {
        this.quartet = quartet;
        this.trio1 = trio1;
        this.trio2 = trio2;
        this.duo1 = duo1;
        this.duo2 = duo2;
        this.uno1 = uno1;
        this.uno2 = uno2;
    }

    public static int[] getUno(String[][] botMap) {
        Random random = new Random(); boolean isAvailable = false;
        String[] directions = new String[4];
        // генерация рандомного поля.
        int row = random.nextInt(10);
        int col = random.nextInt(10);

        // проверяем можно ли вообще начать с этого направления
        isAvailable = checkField(row, col, botMap);
        if (isAvailable) {isAvailable = false;
        } else {return getUno(botMap);}

        int[] res = {row, col, 0};
        botMap[row][col] = "S";
        return res;
    }

    public static int[][] getShip(String[][] botMap, int range) {
        Random random = new Random(); boolean isAvailable = false;
        String[] directions = new String[4]; int counter = 0;
        // генерация рандомного поля.
        int row = random.nextInt(10);
        int col = random.nextInt(10);

        // проверяем можно ли вообще начать с этого направления
        isAvailable = checkField(row, col, botMap);
        if (isAvailable) {isAvailable = false;
        } else {return getShip(botMap, range);}

        // если можно добавить направление вперед, добавляем
        all1: for (int i = 1; i < range+1; i++) {
            if (row + i < 10) {
                isAvailable = checkField(row + i, col, botMap);
                if (isAvailable) {isAvailable = false; 
                } else {break all1;}
            } else {break all1;}
            if (i == range) {
                directions[counter] = "forward";
                counter += 1;
            }
        }

        // если можно добавить направление назад, добавляем
        all2: for (int i = 1; i < range+1; i++) {
            if (row - i >= 0) {
                isAvailable = checkField(row - i, col, botMap);
                if (isAvailable) {isAvailable = false; 
                } else {break all2;}
            } else {break all2;}
            if (i == range) {
                directions[counter] = "backward";
                counter += 1;
            }
        }

        // если можно добавить направление вправо, добавляем
        all3: for (int i = 1; i < range+1; i++) {
            if (col + i < 10) {
                isAvailable = checkField(row, col + i, botMap);
                if (isAvailable) {isAvailable = false; 
                } else {break all3;}
            } else {break all3;}
            if (i == range) {
                directions[counter] = "right";
                counter += 1;
            }
        }

        // если можно добавить направление влево, добавляем
        all4: for (int i = 1; i < range+1; i++) {
            if (col - i >= 0) {
                isAvailable = checkField(row, col - i, botMap);
                if (isAvailable) {isAvailable = false; 
                } else {break all4;}
            } else {break all4;}
            if (i == range) {
                directions[counter] = "left";
                counter += 1;
            }
        }
        
        int intRes = random.nextInt(counter == 0 ? 1 : counter)+1;
        intRes -= 1;
        int[][] res = new int[range][3];
        botMap[row][col] = "S";
        if (directions[intRes] == null) return getShip(botMap, range);
        String strRes = directions[intRes];
        res[0][0] = row; res[0][1] = col;
        if (strRes.equals("forward")) {
            for (int i = 0; i < res.length-1; i++) {
                res[i+1][0] = row + (i+1);
                res[i+1][1] = col;
            }
        } else if (strRes.equals("backward")) {
            for (int i = 0; i < res.length-1; i++) {
                res[i+1][0] = row - (i+1);
                res[i+1][1] = col;
            }
        } else if (strRes.equals("right")) {
            for (int i = 0; i < res.length-1; i++) {
                res[i+1][0] = row;
                res[i+1][1] = col + (i+1);
            }
        } else if (strRes.equals("left")) {
            for (int i = 0; i < res.length-1; i++) {
                res[i+1][0] = row;
                res[i+1][1] = col - (i+1);
            }
        }

        for (int i = 0; i < res.length; i++) {
            res[i][2] = 0;
        }
        // устанавливаем рандомизированиые значения в массив
        for (int i = 0; i < res.length; i++) {botMap[res[i][0]][res[i][1]] = "S";}

        // возвращаем его
        return res;
    }

    public static boolean checkField(int row, int col, String[][] botMap) {
        boolean isAvailable = false; boolean res = false;
        // занято ли само поле
        if (!(botMap[row][col].equals("S"))) {
            // есть ли S спереди?
            if (row+1 < 10) {
                if (!(botMap[row+1][col].equals("S"))) {
                    isAvailable = true;
                } else {return false;}
            } else {isAvailable = true;}
            if (isAvailable) {
                isAvailable = false;
                if(row-1 >= 0) {
                    // есть ли S сзади?
                    if (!(botMap[row-1][col].equals("S"))) {
                        isAvailable = true;
                    } else {return false;}
                } else {isAvailable = true;}
            }
            if (isAvailable) {
                isAvailable = false;
                // есть ли S справа?
                if (col+1 < 10) {
                    if (!(botMap[row][col+1].equals("S"))) {
                        isAvailable = true;
                    } else {return false;}
                } else {isAvailable = true;}
            }
            if (isAvailable) {
                isAvailable = false;
                // есть ли S слева?
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
