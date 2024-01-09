import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

class Pracownik implements Runnable {
    private final List<Double> faktury;
    private final List<Double> wspoldzielonaLista;

    public Pracownik(List<Double> faktury, List<Double> wspoldzielonaLista) {
        this.faktury = faktury;
        this.wspoldzielonaLista = wspoldzielonaLista;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000)); // Losowy czas co 1-3 dni
                synchronized (wspoldzielonaLista) {
                    wspoldzielonaLista.addAll(faktury);
                    System.out.println("Pracownik zapisal faktury.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class MiesiacZakonczenieFirmaScheduler {
    private final List<Double> wspoldzielonaLista;

    public MiesiacZakonczenieFirmaScheduler(List<Double> wspoldzielonaLista) {
        this.wspoldzielonaLista = wspoldzielonaLista;
    }

    public void rozpocznij() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            synchronized (wspoldzielonaLista) {
                System.out.println("Koniec miesiąca! Największa faktura: " + znajdzNajwiekszaFakture(wspoldzielonaLista));
                wspoldzielonaLista.clear();
            }
        }, 0, 31, TimeUnit.SECONDS);
    }

    private double znajdzNajwiekszaFakture(List<Double> faktury) {
        if (faktury.isEmpty()) return 0;
        return faktury.stream().mapToDouble(Double::doubleValue).max().orElse(0);
    }
}

public class zad1b {
    public static void main(String[] args) {
        List<Double> wspoldzielonaLista = Collections.synchronizedList(new ArrayList<>());
        List<Double> faktury = Arrays.asList(1500.0, 2200.0, 1800.0);

        Pracownik pracownik = new Pracownik(faktury, wspoldzielonaLista);
        MiesiacZakonczenieFirmaScheduler scheduler = new MiesiacZakonczenieFirmaScheduler(wspoldzielonaLista);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(pracownik);
        scheduler.rozpocznij();
    }
}
