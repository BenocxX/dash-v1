package lang.expressions;

import lang.ast.AstPrinter;

public abstract class Expression {
    public abstract String print(AstPrinter printer);
}
