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

    // Un autre choix s'offre à nous:
    // 1. Syntax error lors du parsing si on accède une variable déclaré sans valeur
    // 2. Runtime error si on accède une variable déclaré sans valeur
    // 3. Assigné nil lors de la déclaration d'une variable sans valeur
    @Override
    public void interpret(Interpreter interpreter) {
        Object value = null;
        if (initializer != null) {
            value = initializer.interpret(interpreter);
        }

        interpreter.getEnvironment().define(name.lexeme, value);
    }
}
