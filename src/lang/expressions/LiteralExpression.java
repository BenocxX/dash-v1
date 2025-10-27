package lang.expressions;

import lang.interpreter.Interpreter;
import lang.utils.AstPrinter;

public class LiteralExpression extends Expression {
    // TODO: Private pls bro
    public final Object value;

    public LiteralExpression(Object value) {
        this.value = value;
    }

    @Override
    public String print(AstPrinter printer) {
        return value == null ? "nil" : value.toString();
    }

    @Override
    public Object interpret(Interpreter interpreter) {
        return value;
    }
}
