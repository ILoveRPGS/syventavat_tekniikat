import java.util.Scanner;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Birthday {
    public static void main(String[] args) throws Exception {
            String syntymapaiva = System.getenv("BIRTHDAY");
            try{
            LocalDate syntymapaivaDate = LocalDate.parse(syntymapaiva);
            LocalDate tanaan = LocalDate.now();
            long erotus = ChronoUnit.DAYS.between(syntymapaivaDate, tanaan);
            if(syntymapaivaDate.isAfter(tanaan)){
                System.out.println("Syntymäpäivä ei voi olla tulevaisuudessa.");
            } 
            if(syntymapaivaDate.getMonthValue() == tanaan.getMonthValue() && syntymapaivaDate.getDayOfMonth() == tanaan.getDayOfMonth()){
                System.out.println("Hyvää syntymäpäivää!");
            }
            if(erotus % 1000 == 0){
                System.out.println("Eletyt päiväsi ovat tasan jaollisia tuhannella! "+ erotus);
            }
            if(syntymapaivaDate.isBefore(tanaan)){ 
                System.out.println("Olet elänyt " + erotus + " päivää.");
            }
            } catch (Exception e){
                System.out.println("Anna syntymäpäivä muodossa vvvv-kk-pp terminaaliin.");
            }

}
}
