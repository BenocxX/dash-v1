package lang.parser;

import lang.DashLang;
import lang.errors.ParseError;
import lang.expressions.*;
import lang.statements.ExpressionStatement;
import lang.statements.PrintStatement;
import lang.statements.Statement;
import lang.tokens.Token;
import lang.tokens.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;

    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // program -> statement* EOF ;
    public List<Statement> parse() {
        // TODO: Re-add error handling later when we have more statements
        List<Statement> statements = new ArrayList<>();

        while (!isAtEnd()) {
            statements.add(statement());
        }

        return statements;
    }

    // statement -> exprStmt | printStmt
    private Statement statement() {
        if (match(TokenType.PRINT)) return printStatement();

        return expressionStatement();
    }

    // printStmt -> "print" expression ";" ;
    private Statement printStatement() {
        Expression expression = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after value.");
        return new PrintStatement(expression);
    }

    // exprStmt -> expression ";" ;
    private Statement expressionStatement() {
        Expression expression = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after expression.");
        return new ExpressionStatement(expression);
    }

    // expression -> equality ;
    private Expression expression() {
        return equality();
    }

    // (true) == (10 <= 4 + 2 * 3 / 1 - -2 + 3);
    // equality -> comparison ( ( "!=" | "==" ) comparison )* ;
    private Expression equality() {
        Expression expression = comparison();

        while (match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            Token operator = previous();
            Expression right = comparison();
            expression = new BinaryExpression(expression, operator, right);
        }

        return expression;
    }

    // (10) <= (4 + 2 * 3 / 1 - -2 + 3);
    // comparison -> term ( ( "<" | "<=" | ">=" | ">" ) term )* ;
    private Expression comparison() {
        Expression expression = term();

        while (match(TokenType.LESS, TokenType.LESS_EQUAL,
                TokenType.GREATER, TokenType.GREATER_EQUAL)) {
            Token operator = previous();
            Expression right = term();
            expression = new BinaryExpression(expression, operator, right);
        }

        return expression;
    }

    // term -> factor ( ( "+" | "-" ) factor )* ;
    private Expression term() {
        Expression expression = factor();

        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = previous();
            Expression right = factor();
            expression = new BinaryExpression(expression, operator, right);
        }

        return expression;
    }

    // factor -> unary ( ( "*" | "/" ) unary )* ;
    private Expression factor() {
        Expression expression = unary();

        while (match(TokenType.STAR, TokenType.SLASH)) {
            Token operator = previous();
            Expression right = unary();
            expression = new BinaryExpression(expression, operator, right);
        }

        return expression;
    }

    // (-(-4)) + 3;
    // unary -> ( "!" | "-" ) unary | primary ;
    private Expression unary() {
        if (match(TokenType.BANG, TokenType.MINUS)) {
            Token operator = previous();
            Expression right = unary();
            return new UnaryExpression(operator, right);
        }

        return primary();
    }

    // primary -> NUMBER | STRING | "true" | "false" | "nil" | "(" expression ")" ;
    private Expression primary() {
        if (match(TokenType.TRUE)) return new LiteralExpression(true);
        if (match(TokenType.FALSE)) return new LiteralExpression(false);
        if (match(TokenType.NIL)) return new LiteralExpression(null);
        if (match(TokenType.NUMBER)) return new LiteralExpression(previous().literal);
        if (match(TokenType.STRING)) return new LiteralExpression(previous().literal);

        if (match(TokenType.LEFT_PAREN)) {
            Expression expression = expression();
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.");
            return new GroupingExpression(expression);
        }

        throw error(peek(), "Expect expression");
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

    private boolean check(TokenType type) {
        if (isAtEnd()) {
            return false;
        }

        return peek().type == type;
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) {
            return advance();
        }

        throw error(peek(), message);
    }

    private ParseError error(Token token, String message) {
        DashLang.error(token, message);
        return new ParseError();
    }
}
