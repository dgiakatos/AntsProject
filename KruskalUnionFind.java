import java.util.*;

/**
 * @author Δημήτριος Παντελεήμων Γιακάτος
 * @version 1.0.0
 * Η κλάση βρίσκει και αποθηκεύει σε αρχείο το συντομότερο μονοπάτι που ενώνει όλα τα μυρμήγκια. Προκειμένου να βρει
 * αυτό το μονοπάτι χρησιμοποιεί τον αλγόριθμο Kruskal Union-find.
 */
public class KruskalUnionFind {

    private TreeMap<Integer, ArrayList<Number>> data;
    private ArrayList<Ant> partArrayList = new ArrayList<>();
    private int[] parent;
    private HashMap<Integer, ArrayList<Integer>> kuf = new HashMap<>();
    private double totalKuf = 0;

    /**
     * Η μέθοδος είναι ο constructor της κλάσης, που καλεί κάποιες από τις μεθόδους της κλάσεις ώστε να παράγει το
     * ζητούμενο αποτέλεσμα για την άσκηση.
     * @param data Το Tree Map που έχει αποθηκευμένα τα δεδομένα του αρχείου.
     * @throws Exception Σε περίπτωση που δεν θα βρεθεί το αρχείο ή δεν θα μπορέσει να το ανοίξει τότε εκτυπώνει το κατάλληλο
     *                  exception ώστε να μην κωλύσει το πρόγραμμα.
     */
    public KruskalUnionFind(TreeMap<Integer, ArrayList<Number>> data) throws Exception {
        this.data = data;
        setPartArrayList();
        kruskal();
        print();
    }

    /**
     * Η μέθοδος υπολογίζει την ευκλείδεια απόσταση μεταξύ δύο μυρμηγκιών και αποθηκεύει τα δύο μυρμήγκια και την
     * απόσταση τους στη δομή partArrayList.
     */
    private void setPartArrayList() {
        for (Integer ver : data.keySet()) {
            for (Map.Entry<Integer, ArrayList<Number>> edg : data.tailMap(ver).entrySet()) {
                partArrayList.add(new Ant().setDataForKUF(ver,
                        data.get(ver).get(0).doubleValue(), data.get(ver).get(1).doubleValue(), edg.getKey(),
                        data.get(edg.getKey()).get(0).doubleValue(), data.get(edg.getKey()).get(1).doubleValue())
                );
            }
        }
    }

    /**
     * Η μέθοδος υλοποιεί τον αλγόριθμο Kruskal Union-find ώστε να υπολογίσει το ελάχιστο μονοπάτι. Αρχικά αποθηκεύει
     * σε μία ουρά προτεραιότητας τα ζεύγη των μυρμηγκιών με βάση την απόσταση τους, δηλαδή πρώτο στην ουρά θα είναι τα
     * δύο μυρμήγκια που έχουν την μικρότερη απόσταση. Επίσης θα σχηματιστεί και ο πίνακας parent, ο οποίος θα έχει ως
     * κλειδί τα id όλων των μυρμηγκιών και ως τιμή θα έχουν τα μυρμήγκια με τα οποία θα συνεχιστεί το μονοπάτι. Για
     * παράδειγμα από το μυρμήγκι 1 (κλειδί) θα πάμε στο μυρμήγκι 2 (τιμή), άρα parent[1]=2. Τα μυρμήγκια που ακόμα δεν
     * έχουν εξεταστεί θα έχουν ως τιμή τον εαυτό τους, δηλαδή parent[id]=id.
     */
    private void kruskal() {
        PriorityQueue<Ant> priorityQueue = new PriorityQueue<>(partArrayList.size());
        parent = new int[partArrayList.size()];
        for (Ant ant : partArrayList) {
            priorityQueue.add(ant);
            parent[ant.vertical-1] = ant.vertical-1;
        }
        int eCounter = 0;
        Integer ver1;
        Integer ver2;
        while (eCounter < data.size()-1) {
            Ant ant = priorityQueue.remove();
            ver1 = findPath(ant.vertical-1);
            ver2 = findPath(ant.edge-1);
            if (!ver1.equals(ver2)) {
                if (ant.vertical-1 <= ant.edge-1) {
                    kuf.computeIfAbsent(ant.vertical, o -> new ArrayList<>()).add(ant.edge);
                } else {
                    kuf.computeIfAbsent(ant.edge, o -> new ArrayList<>()).add(ant.vertical);
                }
                eCounter++;
                parent[findPath(ver1)] = findPath(ver2);
                totalKuf = totalKuf + ant.distance;
            }
        }
    }

    /**
     * Η μέθοδος βρίσκει και επιστρέφει το id του μυρμηγκιού που δεν έχει κάποιο μονοπάτι, δηλαδή από αυτό το id δεν
     * μπορούμε να πάμε σε κάποιο άλλο id (γυρνάει στον εαυτό του).
     * @param i Το id του μυρμηγκιού.
     * @return Το id του μυρμηγκιού που μπορεί να πάει, δηλαδή να συνεχίσει το μονοπάτι.
     */
    private Integer findPath(Integer i) {
        if (parent[i] == i) {
            return i;
        }
        return findPath(parent[i]);
    }

    /**
     * Η μέθοδος εκτυπώνει στο αρχείο τα δεδομένα της δομής Hash Map (kuf), δηλαδή τα ζεύγη των μυρμηγκιών και το
     * συνολικό βάρος του ελάχιστου μονοπατιού.
     * @throws Exception Σε περίπτωση που δεν θα βρεθεί το αρχείο ή δεν θα μπορέσει να το ανοίξει τότε εκτυπώνει το κατάλληλο
     *                  exception ώστε να μην κωλύσει το πρόγραμμα.
     */
    private void print() throws Exception {
        FileManager fileManager = new FileManager();
        fileManager.output("A.txt", String.valueOf(totalKuf)+"\r\n");
        for (Integer key : kuf.keySet()) {
            Collections.sort(kuf.get(key));
            for (int i=0; i<kuf.get(key).size(); i++) {
                fileManager.output("A.txt", key + " " + kuf.get(key).get(i) + "\r\n");
            }
        }
    }
}
