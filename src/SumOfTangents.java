import java.util.*;

/**
 * Created by Michal Stankiewicz on 25.02.2018.
 */
public class SumOfTangents extends Thread{

    private static double total = 0;
    private static double[] array;
    private static List<Integer[]> partSizesList;
    private static int threadNumber = 0;

    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        Integer dataSize = 100;
        Integer threadsNumber = 0;
        System.out.print("Partition data size: ");
        dataSize = scanner.nextInt();
        System.out.print("Number of threads (1: 1,  2: 2,  3: 4,  4: 8,  5: 16,  6: 32): ");
        threadsNumber = scanner.nextInt()-1;

        Integer length = dataSize * 1000000;
        array = new double[length];

        System.out.println("\nInitializing array random data");
        Random r = new Random();
        for(int i=0; i<array.length; i++){
            array[i] = r.nextDouble();
        }


        //todo przeliczanie partsize dla okreslonych wątków
        Integer partSize[] = new Integer[6];
        List<Integer> availableThreadsNumber = new ArrayList<>(Arrays.asList(1,2,4,8,16,32));
        for(int i=0; i<availableThreadsNumber.size(); i++){
            Integer threadNumber = availableThreadsNumber.get(i);
            partSize[i] = length / threadNumber;
        }

        Integer numberOfParts = length / partSize[threadsNumber];
        List<Thread> threads = new ArrayList<>();

        partSizesList = new ArrayList<>();
        Long startTime = System.currentTimeMillis();
        //todo przeliczanie partycji danych względem wybranej ilości wątków
        Integer startValue = 0;
        Integer endValue = 0;
        for(int i=1; i<numberOfParts+1; i++){
            startValue = endValue + 1;
            endValue = i * partSize[threadsNumber];
            partSizesList.add(new Integer[]{startValue, endValue});
        }

        System.out.println("Threads execution started\n\n");
        for(int i=1; i<numberOfParts+1; i++){
            Thread t = new Thread(new SumOfTangents());
            t.start();
            threads.add(t);
        }

        //todo przechwycenie zakończenia wykonania ostatniego wątku
        for(Thread t : threads){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Long stopTime = System.currentTimeMillis();
        Double totalTime = (stopTime.doubleValue()-startTime.doubleValue())/1000;

        System.out.println("Overall calculation time: "+totalTime);
        System.out.println("Total calculated value: "+total);
    }

    @Override
    public void run() {
        Long startTime = System.currentTimeMillis();


        int localStartValue = partSizesList.get(threadNumber)[0];
        int localEndValue = partSizesList.get(threadNumber)[1];

        int localThreadNumber = threadNumber;
        threadNumber++;

        double localTotal = 0;
        for (int i = localStartValue; i < localEndValue; i++) {
            localTotal += Math.tan(array[i]);
        }

        Long stopTime = System.currentTimeMillis();
        Double totalTime = (stopTime.doubleValue()-startTime.doubleValue())/1000;

        System.out.println("["+localThreadNumber+"] "+"Thread ended with running time: "+totalTime);
        System.out.println("    Calculating data part: "+localStartValue+" - "+localEndValue);
        System.out.println("    Calculated value: "+localTotal+"\n");

        total += localTotal;
    }
}
