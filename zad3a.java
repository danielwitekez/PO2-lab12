import java.io.IOException;

public class zad3a {

    public static void main(String[] args) {
        String sciezkaDoProgramu = "C:\\Windows\\System32\\mspaint.exe"; // Ścieżka do Paint.exe

        try {
            uruchomProgram(sciezkaDoProgramu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void uruchomProgram(String sciezka) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(sciezka);
        processBuilder.start();
    }
}
