package lang.expressions;

import lang.interpreter.Interpreter;
import lang.tokens.Token;
import lang.utils.AstPrinter;

// Access a variable's value
public class VariableExpression extends Expression {
    public final Token name;

    public VariableExpression(Token name) {
        this.name = name;
    }

    @Override
    public String print(AstPrinter printer) {
        return "";
    }

    @Override
    public Object interpret(Interpreter interpreter) {
        return null;
    }
}
