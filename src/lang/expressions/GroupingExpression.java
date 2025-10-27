package lang.expressions;

import lang.interpreter.Interpreter;
import lang.utils.AstPrinter;

public class GroupingExpression extends Expression {
    public final Expression expression;

    public GroupingExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String print(AstPrinter printer) {
        return printer.parenthesize("group", expression);
    }

    @Override
    public Object interpret(Interpreter interpreter) {
        return expression.interpret(interpreter);
    }
}
