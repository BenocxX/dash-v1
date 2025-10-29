package lang.statements;

import lang.expressions.Expression;

public class PrintStatement extends Statement {
    public final Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }
}
