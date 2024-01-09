import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

class Sportowiec implements Runnable {
    private final List<String> osiagniecia;
    private final List<String> wspoldzielonaLista;

    public Sportowiec(List<String> osiagniecia, List<String> wspoldzielonaLista) {
        this.osiagniecia = osiagniecia;
        this.wspoldzielonaLista = wspoldzielonaLista;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000)); // Losowy czas co 1-3 miesiące
                synchronized (wspoldzielonaLista) {
                    wspoldzielonaLista.addAll(osiagniecia);
                    System.out.println("Sportowiec zapisal osiagniecia.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class MiesiacZakonczenieSportScheduler {
    private final List<String> wspoldzielonaLista;

    public MiesiacZakonczenieSportScheduler(List<String> wspoldzielonaLista) {
        this.wspoldzielonaLista = wspoldzielonaLista;
    }

    public void rozpocznij() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            synchronized (wspoldzielonaLista) {
                System.out.println("Koniec miesiąca! Najlepsze osiągnięcie: " + znajdzNajlepszeOsiagniecie(wspoldzielonaLista));
                wspoldzielonaLista.clear();
            }
        }, 0, 24, TimeUnit.SECONDS);
    }

    private String znajdzNajlepszeOsiagniecie(List<String> osiagniecia) {
        if (osiagniecia.isEmpty()) return "Brak osiągnięć";
        return Collections.max(osiagniecia);
    }
}

public class zad1c {
    public static void main(String[] args) {
        List<String> wspoldzielonaLista = Collections.synchronizedList(new ArrayList<>());
        List<String> osiagniecia = Arrays.asList("Złoty medal", "Srebrny medal", "Rekord świata");

        Sportowiec sportowiec = new Sportowiec(osiagniecia, wspoldzielonaLista);
        MiesiacZakonczenieSportScheduler scheduler = new MiesiacZakonczenieSportScheduler(wspoldzielonaLista);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(sportowiec);
        scheduler.rozpocznij();
    }
}
