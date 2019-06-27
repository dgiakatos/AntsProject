import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

/**
 * @author Δημήτριος Παντελεήμων Γιακάτος
 * @version 1.0.0
 * Η κλάση υπολογίζει και εκτυπώνει πόσους σπόρους πρέπει να πάρει το κόκκινο μυρμήγκι από το μαύρο ώστε να γεμίσει
 * πλήρως τον κάδο του. Επίσης ελέγχει αν υπάρχει τέτοιο ζεύγος (κόκκινο – μαύρο) για όλα τα ζευγάρια (με τη σειρά που
 * δίνονται από το αρχείο, δηλαδή 1-2, 3-4, κτλ.) με χρήση του δυναμικού προγραμματισμού. Το συγκεκριμένο πρόβλημα είναι
 * αντίστοιχο με αυτό της ανταλλαγής ρέστων άρα θα χρησιμοποιηθεί ο αλγόριθμος για το πρόβλημα της ανταλλαγής ρέστων.
 */
public class ChangeMaking {

    private TreeMap<Integer, ArrayList<Number>> data;
    private TreeMap<Integer, ArrayList<Integer>> results = new TreeMap<>();
    private ArrayList<Integer> seedPlots = new ArrayList<>();

    /**
     * Η μέθοδος είναι ο constructor της κλάσης, που καλεί κάποιες από τις μεθόδους της κλάσεις ώστε να παράγει το
     * ζητούμενο αποτέλεσμα για την άσκηση.
     * @param data Το Tree Map που έχει αποθηκευμένα τα δεδομένα του αρχείου.
     * @throws Exception Σε περίπτωση που δεν θα βρεθεί το αρχείο ή δεν θα μπορέσει να το ανοίξει τότε εκτυπώνει το κατάλληλο
     *                  exception ώστε να μην κωλύσει το πρόγραμμα.
     */
    public ChangeMaking(TreeMap<Integer, ArrayList<Number>> data) throws Exception {
        this.data = data;
        setData();
        print();
    }

    /**
     * Η μέθοδος υλοποιεί τον αλγόριθμο για το πρόβλημα της ανταλλαγής των ρέστων, δηλαδή βρίσκει τον ελάχιστο αριθμό
     * των σπόρων που μπορεί να γεμίσουν πλήρως τον κουβά του κόκκινου μυρμηγκιού. Στη συνέχεια αποθηκεύει σε ένα πίνακα
     * τα βάρη των σπόρων που απαιτούνται για να γεμίσει πλήρως ο κουβάς. Το κύριο χαρακτηριστικό αυτού του πίνακα είναι
     * ότι αν αθροίσουμε τα δεδομένα του θα προκύψει η χωρητικότητα του κουβά. Έτσι, θα μπορέσουμε να βρούμε τα πλήθος
     * από κάθε είδος σπόρου, όπου και γίνεται στη μέθοδο addDataToResults.
     * @param D Ένα πίνακα που περιλαμβάνει τα βάρη από κάθε είδος σπόρου του μαύρου μυρμηγκιού (άρτιο id).
     * @param n Τη χωρητικότητα του κάδου του κόκκινου μυρμηγκιού (περιττό id).
     */
    private void changeMaking(int[] D, int n) {
        Arrays.sort(D);
        int[] F = new int[n+1];
        int[] seeds = new int[n+1];
        for (int i=1; i<n+1; i++) {
            seeds[i] = Integer.MAX_VALUE;
        }
        F[0] = 0;
        for (int i=1; i<n+1; i++) {
            int temp = Integer.MAX_VALUE - 1;
            int j = 0;
            while (j<D.length && i>=D[j]) {
                if (F[i - D[j]] < temp) {
                    temp = F[i - D[j]];
                    seeds[i] = j;
                }
                j++;
            }
            F[i] = temp + 1;
        }
        if (F[n] != Integer.MAX_VALUE) {
            int findSeed = seeds.length - 1;
            while (findSeed > 0) {
                int i = seeds[findSeed];
                seedPlots.add(D[i]);
                findSeed = findSeed - D[i];
            }
        }
    }

