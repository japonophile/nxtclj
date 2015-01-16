package nxtclj;

import lejos.nxt.LCD;

public class Logger
{
    public static void log(String msg) {
        LCD.clear();
        LCD.drawString(msg, 0, 0);
        LCD.refresh();
    }
}

