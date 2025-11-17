package lang.environment;

import lang.errors.RuntimeError;
import lang.tokens.Token;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    public final Environment enclosing;

    // On utilise des Strings pour les clés au lieu d'utiliser tokens.
    // Pourquoi? Car les tokens représentent un morceau de code précis,
    // plusieurs tokens peuvent référencer la même variable via leur
    // `identifier`. On utilise donc l'`identifier` comme clé de la map.
    private final Map<String, Object> values = new HashMap<>();

    public Environment() {
        enclosing = null;
    }

    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    // ATTENTION: On ne check pas si la clé est déjà utilisée.
    // On permet donc la redéclaration d'une variable déjà existante.
    //
    // Le code suivant est valide:
    // ```
    // var a = "Before";
    // print a; // Before
    // var a = "After";
    // print a; // After
    // ```
    public void define(String name, Object value) {
        values.put(name, value);
    }

    public void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }

        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    // Quoi faire si la variable n'existe pas?
    // 1. Syntax error lors du parsing
    // 2. Throw un runtime error lors de l'execution
    // 3. Return nil comme javascript
    //
    // Return nil est trop "mou" et cause des bugs à nos utilisateurs.
    // Erreur de syntaxe est plus compliqué à implémenter pour nous,
    // car parfois on accède une variable dans une fonction définit
    // avant la variable.
    public Object get(Token token) {
        if (values.containsKey(token.lexeme)) {
            return values.get(token.lexeme);
        }

        if (enclosing != null) {
            return enclosing.get(token);
        }

        throw new RuntimeError(token, "Undefined variable '" + token.lexeme + "'.");
    }
}
