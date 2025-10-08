package lang.expressions;

public class GroupingExpression extends Expression {
    public final Expression expression;

    public GroupingExpression(Expression expression) {
        this.expression = expression;
    }
}
