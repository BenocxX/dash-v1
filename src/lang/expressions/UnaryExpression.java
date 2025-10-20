package lang.expressions;

import lang.ast.AstPrinter;
import lang.tokens.Token;

public class UnaryExpression extends Expression {
    public final Token operator;
    public final Expression right;

    public UnaryExpression(Token operator, Expression right) {
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String print(AstPrinter printer) {
        return printer.parenthesize(operator.lexeme, right);
    }
}
