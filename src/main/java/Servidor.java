    import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import oshi.SystemInfo;

public class Servidor {
    String modeloCPU;
    long qtdRam;
    long qtdDisco;
    String sistemaOperacional;

    public Servidor(String modeloCPU, long qtdRam, long qtdDisco, String sistemaOperacional) {
        this.modeloCPU = modeloCPU;
        this.qtdRam = qtdRam;
        this.qtdDisco = qtdDisco;
        this.sistemaOperacional = sistemaOperacional;
    }

    public static JsonObject capturarInformacoesComputador() {
        SystemInfo si = new SystemInfo();

        String modeloCPU = si.getHardware().getProcessor().getProcessorIdentifier().getName();
        long qtdRam = si.getHardware().getMemory().getTotal() / (1024 * 1024 * 1024);
        String sistemaOperacional = si.getOperatingSystem().toString();
        long qtdDisco = si.getHardware().getDiskStores().getFirst().getSize() / (1024 * 1024 * 1024);

        Servidor servidor = new Servidor(modeloCPU, qtdRam, qtdDisco, sistemaOperacional);
        String json = new Gson().toJson(servidor);

        JsonObject jsonServidor = JsonParser.parseString(json).getAsJsonObject();

        return jsonServidor;
    }
}
