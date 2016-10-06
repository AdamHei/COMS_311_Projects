package cs311.hw4;

import java.util.List;

/**
 * Created by Adam on 10/6/2016.
 */
public class SlowMatrixTest {
    public static void main(String[] args) {
        IMeasureFactory iMeasureFactory = new SlowMatrixFactory();
        IMeasurable iMeasurable = iMeasureFactory.createRandom(2);
        IMeasureTimeComplexity measureTimeComplexity = new MeasureTimeComplexity();
        int iterations = measureTimeComplexity.normalize(iMeasurable, 2);

        System.out.println(iterations);

        List<? extends IResult> results = measureTimeComplexity.measure(iMeasureFactory, iterations, 2, 100, 1);
        final long[] totalTime = {0};
        results.forEach(result -> totalTime[0] += result.getTime());
//        System.out.println(totalTime[0]);

        for (IResult result: results){
            System.out.println(result.getSize() + "," + result.getTime());
        }
    }
}
