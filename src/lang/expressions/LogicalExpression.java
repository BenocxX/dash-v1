package lang.expressions;

import lang.interpreter.Interpreter;
import lang.tokens.Token;
import lang.tokens.TokenType;
import lang.utils.AstPrinter;

public class LogicalExpression extends Expression {
    public final Expression left;
    public final Token operator;
    public final Expression right;

    public LogicalExpression(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String print(AstPrinter printer) {
        return printer.parenthesize(operator.lexeme, left, right);
    }

    @Override
    public Object interpret(Interpreter interpreter) {
        Object leftResult = left.interpret(interpreter);

        if (operator.type == TokenType.OR) {
            // if ("truthy" or ???) { ... }
            // Pas besoin de checker la droite, on peut simplement return le truthy
            if (interpreter.isTruthy(leftResult)) {
                return leftResult;
            }
        } else {
            // if ("falsy" && ????) { ... }
            // Pas besoin de checker la droite, on peut simplement return le falsy
            if (!interpreter.isTruthy(leftResult)) {
                return leftResult;
            }
        }

        // Si on a pas réussi à court-circuité:
        // ex: if ("falsy" or ???) { ... }
        // ex: if ("truthy" and ???) { ... }
        return right.interpret(interpreter);
    }
}
