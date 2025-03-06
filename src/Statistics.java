import java.time.Duration;
import java.time.LocalDateTime;

public class Statistics {
    private int totalTraffic; // Общий объем трафика
    private LocalDateTime minTime; // Минимальное время
    private LocalDateTime maxTime; // Максимальное время

    // Конструктор без параметров
    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = null;
        this.maxTime = null;
    }

    // Метод для добавления записи
    public void addEntry(LogEntry entry) {
        // Увеличиваем общий объем трафика
        totalTraffic += entry.getResponseSize();

        // Обновляем minTime и maxTime
        if (minTime == null || entry.getDateTime().isBefore(minTime)) {
            minTime = entry.getDateTime();
        }
        if (maxTime == null || entry.getDateTime().isAfter(maxTime)) {
            maxTime = entry.getDateTime();
        }
    }

    // Метод для расчета средней скорости трафика
    public double getTrafficRate() {
        if (minTime == null || maxTime == null) {
            return 0; // Если нет записей, возвращаем 0
        }

        // Вычисляем разницу в часах
        long hours = Duration.between(minTime, maxTime).toHours();
        if (hours == 0) {
            return totalTraffic; // Если разница 0 часов, возвращаем общий трафик
        }

        // Возвращаем средний объем трафика за час
        return (double) totalTraffic / hours;
    }
}
