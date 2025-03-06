public class UserAgent {
    private final String operatingSystem;
    private final String browser;
    private final String bot;

    // Конструктор, который принимает строку User-Agent
    public UserAgent(String userAgentString) {
        this.operatingSystem = extractOperatingSystem(userAgentString);
        this.browser = extractBrowser(userAgentString);
        this.bot = extractFragmentOfBots(searchBots(userAgentString));
    }

    // Метод для извлечения операционной системы
    private String extractOperatingSystem(String userAgentString) {
        if (userAgentString.contains("Windows")) {
            return "Windows";
        } else if (userAgentString.contains("Mac OS X") || userAgentString.contains("Macintosh")) {
            return "macOS";
        } else if (userAgentString.contains("Linux")) {
            return "Linux";
        } else {
            return "Unknown OS"; // Если не удалось определить ОС
        }
    }

    // Метод для извлечения браузера
    private String extractBrowser(String userAgentString) {
        if (userAgentString.contains("Edge")) {
            return "Edge";
        } else if (userAgentString.contains("Firefox")) {
            return "Firefox";
        } else if (userAgentString.contains("Chrome") && !userAgentString.contains("Edg")) {
            return "Chrome";
        } else if (userAgentString.contains("Opera") || userAgentString.contains("OPR")) {
            return "Opera";
        } else {
            return "Other"; // Если не удалось определить браузер
        }
    }

    //Метод находит информацию о ботах и возвращает ее
    public String searchBots(String userAgentString) {
        String informationAboutBot = "";

        //Находим информацию между первыми скобками
        int firstBrackets = userAgentString.indexOf("(");
        int lastBrackets = userAgentString.indexOf(")", firstBrackets);

        // Проверяем, что обе скобки найдены
        if (firstBrackets != -1 && lastBrackets != -1 && lastBrackets > firstBrackets) {
            informationAboutBot = userAgentString.substring(firstBrackets + 1, lastBrackets).trim();
        }

        return informationAboutBot;
    }

    //Метод извлекает информацию из фрагмента
    public String extractFragmentOfBots(String bots) {
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

    // Геттер для операционной системы
    public String getOperatingSystem() {
        return operatingSystem;
    }

    // Геттер для браузера
    public String getBrowser() {
        return browser;
    }

    //Геттер для информации о боте
    public String getBot() {
        return bot;
    }
}
