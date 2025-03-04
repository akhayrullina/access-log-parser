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

                while ((line = reader.readLine()) != null) {
                    lineCount++;
                    int length = line.length();

                    // Проверка на длину строки
                    if (length > 1024) {
                        throw new LineTooLongException("Строка превышает 1024 символа: " + length + " символов.");
                    }

                    // Извлечение User-Agent и подсчет количества ботов
                    String userAgent = extractUserAgent(line);
                    if (!userAgent.equals("-")) {
                        String bots = searchBots(userAgent);
                        String botName = extractFragmentOfBots(bots);

                        if (botName.equals("Googlebot")) {
                            googlebotCount++;
                        } else if (botName.equals("YandexBot")) {
                            yandexbotCount++;
                        }
                    }
                }

                // Вывод результатов
                System.out.println("Общее количество строк в файле: " + lineCount);
                if (lineCount > 0) {
                    System.out.printf("Доля запросов от Googlebot: %.2f%%\n", (googlebotCount / (double) lineCount) * 100);
                    System.out.printf("Доля запросов от YandexBot: %.2f%%\n", (yandexbotCount / (double) lineCount) * 100);
                } else {
                    System.out.println("Нет строк для анализа.");
                }


            } catch (LineTooLongException e) {
                System.err.println("Ошибка: " + e.getMessage());
                break; // Прекращаем выполнение программы
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        scanner.close();
    }


    public static String extractUserAgent(String logLine) {
        // Находим индекс последней и предпоследней кавычки
        int lastQuoteIndex = logLine.lastIndexOf("\"");
        int secondLastQuoteIndex = logLine.lastIndexOf("\"", lastQuoteIndex - 1);

        // Проверяем, что кавычки найдены
        if (secondLastQuoteIndex == -1 || lastQuoteIndex == -1 || secondLastQuoteIndex >= lastQuoteIndex) {
            return "-"; // Возвращаем знак минус, если User-Agent не найден
        }

        // Извлекаем строку между последней и предпоследней кавычками
        return logLine.substring(secondLastQuoteIndex + 1, lastQuoteIndex).trim();
    }

    //Метод находит информацию о ботах и возвращает ее
    public static String searchBots(String userAgent) {
        String informationAboutBot = "";

        //Находим информацию между первыми скобками
        int firstBrackets = userAgent.indexOf("(");
        int lastBrackets = userAgent.indexOf(")", firstBrackets);

        // Проверяем, что обе скобки найдены
        if (firstBrackets != -1 && lastBrackets != -1 && lastBrackets > firstBrackets) {
            informationAboutBot = userAgent.substring(firstBrackets + 1, lastBrackets).trim();
        }

        return informationAboutBot;
    }

    //Метод извлекает информацию из фрагмента
    public static String extractFragmentOfBots(String bots) {
        String result = "0"; // По умолчанию, если ничего не найдено
        // Разделяем строку по точке с запятой
        String[] parts = bots.split(";");

        // Проверяем, что есть хотя бы два фрагмента и очищаем от пробела
        if (parts.length >= 2) {
            String fragment = parts[1].trim();

            // Находим индекс слэша
            int slashIndex = fragment.indexOf("/");

            // Если слэш есть, извлекаем часть до слэша
            if (slashIndex != -1) {
                result = fragment.substring(0, slashIndex).trim();
            }
        }
        return result;
    }
}

