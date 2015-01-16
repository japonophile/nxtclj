package nxtclj;

public class Statement
{
    public static boolean parse(StatementInputStream is, boolean execute) throws Exception {
        return
            ActionStatement.parse(is, execute) ||
            FlowStatement.parse(is, execute);
    }
}

