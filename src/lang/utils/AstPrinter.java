package lang.utils;

import lang.expressions.Expression;

public class AstPrinter {
    public String parenthesize(String name, Expression... expressions) {

        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expression expression : expressions) {
            builder.append(" ");
            builder.append(expression.print(this));
        }
        builder.append(")");

        return builder.toString();
    }
}
