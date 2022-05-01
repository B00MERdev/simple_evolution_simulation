import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Symulacja {
    static int koszt_tury;
    static int ile_daje_jedzenie;
    static double pr_powielenia;
    static int limit_powielania;
    static int pocz_ile_robów;
    static Program_roba pocz_progr;
    static int pocz_energia;
    static int ile_rośnie_jedzenie;
    static String spis_instr;
    static double ułamek_energii_rodzica;
    static double pr_usunięcia_instr;
    static double pr_dodania_instr;
    static double pr_zmiany_instr;
    static int co_ile_wypisz;
    static Plansza p;
    static int numer_tury;
    static int ile_tur;
    static String str_pocz_pr_roba;

    private static void wczytaj_parametry(String nazwa_pliku) throws IOException, Exception{
        Scanner sc = new Scanner(new File(nazwa_pliku));
        int liczba_parametrów = 0;
        String nazwa_ostatniego_parametru = "";
        String nazwa_bieżącego_parametu = "";
        boolean różne_parametry = true;
        while (sc.hasNextLine()) {
            liczba_parametrów++;
            String nowy_wiersz = sc.nextLine();
            Scanner skaner_wiersza = new Scanner(nowy_wiersz);
            nazwa_ostatniego_parametru = nazwa_bieżącego_parametu;
            switch (nazwa_bieżącego_parametu = skaner_wiersza.next()) {
                case "koszt_tury" -> koszt_tury = skaner_wiersza.nextInt();
                case "ile_daje_jedzenie" -> ile_daje_jedzenie = skaner_wiersza.nextInt();
                case "pocz_ile_robów" -> pocz_ile_robów = skaner_wiersza.nextInt();
                case "pocz_energia" -> pocz_energia = skaner_wiersza.nextInt();
                case "ile_rośnie_jedzenie" -> ile_rośnie_jedzenie = skaner_wiersza.nextInt();
                case "co_ile_wypisz" -> co_ile_wypisz = skaner_wiersza.nextInt();
                case "ile_tur" -> ile_tur = skaner_wiersza.nextInt();
                case "limit_powielania" -> limit_powielania = skaner_wiersza.nextInt();
                case "pr_powielenia" -> pr_powielenia = skaner_wiersza.nextDouble();
                case "ułamek_energii_rodzica" -> ułamek_energii_rodzica = skaner_wiersza.nextDouble();
                case "pr_usunięcia_instr" -> pr_usunięcia_instr = skaner_wiersza.nextDouble();
                case "pr_dodania_instr" -> pr_dodania_instr = skaner_wiersza.nextDouble();
                case "pr_zmiany_instr" -> pr_zmiany_instr = skaner_wiersza.nextDouble();
                case "pocz_progr" -> str_pocz_pr_roba = skaner_wiersza.next();
                case "spis_instr" -> spis_instr = skaner_wiersza.next();
                default -> throw new Exception("Błędny parametr");
            }

            if (nazwa_bieżącego_parametu == nazwa_ostatniego_parametru) {
                różne_parametry = false;
            }
        }

        if (liczba_parametrów != 15) {
            throw new Exception("Zła liczba parametrów!");
        }

        if (!różne_parametry) {
            throw new Exception("Powielenie parametrów!");
        }

        pocz_progr = new Program_roba(str_pocz_pr_roba, pr_dodania_instr, pr_usunięcia_instr, pr_zmiany_instr, spis_instr);
    }

    private static Plansza wczytaj_planszę(int ile_rośnie_jedzenie, String nazwa_pliku) throws IOException, Exception{
        Scanner sc = new Scanner(new File(nazwa_pliku));
        int wysokość = 0;
        int szerokość = 0;
        String nowy_wiersz;
        if (sc.hasNextLine()) {
            nowy_wiersz = sc.nextLine();
            szerokość = nowy_wiersz.length();
            wysokość++;
        }

        while (sc.hasNextLine()) {
            nowy_wiersz = sc.nextLine();
            assert szerokość == nowy_wiersz.length();
            wysokość++;
        }

        if (wysokość == 0 || szerokość == 0) {
            throw new Exception("Złe wymiary planszy");
        }

        Pole[][] plansza = new Pole[wysokość][szerokość];

        int numer_linii = 0;
        sc = new Scanner(new File(nazwa_pliku));
        while (sc.hasNextLine()) {
            nowy_wiersz = sc.nextLine();

            for (int i = 0; i < szerokość; i++) {
                char znak = nowy_wiersz.charAt(i);
                if (znak == ' ') {
                    plansza[numer_linii][i] = new Pole_standardowe();
                } else if (znak == '+') {
                    plansza[numer_linii][i] = new Pole_żywieniowe(ile_rośnie_jedzenie);
                }
            }

            numer_linii++;
        }

        Plansza p = new Plansza(wysokość, szerokość, plansza);

        return p;
    }


    private static boolean czy_jest_jedzonko(Plansza p, Rob bob, int x, int y) {
        if (p.podaj_pole(p.koordynaty_względne(bob, x, y)) instanceof Pole_żywieniowe) {
            return ((Pole_żywieniowe) p.podaj_pole(p.koordynaty_względne(bob, x, y))).jest_jedzenie();
        } else {
            return false;
        }
    }

    private static void wykonaj_turę_roba(Rob bob, Plansza p, List <Rob> żywe_roby) {
        int i = 0;
        while (bob.żyje && i < bob.program_roba.liczba_instrukcji()) {
            switch (bob.program_roba.ita_instrukcja(i)) {
                case 'i' -> {
                    switch (bob.kierunek) {
                        case 0 -> p.przemieść_roba(bob, p.koordynaty_względne(bob, -1, 0));
                        case 1 -> p.przemieść_roba(bob, p.koordynaty_względne(bob, 0, 1));
                        case 2 -> p.przemieść_roba(bob, p.koordynaty_względne(bob, 1, 0));
                        case 3 -> p.przemieść_roba(bob, p.koordynaty_względne(bob, 0, -1));
                        default -> System.out.println("zjebany kierunek " + bob.kierunek);
                    }
                    if (czy_jest_jedzonko(p, bob, 0, 0)) {
                        ((Pole_żywieniowe) p.podaj_pole(bob.koordynaty)).zjedz_jedzenie();
                        bob.energia += ile_daje_jedzenie;
                    }
                }
                case 'l' -> {
                    if (bob.kierunek == 0) {
                        bob.kierunek = 3;
                    } else {
                        bob.kierunek -= 1;
                    }
                }
                case 'p' -> bob.kierunek = (bob.kierunek + 1) % 4;
                case 'w' -> {
                    Random generator = new Random();
                    boolean jedzenie_w_tym_kierunku = false;
                    int losowy_kierunek = generator.nextInt(4);
                    int j = 0;
                    while (!jedzenie_w_tym_kierunku && j < 4) {
                        losowy_kierunek = (losowy_kierunek + 1) % 4;

                        switch (losowy_kierunek) {
                            case 0 ->jedzenie_w_tym_kierunku = czy_jest_jedzonko(p, bob, -1, 0);
                            case 1 ->jedzenie_w_tym_kierunku = czy_jest_jedzonko(p, bob, 0, 1);
                            case 2 ->jedzenie_w_tym_kierunku = czy_jest_jedzonko(p, bob, 1, 0);
                            case 3 ->jedzenie_w_tym_kierunku = czy_jest_jedzonko(p, bob, 0, -1);
                            default -> System.out.println("zjebany losowy kierunek" + losowy_kierunek);
                        }

                        if (jedzenie_w_tym_kierunku) {
                            bob.kierunek = losowy_kierunek;
                        }

                        j++;
                    }
                }
                case 'j' -> {
                    Random generator = new Random();
                    boolean jedzenie_w_tym_kierunku = false;
                    int losowy_kierunek = generator.nextInt(8); // dodatkowe kierunki: 4 -> góra + lewo, 5 -> góra + prawo, 6 -> dół + prawo, 7 -> dół + lewo
                    int j = 0;
                    while (!jedzenie_w_tym_kierunku && j < 8) {
                        losowy_kierunek = (losowy_kierunek + 1) % 8;

                        switch (losowy_kierunek) {
                            case 0 ->jedzenie_w_tym_kierunku = czy_jest_jedzonko(p, bob, -1, 0);
                            case 1 ->jedzenie_w_tym_kierunku = czy_jest_jedzonko(p, bob, 0, 1);
                            case 2 ->jedzenie_w_tym_kierunku = czy_jest_jedzonko(p, bob, 1, 0);
                            case 3 ->jedzenie_w_tym_kierunku = czy_jest_jedzonko(p, bob, 0, -1);
                            case 4 ->jedzenie_w_tym_kierunku = czy_jest_jedzonko(p, bob, -1, -1);
                            case 5 ->jedzenie_w_tym_kierunku = czy_jest_jedzonko(p, bob, -1, 1);
                            case 6 ->jedzenie_w_tym_kierunku = czy_jest_jedzonko(p, bob, 1, 1);
                            case 7 ->jedzenie_w_tym_kierunku = czy_jest_jedzonko(p, bob, 1, -1);
                            default -> System.out.println("zjebany losowy kierunek" + losowy_kierunek);
                        }

                        if (jedzenie_w_tym_kierunku) {
                            switch (losowy_kierunek) {
                                case 0 -> p.przemieść_roba(bob, p.koordynaty_względne(bob, -1, 0));
                                case 1 -> p.przemieść_roba(bob, p.koordynaty_względne(bob, 0, 1));
                                case 2 -> p.przemieść_roba(bob, p.koordynaty_względne(bob, 1, 0));
                                case 3 -> p.przemieść_roba(bob, p.koordynaty_względne(bob, 0, -1));
                                case 4 -> p.przemieść_roba(bob, p.koordynaty_względne(bob, -1, -1));
                                case 5 -> p.przemieść_roba(bob, p.koordynaty_względne(bob, -1, 1));
                                case 6 -> p.przemieść_roba(bob, p.koordynaty_względne(bob, 1, 1));
                                case 7 -> p.przemieść_roba(bob, p.koordynaty_względne(bob, 1, -1));
                                default -> System.out.println("zjebany losowy kierunek" + losowy_kierunek);
                            }
                            bob.energia += ile_daje_jedzenie;
                            ((Pole_żywieniowe) p.podaj_pole(bob.koordynaty)).zjedz_jedzenie();
                        }
                        j++;
                    }
                }
                default -> System.out.println("zjebana instrukcja");
            }
            bob.energia--;

            if (bob.żyje && bob.energia <= 0) {
                bob.zabij_się();
                p.usuń_roba_z_planszy(bob);
                żywe_roby.remove(bob);
            }

            i++;
        }
        bob.energia -= koszt_tury;

        if (bob.żyje && bob.energia <= 0) {
            bob.zabij_się();
            p.usuń_roba_z_planszy(bob);
            żywe_roby.remove(bob);
        }

        if (bob.energia >= limit_powielania && ThreadLocalRandom.current().nextDouble(0, 1) < pr_powielenia) {
            żywe_roby.add(bob.powiel_się());
        }
    }

    private static Rob losowy_rob_początkowy(Plansza p) {
        Random generator = new Random();
        int kierunek = generator.nextInt(4);
        return new Rob(p.losowe_koordynaty(), kierunek, pocz_progr, pocz_energia, false, ułamek_energii_rodzica);
    }

    private static void wypisz_szczegółowy_stan_symulacji(Plansza p, List <Rob> lista_żwywch_robów, int numer_tury) {
        for (int i = 0; i < lista_żwywch_robów.size(); i++) {
            System.out.println("Numer tury : " + numer_tury);
            System.out.println("Numer roba : " + i);
            lista_żwywch_robów.get(i).wypisz_się();
        }
    }

    private static int minimalna_energia(List <Rob> lista_żwywch_robów) {
        if (lista_żwywch_robów.size() > 0) {
            int minimalna_energia = Integer.MAX_VALUE;
            for (Rob rob : lista_żwywch_robów) {
                if (rob.energia < minimalna_energia) {
                    minimalna_energia = rob.energia;
                }
            }

            return minimalna_energia;
        } else {
            return 0;
        }
    }

    private static int maksymalna_energia(List <Rob> lista_żwywch_robów) {
        if (lista_żwywch_robów.size() > 0) {
            int maksymalna_energia = Integer.MIN_VALUE;
            for (Rob rob : lista_żwywch_robów) {
                if (rob.energia > maksymalna_energia) {
                    maksymalna_energia = rob.energia;
                }
            }

            return maksymalna_energia;
        } else {
            return 0;
        }
    }

    private static double średnia_energia(List <Rob> lista_żwywch_robów) {
        double średnia_energia = 0;
        for (Rob rob : lista_żwywch_robów) {
            średnia_energia += rob.energia;
        }
        średnia_energia /= lista_żwywch_robów.size();
        return średnia_energia;
    }

    private static int minimalny_wiek(List <Rob> lista_żwywch_robów) {
        if (lista_żwywch_robów.size() > 0) {
            int minimalny_wiek = Integer.MAX_VALUE;
            for (Rob rob : lista_żwywch_robów) {
                if (rob.wiek < minimalny_wiek) {
                    minimalny_wiek = rob.wiek;
                }
            }

            return minimalny_wiek;
        } else {
            return 0;
        }
    }

    private static int maksymalny_wiek(List <Rob> lista_żwywch_robów) {
        if (lista_żwywch_robów.size() > 0) {
            int maksymalny_wiek = Integer.MIN_VALUE;
            for (Rob rob : lista_żwywch_robów) {
                if (rob.wiek > maksymalny_wiek) {
                    maksymalny_wiek = rob.wiek;
                }
            }

            return maksymalny_wiek;
        } else {
            return 0;
        }
    }

    private static double średni_wiek(List <Rob> lista_żwywch_robów) {
        double średni_wiek = 0;
        for (Rob rob : lista_żwywch_robów) {
            średni_wiek += rob.wiek;
        }
        średni_wiek /= lista_żwywch_robów.size();
        return średni_wiek;
    }

    private static int minimalna_długość_programu(List <Rob> lista_żwywch_robów) {
        if (lista_żwywch_robów.size() > 0) {
            int minimalna_długość_programu = Integer.MAX_VALUE;
            for (Rob rob : lista_żwywch_robów) {
                if (rob.program_roba.liczba_instrukcji() < minimalna_długość_programu) {
                    minimalna_długość_programu = rob.program_roba.liczba_instrukcji();
                }
            }

            return minimalna_długość_programu;
        } else {
            return 0;
        }
    }

    private static int maksymalna_długość_programu(List <Rob> lista_żwywch_robów) {
        if (lista_żwywch_robów.size() > 0) {
            int maksymalna_długość_programu = Integer.MIN_VALUE;
            for (Rob rob : lista_żwywch_robów) {
                if (rob.program_roba.liczba_instrukcji() > maksymalna_długość_programu) {
                    maksymalna_długość_programu = rob.program_roba.liczba_instrukcji();
                }
            }

            return maksymalna_długość_programu;
        } else {
            return 0;
        }
    }

    private static double średnia_długość_programu(List <Rob> lista_żwywch_robów) {
        double średnia_długość_programu = 0;
        for (Rob rob : lista_żwywch_robów) {
            średnia_długość_programu += rob.program_roba.liczba_instrukcji();
        }
        średnia_długość_programu /= lista_żwywch_robów.size();
        return średnia_długość_programu;
    }

    private static void wypisz_ogólny_stan_symulacji(Plansza p, List <Rob> lista_żwywch_robów, int numer_tury) {
        System.out.println(numer_tury + ", rob: " + lista_żwywch_robów.size() + ", żyw: " + p.liczba_pól_z_jedzeniem()
                + ", prg: " + minimalna_długość_programu(lista_żwywch_robów) + "/" + średnia_długość_programu(lista_żwywch_robów)
        + "/" + maksymalna_długość_programu(lista_żwywch_robów) + ", energ: " + minimalna_energia(lista_żwywch_robów) +
                "/" + średnia_energia(lista_żwywch_robów) + "/" + maksymalna_energia(lista_żwywch_robów) + ", wiek: " +
                minimalny_wiek(lista_żwywch_robów) + "/" + średni_wiek(lista_żwywch_robów) + "/" +
                maksymalny_wiek(lista_żwywch_robów));
    }

    private static void przeprowadź_turę(List <Rob> lista_żywych_robów, Plansza p, boolean czy_wypisać_szczegółowo) throws IOException{
        int i =0;
        while (i < lista_żywych_robów.size()) {
            int liczba_robów = lista_żywych_robów.size();
            Rob aktualny_rob = lista_żywych_robów.get(i);
            if (aktualny_rob.żyje && !(aktualny_rob.powstał_przez_powielenie && aktualny_rob.wiek == 0)) {
                wykonaj_turę_roba(lista_żywych_robów.get(i), p, lista_żywych_robów);
            }

            if (liczba_robów <= lista_żywych_robów.size()) {
                aktualny_rob.wiek++;
                i++;
            }
        }

        p.deszczyk();


        if (czy_wypisać_szczegółowo) {
            wypisz_szczegółowy_stan_symulacji(p, lista_żywych_robów, numer_tury);
        }

        wypisz_ogólny_stan_symulacji(p, lista_żywych_robów, numer_tury);

        p.wypisz_planszę();
    }

    public static void main(String[] args) throws IOException, Exception {

        if (args.length != 2) {
            throw  new Exception("Zła liczba argumentów");
        } else {
            String nazwa_pliku_z_planszą = args[0];
            String nazwa_pliku_z_parametrami = args[1];


            wczytaj_parametry(nazwa_pliku_z_parametrami);

            p = wczytaj_planszę(ile_rośnie_jedzenie, nazwa_pliku_z_planszą);

            List<Rob> lista_żywych_robów = new ArrayList<Rob>();

            for (int i = 0; i < pocz_ile_robów; i++) {
                lista_żywych_robów.add(losowy_rob_początkowy(p));
            }

            wypisz_szczegółowy_stan_symulacji(p, lista_żywych_robów, numer_tury);
            while (numer_tury < ile_tur) {
                przeprowadź_turę(lista_żywych_robów, p, numer_tury % co_ile_wypisz == 0);
                numer_tury++;
            }

            wypisz_szczegółowy_stan_symulacji(p, lista_żywych_robów, numer_tury);
        }
    }
}
