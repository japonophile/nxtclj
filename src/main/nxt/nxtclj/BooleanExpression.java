package nxtclj;

public class BooleanExpression
{
    public static boolean evaluate(StatementInputStream is, boolean execute) throws Exception {
        boolean result = false;
        switch (is.peek()) {
            case Sensor.PRESSED:
            case Sensor.OBSTACLE_WITHIN:
            case Sensor.OBSTACLE_BETWEEN:
            case Sensor.OBSTACLE_NONE:
                result = evaluateSensorCondition(is, execute);
                break;
            case Condition.NOT:
                result = evaluateNotExpression(is, execute);
                break;
            default:
                throw new Exception("Unknown condition");
        }
        return result;
    }

    private static boolean evaluateSensorCondition(StatementInputStream is, boolean execute) throws Exception {
        return SensorStatement.parse(is, execute);
    }

    private static boolean evaluateNotExpression(StatementInputStream is, boolean execute) throws Exception {
        is.next(); // pop "not"
        return !BooleanExpression.evaluate(is, execute);
    }
}

