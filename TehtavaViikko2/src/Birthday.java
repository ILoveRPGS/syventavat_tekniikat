import java.util.Scanner;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Birthday {
    public static void main(String[] args) throws Exception {
        while(true){
            Scanner scanner = new Scanner(System.in);
            System.out.println("Anna syntymäpäiväsi (vvvv.kk.pp): ");
            String syntymapaiva = scanner.nextLine();
            String[] syntymapaivaArray = syntymapaiva.split("\\.");
            int syntymapaivaPaiva = Integer.parseInt(syntymapaivaArray[2]);
            int syntymapaivaKuukausi = Integer.parseInt(syntymapaivaArray[1]);
            int syntymapaivaVuosi = Integer.parseInt(syntymapaivaArray[0]);
            LocalDate syntymapaivaDate = LocalDate.of(syntymapaivaVuosi, syntymapaivaKuukausi, syntymapaivaPaiva);
            LocalDate tanaan = LocalDate.now();
            long erotus = ChronoUnit.DAYS.between(syntymapaivaDate, tanaan);
        if(syntymapaivaDate.isAfter(tanaan)){
            System.out.println("Syntymäpäivä ei voi olla tulevaisuudessa.");
        } 
        if(syntymapaivaDate.isEqual(tanaan)){
            System.out.println("Hyvää syntymäpäivää!");
        }
        if(erotus % 1000 == 0){
            System.out.println("Eletyt päiväsi ovat tasan jaollisia tuhannella!"+ erotus);
        }
        if(syntymapaivaDate.isBefore(tanaan)){ 
            System.out.println("Olet elänyt " + erotus + " päivää.");
        }
    }
}
}
