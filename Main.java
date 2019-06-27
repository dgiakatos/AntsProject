import java.util.ArrayList;
import java.util.TreeMap;

/**
 * @author Δημήτριος Παντελεήμων Γιακάτος
 * @version 1.0.0
 * Η κλάση Main είναι η πρώτη που θα εκτελεστεί από τον υπολογιστή.
 */
public class Main {

    /**
     * Η μέθοδος δημιουργεί τα πρώτα αντικείμενα ώστε να υλοποιηθεί το παιχνίδι.
     *
     * @param args Είναι απαραίτητη για την εκτέλεση του προγράμματος από τον υπολογιστή. Ουσιαστικά δέχεται το όνομα
     *            του αρχείου, με το οποίο θα γίνει η εισαγωγή των δεδομένων στο πρόγραμμα.
     * @throws Exception Σε περίπτωση που δεν θα βρεθεί αρχείο ή δεν θα μπορέσει να ανοίξει τότε εκτυπώνει το κατάλληλο
     *                  exception ώστε να μην κωλύσει το πρόγραμμα.
     */
    public static void main(String[] args) throws Exception {
        FileManager fileManager = new FileManager();
        fileManager.delete("A.txt");
        fileManager.delete("B.txt");
        fileManager.delete("C.txt");
        TreeMap<Integer, ArrayList<Number>> data = fileManager.input(args[0]);
        new KruskalUnionFind(data);
        new GaleShapley(data);
        new ChangeMaking(data);
    }
}
