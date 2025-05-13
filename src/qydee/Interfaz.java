package qydee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Interfaz extends JFrame{

    public Interfaz() {
        setTitle("Documentador Qyde");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);

        JTextArea codigoArea = new JTextArea();
        JPanel panelCodigo = new JPanel(new BorderLayout());
        JLabel labelCodigo = new JLabel("Código:");
        panelCodigo.add(labelCodigo, BorderLayout.NORTH);
        panelCodigo.add(new JScrollPane(codigoArea), BorderLayout.CENTER);

        JTextArea lexerArea = new JTextArea();
        lexerArea.setEditable(false);
        lexerArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JPanel panelLexer = new JPanel(new BorderLayout());
        JLabel labelLexer = new JLabel("Análisis léxico:");
        panelLexer.add(labelLexer, BorderLayout.NORTH);
        panelLexer.add(new JScrollPane(lexerArea), BorderLayout.CENTER);

        JTextArea otraArea = new JTextArea();
        JPanel panelOtra = new JPanel(new BorderLayout());
        JLabel labelOtra = new JLabel("Análisis semantico¿?:");
        panelOtra.add(labelOtra, BorderLayout.NORTH);
        panelOtra.add(new JScrollPane(otraArea), BorderLayout.CENTER);

        JButton analizarButton = new JButton("Analizar código");
        JButton subirArchivoButton = new JButton("Subir archivo");
        JButton generarPDFButton = new JButton("Generar PDF");

        JPanel panelCentro = new JPanel(new GridLayout(2, 2, 30, 30));
        panelCodigo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCentro.add(panelCodigo);
        panelLexer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCentro.add(panelLexer);
        panelOtra.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCentro.add(panelOtra);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        panelBotones.add(analizarButton);
        panelBotones.add(subirArchivoButton);
        panelBotones.add(generarPDFButton);

        JPanel panelInferior = new JPanel(new BorderLayout());
        JTextField rutaArchivoField = new JTextField();
        panelInferior.add(rutaArchivoField, BorderLayout.CENTER);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);

        add(panelCentro, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        analizarButton.addActionListener((ActionEvent e) -> {
            String codigoFuente = codigoArea.getText();
            Scanner scanner = new Scanner(codigoFuente);
            List<Token> tokens = scanner.scanTokens();

            StringBuilder resultado = new StringBuilder();
            for (Token token : tokens) {
                resultado.append(token).append("\n");
            }

            lexerArea.setText(resultado.toString());
        });
        
        subirArchivoButton.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar archivo de código");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos de texto (.txt)", "txt"));

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File archivoSeleccionado = fileChooser.getSelectedFile();
                try {
                    StringBuilder contenido = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new FileReader(archivoSeleccionado));
                    String linea;
                    while ((linea = reader.readLine()) != null) {
                        contenido.append(linea).append("\n");
                    }
                    reader.close();

                    // Colocar el contenido en el área de código
                    codigoArea.setText(contenido.toString());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al leer el archivo", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }
}