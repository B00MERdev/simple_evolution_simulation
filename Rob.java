public class Rob {
    double ułamek_energii_rodzica;
    Koordynaty koordynaty;
    int kierunek; // 0 -> góra, 1 -> prawo, 2 -> dół, 3 -> lewo
    Program_roba program_roba;
    int energia;
    int wiek;
    boolean żyje;
    boolean powstał_przez_powielenie;

    public void przemieść_się(Koordynaty koordynaty_docelowe) {
        koordynaty = koordynaty_docelowe;
    }

    public Rob powiel_się() {
        energia -= (int)(energia * ułamek_energii_rodzica);
        return new Rob(koordynaty, (6 - kierunek) % 4, program_roba.daj_zmutowany(),
                (int)(energia * ułamek_energii_rodzica), true, ułamek_energii_rodzica);
    }

    public void zabij_się() {
        this.żyje = false;
    }

    public void wypisz_się() {
        System.out.println("Koordynaty : " + koordynaty.koordynata_x + "/" + koordynaty.koordynata_y);
        System.out.println("Kierunek : " + kierunek);
        System.out.println("Program : " + program_roba.program_roba);
        System.out.println("Energia : " + energia);
        System.out.println("Wiek : " + wiek);
        System.out.println("Powstałem przez powielenie : " + powstał_przez_powielenie);
    }

    Rob(Koordynaty koordynaty, int kierunek, Program_roba program_roba, int energia, boolean powstał_przez_powielenie, double ułamek_energii_rodzica) {
        this.koordynaty = koordynaty;
        this.kierunek = kierunek;
        this.program_roba = program_roba;
        this.energia = energia;
        this.wiek = 0;
        this.żyje = true;
        this.powstał_przez_powielenie = powstał_przez_powielenie;
        this.ułamek_energii_rodzica = ułamek_energii_rodzica;
    }
}
