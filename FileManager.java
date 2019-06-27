import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * @author Δημήτριος Παντελεήμων Γιακάτος
 * @version 1.0.0
 * Η κλάση χρησιμοποιείται για την διαχείριση του αρχείου, δηλαδή το άνοιγμα, το διάβασμα, το γράψιμο και τη διαγραφή.
 */
public class FileManager {

    /**
     * Η μέθοδος είναι ο κενός constructor.
     */
    public FileManager() {}

    /**
     * Η μέθοδος είναι υπεύθυνη για το άνοιγμα και το διάβασμα του αρχείου καθώς και την αποθήκευση των δεδομένων του
     * αρχείου σε μία δομή Tree Map, που θα χρησιμοποιηθεί στις βασικές λειτουργείες του προγράμματος για την παραγωγή
     * των απαιτούμενων αποτελεσμάτων.
     * @param fileName Το όνομα του αρχείου.
     * @return Ένα Tree Map που έχει αποθηκευμένα τα δεδομένα του αρχείου.
     * @throws Exception Σε περίπτωση που δεν θα βρεθεί το αρχείο ή δεν θα μπορέσει να το ανοίξει τότε εκτυπώνει το κατάλληλο
     *                  exception ώστε να μην κωλύσει το πρόγραμμα.
     */
    public TreeMap<Integer, ArrayList<Number>> input(String fileName) throws Exception {
        String reader;
        String[] splitReader;
        TreeMap<Integer, ArrayList<Number>> data = new TreeMap<>();
        ArrayList<Number> subData = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fileName)));
        while ((reader=bufferedReader.readLine())!=null) {
            splitReader = reader.split(" ");
            for (int i=1; i<splitReader.length; i++) {
                if (i<3) {
                    subData.add(Double.valueOf(splitReader[i].trim()));
                } else {
                    subData.add(Integer.valueOf(splitReader[i].trim()));
                }
            }
            data.put(Integer.valueOf(splitReader[0].trim()), new ArrayList<>(subData));
            subData.clear();
        }
        bufferedReader.close();
        return data;
    }

    /**
     * Η μέθοδος ανοίγει το αρχείο που δέχεται ως όρισμα και γράφει (αποθηκεύει) τα δεδομένα που επίσης δέχεται ως όρισμα.
     * @param fileName Το όνομα του αρχείου.
     * @param data Τα δεδομένα που πρέπει να εκτυπώσει (αποθηκεύσει) στο αρχείο.
     * @throws Exception Σε περίπτωση που δεν θα βρεθεί αρχείο ή δεν θα μπορέσει να το ανοίξει τότε εκτυπώνει το κατάλληλο
     *                  exception ώστε να μην κωλύσει το πρόγραμμα.
     */
    public void output(String fileName, String data) throws Exception {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName, true), StandardCharsets.UTF_8);
        outputStreamWriter.write(data);
        outputStreamWriter.close();
    }

    /**
     * Η μέθοδος διαγράφει το αχρείο που δέχεται ως όρισμα.
     * @param fileName Το όνομα του αρχείου.
     * @throws Exception Σε περίπτωση που δεν θα βρεθεί αρχείο ή δεν θα μπορέσει να το διαγράψει τότε εκτυπώνει το κατάλληλο
     *                  exception ώστε να μην κωλύσει το πρόγραμμα
     */
    public void delete(String fileName) throws Exception {
        File file = new File(fileName);
        file.delete();
    }
}
