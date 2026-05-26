// superclass for classes which keeps ships and methods for operations with it
public class Ships {
    /* fields which keep ships. first index keeps ship's decks, second keeps deck's coordinates
    * (row and column), third keeps deck's status. if deck's status = -1 then deck is destroyed */
    public int[][] quartet = new int[4][3];
    public int[][] trio1 = new int[3][3];
    public int[][] trio2 = new int[3][3];
    public int[][] duo1 = new int[2][3];
    public int[][] duo2 = new int[2][3];
    public int[] uno1 = new int[3];
    public int[] uno2 = new int[3];
    /*
    The constructor takes the coordinates of all the ships, then sets these coordinates in the fields.
    We get the ships using the getQuartet, getTrio... methods, which use the getCoordinates, getInt, getLetter
    methods in the Game class to get the coordinates from the player.
    We get the coordinates of the bot's ships using the same methods, which generate the location of the ships
    based on the game rules.
    */
    public Ships(int[][] quartet, int[][] trio1, int[][] trio2, int[][] duo1, int[][] duo2, int[] uno1, int[] uno2) {
        this.quartet = quartet;
        this.trio1 = trio1;
        this.trio2 = trio2;
        this.duo1 = duo1;
        this.duo2 = duo2;
        this.uno1 = uno1;
        this.uno2 = uno2;
    }
}
