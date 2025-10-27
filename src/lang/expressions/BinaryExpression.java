package lang.expressions;

import lang.interpreter.Interpreter;
import lang.tokens.TokenType;
import lang.utils.AstPrinter;
import lang.tokens.Token;

public class BinaryExpression extends Expression {
    public final Expression left;
    public final Token operator;
    public final Expression right;

    public BinaryExpression(Expression left, Token operator, Expression right) {
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
        Object left = this.left.interpret(interpreter);
        Object right = this.right.interpret(interpreter);

        switch (operator.type) {
            case TokenType.PLUS:
                return interpreter.add(left, right);
            case TokenType.MINUS:
                return interpreter.substract(left, right);
            case TokenType.STAR:
                return interpreter.multiply(left, right);
            case TokenType.SLASH:
                return interpreter.divide(left, right);
            case TokenType.LESS:
                return interpreter.isLesser(left, right);
            case TokenType.LESS_EQUAL:
                return interpreter.isLesserOrEqual(left, right);
            case TokenType.GREATER:
                return interpreter.isGreater(left, right);
            case TokenType.GREATER_EQUAL:
                return interpreter.isGreaterOrEqual(left, right);
            case TokenType.EQUAL_EQUAL:
                return interpreter.isEqual(left, right);
            case TokenType.BANG_EQUAL:
                return !interpreter.isEqual(left, right);
        }
        return null;
    }
}
