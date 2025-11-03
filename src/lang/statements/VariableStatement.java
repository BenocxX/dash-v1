package lang.statements;

import lang.expressions.Expression;
import lang.interpreter.Interpreter;
import lang.tokens.Token;

// Declare a new variable
public class VariableStatement extends Statement {
    public final Token name;
    public final Expression initializer;

    public VariableStatement(Token name, Expression initializer) {
        this.name = name;
        this.initializer = initializer;
    }

    @Override
    public void interpret(Interpreter interpreter) {
        // TODO: Implement when Environment is ready
    }
}
