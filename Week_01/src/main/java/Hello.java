public class Hello {

    public static void main(String[] args) {
        int a = 12;
        int b = 3;
        int add = a + b;
        int sub = a - b;
        int mul = a * b;
        float div = a / b;

        int[] numbers = {1, 4, 6};
        int sum = 0;

        for (int i : numbers) {
            if (i % 2 == 0) {
                sum += i;
            }
        }
    }
}
