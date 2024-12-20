package Utility;

import java.awt.*;

public class Constants {
    public static final int DELAY_TIME = 1000;
    public static final int INSTANT = 1;
    public static final int MENUBAR_HEIGHT = 30;
    public static final int INSET = 30;
    public static final int BOARD_SIZE = 8;  // 棋盘的大小
    public static final int TILE_SIZE = 80;  // 每个格子的大小
    public static final int WINDOWS_WIDTH = BOARD_SIZE*TILE_SIZE+10;
    public static final int WINDOWS_HEIGHT = BOARD_SIZE*TILE_SIZE+MENUBAR_HEIGHT+40;
    public static final Rectangle RECTANGLE = new Rectangle(0,0,WINDOWS_WIDTH,WINDOWS_HEIGHT);
}
