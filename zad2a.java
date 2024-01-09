import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;
class Klient implements Runnable {
    private final Queue<String> zamowienia;
    private final Queue<String> wspoldzielonaKolejka;

    public Klient(Queue<String> zamowienia, Queue<String> wspoldzielonaKolejka) {
        this.zamowienia = zamowienia;
        this.wspoldzielonaKolejka = wspoldzielonaKolejka;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000)); // Losowy czas co 1-3 dni
                synchronized (wspoldzielonaKolejka) {
                    wspoldzielonaKolejka.addAll(zamowienia);
                    System.out.println("Klient wyslal zamowienia.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class MiesiacZakonczenieScheduler {
    private final Queue<String> wspoldzielonaKolejka;

    public MiesiacZakonczenieScheduler(Queue<String> wspoldzielonaKolejka) {
        this.wspoldzielonaKolejka = wspoldzielonaKolejka;
    }

    public void rozpocznij() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            synchronized (wspoldzielonaKolejka) {
                System.out.println("Koniec miesiąca! Największa faktura: " + znajdzNajwiekszaFakture(wspoldzielonaKolejka));
                wspoldzielonaKolejka.clear();
            }
        }, 0, 31, TimeUnit.SECONDS);
    }

    private String znajdzNajwiekszaFakture(Queue<String> faktury) {
        if (faktury.isEmpty()) return "Brak faktur";
        return faktury.stream().max(String::compareTo).orElse("");
    }
}

public class zad2a {
    public static void main(String[] args) {
        Queue<String> wspoldzielonaKolejka = new LinkedList<>();
        Queue<String> zamowienia = new LinkedList<>();
        zamowienia.add("Zamowienie 1");
        zamowienia.add("Zamowienie 2");

        Klient klient = new Klient(zamowienia, wspoldzielonaKolejka);
        MiesiacZakonczenieScheduler scheduler = new MiesiacZakonczenieScheduler(wspoldzielonaKolejka);

        Thread klientThread = new Thread(klient);
        klientThread.start();
        scheduler.rozpocznij();
    }
}
