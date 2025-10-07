import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
    static String ipServidor = "98.81.127.66";

    public static HttpResponse<String> autenticarUsuario(String json) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String url = String.format("http://%s:3333/usuarios/autenticar", ipServidor);
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

            System.out.println("buscando servidor uuid");

            String url = String.format("http://%s:3333/maquina/buscarServidorUUID/%s", ipServidor, uuid);
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

            String url = String.format("http://%s:3333/maquina/adicionarServidor", ipServidor);

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

            String url = String.format("http://%s:3333/maquina/atualizarServidor", ipServidor);

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
}
