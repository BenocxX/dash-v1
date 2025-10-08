package lang.lexer;

import lang.DashLang;
import lang.tokens.Token;
import lang.tokens.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
    private final String source;
    private final List<Token> tokens;
    private final Map<String, TokenType> keywords;

    private int start;
    private int current;
    private int line;

    public Lexer(String source) {
        this.source = source;
        this.tokens = new ArrayList<>();
        this.keywords = new HashMap<>();

        this.start = 0;
        this.current = 0;
        this.line = 1;

        initializeKeywords();
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            // Start représente le début d'un nouveau lexeme
            start = current;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        char character = advance();

        switch (character) {
            // Single character
            case '(': addToken(TokenType.LEFT_PAREN); break;
            case ')': addToken(TokenType.RIGHT_PAREN); break;
            case '{': addToken(TokenType.LEFT_BRACE); break;
            case '}': addToken(TokenType.RIGHT_BRACE); break;
            case ',': addToken(TokenType.COMMA); break;
            case '.': addToken(TokenType.DOT); break;
            case ';': addToken(TokenType.SEMICOLON); break;
            case '+': addToken(TokenType.PLUS); break;
            case '-': addToken(TokenType.MINUS); break;
            case '*': addToken(TokenType.STAR); break;

            case '!':
                addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
                break;
            case '=':
                addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
                break;
            case '<':
                addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                break;
            case '>':
                addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;

            case '/':
                if (match('/')) { // Is a comment
                    while (peek() != '\n' && !isAtEnd()) {
                        advance();
                    }
                } else { // Is a division
                    addToken(TokenType.SLASH);
                }
                break;

            case ' ':
            case '\r':
            case '\t':
                break;

            case '\n':
                line++;
                break;

            case '"':
                string();
                break;

            default:
                if (isDigit(character)) {
                    number();
                } else if (isAlpha(character)) {
                    identifier();
                } else {
                    // Unused characters (ex: '@', '#', '$', '^', '%', etc.)
                    DashLang.error(line, "Unexpected character: " + character);
                }
                break;
        }
    }

    private boolean match(char expected) {
        if (isAtEnd()) {
            return false;
        }

        if (source.charAt(current) != expected) {
            return false;
        }

        current++;
        return true;
    }

    private char advance() {
        return source.charAt(current++);
    }

    private char peek() {
        if (isAtEnd()) {
            return Character.MIN_VALUE;
        }

        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) {
            return Character.MIN_VALUE;
        }

        return source.charAt(current + 1);
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private boolean isDigit(char character) {
        return character >= '0' && character <= '9';
    }

    private boolean isAlpha(char character) {
        return (character >= 'a' && character <= 'z') ||
                (character >= 'A' && character <= 'Z') ||
                character == '_';
    }

    private boolean isAlphaNumeric(char character) {
        return isAlpha(character) || isDigit(character);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String lexeme = source.substring(start, current);
        tokens.add(new Token(type, lexeme, literal, line));
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                line++;
            }

            advance();
        }

        if (isAtEnd()) {
            DashLang.error(line, "Unterminated string.");
        }

        // Une fois qu'on a trouvé le guillemet fermant,
        // on passe au prochain caractère
        advance();

        // value = "ASDASDASD";
        String value = source.substring(start + 1, current - 1);
        addToken(TokenType.STRING, value);
    }

    private void number() {
        while (isDigit(peek())) {
            advance();
        }

        if (peek() == '.' && isDigit(peekNext())) {
            do {
                advance();
            } while (isDigit(peek()));
        }

        Double value = Double.parseDouble(source.substring(start, current));
        addToken(TokenType.NUMBER, value);
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) {
            advance();
        }

        String lexeme = source.substring(start, current);
        TokenType type = keywords.get(lexeme);
        addToken(type == null ? TokenType.IDENTIFIER : type);
    }

    private void initializeKeywords() {
        keywords.put("var", TokenType.VAR);
        keywords.put("true", TokenType.TRUE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("nil", TokenType.NIL);

        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("or", TokenType.OR);
        keywords.put("and", TokenType.AND);

        keywords.put("while", TokenType.WHILE);
        keywords.put("for", TokenType.FOR);

        keywords.put("print", TokenType.PRINT);
    }
}
