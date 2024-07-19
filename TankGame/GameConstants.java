package TankGame;

public class GameConstants {
    public static final int GAME_SCREEN_WIDTH = 1904;
    public static final int GAME_SCREEN_HEIGHT = 1064;

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 960;

    public static final int START_MENU_SCREEN_WIDTH = 516;
    public static final int START_MENU_SCREEN_HEIGHT = 564;

    public static final int END_MENU_SCREEN_WIDTH = 516;
    public static final int END_MENU_SCREEN_HEIGHT = 664;

    public static String winner;

    public static void setWinner(String winner)
    {
        GameConstants.winner = winner;
    }

    public static String getWinner()
    {
        return winner;
    }
}
