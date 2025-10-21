import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Garagem {
    private int idGaragem;
    private String nome;
    private double latitude;
    private double longitude;

    public Garagem(int idGaragem, String nome, double latitude, double longitude) {
        this.idGaragem = idGaragem;
        this.nome = nome;
        this.latitude = latitude;
        this. longitude = longitude;
    }
    private static Scanner getScannerGaragens() throws FileNotFoundException {
        InputStream resourceStream = Garagem.class.getResourceAsStream("/data/garagens.csv");
        if (resourceStream != null) {
            return new Scanner(new InputStreamReader(resourceStream));
        }

        File arquivoGaragens = new File("src/main/resources/data/garagens.csv");
        return new Scanner(arquivoGaragens);
    }

    public static ArrayList<String> buscarGaragensNome(String texto) {
        ArrayList<String> nomesGaragens = new ArrayList<>();

        try(Scanner scanner = getScannerGaragens()) {
            scanner.nextLine();
            while(scanner.hasNextLine()) {
                String[] campos = scanner.nextLine().split(",");

                if(campos[1].toLowerCase().contains(texto.toLowerCase())) {
                    nomesGaragens.add(campos[1]);
                }
            }

            int i = 0;

            System.out.println("\nGaragens encontradas: ");
            String formato = "[%02d] %-25s";

            for(String garagem : nomesGaragens) {
                if(i % 3 == 0 && i != 0) {
                    System.out.println();
                }

                System.out.printf(formato, i + 1, garagem);

                i++;
            }
        } catch(FileNotFoundException e) {
            System.out.println("Arquivo garagens não encontrado.");
        }

        return nomesGaragens;
    }

    public static Garagem retornarGaragem(String nome) {
        try(Scanner scanner = getScannerGaragens()) {
            scanner.nextLine();

            while(scanner.hasNextLine()) {
               String[] campos = scanner.nextLine().split(",");

               if(nome.equals(campos[1])) {
                   Garagem garagem = new Garagem(
                           Integer.parseInt(campos[0]),
                           campos[1],
                           Double.parseDouble(campos[2]),
                           Double.parseDouble(campos[3]));

                   return garagem;
               }
            }
        } catch(FileNotFoundException e) {
            System.out.println("Arquivo não encontrado");
        }

        return null;
    }

    public static boolean isNumeric(String texto) {
        try {
            int number =  Integer.parseInt(texto);
        } catch(NumberFormatException e) {
            return false;
        }

        return true;
    }

    public int getIdGaragem() {
        return this.idGaragem;
    }

    public String getNome() {
        return this.nome;
    }
}
