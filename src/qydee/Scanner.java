package qydee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
    private final String codigo;
    private final List<Token> tokens = new ArrayList<>(); //pa almacenar los tokens
    private int inicio = 0; //inicio del lexema
    private int actual = 0; //posicion actual en el codigo
    private int linea = 1; //numero de linea actual
    Scanner(String codigo){ //constructor
        this.codigo = codigo;
    }
    
    List<Token> scanTokens(){
        while(!isAtEnd()){ //mientras q no sea el final del codigo
            inicio = actual; 
            scanToken(); //llamar scantoken
        }
        tokens.add(new Token(TokenType.EOF, "", null, linea)); //si se acaba EOF (END OF FILE)
        return tokens; //retornqr
    }
    private void scanToken(){ //examina el caracter actual y deerminar 
        char c = advance();
        switch(c){
            //tokens de un caracter
            case '(': addToken(TokenType.PARENTESIS_IZQUIERDO); break;
            case ')': addToken(TokenType.PARENTESIS_DERECHO); break;
            case '+': addToken(TokenType.SUMA); break; 
            case '*': addToken(TokenType.MULTI); break;
            case ';': addToken(TokenType.PUNTO_COMA); break;
            case '!':
                if (match('=')) {
                    addToken(TokenType.EXCL_DISTINTO);
                } else {
                    addToken(TokenType.EXCL);
                }
                break;
            case ':':
                if (match('=')) {
                    addToken(TokenType.ASIGNACION);
                } else {
                    addToken(TokenType.FUNCION);
                }
                break;
            case '<':
                addToken(match('=') ? TokenType.MENOR : TokenType.IGUAL_MENOR);
                break;
            case '>':
                addToken(match('=') ? TokenType.MAYOR : TokenType.IGUAL_MAYOR);
                break;
            case '/':
                if(match('/')){
                    while(peek() != '\n' && !isAtEnd()) advance();
                }else{
                    addToken(TokenType.DIV);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                linea++;
                break;
            case '"': string(); break;
            case '&':
                if (match('&')) {
                    addToken(TokenType.YY);
                } else {
                    Qydee.error(linea, "Carácter '&' sin segundo '&'");
                }
                break;

            case '|':
                if (match('|')) {
                    addToken(TokenType.OO);
                } else {
                    Qydee.error(linea, "Carácter '|' sin segundo '|'");
                }
                break;
            case ',':
            addToken(TokenType.COMA);
            break;
        case '-':
            if (match('>')) {
                addToken(TokenType.FLECHA);
            } else {
                addToken(TokenType.RESTA);
            }
            break;
        case '=':
            addToken(TokenType.IGUAL);
            break;
            default:
               if(isDigit(c)){
                  number();
               }else if(isAlpha(c)){
                       identificador();
               }else{
                  Qydee.error(linea, "Carcter no valido.");
               }
               break;
        }
    }
    
    private void identificador(){
        while(isAlphaNumeric(peek())) advance(); //se mantiene hasta que ya no sea alfanumerico
        //peek devuelve el caracter actual sin avanzar el actual
        //si es alfanumerico, el actual avanza (advance)
        String text = codigo.substring(inicio, actual); //subcadena con el identificador
        TokenType type = keywords.get(text); //checar si es una keyword
        if (type == null) type = TokenType.IDENTIFICADOR; //si no es keywotd, es identificador
        addToken(type);
    }
    
      private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
                c == '_'; //los identificadores no pueden comensar con un digito
                //pero si contenerlos
      }

      private boolean isAlphaNumeric(char c) { 
        return isAlpha(c) || isDigit(c);
        //identificadores con letras, numeros pero no otros
      }

    private void number(){
        while(isDigit(peek())) advance(); //mientras peek sea un digito, avanza
        //buscar una parte
        if(peek() == '.' && isDigit(peekNext())){ //verifica si es un punto 
            //si el sig peeknext es un digito
            advance(); //consumir el punto decimal
            while(isDigit(peek())) advance();
        }
        addToken(TokenType.NUMERO, Double.parseDouble(codigo.substring(inicio, actual)));
    }
    //solo se toma el caracter actual si es lo que se busca
    private boolean match(char esperado){
        if(isAtEnd()) return false;
        if(codigo.charAt(actual) != esperado) return false;
        
        actual++;
        return true;
    }
    
    private char peek(){ //retorna el caracter actual sin avanzar
        if(isAtEnd()) return '\0'; //si es el final del codigo
        return codigo.charAt(actual);
    }
    
    private char peekNext(){ //retorna el siguiente caracter
        if(actual + 1 >= codigo.length()) return '\0';
        return codigo.charAt(actual + 1);
    }
    private boolean isDigit(char c){
        return c >= '0' && c <= '9';
    }
    private boolean isAtEnd(){
        return actual >= codigo.length();
    }
    
    private void string(){ //reconoce cadenas delimitadas por comillas
        //avanza caracteres hasta encontrar el cierre de la cadena
        while (peek() != '"' && !isAtEnd()){
           if (peek() == '\n') linea++;
            advance();
        }
        if (isAtEnd()) {
            Qydee.error(linea, "Cadena no valida.");
            return;
        } //si no hay cieere, devuelve error
            //se pasan los caracteres hasta que se llegue al " que finaliza la cadena
          advance();
          String valor = codigo.substring(inicio + 1, actual - 1);
          addToken(TokenType.CADENA, valor);
    }
    //consume el siugiente caracter en el archivo y lo retorna
    //para entrada
    private char advance(){ //avanza actual++  y retorna el caracter en la 
        //posicion anterior
        return codigo.charAt(actual++);
    }
    
    //para salida
    private void addToken(TokenType tipo) {
        addToken(tipo, null);
    }
    
    private void addToken(TokenType tipo, Object literal){
        String texto = codigo.substring(inicio, actual);
        tokens.add(new Token(tipo, texto, literal, linea));
    }
    
      private static final Map<String, TokenType> keywords;

    static {
      keywords = new HashMap<>();
      keywords.put("&&",    TokenType.YY);
      keywords.put("for",    TokenType.FOR);
      keywords.put("match", TokenType.MATCH);
      keywords.put("||", TokenType.OO);
      keywords.put("print", TokenType.PRINT);
      keywords.put("return", TokenType.RETURN);
      keywords.put("while", TokenType.WHILE);
      keywords.put("input", TokenType.INPUT);
      keywords.put("end", TokenType.END_MATCH);
    }
}
