package lang.parser;

import lang.DashLang;
import lang.errors.ParseError;
import lang.expressions.*;
import lang.statements.*;
import lang.tokens.Token;
import lang.tokens.TokenType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
    private final List<Token> tokens;

    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // program -> declaration* EOF ;
    public List<Statement> parse() {
        List<Statement> statements = new ArrayList<>();

        while (!isAtEnd()) {
            statements.add(declaration());
        }

        return statements;
    }

    // declaration -> varDeclaration | statement ;
    public Statement declaration() {
        try {
            if (match(TokenType.VAR)) {
                return varDeclaration();
            }

            return statement();
        } catch (ParseError error) {
            synchronize();
            return null;
        }
    }

    // var IDENTIFIER;
    // var IDENTIFIER = expression;
    // varDeclaration -> "var" IDENTIFIER ( "=" expression )? ";" ;
    private Statement varDeclaration() {
        Token name = consume(TokenType.IDENTIFIER, "Expect variable name.");

        Expression initializer = null;
        if (match(TokenType.EQUAL)) {
            initializer = expression();
        }

        consume(TokenType.SEMICOLON, "Expect ';' after variable declaration.");
        return new VariableStatement(name, initializer);
    }

    // statement -> exprStmt | ifStmt | whileStmt | forStmt | printStmt | block
    private Statement statement() {
        if (match(TokenType.IF)) return ifStatement();
        if (match(TokenType.WHILE)) return whileStatement();
        if (match(TokenType.FOR)) return forStatement();
        if (match(TokenType.PRINT)) return printStatement();
        if (match(TokenType.LEFT_BRACE)) return new BlockStatement(block());

        return expressionStatement();
    }

    // if -> "if" "(" expression ")" statement ( "else" statement )? ;
    private Statement ifStatement() {
        consume(TokenType.LEFT_PAREN, "Expect '(' after 'if'.");
        Expression condition = expression();
        consume(TokenType.RIGHT_PAREN, "Expect ')' after 'if' condition.");

        Statement thenBranch = statement();
        Statement elseBranch = match(TokenType.ELSE) ? statement() : null;

        return new IfStatement(condition, thenBranch, elseBranch);
    }

    // whileStmt -> "while" "(" expression ")" statement ;
    private Statement whileStatement() {
        consume(TokenType.LEFT_PAREN, "Expect '(' after 'while'.");
        Expression condition = expression();
        consume(TokenType.RIGHT_PAREN, "Expect ')' after 'while' condition.");

        Statement body = statement();

        return new WhileStatement(condition, body);
    }

    // forStmt -> "for" "(" ( varDeclaration | exprStmt ) ";" expression ";" expression ")" statement ;
    private Statement forStatement() {
        consume(TokenType.LEFT_PAREN, "Expect '(' after 'for'.");

        Statement initializer = match(TokenType.VAR) ? varDeclaration() : expressionStatement();

        Expression condition = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after for-loop condition.");

        Statement increment = new ExpressionStatement(expression());
        consume(TokenType.RIGHT_PAREN, "Expect ')' after for-loop increment.");

        Statement body = new BlockStatement(Arrays.asList(statement(), increment));
        Statement whileStatement = new WhileStatement(condition, body);

        return new BlockStatement(Arrays.asList(initializer, whileStatement));
    }

    // printStmt -> "print" expression ";" ;
    private Statement printStatement() {
        Expression expression = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after value.");
        return new PrintStatement(expression);
    }

    // block -> "{" declaration* "}" ;
    private List<Statement> block() {
        List<Statement> statements = new ArrayList<>();

        while(!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            statements.add(declaration());
        }

        consume(TokenType.RIGHT_BRACE, "Expect '}' after block.");
        return statements;
    }

    // exprStmt -> expression ";" ;
    private Statement expressionStatement() {
        Expression expression = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after expression.");
        return new ExpressionStatement(expression);
    }

    // expression -> assignment ;
    private Expression expression() {
        return assignment();
    }

    // assignment -> IDENTIFIER "=" assignment | logicOr ;
    // N'oubliez pas que assignment est "récursif", il peut se chain
    // ex: x = y = z = 8; // x va avoir la valeur de 8
    private Expression assignment() {
        Expression expression = logicOr();

        if (match(TokenType.EQUAL)) {
            Token equals = previous();
            Expression value = assignment();

            if (expression instanceof VariableExpression) {
                Token name = ((VariableExpression) expression).name;
                return new AssignExpression(name, value);
            }

            // On report l'erreur, mais on ne throw pas car les parser n'est
            // pas mélangé, il va simplement passer au prochain statement.
            // ex: 5 = 2 + 2;
            error(equals, "Invalid assignment target.");
        }

        return expression;
    }

    // logicOr -> logicAnd ( "or" logicAnd )* ;
    private Expression logicOr() {
        Expression expression = logicAnd();

        while (match(TokenType.OR)) {
            Token operator = previous();
            Expression right = logicAnd();
            expression = new LogicalExpression(expression, operator, right);
        }

        return expression;
    }

    // logicAnd -> equality ( "and" equality )* ;
    private Expression logicAnd() {
        Expression expression = equality();

        while (match(TokenType.AND)) {
            Token operator = previous();
            Expression right = equality();
            expression = new LogicalExpression(expression, operator, right);
        }

        return expression;
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

    // primary -> NUMBER | STRING | "true" | "false" | "nil" | IDENTIFIER | "(" expression ")" ;
    private Expression primary() {
        if (match(TokenType.TRUE)) return new LiteralExpression(true);
        if (match(TokenType.FALSE)) return new LiteralExpression(false);
        if (match(TokenType.NIL)) return new LiteralExpression(null);
        if (match(TokenType.NUMBER)) return new LiteralExpression(previous().literal);
        if (match(TokenType.STRING)) return new LiteralExpression(previous().literal);
        if (match(TokenType.IDENTIFIER)) return new VariableExpression(previous());

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

    // Ignores tokens until next declaration/statement
    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            switch (peek().type) {
                case TokenType.SEMICOLON, TokenType.IF, TokenType.PRINT,
                     TokenType.VAR, TokenType.WHILE:
                    return;
            }

            advance();
        }
    }
}