    /**
     * Η μέθοδος δέχεται ως όρισμα το ζεύγος των μυρμηγκιών και υπολογίζει το πλήθος των σπόρων από κάθε βάρος του
     * μαύρου μυρμηγκιού, που μπορεί να πάρει ο κουβάς του κόκκινου. Στη συνέχεια, εφόσον υπάρχει κάποιο ζεύγος που
     * μπορεί να γεμίσει πλήρως των κουβά, τα αποθηκεύει (ζεύγος και το πλήθος των σπόρων) στη δομή Tree Map (results).
     * Η μέθοδος χρησιμοποιεί τα αποτελέσματα της μεθόδου changeMaking από όπου παίρνει έναν πίνακα με τους σπόρους που
     * γεμίζουν πλήρως το κουβά του κόκκινου.
     * @param key1Result Το κόκκινο μυρμήγκι (περιττό id).
     * @param key2Result Το μαύρο μυρμήγκι (άρτιο id).
     */
    private void addDataToResults(Integer key1Result, Integer key2Result) {
        ArrayList<Integer> seedResults = new ArrayList<>();
        boolean hasValue = false;
        seedResults.add(key2Result);
        for (int i=2; i<data.get(key2Result).size(); i++) {
            int count = 0;
            for (int j=0; j<seedPlots.size(); j++) {
                if (seedPlots.get(j).equals(data.get(key2Result).get(i).intValue())) {
                    count++;
                    hasValue = true;
                }
            }
            seedResults.add(count);
        }
        if (hasValue) {
            results.put(key1Result, new ArrayList<>(seedResults));
        }
    }

    /**
     * Η μέθοδος παίρνει τα ζεύγη των μυρμηγκιών (1-2, 3-4, 5-6, κτλ.) από τη δομής Tree Map (data) και καλεί τη μέθοδο
     * changeMaking όπου περνάει με μορφή πίνακα τα βάρη των σπόρων που μπορεί να κουβαλήσει το μαύρο (άρτιο) μυρμήγκι
     * και με τη μορφή μεταβλητής τύπου Integer παίρνει τη χωρητικότητα του κάδου του κόκκινου (περιττού) μυρμηγκιού.
     * Στη συνέχεια καλεί τη συνάρτηση addDataToResults, η οποία περνάει ως ορίσματα τα ζεύγη των μυρμηγκιών.
     */
    private void setData() {
        int n;
        for (Integer keyData : data.keySet()) {
            if ((keyData % 2) != 0) {
                n = data.get(keyData).get(2).intValue();
                seedPlots.clear();
                changeMaking(new int[]{data.get(keyData+1).get(2).intValue(),
                                        data.get(keyData+1).get(3).intValue(),
                                        data.get(keyData+1).get(4).intValue(),
                                        data.get(keyData+1).get(5).intValue(),
                                        data.get(keyData+1).get(6).intValue()}, n);
                addDataToResults(keyData, keyData+1);
            }
        }
    }

    /**
     * Η μέθοδος εκτυπώνει στο αρχείο τα δεδομένα της δομής Tree Map (results), δηλαδή τα ζεύγη των μυρμηγκιών, αν
     * υπάρχουν, και το πλήθος των σπόρων από κάθε βάρος.
     * @throws Exception Σε περίπτωση που δεν θα βρεθεί το αρχείο ή δεν θα μπορέσει να το ανοίξει τότε εκτυπώνει το κατάλληλο
     *                  exception ώστε να μην κωλύσει το πρόγραμμα.
     */
    private void print() throws Exception {
        FileManager fileManager = new FileManager();
        String str;
        for (Integer key : results.keySet()) {
            str = String.valueOf(key);
            for (int i=0; i<results.get(key).size(); i++) {
                str = str + " " + results.get(key).get(i);
            }
            fileManager.output("C.txt", str+"\r\n");
        }
    }
}
