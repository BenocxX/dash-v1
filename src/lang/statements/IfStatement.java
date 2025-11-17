package lang.statements;

import lang.expressions.Expression;
import lang.interpreter.Interpreter;

public class IfStatement extends Statement {
    public final Expression condition;
    public final Statement thenBranch;
    public final Statement elseBranch;

    public IfStatement(Expression condition, Statement thenBranch, Statement elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public void interpret(Interpreter interpreter) {
        if (interpreter.isTruthy(condition.interpret(interpreter))) {
            thenBranch.interpret(interpreter);
        } else if (elseBranch != null) {
            elseBranch.interpret(interpreter);
        }
    }
}
