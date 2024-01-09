import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

class Nauczyciel implements Runnable {
    private final List<Integer> oceny;
    private final List<Integer> wspoldzielonaLista;

    public Nauczyciel(List<Integer> oceny, List<Integer> wspoldzielonaLista) {
        this.oceny = oceny;
        this.wspoldzielonaLista = wspoldzielonaLista;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000)); // Losowy czas co 1-3 tygodnie
                synchronized (wspoldzielonaLista) {
                    wspoldzielonaLista.addAll(oceny);
                    System.out.println("Nauczyciel zapisal oceny.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class RokSzkolnyScheduler {
    private final List<Integer> wspoldzielonaLista;

    public RokSzkolnyScheduler(List<Integer> wspoldzielonaLista) {
        this.wspoldzielonaLista = wspoldzielonaLista;
    }

    public void rozpocznij() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            synchronized (wspoldzielonaLista) {
                System.out.println("Koniec roku! Średnia ocen: " + obliczSrednia(wspoldzielonaLista));
                wspoldzielonaLista.clear();
            }
        }, 0, 30, TimeUnit.SECONDS);
    }

    private double obliczSrednia(List<Integer> oceny) {
        if (oceny.isEmpty()) return 0;
        return oceny.stream().mapToDouble(Integer::doubleValue).average().orElse(0);
    }
}

public class zad1a {
    public static void main(String[] args) {
        List<Integer> wspoldzielonaLista = Collections.synchronizedList(new ArrayList<>());
        List<Integer> oceny = Arrays.asList(4, 5, 3, 4, 5);

        Nauczyciel nauczyciel = new Nauczyciel(oceny, wspoldzielonaLista);
        RokSzkolnyScheduler scheduler = new RokSzkolnyScheduler(wspoldzielonaLista);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(nauczyciel);
        scheduler.rozpocznij();
    }
}

