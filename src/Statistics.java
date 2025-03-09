import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Statistics {
    private double totalMB; // Общий объем трафика в мегабайтах
    private LocalDateTime minTime; // Минимальное время
    private LocalDateTime maxTime; // Максимальное время
    private long totalHours; // Общее количество часов, за которые есть записи
    private HashSet<String> addressesPage200; //Адреса, существующих страниц
    private HashSet<String> addressesPage404; //Адреса, несуществующих страниц
    private HashMap<String, Integer> osFrequency; // Частота встречаемости операционных систем
    private HashMap<String, Integer> browserFrequency; // Частота встречаемости браузеров
    private long totalEntries; // Общее количество записей
    private long userVisits; // Количество посещений обычных пользователей
    private long errorRequests; // Количество ошибочных запросов
    private Set<String> uniqueUserIPs; // Уникальные IP-адреса обычных пользователей

    // Конструктор без параметров
    public Statistics() {
        this.addressesPage404 = new HashSet<>();
        this.totalMB = 0;
        this.minTime = null;
        this.maxTime = null;
        this.totalHours = 0;
        this.addressesPage200 = new HashSet<>();
        this.browserFrequency = new HashMap<>();
        this.osFrequency = new HashMap<>();
        this.totalEntries = 0;
        this.userVisits = 0;
        this.errorRequests = 0;
        this.uniqueUserIPs = new HashSet<>();
    }

    // Метод для добавления записи
    public void addEntry(LogEntry entry) {
        totalEntries++;// Увеличиваем общее количество записей
        // Увеличиваем общий объем трафика
        totalMB += entry.getResponseSize() / 1048576.0; // 1 МБ = 1,048,576 байт;

        // Обновляем minTime и maxTime
        if (minTime == null || entry.getDateTime().isBefore(minTime)) {
            minTime = entry.getDateTime();
        }
        if (maxTime == null || entry.getDateTime().isAfter(maxTime)) {
            maxTime = entry.getDateTime();
        }
        //Считаем общее количество часов
        totalHours = Duration.between(minTime, maxTime).toHours();

        //Кладем адреса, существующих страниц
        if (entry.getResponseCode() == 200 && entry.getReferer() != null) {
            addressesPage200.add(entry.getReferer());
        }

        //Кладем адреса, несуществующих страниц
        if (entry.getResponseCode() == 404 && entry.getReferer() != null) {
            addressesPage404.add(entry.getReferer());
        }

        // Проверяем, был ли ошибочный код ответа (4xx или 5xx)
        if (entry.getResponseCode() >= 400 && entry.getResponseCode() < 600) {
            errorRequests++; // Увеличиваем количество ошибочных запросов
        }

        // Подсчет частоты встречаемости операционных систем и браузеров
        UserAgent userAgent = new UserAgent(entry.getUserAgent());

        String operatingSystem = userAgent.getOperatingSystem();
        String browser = userAgent.getBrowser();
        osFrequency.put(operatingSystem, osFrequency.getOrDefault(operatingSystem, 0) + 1);
        browserFrequency.put(browser, browserFrequency.getOrDefault(browser, 0) + 1);

        // Проверяем, является ли User-Agent ботом
        if (!isBot(userAgent)) {
            userVisits++; // Увеличиваем количество посещений обычных пользователей
            uniqueUserIPs.add(entry.getIpAddress()); // Добавляем уникальный IP-адрес
        }
    }

    //Метод, проверяющий является ли User-Agent ботом
    public boolean isBot(UserAgent userAgent) {
        if (userAgent.getBot() == "0") {
            return false;
        }
        return true;
    }

    // Метод для расчета среднего объема трафика за час
    public double getTrafficRate() {
        if (totalHours == 0) {
            return totalMB; // Если разница 0 часов, возвращаем общий трафик
        }
        return totalMB / totalHours;
    }

    // Метод для подсчета среднего количества посещений за час
    public double getAverageVisitsPerHour() {
        if (totalHours == 0) {
            return 0; // Избегаем деления на ноль
        }
        return (double) userVisits / totalHours;
    }

    // Метод для расчёта средней посещаемости одним пользователем
    public double getAverageVisitsPerUser () {
        if (uniqueUserIPs.size() == 0) {
            return 0; // Избегаем деления на ноль
        }
        return (double) userVisits / uniqueUserIPs.size();
    }

    // Метод для подсчета среднего количества ошибочных запросов за час
    public double getAverageErrorRequestsPerHour() {
        if (totalHours == 0) {
            return 0; // Избегаем деления на ноль
        }
        return (double) errorRequests / totalHours;
    }


    // Метод для получения долей операционных систем
    public HashMap<String, Double> getOSShare() {
        HashMap<String, Double> osShare = new HashMap<>();
        for (String os : osFrequency.keySet()) {
            osShare.put(os, (double) osFrequency.get(os) / totalEntries); // Доля для каждой ОС
        }
        return osShare;
    }

    // Метод для получения долей каждого браузера
    public HashMap<String, Double> getBrowserShare() {
        HashMap<String, Double> browserShare = new HashMap<>();
        for (String browser : browserFrequency.keySet()) {
            browserShare.put(browser, (double) browserFrequency.get(browser) / totalEntries); // Доля для каждого браузера
        }
        return browserShare;
    }

    //Геттеры
    public HashSet<String> getAddressesPage200() {
        return addressesPage200;
    }

    public HashSet<String> getAddressesPage404() {
        return addressesPage404;
    }

    public long getUserVisits() {
        return userVisits;
    }

    public double getTotalHours() {
        return totalHours;
    }
}
