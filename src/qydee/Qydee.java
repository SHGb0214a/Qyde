package qydee;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Qydee {
    static boolean fallo = false;
    public static void main(String[] args) throws IOException {
        /*
        if (args.length > 1) {
            System.out.println("Uso: Qyde [script]");
            System.exit(64);
        }else if(args.length==1){
            runFile(args[0]);
        }else{
            runPrompt();
        }
        */
        new Interfaz();
    }
    
    private static void runFile(String path) throws IOException{
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        
        //indicar un error en la salida
        if(fallo) System.exit(65);
    }
    
    private static void runPrompt() throws IOException{
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        
        for(;;){
            System.out.print("> ");
            String linea = reader.readLine();
            if(linea == null) break;
            run(linea);
            fallo = false;
        }
    }
    
    private static void run (String codigo){
        Scanner scanner = new Scanner(codigo);
        List<Token> tokens = scanner.scanTokens();
        
        for(Token token : tokens){
            System.out.println(token);
        }
    }
    
    static void error(int linea, String mensaje){
        report(linea, "", mensaje);
    }
    
    private static void report(int linea, String donde, String mensaje){
        System.err.println("[linea " + linea + "] Error" + donde + ": " + mensaje);
        fallo = true;
    }
}
