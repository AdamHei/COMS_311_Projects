package cs311.hw4;

import java.security.SecureRandom;

/**
 * Created by Adam on 10/5/2016.
 */
public class SlowMatrixFactory implements IMeasureFactory {

    @Override
    public IMeasurable createRandom(int size) {
        SlowMatrix slowMatrix = new SlowMatrix(size, size);
        SecureRandom rand = new SecureRandom();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                slowMatrix.setElement(i, j, rand.nextInt());
            }
        }

        return slowMatrix;
    }
}
