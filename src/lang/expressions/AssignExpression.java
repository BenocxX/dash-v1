package lang.expressions;

import lang.interpreter.Interpreter;
import lang.tokens.Token;
import lang.utils.AstPrinter;

// Assign a variable
public class AssignExpression extends Expression {
    public final Token name;
    public final Expression value;

    public AssignExpression(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String print(AstPrinter printer) {
        return "";
    }

    // ```
    // var x = 1;
    // print x = 2; // 2
    // ```
    @Override
    public Object interpret(Interpreter interpreter) {
        Object value = this.value.interpret(interpreter);
        interpreter.getEnvironment().assign(name, value);
        return value;
    }
}
