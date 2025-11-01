package lang.statements;

import lang.interpreter.Interpreter;

public abstract class Statement {
    public abstract void interpret(Interpreter interpreter);
}
