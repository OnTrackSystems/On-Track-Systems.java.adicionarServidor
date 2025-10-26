import java.net.CookieManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SpTransApiClient {
    static String linkServidor = "http://api.olhovivo.sptrans.com.br/v2.1";
    static String apiToken = "27fe7a14f9c8c95d690bb621658448e3f26c56bf18cdf9e36698d7a52f180971";
    private final HttpClient client;

    public SpTransApiClient() {
        this.client = HttpClient.newBuilder()
                .cookieHandler(new CookieManager())
                .build();
    }

    public HttpResponse<String> autenticarUsuarioSpTrans() {
        String chave = "27fe7a14f9c8c95d690bb621658448e3f26c56bf18cdf9e36698d7a52f180971";

        try {
            String url = String.format("%s/Login/Autenticar?token=%s", linkServidor, chave);
            URI uri = URI.create(url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response;
        } catch (Exception e) {
            System.out.println("erro: " + e);
            e.printStackTrace();
            return null;
        }
    }

    public HttpResponse<String> buscarPosicaoOnibus() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://api.olhovivo.sptrans.com.br/v2.1/Posicao"))
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
}
