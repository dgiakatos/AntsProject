import java.util.*;

/**
 * @author Δημήτριος Παντελεήμων Γιακάτος
 * @version 1.0.0
 * Η κλάση υπολογίζει και αποθηκεύει σε ένα αρχείο τα ζεύγη των μυρμηγκιών που προκύπτουν μετά την εφαρμογή του
 * αλγορίθμου του ευσταθούς ταιριάσματος (Gale Shapley). Αρχικά η κλάση δημιουργεί των πίνακα προτιμήσεων όλων των
 * μυρμηγκιών και στη συνέχεια εφαρμόζει τον αλγόριθμο για να προκύψουν τα ζεύγη όπου και στο τέλος τα αποθηκεύει σε
 * ένα αρχείο.
 */
public class GaleShapley {

    private TreeMap<Integer, ArrayList<Number>> data;
    ArrayList<Integer> freeRed = new ArrayList<>();
    private HashMap<Integer, ArrayList<Ant>> preferences = new HashMap<>();
    private TreeMap<Integer, Integer> finalCouple = new TreeMap<>();

    /**
     * Η μέθοδος είναι ο constructor της κλάσης, που καλεί κάποιες από τις μεθόδους της κλάσεις ώστε να παράγει το
     * ζητούμενο αποτέλεσμα για την άσκηση.
     * @param data Το Tree Map που έχει αποθηκευμένα τα δεδομένα του αρχείου.
     * @throws Exception Σε περίπτωση που δεν θα βρεθεί το αρχείο ή δεν θα μπορέσει να το ανοίξει τότε εκτυπώνει το κατάλληλο
     *                  exception ώστε να μην κωλύσει το πρόγραμμα.
     */
    public GaleShapley(TreeMap<Integer, ArrayList<Number>> data) throws Exception {
        this.data = data;
        setData();
        galeShaple();
        print();
    }

    /**
     * Η μέθοδος δημιουργεί τον πίνακα προτιμήσεων όλων των μυρμηγκιών και τα ταξινομεί με βάση την απόσταση. Δηλαδή τα
     * μαύρα μυρμήγκια θα έχουν πρώτα στη λίστα προτιμήσεων τους τα κόκκινα μυρμήγκια που έχει την μικρότερη απόσταση με
     * αυτά και στο τέλος θα έχουν αυτά που έχουν την μεγαλύτερη απόσταση. Η αντίστοιχη διαδικασία γίνεται και για τα
     * κόκκινα μυρμήγκια, δηλαδή στη λίστα προτιμήσεων τους θα έχουν πρώτα τα μαύρα με την μικρότερη απόσταση από αυτά
     * και στο τέλος αυτό με τη μεγαλύτερη. Επίσης δημιουργεί και ένα πίνακα που αποθηκεύει τα μυρμήγκια που είναι ελεύθερα.
     * Στη συγκεκριμένη περίπτωση θα αποθηκευτούν όλα τα κόκκινα μυρμήγκια σε αυτή καθώς είναι όλα ελεύθερα, δηλαδή δεν έχουν κανένα ζεύγος.
     */
    private void setData() {
        ArrayList<Ant> redCouple = new ArrayList<>();
        ArrayList<Ant> blackCouple = new ArrayList<>();
        for (Integer key1 : data.keySet()) {
            for (Integer key2 : data.keySet()) {
                if ((key1%2 != 0) && (key2%2 == 0)) {
                    blackCouple.add(new Ant().setDataForGS(key2,
                            data.get(key1).get(0).doubleValue(), data.get(key1).get(1).doubleValue(),
                            data.get(key2).get(0).doubleValue(), data.get(key2).get(1).doubleValue()
                    ));
                } else if ((key1%2 == 0) && (key2%2 != 0)) {
                    redCouple.add(new Ant().setDataForGS(key2,
                            data.get(key1).get(0).doubleValue(), data.get(key1).get(1).doubleValue(),
                            data.get(key2).get(0).doubleValue(), data.get(key2).get(1).doubleValue()
                    ));
                }
            }
            if (key1%2 != 0) {
                Collections.sort(blackCouple);
                preferences.put(key1, new ArrayList<>(blackCouple));
                blackCouple.clear();
                freeRed.add(key1);
            } else {
                Collections.sort(redCouple);
                preferences.put(key1, new ArrayList<>(redCouple));
                redCouple.clear();
            }
        }
    }

