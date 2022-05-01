import java.util.ArrayList;
import java.util.List;

public abstract class Pole {
    List <Rob> pole = new ArrayList <Rob>();

    public void umieść_roba_na_polu(Rob rob) {
        pole.add(rob);
    }
    
    public void usuń_roba_z_pola(Rob rob) {
        pole.remove(rob);
    }

    public abstract char podaj_rodzaj_pola();

    public boolean jest_rob_na_polu() {
        return !pole.isEmpty();
    }

    public int ile_robów_na_polu() {
        return pole.size();
    }
}
