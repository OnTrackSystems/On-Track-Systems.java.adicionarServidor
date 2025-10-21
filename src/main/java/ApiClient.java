import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
    static String ipServidor = "13.220.58.95";
    static String portaServidor = "3333";

    public static HttpResponse<String> autenticarUsuario(String json) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String url = String.format("http://%s:%s/usuarios/autenticar", ipServidor, portaServidor);
            URI uri = URI.create(url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HttpResponse<String> buscarServidorUUID(String uuid) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String url = String.format("http://%s:%s/maquina/buscarServidorUUID/%s", ipServidor, portaServidor, uuid);
            URI uri = URI.create(url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HttpResponse<String> adicionarServidor(String json) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String url = String.format("http://%s:%s/maquina/adicionarServidor", ipServidor, portaServidor);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response;

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HttpResponse<String> atualizarServidor(String json) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String url = String.format("http://%s:%s/maquina/atualizarServidor", ipServidor, portaServidor);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HttpResponse<String> verificarGaragemNome(int idGaragem) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String url = String.format("http://%s:%s/garagens/verificarGaragemId/%s", ipServidor, portaServidor, idGaragem);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri((URI.create(url)))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HttpResponse<String> cadastrarGaragem(String json) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String url = String.format("http://%s:%s/garagens/adicionarGaragem", ipServidor, portaServidor);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
