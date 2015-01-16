package nxtclj;

import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.robotics.Color;

public class ActionStatement
{
    public static boolean parse(StatementInputStream is, boolean execute) throws Exception {
        boolean result = false;
        switch (is.peek()) {
            case Action.MOTORA:
            case Action.MOTORB:
            case Action.MOTORC:
                result = parseMotorCommand(is, execute);
                break;
            case Action.SLEEP:
                result = parseSleepCommand(is, execute);
                break;
            case Action.LAMP:
                result = parseLampCommand(is, execute);
                break;
        }
        return result;
    }

    private static boolean parseMotorCommand(StatementInputStream is, boolean execute) throws Exception {
        int motorId = is.next();
        int command = is.next();
        int arg = 0;
        if (command == Action.FORWARD ||
            command == Action.BACKWARD ||
            command == Action.ROTATE) {
            arg = is.next();
        }
        if (execute) {
            NXTRegulatedMotor motor = getMotor(motorId);
            Logger.log("M "+motorId+" "+command+" "+arg);
            switch (command) {
                case Action.FORWARD:
                    motor.setSpeed(arg);
                    motor.forward();
                    break;
                case Action.BACKWARD:
                    motor.setSpeed(arg);
                    motor.backward();
                    break;
                case Action.ROTATE:
                    motor.rotate(arg);
                    break;
                case Action.STOP:
                    motor.stop();
                    break;
                case Action.FLOAT:
                    motor.flt();
                    break;
                default:
                    throw new Exception("Invalid motor command");
            }
        }
        return true;
    }

    private static NXTRegulatedMotor getMotor(int motorId) throws Exception {
        NXTRegulatedMotor motor;
        switch (motorId) {
            case Action.MOTORA:
                motor = Motor.A;
                break;
            case Action.MOTORB:
                motor = Motor.B;
                break;
            case Action.MOTORC:
                motor = Motor.C;
                break;
            default:
                throw new Exception("Invalid motorId");
        }
        return motor;
    }

    private static boolean parseSleepCommand(StatementInputStream is, boolean execute) throws Exception {
        is.next(); // pop "sleep"
        int sleepMsec = is.next();
        if (execute) {
            // Logger.log("SLEEP "+sleepMsec);
            Thread.sleep(sleepMsec);
        }
        return true;
    }

    private static boolean parseLampCommand(StatementInputStream is, boolean execute) throws Exception {
        is.next(); // pop "lamp"
        int sensorId = is.next();
        int color = is.next();
        if (execute) {
            ColorSensor lamp = getColorSensor(sensorId);
            lamp.setFloodlight(color);
        }
        return true;
    }

    private static ColorSensor getColorSensor(int sensorId) throws Exception {
        return new ColorSensor(SensorStatement.getSensorPort(sensorId));
    }
}

