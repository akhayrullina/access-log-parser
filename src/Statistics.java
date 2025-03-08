import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;

public class Statistics {
    private double totalMB; // Общий объем трафика в мегабайтах
    private LocalDateTime minTime; // Минимальное время
    private LocalDateTime maxTime; // Максимальное время
    private HashSet<String> addressesPage; //Адреса, существующих страниц
    private HashMap<String, Integer> osFrequency; // Частота встречаемости операционных систем
    private long totalEntries; // Общее количество записей

    // Конструктор без параметров
    public Statistics() {
        this.totalMB = 0;
        this.minTime = null;
        this.maxTime = null;
        this.addressesPage = new HashSet<>();
        this.osFrequency = new HashMap<>();
        this.totalEntries = 0;
    }

    // Метод для добавления записи
    public void addEntry(LogEntry entry) {
        // Увеличиваем общий объем трафика
        totalMB += entry.getResponseSize() / 1048576.0; // 1 МБ = 1,048,576 байт;

        // Обновляем minTime и maxTime
        if (minTime == null || entry.getDateTime().isBefore(minTime)) {
            minTime = entry.getDateTime();
        }
        if (maxTime == null || entry.getDateTime().isAfter(maxTime)) {
            maxTime = entry.getDateTime();
        }

        //Кладем адреса, существующих страниц
        if (entry.getResponseCode() == 200 && entry.getReferer() != null) {
            addressesPage.add(entry.getReferer());
        }

        // Подсчет частоты встречаемости операционных систем
        UserAgent userAgent = new UserAgent(entry.getUserAgent());
        String operatingSystem = userAgent.getOperatingSystem();
        osFrequency.put(operatingSystem, osFrequency.getOrDefault(operatingSystem, 0) + 1);
        totalEntries++; // Увеличиваем общее количество записей
    }

    // Метод для расчета средней скорости трафика
    public double getTrafficRate() {
        if (minTime == null || maxTime == null) {
            return 0; // Если нет записей, возвращаем 0
        }

        // Вычисляем разницу в часах
        long hours = Duration.between(minTime, maxTime).toHours();
        if (hours == 0) {
            return totalMB; // Если разница 0 часов, возвращаем общий трафик
        }

        // Возвращаем средний объем трафика за час
        return totalMB / hours;
    }

    // Метод для получения долей операционных систем
    public HashMap<String, Double> getOSShare() {
        HashMap<String, Double> osShare = new HashMap<>();
        for (String os : osFrequency.keySet()) {
            osShare.put(os, (double) osFrequency.get(os) / totalEntries); // Доля для каждой ОС
        }
        return osShare;
    }

    //Геттеры
    public HashSet<String> getAddressesPage() {
        return addressesPage;
    }
}
