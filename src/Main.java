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

            Game gameClass = new Game();
            gameClass.game(gameClass.yourMap, gameClass.botMap);
    }
}