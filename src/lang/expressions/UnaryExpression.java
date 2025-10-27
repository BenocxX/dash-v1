package lang.expressions;

import lang.interpreter.Interpreter;
import lang.tokens.TokenType;
import lang.utils.AstPrinter;
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

    @Override
    public Object interpret(Interpreter interpreter) {
        Object right = this.right.interpret(interpreter);

        return switch (operator.type) {
            case TokenType.MINUS -> -(double) right;
            case TokenType.BANG -> !interpreter.isTruthy(right);
            default -> null;
        };
    }
}
