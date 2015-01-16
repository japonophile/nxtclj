package nxtclj;

public class FlowStatement
{
    public static boolean parse(StatementInputStream is, boolean execute) throws Exception {
        boolean result = false;
        switch (is.peek()) {
            case Flow.IF:
                result = parseIfStatement(is, execute);
                break;
            case Flow.WHILE:
                result = parseWhileStatement(is, execute);
                break;
            case Flow.DO:
                result = parseDoStatement(is, execute);
                break;
        }
        return result;
    }

    private static boolean parseIfStatement(StatementInputStream is, boolean execute) throws Exception {
        is.next();
        Logger.log("IF");
        boolean expression = BooleanExpression.evaluate(is, execute);
        Statement.parse(is, execute && expression);
        return true;
    }

    private static boolean parseWhileStatement(StatementInputStream is, boolean execute) throws Exception {
        is.next();
        int pos = is.getPos();
        boolean expression = BooleanExpression.evaluate(is, execute);
        if (execute && expression) {
            while (expression) {
                Statement.parse(is, true);
                is.rewindTo(pos);
                expression = BooleanExpression.evaluate(is, true);
            }
        }
        Statement.parse(is, false);
        return true;
    }

    private static boolean parseDoStatement(StatementInputStream is, boolean execute) throws Exception {
        is.next();
        int numStmt = is.next();
        for (int i = 0; i < numStmt; i++) {
            Statement.parse(is, execute);
        }
        return true;
    }
}

