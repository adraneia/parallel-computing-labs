# SharedCounterExercise_2 — Πλήρης Λύση (12 αρχεία)

Κάθε αρχείο βρίσκεται στον δικό του φάκελο (ένα .java ανά φάκελο), ώστε
να μπορείς να ανοίγεις κάθε φάκελο ξεχωριστά στο VS Code χωρίς
συγκρούσεις κλάσεων (`duplicate class` errors).

## Χάρτης φακέλων

| # | Φάκελος | Αρχείο | Πηγή | Τεχνική διαμοιρασμού | Mutex | Αναμενόμενο |
|---|---|---|---|---|---|---|
| 01 | Global-Original | `SharedCounterArrayGlobal.java` | Global | static | ❌ | errors > 0 |
| 02 | Global-Args | `SharedCounterArrayGlobal_Args.java` | Global | ορίσματα constructor | ❌ | errors > 0 |
| 03 | Global-Object | `SharedCounterArrayGlobal_Object.java` | Global | κοινό αντικείμενο (struct-style) | ❌ | errors > 0 |
| 04 | Global-Original-Mutex | `SharedCounterArrayGlobal_Mutex.java` | Global | static | ✅ Lock | **0 errors** |
| 05 | Global-Args-Mutex | `SharedCounterArrayGlobal_Args_Mutex.java` | Global | ορίσματα constructor | ✅ Lock | **0 errors** |
| 06 | Global-Object-Mutex | `SharedCounterArrayGlobal_Object_Mutex.java` | Global | κοινό αντικείμενο (πλήρως OOP) | ✅ Lock | **0 errors** |
| 07 | While-Original | `SharedCounterArrayGlobalWhile.java` | While | static | ❌ | errors > 0 |
| 08 | While-Args | `SharedCounterArrayGlobalWhile_Args.java` | While | ορίσματα + `Counter` wrapper | ❌ | errors > 0 |
| 09 | While-Object | `SharedCounterArrayGlobalWhile_Object.java` | While | κοινό αντικείμενο (struct-style) | ❌ | errors > 0 |
| 10 | While-Original-Mutex | `SharedCounterArrayGlobalWhile_Mutex.java` | While | static | ✅ Lock | **0 errors** |
| 11 | While-Args-Mutex | `SharedCounterArrayGlobalWhile_Args_Mutex.java` | While | ορίσματα + `Counter` wrapper | ✅ Lock | **0 errors** |
| 12 | While-Object-Mutex | `SharedCounterArrayGlobalWhile_Object_Mutex.java` | While | κοινό αντικείμενο (πλήρως OOP) | ✅ Lock | **0 errors** |

## Σημαντικά σημεία σχεδίασης (γιατί ο κώδικας είναι έτσι)

### 1. Primitives περνάνε by value, αντικείμενα by reference
Το `end` και το `numThreads` είναι `int` (primitive) — περνάνε σαν
constructor argument **by value**. Δεν πειράζει, γιατί κανένα thread
δεν τα **τροποποιεί**, μόνο τα διαβάζει. Ο πίνακας `array[]` είναι
αντικείμενο — περνάει **by reference**, άρα είναι πραγματικά shared.

### 2. Το `Counter` wrapper (μόνο στα "While" Args αρχεία, #08 και #11)
Στα αρχεία "While", ο μετρητής `counter` **πρέπει** να είναι shared
ΚΑΙ τροποποιήσιμος από όλα τα threads. Αν τον περνούσαμε σαν απλό
`int counter` argument, κάθε thread θα έπαιρνε το δικό του ανεξάρτητο
αντίγραφο (λάθος!). Γι' αυτό τον τυλίγουμε σε μια μικρή κλάση
`Counter { int value; }`, ώστε να περνάει by reference.
Στα "Object" αρχεία (#09, #12) αυτό δεν χρειάζεται, γιατί ο μετρητής
ήδη ζει μέσα στο shared `SharedData`/αντικείμενο.

### 3. Ενιαίο κρίσιμο τμήμα στα "While" mutex αρχεία (#10, #11, #12)
Το `if (counter >= end)`, το `array[counter]++`, και το `counter++`
πρέπει να είναι **όλα μαζί** μέσα στο ίδιο `lock()/unlock()` block.
Αν κλειδώναμε καθένα ξεχωριστά, θα παρέμενε race condition (δύο
threads θα μπορούσαν να διαβάσουν τον ίδιο `counter` πριν προλάβει
κάποιο να τον αυξήσει).

### 4. `static class` για nested κλάσεις
Όλες οι εσωτερικές (nested) κλάσεις (`CounterThread`, `SharedData`,
`Counter`) είναι δηλωμένες με `static`, ώστε να μπορούν να
δημιουργηθούν μέσα στο static `main()` χωρίς σφάλμα
("non-static variable this cannot be referenced from a static context").

## Πώς τρέχεις κάθε αρχείο

```bash
cd 04-Global-Original-Mutex
javac SharedCounterArrayGlobal_Mutex.java
java SharedCounterArrayGlobal_Mutex
```
