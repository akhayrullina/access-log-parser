import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

            if (!fileExists || isDirectory) {
                System.out.println("Указанный файл не существует или это путь к папке.");
                continue;
            }

            validFileCount++;
            System.out.println("Путь указан верно. Это файл номер " + validFileCount);

            // Чтение файла
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                int lineCount = 0;
                int maxLength = 0;
                int minLength = Integer.MAX_VALUE;

                while ((line = reader.readLine()) != null) {
                    lineCount++;
                    int length = line.length();

                    // Проверка на длину строки
                    if (length > 1024) {
                        throw new LineTooLongException("Строка превышает 1024 символа: " + length + " символов.");
                    }

                    // Определение максимальной и минимальной длины
                    if (length > maxLength) {
                        maxLength = length;
                    }
                    if (length < minLength) {
                        minLength = length;
                    }
                }

                // Вывод результатов
                System.out.println("Общее количество строк в файле: " + lineCount);
                System.out.println("Длина самой длинной строки: " + maxLength);
                System.out.println("Длина самой короткой строки: " + (minLength == Integer.MAX_VALUE ? 0 : minLength));

            } catch (LineTooLongException e) {
                System.err.println("Ошибка: " + e.getMessage());
                break; // Прекращаем выполнение программы
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        scanner.close();
    }
}