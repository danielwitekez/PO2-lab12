import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

class Mieszkaniec implements Runnable {
    private final Queue<String> usterek;
    private final Queue<String> wspoldzielonaKolejka;

    public Mieszkaniec(Queue<String> usterek, Queue<String> wspoldzielonaKolejka) {
        this.usterek = usterek;
        this.wspoldzielonaKolejka = wspoldzielonaKolejka;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000)); // Losowy czas co 1-3 dni
                synchronized (wspoldzielonaKolejka) {
                    wspoldzielonaKolejka.addAll(usterek);
                    System.out.println("Mieszkaniec zglosil usterki.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class MiesiacZakonczenieAwariaScheduler {
    private final Queue<String> wspoldzielonaKolejka;

    public MiesiacZakonczenieAwariaScheduler(Queue<String> wspoldzielonaKolejka) {
        this.wspoldzielonaKolejka = wspoldzielonaKolejka;
    }

    public void rozpocznij() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            synchronized (wspoldzielonaKolejka) {
                System.out.println("Koniec miesiąca! Naprawione usterki: " + wspoldzielonaKolejka);
                wspoldzielonaKolejka.clear();
            }
        }, 0, 31, TimeUnit.SECONDS);
    }
}

public class zad2b {
    public static void main(String[] args) {
        Queue<String> wspoldzielonaKolejka = new LinkedList<>();
        Queue<String> usterek = new LinkedList<>();
        usterek.add("Awaria 1");
        usterek.add("Awaria 2");

        Mieszkaniec mieszkaniec = new Mieszkaniec(usterek, wspoldzielonaKolejka);
        MiesiacZakonczenieAwariaScheduler scheduler = new MiesiacZakonczenieAwariaScheduler(wspoldzielonaKolejka);

        Thread mieszkaniecThread = new Thread(mieszkaniec);
        mieszkaniecThread.start();
        scheduler.rozpocznij();
    }
}
