package cs311.hw4;

/**
 * Created by Adam on 10/6/2016.
 */
public class ResultImpl implements IResult {
    private int size, time;

    public ResultImpl(int s, int t){
        size = s;
        time = t;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public long getTime() {
        return time;
    }
}
