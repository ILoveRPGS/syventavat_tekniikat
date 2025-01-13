import java.lang.Math;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        
        Scanner scanner = new Scanner(System.in);
        int kierros = 1;
        int min = 1;
        int max = 100;
        int random_int = (int)(Math.random() * (max - min + 1) + min);
        while(true){
            System.out.println("Anna luku: ");
            int luku = scanner.nextInt();
            if(luku !=random_int){
                System.out.println("Väärin! Yritä uudelleen.");
                kierros++;
            }
            if (kierros == 7){
                System.out.println("Oikea luku oli: " + random_int);
                System.out.println("Hävisit pelin!");
                break;
            }
            if(luku == random_int){
                System.out.println("Oikein! Arvasit oikein " + kierros + ". kierroksella.");
                break;
            }
        }
    }
}
