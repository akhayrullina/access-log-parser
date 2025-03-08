import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LogEntry {
    // Поля класса
    private final String ipAddress; //IP адрес
    private final LocalDateTime dateTime; //Дата и время запроса
    private final HttpMethod method; //Метод запроса
    private final String requestPath; //Путь, по которому сделан запрос, и протокол
    private final int responseCode; //Код HTTP-ответа
    private final double responseSize; //Размер отданных данных в байтах
    private final String referer; //Путь к странице, с которой перешли на текущую страницу
    private final String userAgent; //(или UserAgent agent)

    // Конструктор
    public LogEntry(String logLine) {
        // Разбиваем строку на части
        String[] parts = logLine.split(" ", 12); // Разбиваем строку на 12 частей

        // Извлекаем IP-адрес
        this.ipAddress = parts[0];

        // Извлекаем дату и время
        String dateTimeString = parts[3].substring(1) + " " + parts[4].substring(0, parts[4].length() - 1);
        this.dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));

        // Извлекаем метод запроса и путь
        String request = parts[5] + " " + parts[6] + " " + parts[7];
        String[] requestParts = request.split(" ");
        this.method = HttpMethod.valueOf(requestParts[0].replace("\"", ""));
        this.requestPath = requestParts[1] + " " + requestParts[2].replace("\"", "");

        // Извлекаем код ответа и размер данных
        this.responseCode = Integer.parseInt(parts[8]);
        this.responseSize = Double.parseDouble(parts[9]);

        // Извлекаем referer
        this.referer = parts[10].equals("\"-\"") ? null : parts[10].replace("\"", "");

        // Извлекаем User-Agent
        this.userAgent = parts[11].replace("\"", "");

    }

    // Геттеры
    public String getIpAddress() {
        return ipAddress;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public double getResponseSize() {
        return responseSize;
    }

    public String getReferer() {
        return referer;
    }

    public String getUserAgent() {
        return userAgent;
    }

    // Перечисление для методов HTTP-запросов
    public enum HttpMethod {
        GET, POST, PUT, DELETE, HEAD, OPTIONS, PATCH
    }
}