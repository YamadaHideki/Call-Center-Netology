import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) {

        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Queue<Call> callQueue = new LinkedBlockingQueue<>();

        Thread atc = new Thread(() -> {
            int sleep = 1000;
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 60; j++) {
                    callQueue.add(new Call());
                }
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        atc.start();

        for (int i = 0; i < 20; i++) {
            pool.execute(() -> {
                int sleep = 4000;
                while (true) {
                    Call call = callQueue.poll();
                    if (call != null) {
                        try {
                            Thread.sleep(sleep);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        call.changeStatus();
                    } else {
                        System.out.println(Thread.currentThread().getName() + " ожидает звонок");
                    }
                }
            });
        }

        Thread callInfo = new Thread(() -> {
            while (callQueue.size() > 0) {
                System.out.println("Звонков в очереди: " + callQueue.size());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });
        callInfo.start();

        pool.shutdown();
    }
}
