package lang.interpreter;

public class Interpreter {
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
