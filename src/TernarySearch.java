import java.util.Arrays;
import java.util.Random;

public class TernarySearch {

    public static void main(String[] args) {
        Random rand = new Random();

        Node[] arr = new Node[5];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new Node(i);
        }
//        Node[] arr = new Node[rand.nextInt(101)];
//        for (int i = 0; i < arr.length; i++) {
//            arr[i] = new Node(rand.nextInt(101));
//        }
//        Arrays.sort(arr);
        Node toFind = new Node(5);

        Node node = ternarySearch(arr, toFind);

        //Perhaps use Optionals...
        if (node != null){
            System.out.println(node.data);
        }
        else {
            System.out.println("Could not find the node " + toFind.data);
        }
    }

    private static <T extends Comparable<T>> T ternarySearch(T[] arr, T toFind){
        int left = 0;
        int right = arr.length - 1;
        int leftThird, rightThird;

        while (left < right){
            leftThird = left + (right - left) / 3;
            rightThird = right - (right - left) / 3;

            if (toFind.compareTo(arr[left]) == 0){
                return arr[left];
            }
            if (toFind.compareTo(arr[right]) == 0){
                return arr[right];
            }
            if (toFind.compareTo(arr[leftThird]) <= 0){
                if (toFind.compareTo(arr[leftThird]) == 0){
                    return arr[leftThird];
                }
                right = leftThird - 1;
            }
            else if (toFind.compareTo(arr[rightThird]) >= 0){
                if (toFind.compareTo(arr[rightThird]) == 0){
                    return arr[rightThird];
                }
                left = rightThird + 1;
            }
            else {
                left = leftThird + 1;
                right = rightThird - 1;
            }
        }
        return null;
    }

    private static void printWithBounds(Node[] arr, int left, int right){
        for (int i = left; i <= right; i++){
            System.out.print(arr[i].data + " ");
        }
        System.out.println();
    }

    private static class Node implements Comparable<Node> {

        int data;
        Node(int n){
            this.data = n;
        }

        @Override
        public int compareTo(Node o) {
            if (this.data < o.data){
                return -1;
            }
            else if (this.data > o.data){
                return 1;
            }
            return 0;
        }
    }
}
