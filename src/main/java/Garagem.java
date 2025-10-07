import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Scanner;

public class Garagem {
    private static final File arquivoGaragens = new File("src/data/garagens.csv");
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

    public static ArrayList<String> buscarGaragensNome(String texto) {
        ArrayList<String> nomesGaragens = new ArrayList<>();

        try(Scanner scanner = new Scanner(arquivoGaragens)) {
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
        try(Scanner scanner = new Scanner(arquivoGaragens)) {
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

    public static void main(String[] args) throws Exception{
        Scanner scanner = new Scanner(System.in);

        System.out.println("Servidor ainda não cadastrado. Vamos primeiramente efetuar o cadastro desta garagem.");

        System.out.print("Procure pelo nome desta garagem pressionando ENTER (digite ao menos 3 caracteres): ");
        String nomeGaragem = scanner.nextLine();
        ArrayList<String> garagensEncontradas = buscarGaragensNome(nomeGaragem);

        while(true) {
            System.out.print("\nDigite o número da garagem para cadastrar ou pesquise novamente: ");

            String texto = scanner.nextLine();

            if(texto.isEmpty()) {
                System.out.println("Entrada inválida!");
                continue;
            }

            if(isNumeric(texto)) {
                int numeroGaragem = Integer.parseInt(texto);

                Garagem garagem = retornarGaragem(garagensEncontradas.get(numeroGaragem - 1));

                boolean verificarGaragem = Boolean.parseBoolean(ApiClient.verificarGaragemNome(garagem.getIdGaragem()).body());
                System.out.println(verificarGaragem);

                if(verificarGaragem) {
                    System.out.printf("\nGaragem %s já cadastrada!\n", garagem.getNome());
                    continue;
                }

                String json = new Gson().toJson(garagem);

                int idEmpresa = 1;
                JsonObject jsonGaragem = JsonParser.parseString(json).getAsJsonObject();
                jsonGaragem.addProperty("idEmpresa", idEmpresa);

                json = new Gson().toJson(jsonGaragem);

                HttpResponse<String> response = ApiClient.cadastrarGaragem(json);

                System.out.println("\n" + response.body());

                break;
            }

            buscarGaragensNome(texto);
        }
    }
}
