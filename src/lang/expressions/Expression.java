package lang.expressions;

import lang.interpreter.Interpreter;
import lang.utils.AstPrinter;

public abstract class Expression {
    public abstract String print(AstPrinter printer);
    public abstract Object interpret(Interpreter interpreter);
}
