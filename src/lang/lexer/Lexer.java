package lang.lexer;

import lang.tokens.Token;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String source;
    private final List<Token> tokens;

    private int start;
    private int current;
    private int line;

    public Lexer(String source) {
        this.source = source;
        this.tokens = new ArrayList<>();

        this.start = 0;
        this.current = 0;
        this.line = 1;
    }
}