    /**
     * Η μέθοδος υλοποιεί τον αλγόριθμο Gale-Shapley, δηλαδή τον αλγόριθμο ευσταθούς ταιριάσματος. Με λίγα λόγια με βάση
     * τον πίνακα προτιμήσεων σχηματίζει τα ζευγάρια μεταξύ των μυρμηγκιών (κόκκινο-μαύρο). Τα μυρμήγκια που προτείνουν
     * είναι τα κόκκινα και τα μυρμήγκια που μπορούν να αφήσουν κάποια ζεύγη για να δημιουργήσουν κάποια άλλα είναι τα
     * μαύρα. Για αυτό το λόγο τα αποτελέσματα των ζευγαριών προκύπτουν σε ένα πίνακα που έχει ως κλειδιά τα id των
     * μαύρων μυρμηγκιών και ως τιμές τα id των κόκκινων μυρμηγκιών. Τέλος αφού προκύψουν τα ζεύγη καλεί τη μέθοδο
     * setFinalCouple ώστε να αντιστρέψει το πίνακα με τα τελικά ζεύγη.
     */
    private void galeShaple() {
        boolean breakUp;
        HashMap<Integer, Integer> pairsBlackRed = new HashMap<>();
        while (!freeRed.isEmpty()) {
            int newRed = freeRed.remove(0);
            ArrayList<Ant> redPreferences = preferences.get(newRed);
            breakUp = false;
            for (Ant black : redPreferences) {
                if (pairsBlackRed.get(black.antName) == null) {
                    pairsBlackRed.put(black.antName, newRed);
                    break;
                } else {
                    int oldRed = pairsBlackRed.get(black.antName);
                    ArrayList<Ant> blackPreferences = preferences.get(black.antName);
                    for (Ant red : blackPreferences) {
                        if (red.antName==newRed) {
                            pairsBlackRed.put(black.antName, newRed);
                            freeRed.add(oldRed);
                            breakUp = true;
                            break;
                        }
                        if (red.antName==oldRed) {
                            break;
                        }
                    }
                    if (breakUp) {
                        break;
                    }
                }
            }
        }
        setFinalCouple(pairsBlackRed);
    }

    /**
     * Η μέθοδος δέχεται ως όρισμα τον πίνακα με τα τελικά ζευγάρια των μυρμηγκιών και αντιστρέφει τα αποτελέσματα,
     * δηλαδή δημιουργεί μία καινούργια δομή με κλειδί το id του κόκκινου μυρμηγκιού και με τιμή το id του μαύρου
     * μυρμηγκιού, ώστε να προκύψει το αποτέλεσμα που θέλουμε.
     * @param pairsBlackRed Ο πίνακας με τα τελικά ζευγάρια με κλειδί το id των μαύρων μυρμηγκιών και με τιμή το id των κόκκινων.
     */
    private void setFinalCouple(HashMap<Integer, Integer> pairsBlackRed) {
        for (Integer antName : pairsBlackRed.keySet()) {
            finalCouple.put(pairsBlackRed.get(antName), antName);
        }
    }

    /**
     * Η μέθοδος εκτυπώνει στο αρχείο τα δεδομένα της δομής Tree Map (finalCouple), δηλαδή τα ζεύγη των μυρμηγκιών.
     * @throws Exception Σε περίπτωση που δεν θα βρεθεί το αρχείο ή δεν θα μπορέσει να το ανοίξει τότε εκτυπώνει το κατάλληλο
     *                  exception ώστε να μην κωλύσει το πρόγραμμα.
     */
    private void print() throws Exception {
        FileManager fileManager = new FileManager();
        for (Integer antName : finalCouple.keySet()) {
            fileManager.output("B.txt", antName + " " + finalCouple.get(antName) +"\r\n");
        }
    }
}
