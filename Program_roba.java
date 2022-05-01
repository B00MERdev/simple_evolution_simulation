import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Program_roba {
    String program_roba;
    double pr_dodania_instr;
    double pr_usunięcia_instr;
    double pr_zmiany_instr;
    String spis_instr;

    public Program_roba daj_zmutowany() {
        String nowy_program_roba = program_roba;
        Random generator = new Random();
        char nowa_instrukcja;

        if (program_roba.length() > 0 && ThreadLocalRandom.current().nextDouble(0, 1) < pr_usunięcia_instr) {
            nowy_program_roba = program_roba.substring(0, program_roba.length() - 1);
        }

        if (ThreadLocalRandom.current().nextDouble(0, 1) < pr_dodania_instr) {
            nowa_instrukcja = spis_instr.charAt(generator.nextInt(spis_instr.length()));
            nowy_program_roba = nowy_program_roba + nowa_instrukcja;
        }

        if (nowy_program_roba.length() > 0 && ThreadLocalRandom.current().nextDouble(0, 1) < pr_zmiany_instr) {
            nowa_instrukcja = spis_instr.charAt(generator.nextInt(spis_instr.length()));
            char[] nowy_program_roba_tablica = nowy_program_roba.toCharArray();
            nowy_program_roba_tablica[generator.nextInt(nowy_program_roba.length())] = nowa_instrukcja;
            nowy_program_roba = new String(nowy_program_roba_tablica);
        }

        return new Program_roba(nowy_program_roba, pr_dodania_instr, pr_usunięcia_instr, pr_zmiany_instr, spis_instr);
    }

    public int liczba_instrukcji() {
        return program_roba.length();
    }

    public char ita_instrukcja(int i) {
        return program_roba.charAt(i);
    }

    Program_roba(String program_roba, double pr_dodania_instr, double pr_usunięcia_instr, double pr_zmiany_instr,
                 String spis_instr) {
        this.program_roba = program_roba;
        this.pr_dodania_instr = pr_dodania_instr;
        this.pr_usunięcia_instr = pr_usunięcia_instr;
        this.pr_zmiany_instr = pr_zmiany_instr;
        this.spis_instr = spis_instr;
    }
}
