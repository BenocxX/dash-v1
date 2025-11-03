package lang.interpreter;

import lang.DashLang;
import lang.environment.Environment;
import lang.errors.RuntimeError;
import lang.expressions.Expression;
import lang.statements.Statement;
import lang.tokens.Token;

import java.util.List;

public class Interpreter {
    private Environment environment = new Environment();

    public void interpret(List<Statement> statements) {
        try {
            for (Statement statement : statements) {
                statement.interpret(this);
            }
        } catch (RuntimeError error) {
            DashLang.runtimeError(error);
        }
    }

    public Object add(Object left, Object right) {
        if (left instanceof Double && right instanceof Double) {
            return (double) left + (double) right;
        }

        if (left instanceof String && right instanceof String) {
            return (String) left + (String) right;
        }

        return null;
    }

    public double substract(Object left, Object right) {
        return (double) left - (double) right;
    }

    public double multiply(Object left, Object right) {
        return (double) left * (double) right;
    }

    public double divide(Object left, Object right) {
        return (double) left / (double) right;
    }

    public boolean isLesser(Object left, Object right) {
        return (double) left < (double) right;
    }

    public boolean isLesserOrEqual(Object left, Object right) {
        return (double) left <= (double) right;
    }

    public boolean isGreater(Object left, Object right) {
        return (double) left > (double) right;
    }

    public boolean isGreaterOrEqual(Object left, Object right) {
        return (double) left >= (double) right;
    }

    public boolean isEqual(Object left, Object right) {
        if (left == null && right == null) {
            return true;
        }

        // On check si left est null afin d'éviter un NullPointerException
        // lorsqu'on call "left.equals(right)". Sans le if, ça pourrait
        // essayer de call "null.equals(right)".
        if (left == null) {
            return false;
        }

        return left.equals(right);
    }

    public String stringify(Object value) {
        if (value == null) {
            return "nil";
        }

        if (value instanceof Double) {
            String text = value.toString();

            // var x = 27;
            // print x; // Normalement 27.0, mais on coupe le ".0" afin de
            //          // cacher notre utilisation des Doubles.
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }

            return text;
        }

        return value.toString();
    }

    // Seulement "nil" et "false" sont des valeurs "falsy", tout le reste est "truthy"
    public boolean isTruthy(Object value) {
        if (value == null) {
            return false;
        }

        if (value instanceof boolean) {
            return (boolean) value;
        }

        return true;
    }

    public void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) {
            return;
        }

        throw new RuntimeError(operator, "Operand must be a number");
    }

    public void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) {
            return;
        }

        throw new RuntimeError(operator, "Operands must be numbers");
    }

    public Environment getEnvironment() {
        return environment;
    }
}
