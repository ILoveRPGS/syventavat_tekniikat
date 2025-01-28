
import java.time.LocalDate;
import java.util.Arrays;

public class App {
    public static void main(String[] args) throws Exception {
        Event[] events = new Event[5];
        Category appleMacOS = new Category("apple", "macOS");

        events[0] = new Event(LocalDate.of(2024, 9, 16), "macOS 15 Sequoia released", appleMacOS);
        events[1] = new Event(LocalDate.of(2023, 9, 26), "macOS 14 Sonoma released", appleMacOS);
        events[2] = new Event(LocalDate.of(2022, 10, 24), "macOS 13 Ventura released", appleMacOS);
        events[3] = new Event(LocalDate.of(2021, 10, 25), "macOS 12 Monterey released", appleMacOS);
        events[4] = new Event(LocalDate.of(2020, 11, 12), "macOS 11 Big Sur released", appleMacOS);
        String[] tapahtumat = new String[5];
        int i = 0;
        for (Event event : events) {
            StringBuilder sb = new StringBuilder();
            String text = event.getDate().getDayOfWeek().toString().toLowerCase();
            String day = text.substring(0, 1).toUpperCase() + text.substring(1);
            sb.append(event.getDescription().split(" ")[0])
              .append(" ")
              .append(event.getDescription().split(" ")[1]) 
              .append(" ")
              .append(event.getDescription().split(" ")[2]);
            if(event.getDescription().split(" ")[2].equals("Big")) {
                sb.append(" ");
                sb.append(event.getDescription().split(" ")[3]);
            }
              sb.append(" was released on a ")
              .append(day);
            if(event.getDescription().split(" ")[2].equals("Big")) {
                tapahtumat[i] = event.getDescription().split(" ")[2] + " " + event.getDescription().split(" ")[3];
            }
            else{
                tapahtumat[i] = event.getDescription().split(" ")[2];
            }
            System.out.println(sb.toString());
            i++;
        }
        Arrays.sort(tapahtumat);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("In alphabetical order: [");
        for(String tapahtuma : tapahtumat) {
            sb2.append(tapahtuma);
            sb2.append(", ");
        }
        sb2.delete(sb2.length()-2, sb2.length());
        sb2.append("]");
        System.out.println(sb2.toString());

    }

}
