# Εργαστήριο 2 — Μοιραζόμενες Μεταβλητές, Αμοιβαίος Αποκλεισμός

## Δομή

```
Lab2-SharedMemory/
├── reference-code/          <- έτοιμος κώδικας του καθηγητή (μόνο Java, μελέτη)
│   ├── Shared/               3 τεχνικές διαμοιρασμού δεδομένων
│   ├── SharedCounter/        μετρητής ΧΩΡΙΣ mutex -> ΛΑΘΟΣ αποτέλεσμα
│   └── SharedCounterMutex/   ίδιος μετρητής ΜΕ mutex -> ΣΩΣΤΟ αποτέλεσμα
│
└── SharedCounterExercise/   <- τα δικά σου αρχεία-αφετηρία για την άσκηση
    ├── SharedCounterArrayGlobal.java
    └── SharedCounterArrayGlobalWhile.java
```

---

## reference-code/Shared/ — Τρεις τεχνικές διαμοιρασμού

Όλα λύνουν το **ίδιο πρόβλημα** (κάθε thread `i` κάνει `n = n + threadID` και
ενημερώνει `a[threadID]`), απλά με διαφορετικό τρόπο διαμοιρασμού των `n`, `a[]`:

| Αρχείο | Τεχνική |
|---|---|
| `GlobalVariables.java` | `static int n`, `static int[] a` στην κύρια κλάση (καθολικές) |
| `MainArguments.java` | Το `main` περνάει `n` (τιμή) και `a[]` (αναφορά) στον constructor κάθε thread |
| `MainArgsObj.java` | Ένα κοινό αντικείμενο `SharedData` (με `n`, `a[]`) περνιέται σαν αναφορά σε όλα τα threads |

**Σημείωση**: Στο `MainArguments.java`/`MainArgsObj.java`, το `n` περνάει σαν
**τιμή** σε κάθε thread (`this.n = data.n` ή `this.n = n`) — άρα κάθε thread
έχει το **δικό του** αντίγραφο του `n` (instance variable, όχι shared). Μόνο ο
πίνακας `a[]` είναι πραγματικά shared (περνάει by reference). Παρατήρησε πώς
το `main` μαζεύει τα αποτελέσματα των `n` κάθε thread χειροκίνητα, μέσα στο
loop του `join()` (`sum = sum + counterThreads[i].n`).

---

## reference-code/SharedCounter/ — Race Condition σε δράση

Και τα δύο αρχεία κάνουν το ίδιο: 10 threads, καθένα αυξάνει έναν κοινό
μετρητή 10.000 φορές. **Αναμενόμενο αποτέλεσμα: n = 100.000.**
**Πραγματικό αποτέλεσμα όταν τρέξεις: κάτι μικρότερο, διαφορετικό κάθε φορά.**

| Αρχείο | Τεχνική διαμοιρασμού |
|---|---|
| `GlobalVars.java` | `static int n` (καθολική) |
| `MainArgs.java` | Κοινό αντικείμενο `SharedCounter` με μέθοδο `inc()` (OOP) |

**Γιατί αποτυγχάνει**: `n = n + 1` δεν είναι atomic (load + increment + store,
3 βήματα). Δύο threads μπορούν να διαβάσουν την ίδια τιμή πριν προλάβει
κανείς να γράψει πίσω το αποτέλεσμα → χαμένη ενημέρωση (lost update).
Δες `notes/Σημειώσεις_Μάθημα.md`, Ενότητα 36, για πλήρη ανάλυση.

---

## reference-code/SharedCounterMutex/ — Η Λύση

Ακριβώς ο ίδιος κώδικας με το `SharedCounter/`, αλλά η αύξηση του μετρητή
τυλίγεται σε `lock.lock() / unlock()`:

```java
lock.lock();
try {
    n = n + 1;
} finally {
    lock.unlock();
}
```

| Αρχείο | Τεχνική |
|---|---|
| `GlobalVarsLock.java` | `static int n` + `static Lock lock` (και τα δύο καθολικά) |
| `MainArgsLock.java` | Το `Lock` ζει **μέσα** στο αντικείμενο `SharedCounter`, μαζί με το `n` — οι μέθοδοι `inc()`/`get()` κάνουν lock/unlock εσωτερικά |

Με mutex, το αποτέλεσμα είναι **πάντα 40.000** (4 threads × 10.000), σταθερό
σε κάθε εκτέλεση — δες Ενότητα 39 στις σημειώσεις για το γιατί το `Lock`
λύνει το πρόβλημα.

---

## SharedCounterExercise/ — Η Αφετηρία της Άσκησής σου

Δύο πιο σύνθετα προβλήματα (πίνακας αντί για απλός μετρητής), και τα δύο
**χωρίς** mutex, άρα **και τα δύο δίνουν λάθος αποτέλεσμα**:

### `SharedCounterArrayGlobal.java`
- 4 threads, κάθε ένα διατρέχει **ολόκληρο** τον πίνακα `array[1000]`.
- Για κάθε θέση `i`, κάνει `array[i]++` ακριβώς `i` φορές.
- Αναμενόμενο: `array[i] == numThreads * i` (κάθε θέση αυξάνεται από όλα τα 4 threads).
- Race condition πάνω σε **κάθε στοιχείο** του πίνακα.

### `SharedCounterArrayGlobalWhile.java`
- 4 threads μοιράζονται δουλειά μέσω ενός κοινού μετρητή `counter` (0..9999).
- Κάθε thread: πάρε τον επόμενο δείκτη (`counter`), αύξησε `array[counter]`, προχώρα τον μετρητή.
- Αναμενόμενο: κάθε θέση αυξάνεται **ακριβώς μία φορά** (`array[i] == 1`).
- **Διπλό** race condition: πάνω στο `counter` *και* πάνω στα στοιχεία του `array`.

---

## Τι ζητάει η εκφώνηση (βλ. και `notes/`, Ενότητα 42)

1. **Βήμα 1.1**: Ξαναγράψε τα 2 αρχεία της `SharedCounterExercise/` περνώντας
   τα δεδομένα σαν **ορίσματα στον constructor** (τεχνική `MainArguments`).
2. **Βήμα 1.2**: Ξαναγράψε τα ίδια 2 αρχεία με **κοινό αντικείμενο** (τεχνική `MainArgsObj`).
3. **Βήμα 2**: Πάρε **και τα 6** αρχεία (2 αρχικά + 2 από 1.1 + 2 από 1.2) και
   πρόσθεσε mutex (`Lock`, όπως στο `SharedCounterMutex/`) σε καθένα.

Σύνολο: **12 αρχεία**.
