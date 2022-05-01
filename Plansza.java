import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class Plansza {
    int wysokość;
    int szerokość;
    Pole[][] plansza;

    public Koordynaty zmoduluj_koordynaty(Koordynaty koordynaty) {
        int x = koordynaty.koordynata_x % wysokość;
        int y = koordynaty.koordynata_y % szerokość;

        if (x < 0) {
            x += wysokość;
        }
        if (y < 0) {
            y += szerokość;
        }
        return new Koordynaty(x, y);
    }

    public Koordynaty koordynaty_względne(Rob rob, int x, int y) {
        return zmoduluj_koordynaty(new Koordynaty(rob.koordynaty.koordynata_x + x, rob.koordynaty.koordynata_y + y));
    }

    public Koordynaty losowe_koordynaty() {
        Random generator = new Random();
        return new Koordynaty(generator.nextInt(wysokość), generator.nextInt(szerokość));
    }

    void umieść_roba_na_planszy(Rob rob) {
        plansza[rob.koordynaty.koordynata_x][rob.koordynaty.koordynata_y].umieść_roba_na_polu(rob);
    }

    void usuń_roba_z_planszy(Rob rob) {
        plansza[rob.koordynaty.koordynata_x][rob.koordynaty.koordynata_y].usuń_roba_z_pola(rob);
    }

    void przemieść_roba(Rob rob, Koordynaty koordynaty_docelowe) {
        usuń_roba_z_planszy(rob);
        rob.przemieść_się(koordynaty_docelowe);
        umieść_roba_na_planszy(rob);
    }

    Plansza(int wysokość, int szerokość, Pole[][] plansza) {
        this.wysokość = wysokość;
        this.szerokość = szerokość;
        this.plansza = plansza;
    }

    public void wypisz_planszę() {
        for (int i = 0; i < this.wysokość; i++) {
            for (int j = 0; j < this.szerokość; j++) {
                if (this.plansza[i][j].jest_rob_na_polu()) {
                    if (this.plansza[i][j].ile_robów_na_polu() >= 10) {
                        System.out.print("D");
                    } else if (this.plansza[i][j].ile_robów_na_polu() > 0) {
                        System.out.print(this.plansza[i][j].ile_robów_na_polu());
                    }
                } else {
                    System.out.print('_');
                }
            }

            System.out.println("");
        }
    }

    public void wypisz_rodzaje_pól_na_planszy() {
        for (int i = 0; i < this.wysokość; i++) {
            for (int j = 0; j < this.szerokość; j++) {
                System.out.print(this.plansza[i][j].podaj_rodzaj_pola());
            }

            System.out.println("");
        }
    }

    public Pole podaj_pole(Koordynaty koordynaty) {
        return plansza[koordynaty.koordynata_x][koordynaty.koordynata_y];
    }

    public void deszczyk() {
        for (int i = 0; i < wysokość; i++) {
            for (int j = 0; j < szerokość; j++) {
                if (plansza[i][j] instanceof Pole_żywieniowe) {
                    ((Pole_żywieniowe) plansza[i][j]).urośnij_jedzenie_o_1_turę();
                }
            }
        }
    }

    public int liczba_pól_z_jedzeniem() {
        int liczba_pól_z_jedzeniem= 0;

        for (int i = 0; i < wysokość; i++) {
            for (int j = 0; j < szerokość; j++) {
                if (plansza[i][j] instanceof Pole_żywieniowe) {
                    if (((Pole_żywieniowe) plansza[i][j]).jest_jedzenie()) {
                        liczba_pól_z_jedzeniem++;
                    }
                }
            }
        }

        return liczba_pól_z_jedzeniem;
    }

}
