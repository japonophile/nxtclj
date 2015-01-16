package nxtclj;

import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;

public class SensorStatement
{
    public static boolean parse(StatementInputStream is, boolean execute) throws Exception {
        boolean result = false;
        int state = is.next();
        switch (state) {
            case Sensor.PRESSED:
                result = isPressed(is, execute);
                break;
            case Sensor.OBSTACLE_WITHIN:
                result = isObstacleWithin(is, execute);
                break;
            case Sensor.OBSTACLE_BETWEEN:
                result = isObstacleBetween(is, execute);
                break;
            case Sensor.OBSTACLE_NONE:
                result = isObstacleNone(is, execute);
                break;
            default:
                throw new Exception("Unknown sensor state");
        }
        return result;
    }

    private static boolean isPressed(StatementInputStream is, boolean execute) throws Exception {
        int sensorId = is.next();
        boolean sensorPressed = false;
        if (execute) {
            TouchSensor sensor = getTouchSensor(sensorId);
            sensorPressed = sensor.isPressed();
        }
        return sensorPressed;
    }

    private static TouchSensor getTouchSensor(int sensorId) throws Exception {
        return new TouchSensor(getSensorPort(sensorId));
    }

    private static boolean isObstacleWithin(StatementInputStream is, boolean execute) throws Exception {
        int sensorId = is.next();
        int distance = is.next();
        int reading = 0;
        if (execute) {
            reading = getUltrasonicSensorReading(sensorId);
        }
        return execute && (reading < distance);
    }

    private static boolean isObstacleBetween(StatementInputStream is, boolean execute) throws Exception {
        int sensorId = is.next();
        int distance1 = is.next();
        int distance2 = is.next();
        int reading = 0;
        if (execute) {
            reading = getUltrasonicSensorReading(sensorId);
        }
        return execute && (reading >= distance1) && (reading < distance2);
    }

    private static boolean isObstacleNone(StatementInputStream is, boolean execute) throws Exception {
        int sensorId = is.next();
        int reading = 0;
        if (execute) {
            reading = getUltrasonicSensorReading(sensorId);
        }
        return execute && (reading == 255);
    }

    private static int getUltrasonicSensorReading(int sensorId) throws Exception {
        UltrasonicSensor sensor = new UltrasonicSensor(getSensorPort(sensorId));
        //sensor.continuous();
        sensor.ping();
        int reading = sensor.getDistance();
        Logger.log("D "+reading);
        return reading;
    }

    public static SensorPort getSensorPort(int sensorId) throws Exception {
        SensorPort port;
        switch (sensorId) {
            case Sensor.SENSOR1:
                port = SensorPort.S1;
                break;
            case Sensor.SENSOR2:
                port = SensorPort.S2;
                break;
            case Sensor.SENSOR3:
                port = SensorPort.S3;
                break;
            case Sensor.SENSOR4:
                port = SensorPort.S4;
                break;
            default:
                throw new Exception("Unknown sensorId");
        }
        return port;
    }
}

