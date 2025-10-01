package lang.tokens;

public enum TokenType {
    // Single characters
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    PLUS, MINUS, STAR, SLASH, SEMICOLON, COMMA, DOT,

    // One or two characters
    EQUAL, EQUAL_EQUAL, GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL, BANG, BANG_EQUAL,

    // Literals
    STRING, NUMBER, IDENTIFIER,

    // Keywords
    VAR, TRUE, FALSE, NIL,
    IF, ELSE, AND, OR,
    FOR, WHILE,
    PRINT,

    EOF
}
