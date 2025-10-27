package lang.interpreter;

public class Interpreter {
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
}
