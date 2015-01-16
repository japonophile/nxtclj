package nxtclj;

import java.io.DataInputStream;

import lejos.nxt.LCD;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

public class Interpreter {

    public static void log(String msg) {
        LCD.clear();
        LCD.drawString(msg, 0, 0);
        LCD.refresh();
    }

    public static void main(String[] args) {
        boolean stopped = false;
        NXTConnection connection = null;
        StatementInputStream is = null;

        while (!stopped) {
            try {
                log("CONNECTION...");

                connection = Bluetooth.waitForConnection();
                is = new StatementInputStream(connection.openDataInputStream());

                while (true) {
                    int command = is.peek();
                    if (command == 0) {
                        stopped = true;
                        break;
                    }
                    Statement.parse(is, true);
                    is.flush();
                }

                is.close();
                connection.close();
            }
            catch (Exception e) {
                try {
                    is.close();
                    connection.close();
                }
                catch (Exception e2) {}
                
                log("INTERRUPTION!");
                
                try {
                    Thread.sleep(1000);
                }
                catch (Exception e3) {}
            }
        }
    }
}

