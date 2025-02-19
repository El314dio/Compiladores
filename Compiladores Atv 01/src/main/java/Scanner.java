import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Scanner {
    
    private byte[] input;
    private int current; 

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("let", TokenType.LET);
        keywords.put("while", TokenType.WHILE);
        keywords.put("int", TokenType.INT);
        keywords.put("class", TokenType.CLASS);
        keywords.put("constructor", TokenType.CONSTRUCTOR);
        keywords.put("function", TokenType.FUNCTION);
        keywords.put("method", TokenType.METHOD);
        keywords.put("field", TokenType.FIELD);
        keywords.put("static", TokenType.STATIC);
        keywords.put("var", TokenType.VAR);
        keywords.put("char", TokenType.CHAR);
        keywords.put("boolean", TokenType.BOOLEAN);
        keywords.put("void", TokenType.VOID);
        keywords.put("true", TokenType.TRUE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("null", TokenType.NULL);
        keywords.put("this", TokenType.THIS);        
        keywords.put("do", TokenType.DO);
        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("return", TokenType.RETURN);
    }

    public Scanner (byte[] input) {
        this.input = input;
    }

    private char peek () {
        if (current < input.length)
           return (char)input[current];
       return '\0';
    }

    private void advance()  {
        char ch = peek();
        if (ch != '\0') {
            current++;
        }
    }

    public Token nextToken () {
        skipWhitespace();
        char ch = peek();

        if (isAlpha(ch)) {
               return identifier();
        }
        
        if (ch == '0') {
            advance();
            return new Token (TokenType.NUMBER,Character.toString(ch));
        }  else if (Character.isDigit(ch))
            return number();

        switch (ch) {
                case '"':
                return string();
                
                case '/':
                if (peekNext() == '/') {
                    skipLineComments();
                    return nextToken();
                } else if (peekNext() == '*') {
                    skipBlockComments();
                    return nextToken();
                }
                else {
                    advance();
                    return new Token (TokenType.SLASH,"/");
                }

                case '+':
                    advance();
                    return new Token (TokenType.PLUS,"+");
                case '-':
                    advance();
                    return new Token (TokenType.MINUS,"-"); 
                case '*':
                    advance();
                    return new Token (TokenType.ASTERISK,"*"); 
                case '.':
                    advance();
                    return new Token (TokenType.DOT,"."); 
                case '&':
                    advance();
                    return new Token (TokenType.AND,"&"); 
                case '|':
                    advance();
                    return new Token (TokenType.OR,"|"); 
                case '~':
                    advance();
                    return new Token (TokenType.NOT,"~"); 

                case '>':
                    advance();
                    return new Token (TokenType.GT,">"); 
                case '<':
                    advance();
                    return new Token (TokenType.LT,"<"); 
                case '=':
                    advance();
                    return new Token (TokenType.EQ,"="); 

                case '(':
                    advance();
                    return new Token (TokenType.LPAREN,"("); 
                case ')':
                    advance();
                    return new Token (TokenType.RPAREN,")"); 
                case '{':
                    advance();
                    return new Token (TokenType.LBRACE,"{"); 
                case '}':
                    advance();
                    return new Token (TokenType.RBRACE,"}"); 
                case '[':
                    advance();
                    return new Token (TokenType.LBRACKET,"["); 
                case ']':
                    advance();
                    return new Token (TokenType.RBRACKET,"]"); 
                case ';':
                    advance();
                    return new Token (TokenType.SEMICOLON,";"); 
                case ',':
                    advance();
                    return new Token (TokenType.COMMA,",");
                case 0:
                    advance();
                    return new Token (TokenType.EOF,"EOF");
                default:
                    advance();
                    return new Token(TokenType.ILLEGAL, Character.toString(ch));
        }

        
    }

    private Token number() {
        int start = current ;
        while (Character.isDigit(peek())) {
            advance();
        }

        String n = new String(input, start, current-start)  ;
        return new Token(TokenType.NUMBER, n);
    }
    
    private Token identifier() {
            int start = current;
            while (isAlphaNumeric(peek())) advance();

            String id = new String(input, start, current-start)  ;
            TokenType type = keywords.get(id);
            if (type == null) type = TokenType.IDENT;
            return new Token(type, id);
      }
    
    private void skipWhitespace() {
        char ch = peek();
        while (ch == ' ' || ch == '\r' || ch == '\t' || ch == '\n') {
            advance();
            ch = peek();
        }
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || Character.isDigit((c));
    }

    private Token string () {
        advance();
        int start = current;
        while (peek() != '"' && peek() != 0) {
            advance();
        }
        String s = new String(input, start, current-start, StandardCharsets.UTF_8);
        Token token = new Token (TokenType.STRING,s);
        advance();
        return token;
    }

    private void skipLineComments() {
        for (char ch = peek(); ch != '\n' && ch != 0;  advance(), ch = peek()) ;
    }

    private void skipBlockComments() {
        boolean endComment = false;
        advance();
        while (!endComment) {
            advance();
            char ch = peek();
            if ( ch == 0) { // eof, lexical error
                System.exit(1);
            }
         
            if (ch == '*') {
               for (ch = peek(); ch == '*';  advance(), ch = peek()) ;
                if (ch == '/') {
                    endComment = true;
                    advance();
                }
            }

        }
    }

    private char peekNext () {
        int next = current + 1;
        if ( next  < input.length) {
            return (char)input[next];
        } else {
            return 0;
        }
   }
    

}