import com.google.gson.*;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static JsonObject login() {
        Scanner scanner = new Scanner(System.in);

        String body = "";
        JsonObject jsonUsuario = new JsonObject();

        while(true) {
            System.out.print("\nInforme seu email: ");
            String emailUsuario = scanner.nextLine();

            System.out.print("Informe sua senha: ");
            String senhaUsuario = scanner.nextLine();

            String jsonUsuarioCredenciais = String.format("{\"emailServer\": \"%s\", \"senhaServer\": \"%s\"}", emailUsuario, senhaUsuario);

            HttpResponse<String> response = ApiClient.autenticarUsuario(jsonUsuarioCredenciais);
            body = response.body();

            if(body.equals("Email e/ou senha inválido(s)")) {
                System.out.println("\n⚠\uFE0F " + response.body());
            } else {
                jsonUsuario = JsonParser.parseString(body).getAsJsonObject();

                System.out.println("\n✅ Login realizado com sucesso!");

                return jsonUsuario;
            }
        }
    }

    public static long adicionarGaragem(String idEmpresa) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nServidor ainda não cadastrado. Vamos primeiramente efetuar o cadastro desta garagem.");

        System.out.print("Procure pelo nome desta garagem pressionando ENTER (digite ao menos 3 caracteres): ");
        String nomeGaragem = scanner.nextLine();
        ArrayList<String> garagensEncontradas = Garagem.buscarGaragensNome(nomeGaragem);

        while(true) {
            System.out.print("\nDigite o número da garagem para cadastrar ou pesquise novamente: ");

            String texto = scanner.nextLine();

            if(texto.isEmpty()) {
                System.out.println("Entrada inválida!");
                continue;
            }

            if(Garagem.isNumeric(texto)) {
                int numeroGaragem = Integer.parseInt(texto);

                Garagem garagem = Garagem.retornarGaragem(garagensEncontradas.get(numeroGaragem - 1));

                boolean verificarGaragem = Boolean.parseBoolean(ApiClient.verificarGaragemNome(garagem.getIdGaragem()).body());
                System.out.println(verificarGaragem);

                if(verificarGaragem) {
                    System.out.printf("\nGaragem %s já cadastrada!\n", garagem.getNome());
                    continue;
                }

                String json = new Gson().toJson(garagem);

                JsonObject jsonGaragem = JsonParser.parseString(json).getAsJsonObject();
                jsonGaragem.addProperty("idEmpresa", idEmpresa);

                json = new Gson().toJson(jsonGaragem);

                HttpResponse<String> response = ApiClient.cadastrarGaragem(json);

                System.out.println("\n" + response.body());

                return garagem.getIdGaragem();
            }

            Garagem.buscarGaragensNome(texto);
        }
    }

    public static void adicionarServidor(String idEmpresa, String uuid, long idGaragem) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nServidor ainda não cadastrado. Efetuaremos o cadastro em nosso sistema.");
        System.out.print("Para prosseguir, pressione ENTER: ");
        String continuar = scanner.nextLine();

        JsonObject jsonServidor = Servidor.capturarInformacoesComputador();

        jsonServidor.addProperty("uuid", uuid);
        jsonServidor.addProperty("idEmpresa", idEmpresa);
        jsonServidor.addProperty("idGaragem", idGaragem);

        String json = new Gson().toJson(jsonServidor);

        HttpResponse<String> response = ApiClient.adicionarServidor(json);

        System.out.println("\n" + response.body());
    }

    public static void atualizarServidor(String uuid) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nServidor já cadastrado. Atualizaremos as informações em nosso sistema.");
        System.out.print("Para prosseguir, pressione ENTER: ");
        String continuar = scanner.nextLine();

        JsonObject jsonServidor = Servidor.capturarInformacoesComputador();
        jsonServidor.addProperty("uuid", uuid);

        String json = new Gson().toJson(jsonServidor);

        HttpResponse<String> response = ApiClient.atualizarServidor(json);

        System.out.println("\n" + response.body());
    }

    public static boolean autenticarSpTrans(SpTransApiClient apiSpTrans) {
        HttpResponse<String> response = apiSpTrans.autenticarUsuarioSpTrans();

        return response.statusCode() == 200 && response.body().equals("true");
    }

    public static void listarLinhas(JsonArray linhasOnibus) {
        for(JsonElement linhaElement : linhasOnibus) {
            JsonObject linhaOnibus = linhaElement.getAsJsonObject();

            String letreiro = linhaOnibus.get("c").getAsString();
            String destino = linhaOnibus.get("lt1").getAsString();
            int quantidadeVeiculos = linhaOnibus.get("qv").getAsInt();

            System.out.println("Linha: " + letreiro);
            System.out.println("Destino: " + destino);
            System.out.println("Quantidade de Veículos: " + quantidadeVeiculos);

            JsonArray veiculosArray = linhaOnibus.get("vs").getAsJsonArray();

            System.out.println("  --- Veículos nesta linha ---");

            for (JsonElement veiculoElement : veiculosArray) {
                JsonObject veiculoObj = veiculoElement.getAsJsonObject();

                int prefixo = veiculoObj.get("p").getAsInt();
                double latitude = veiculoObj.get("py").getAsDouble();
                double longitude = veiculoObj.get("px").getAsDouble();
                String timestamp = veiculoObj.get("ta").getAsString();

                System.out.println("    Prefixo: " + prefixo + " | Lat: " + latitude + " | Lon: " + longitude + " | Hora: " + timestamp);
            }
            System.out.println("------------------------------------");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("""
                
                Este software tem como objetivo cadastrar seu servidor em nosso sistema para que futuramente possamos monitorá-lo,
                ou caso já cadastrado, atualizar as informações em nosso sistema.
                
                Para utilizá-lo:
                    - É necessária uma conexão com a internet;
                    - Será solicitado um login, utilize as mesmas credencias usadas em nosso website.
                
                Para iniciar, pressione ENTER:""");
        String iniciar = scanner.nextLine();

        JsonObject informacoesUsuarios = login();

        String idEmpresa = informacoesUsuarios.get("fkEmpresa").getAsString();

        if(Uuid.verificarArquivoUuid()) {
            String uuid = Uuid.buscarUuid();

            HttpResponse<String> response = ApiClient.buscarServidorUUID(uuid);

            if(response.statusCode() == 403) {
                long idGaragem = adicionarGaragem(idEmpresa);
                adicionarServidor(idEmpresa, uuid, idGaragem);
            } else {
                //atualizarServidor(uuid);
            }
        } else {
            String uuid = Uuid.criarUuid();

            long idGaragem = adicionarGaragem(idEmpresa);
            adicionarServidor(idEmpresa, uuid, idGaragem);
        }

        System.out.println("\nIniciando monitoramento do servidor...");

        SpTransApiClient apiSpTrans = new SpTransApiClient();

        boolean loginSpTrans = false;
        while(!loginSpTrans) {
            loginSpTrans = autenticarSpTrans(apiSpTrans);
        }

        System.out.println("\n✅Login na API da SPTrans realizado com sucesso!\n");

        HttpResponse<String> response = apiSpTrans.buscarPosicaoOnibus();

        JsonObject jsonOnibus = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonArray linhasOnibus = jsonOnibus.getAsJsonArray("l");

        listarLinhas(linhasOnibus);
    }
}
