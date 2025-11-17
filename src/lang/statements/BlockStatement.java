package lang.statements;

import lang.environment.Environment;
import lang.interpreter.Interpreter;

import java.util.List;

public class BlockStatement extends Statement {
    public final List<Statement> statements;

    public BlockStatement(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public void interpret(Interpreter interpreter) {
        Environment enclosing = interpreter.getEnvironment();
        interpreter.interpretBlock(statements, new Environment(enclosing));
    }
}
