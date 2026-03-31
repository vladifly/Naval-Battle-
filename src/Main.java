import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.printf("Welcome to naval battle game!%n%n");
        System.out.printf("Rules:%n  You and your opponent have a 10x10 field where " +
                "%n  the ships are located: his ships are on his field, and your ships are on your field. Your goal is " +
                "%n  to destroy each other's ships. You will have to name the point using its coordinates. " +
                "%n  For example, C2, K1, G10... If there is a ship at these coordinates, its part will be destroyed. " +
                "%n  By the way, there are four types of ships: quadruple-dock ships, which can only be 1, " +
                "%n  triple-docked ships, which can only be 2, double-docked ships, which can only be 2, and single-dock ships, which can be 2." +
                "%n  Ships cannot be adjacent to each other (vertically or horizontally) on the field, and they must be " +
                "%n  straight and either vertically or horizontally aligned.%n%n");

        System.out.println();
        System.out.printf("Choose your mode: %n" +
                "1. No color --> You don't need to do anything extra. However, the author recommends playing with the color mod, %n" +
                "as it makes the game much more enjoyable and understandable.%n" +
                "2. Color --> To play with the color mod, you need to open the command line (Win + R, then write cmd in the field) and enter the following command: %n" +
                "reg add HKEY_CURRENT_USER\\Console /v VirtualTerminalLevel /t REG_DWORD /d 1 /f After that, restart the game, and %n" +
                "if it doesn't work, reinstall it. This command provides access to colored characters in the command line. If you have %n" +
                "entered this command or have ever entered it, you don't need to perform this operation again, as it will work permanently after %n" +
                "the first time.%nEnter your mod (1 or 2) --> ");

        int mod = getMode();
        System.out.println();

        Game gameClass = new Game(mod);
        gameClass.game(gameClass.yourMap, gameClass.botMap);
    }

    public static int getMode() {
        Scanner scanner = new Scanner(System.in);
        String strMod;
        int mod = 0;
        try {
            strMod = scanner.nextLine().trim();

            if (strMod.equals("1") || strMod.equals("2")) {
                mod = Integer.parseInt(strMod);
            }
        } catch (Exception e) {
            System.out.println("Enter mode");
            scanner.nextLine();
        }

        System.out.println("Your mod: " + (mod == 1 ? "no color" : "color"));
        return mod;
    }
}