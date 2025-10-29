package lang.expressions;

import lang.errors.RuntimeError;
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
                Object result = interpreter.add(left, right);
                if (result == null) {
                    throw new RuntimeError(operator, "Operands must be two numbers or two strings");
                }
                return result;
            case TokenType.MINUS:
                interpreter.checkNumberOperands(operator, left, right);
                return interpreter.substract(left, right);
            case TokenType.STAR:
                interpreter.checkNumberOperands(operator, left, right);
                return interpreter.multiply(left, right);
            case TokenType.SLASH:
                interpreter.checkNumberOperands(operator, left, right);
                return interpreter.divide(left, right);
            case TokenType.LESS:
                interpreter.checkNumberOperands(operator, left, right);
                return interpreter.isLesser(left, right);
            case TokenType.LESS_EQUAL:
                interpreter.checkNumberOperands(operator, left, right);
                return interpreter.isLesserOrEqual(left, right);
            case TokenType.GREATER:
                interpreter.checkNumberOperands(operator, left, right);
                return interpreter.isGreater(left, right);
            case TokenType.GREATER_EQUAL:
                interpreter.checkNumberOperands(operator, left, right);
                return interpreter.isGreaterOrEqual(left, right);
            case TokenType.EQUAL_EQUAL:
                return interpreter.isEqual(left, right);
            case TokenType.BANG_EQUAL:
                return !interpreter.isEqual(left, right);
        }
        return null;
    }
}
