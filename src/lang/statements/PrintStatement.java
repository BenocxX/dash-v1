package lang.statements;

import lang.expressions.Expression;
import lang.interpreter.Interpreter;

public class PrintStatement extends Statement {
    public final Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void interpret(Interpreter interpreter) {
        Object value = expression.interpret(interpreter);
        System.out.println(interpreter.stringify(value));
    }
}
