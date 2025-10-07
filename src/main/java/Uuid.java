import java.io.*;
import java.util.UUID;

public class Uuid {
    public static File arquivo = new File(".uuid");

    public static boolean verificarArquivoUuid() {
        return arquivo.exists();
    }

    public static String buscarUuid() {
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao acessar o arquivo de UUID da máquina");
        }
    }

    public static String criarUuid() {
        String uuid = UUID.randomUUID().toString();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            writer.write(uuid);
            return uuid;
        } catch(IOException e) {
            throw new RuntimeException("Falha ao salvar o UUID no arquivo");
        }
    }
}
