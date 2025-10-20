package lang.expressions;

import lang.ast.AstPrinter;

public class GroupingExpression extends Expression {
    public final Expression expression;

    public GroupingExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String print(AstPrinter printer) {
        return printer.parenthesize("group", expression);
    }
}
