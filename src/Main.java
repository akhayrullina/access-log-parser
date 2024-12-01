import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите число 1: ");
        int firstNumber = new Scanner(System.in).nextInt();
        System.out.println("Введите число 2: ");
        int secondNumber = new Scanner(System.in).nextInt();
        System.out.println("Сумма двух чисел: " + (firstNumber + secondNumber));
        System.out.println("Разность: " + (firstNumber - secondNumber));
        System.out.println("Произведение: " + (firstNumber * secondNumber));
        System.out.println("Частное: " + ((double)firstNumber / secondNumber));
    }
}