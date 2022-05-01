public class Pole_żywieniowe extends Pole{
    private boolean jedzenie;
    private int za_ile_tur_urośnie;
    int ile_rośnie_jedzenie;

    public char podaj_rodzaj_pola() {
        return 'ż';
    }

    public boolean jest_jedzenie() {
        return jedzenie;
    }

    public void zjedz_jedzenie() {
        jedzenie = false;
        za_ile_tur_urośnie = ile_rośnie_jedzenie;
    }

    public void urośnij_jedzenie_o_1_turę() {
        if (za_ile_tur_urośnie > 0) {
            if (za_ile_tur_urośnie == 1) {
                jedzenie = true;
            }
            za_ile_tur_urośnie--;
        }
    }

    Pole_żywieniowe(int ile_rośnie_jedzenie) {
        this.jedzenie = true;
        this.za_ile_tur_urośnie = 0;
        this.ile_rośnie_jedzenie = ile_rośnie_jedzenie;
    }
}
