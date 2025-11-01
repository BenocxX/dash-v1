package lang.statements;

import lang.expressions.Expression;
import lang.interpreter.Interpreter;

public class ExpressionStatement extends Statement {
    public final Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void interpret(Interpreter interpreter) {
        expression.interpret(interpreter);
    }
}
