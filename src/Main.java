import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int validFileCount = 0;

        while (true) {
            System.out.print("Введите путь к файлу: ");
            String path = scanner.nextLine();

            //Определяем, существует ли файл, путь к которому был указан. Является ли указанный путь путём именно к файлу, а не к папке
            File file = new File(path);
            boolean fileExists = file.exists();
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
                int googlebotCount = 0;
                int yandexbotCount = 0;
                Statistics stats = new Statistics();

                while ((line = reader.readLine()) != null) {
                    lineCount++;
                    int length = line.length();

                    // Проверка на длину строки
                    if (length > 1024) {
                        throw new LineTooLongException("Строка превышает 1024 символа: " + length + " символов.");
                    }

                    //Создание нового объекта LogEntry
                    LogEntry logEntry = new LogEntry(line);

                    // Извлечение User-Agent и подсчет количества ботов
                    UserAgent userAgent = new UserAgent(logEntry.getUserAgent());
                    if (!userAgent.equals("-")) {
                        String botName = userAgent.getBot();
                        if (botName.equals("Googlebot")) {
                            googlebotCount++;
                        } else if (botName.equals("YandexBot")) {
                            yandexbotCount++;
                        }
                    }
                    //Подсчет статистики средней скорости трафика
                    stats.addEntry(logEntry);
                }

                // Вывод результатов
                System.out.println("Общее количество строк в файле: " + lineCount);
                if (lineCount > 0) {
                    System.out.printf("Доля запросов от Googlebot: %.2f%%\n", (googlebotCount / (double) lineCount) * 100);
                    System.out.printf("Доля запросов от YandexBot: %.2f%%\n", (yandexbotCount / (double) lineCount) * 100);
                } else {
                    System.out.println("Нет строк для анализа.");
                }
                double trafficRate = stats.getTrafficRate();
                System.out.println("Средний объем трафика за час: " + trafficRate + " МБ/час");

                // Получаем доли операционных систем
               /* HashMap<String, Double> osShare = new HashMap<>(stats.getOSShare());
                System.out.println("Доли операционных систем:");
                for (HashMap.Entry<String, Double> entry : osShare.entrySet()) {
                    System.out.printf("%s: %.2f%%\n", entry.getKey(), entry.getValue() * 100);
                }
                System.out.println(osShare);*/

                /*//Получаем доли каждого браузера
                HashMap<String, Double> browserShare = new HashMap<>(stats.getBrowserShare());
                System.out.println("Доли каждого браузера:");
                for (HashMap.Entry<String, Double> entry : browserShare.entrySet()) {
                    System.out.printf("%s: %.2f%%\n", entry.getKey(), entry.getValue() * 100);
                }
                System.out.println(browserShare);*/

                /*//Адреса, существующих станиц
                HashSet<String> addressesPage200 = new HashSet<>(stats.getAddressesPage200());
                System.out.println("Адреса, существующих страниц: " + addressesPage200);

                //Адреса, несуществующих станиц
                HashSet<String> addressesPage404 = new HashSet<>(stats.getAddressesPage404());
                System.out.println("Адреса, несуществующих страниц: " + addressesPage404);*/

                //Вывод статистики
                System.out.printf("Среднее количество посещений сайта за час: %.2f\n", stats.getAverageVisitsPerHour());
                System.out.printf("Среднее количество ошибочных запросов в час: %.2f\n", stats.getAverageErrorRequestsPerHour());
                System.out.printf("Средняя посещаемость одним пользователем: %.2f\n", stats.getAverageVisitsPerUser ());

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

