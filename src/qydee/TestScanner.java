package qydee;

import java.util.List;

public class TestScanner {
    public static void main(String[] args) {
        String codigo = """
            Qyde
                print("Bienvenido al lenguaje de programacion Qyde")
                nombre := input("Ingresa tu nombre: ")
                edad : int = input("Ingresa tu edad: ")
                
                match edad
                    >= 18 ->print("eres mayor de edad")
                    < 18 -> print ("Eres menor de edad")
                end
                
                n1 := input("Ingresa un numero: ")
                n2 := input ("Ingresa un segundo numero")
                print("La suma de los dos numeros es: " + suma(n1, n2))
            
                    suma : int = (a,b) ->
                        return a + b;
            """;

        Scanner scanner = new Scanner(codigo);
        List<Token> tokens = scanner.scanTokens();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}