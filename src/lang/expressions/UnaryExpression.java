package lang.expressions;

import lang.tokens.Token;

public class UnaryExpression extends Expression {
    public final Token operator;
    public final Expression expression;

    public UnaryExpression(Token operator, Expression expression) {
        this.operator = operator;
        this.expression = expression;
    }
}
