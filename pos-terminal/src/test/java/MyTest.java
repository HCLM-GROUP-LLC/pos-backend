import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class MyTest {
    public static void main(String[] args) {
        Instant instant = Instant.now();
        String formatted = instant.atZone(ZoneId.of("Asia/Shanghai"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(formatted);
    }
}
