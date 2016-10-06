package cs311.hw4;

/**
 * Created by Adam on 10/5/2016.
 */
public class SlowMatrix implements IMatrix, IMeasurable {

    private int[][] matrix;

    public SlowMatrix(int n, int m){
        matrix = new int[n][m];
    }

    @Override
    public void execute() {
        multiply(this);
//        add(this);
    }

    @Override
    public IMatrix subMatrix(int upperLeftRow, int upperLeftCol, int lowerRightRow, int lowerRightCol) throws IllegalArgumentException {
        if (isIllegalBounds(upperLeftRow, upperLeftCol, lowerRightRow, lowerRightCol)) throw new IllegalArgumentException("Check your bounds");

        int rows = (lowerRightRow - upperLeftRow) + 1;
        int cols = (lowerRightCol - upperLeftCol) + 1;
        IMatrix subMatrix = new SlowMatrix(rows, cols);

        for (int i = upperLeftRow; i <= lowerRightCol; i++) {
            for (int j = upperLeftCol; j <= lowerRightCol; j++) {
                subMatrix.setElement(i - upperLeftRow, j - upperLeftCol, matrix[i][j]);
            }
        }

        return subMatrix;
    }

    @Override
    public void setElement(int row, int col, Number val) throws IllegalArgumentException {
        if (isSimpleOutOfBounds(row, col)) throw new IllegalArgumentException("Check your indicies");
        matrix[row][col] = val.intValue();
    }

    @Override
    public Number getElement(int row, int col) throws IllegalArgumentException {
        if (isSimpleOutOfBounds(row, col)) throw new IllegalArgumentException("Check your indicies");
        return matrix[row][col];
    }

    @Override
    public IMatrix multiply(IMatrix mat) throws IllegalArgumentException {
        SlowMatrix multiplier = (SlowMatrix) mat;
        if (matrix[0].length != multiplier.matrix.length) throw new IllegalArgumentException("Incompatible matricies");

        IMatrix product = new SlowMatrix(matrix.length, multiplier.matrix[0].length);

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < multiplier.matrix[0].length; j++) {
                int sum = 0;
                int[] row = matrix[i];
                for (int k = 0; k < multiplier.matrix[j].length; k++) {
                    sum += row[k] * multiplier.matrix[j][k];
                }
                product.setElement(i, j, sum);
            }
        }

        return product;
    }

    @Override
    public IMatrix add(IMatrix mat) throws IllegalArgumentException {
        SlowMatrix addMatrix = (SlowMatrix) mat;
        if (matrix.length != addMatrix.matrix.length || matrix[0].length != addMatrix.matrix[0].length){
            throw new IllegalArgumentException("These matricies have differing sizes");
        }

        IMatrix sumMatrix = new SlowMatrix(matrix.length, matrix[0].length);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                int sum = getElement(i, j).intValue() + addMatrix.getElement(i, j).intValue();
                sumMatrix.setElement(i, j, sum);
            }
        }

        return sumMatrix;
    }

    private boolean isSimpleOutOfBounds(int row, int col){
        return row < 0 ||
                row >= matrix.length ||
                col < 0 ||
                col >= matrix[0].length;
    }

    private boolean isIllegalBounds(int upperLeftRow, int upperLeftCol, int lowerRightRow, int lowerRightCol) {
        return upperLeftRow < 0 ||
                upperLeftRow >= matrix.length ||
                upperLeftCol < 0 ||
                upperLeftCol >= matrix[0].length ||
                lowerRightRow < 0 ||
                lowerRightRow >= matrix.length ||
                lowerRightCol < 0 ||
                lowerRightCol >= matrix[0].length ||
                upperLeftRow > lowerRightRow ||
                upperLeftCol > lowerRightCol;
    }
}
