package com.panjohnny.game;

import lombok.Setter;

public class Options {
    public static int MAX_FPS = 60;

    public static boolean DEVELOPER_MODE = false;

    public static void setDev() {
        DEVELOPER_MODE = true;
    }
}
