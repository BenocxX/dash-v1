package lang.statements;

import lang.expressions.Expression;
import lang.interpreter.Interpreter;

public class WhileStatement extends Statement {
    public final Expression condition;
    public final Statement body;

    public WhileStatement(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void interpret(Interpreter interpreter) {
        while (interpreter.isTruthy(condition.interpret(interpreter))) {
            body.interpret(interpreter);
        }
    }
}
