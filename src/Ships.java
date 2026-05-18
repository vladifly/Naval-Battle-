public class Ships {
    public int[][] quartet = new int[4][3];
    public int[][] trio1 = new int[3][3];
    public int[][] trio2 = new int[3][3];
    public int[][] duo1 = new int[2][3];
    public int[][] duo2 = new int[2][3];
    public int[] uno1 = new int[3];
    public int[] uno2 = new int[3];
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
