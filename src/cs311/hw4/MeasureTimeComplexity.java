package cs311.hw4;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 10/6/2016.
 */
public class MeasureTimeComplexity implements IMeasureTimeComplexity {

    private final long NANOS_IN_MILLIS = 1000000;

    @Override
    public int normalize(IMeasurable m, long timeInMilliseconds) {
        long start = System.nanoTime();
        int iterations = 0;

        while ((System.nanoTime() - start) / NANOS_IN_MILLIS < timeInMilliseconds){
            m.execute();
            iterations++;
        }

        return iterations;
    }

    @Override
    public List<? extends IResult> measure(IMeasureFactory factory, int nmeasures, int startsize, int endsize, int stepsize) {
        List<IResult> results = new ArrayList<>();

        for (int i = startsize; i <= endsize; i += stepsize) {
            IMeasurable measurable = factory.createRandom(i);
            long start = System.nanoTime();
            for (int j = 0; j < nmeasures; j++) {
                measurable.execute();
            }
            results.add(new ResultImpl(i, (int) ((System.nanoTime() - start) / NANOS_IN_MILLIS)));
        }

        return results;
    }
}
