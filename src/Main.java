import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int validFileCount = 0;

        while (true) {
            System.out.print("Введите путь к файлу: ");
            String path = scanner.nextLine();

            //Определяем, существует ли файл, путь к которому был указан
            File file = new File(path);
            boolean fileExists = file.exists();

            //Определяем, является ли указанный путь путём именно к файлу, а не к папке
            boolean isDirectory = file.isDirectory();

            if (!fileExists || !isDirectory) {
                System.out.println("Указанный файл не существует или это путь к папке.");
                continue;
            }

            validFileCount++;
            System.out.println("Путь указан верно. Это файл номер " + validFileCount);
        }
    }
}
