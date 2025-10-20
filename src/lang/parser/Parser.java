package lang.parser;

import lang.tokens.Token;
import lang.tokens.TokenType;

import java.util.List;

public class Parser {
    private final List<Token> tokens;

    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Token advance() {
        if (!isAtEnd()) {
            current++;
        }

        return previous();
    }
}
