import java.net.*;
import java.io.*;

public class IterativeEchoServerTCP {
	private static final int PORT = 1234;
	private static final String EXIT = "CLOSE";

	public static void main(String args[]) throws IOException {

		ServerSocket connectionSocket = new ServerSocket(PORT);
		
		while (true) {	

			System.out.println("Server is listening to port: " + PORT);
			Socket dataSocket = connectionSocket.accept(); // περιμένει νέο πελάτη
			System.out.println("Received request from " + dataSocket.getInetAddress());

			InputStream is = dataSocket.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			OutputStream os = dataSocket.getOutputStream();
			PrintWriter out = new PrintWriter(os,true);
					
			String inmsg, outmsg;
			inmsg = in.readLine();   // ΠΕΡΙΜΕΝΕΙ τον ΣΥΓΚΕΚΡΙΜΕΝΟ πελάτη
			ServerProtocol app = new ServerProtocol();
			outmsg = app.processRequest(inmsg);
			while(!outmsg.equals(EXIT)) {
				out.println(outmsg);
				inmsg = in.readLine();  // και ΞΑΝΑπεριμένει...
				outmsg = app.processRequest(inmsg);
			}

			dataSocket.close(); // ΜΟΝΟ ΤΩΡΑ ξαναγυρνάει στην κορυφή για το επόμενο accept()
			System.out.println("Data socket closed");	
		}
	}
}			

// ----------------------Τύπος 1 — "Δίνεται ο ακολουθιακός/iterative κώδικας, μετατρέψτε τον" ??????????
// "Δίνεται ο παρακάτω κώδικας διακομιστή [iterative]. 
// Τροποποιήστε τον ώστε να εξυπηρετεί πολλούς πελάτες ταυτόχρονα."
// → Ακριβώς αυτό κάναμε μόλις τώρα θεωρητικά (μετατροπή σε ServerThread).

// ----------------------Τύπος 2 — "Εντοπίστε το πρόβλημα" (trace/κριτική)
// "Ο παρακάτω κώδικας διακομιστή δουλεύει σωστά με έναν πελάτη, 
// αλλά οι χρήστες παραπονιούνται ότι όταν συνδέονται πολλοί πελάτες ταυτόχρονα, κάποιοι 'κολλάνε'. 
// Εξηγήστε γιατί συμβαίνει αυτό και προτείνετε διόρθωση."
// → Θα έπρεπε να εντοπίσεις ότι το in.readLine() βρίσκεται μέσα στο ίδιο while(true) με το accept().
// Άρα όσο ο διακομιστής "μιλάει" με τον πελάτη Α (μπλοκαρισμένος στο in.readLine()), 
// δεν μπορεί να καλέσει ξανά accept() — είναι το ίδιο νήμα, δεν μπορεί να κάνει δύο πράγματα ταυτόχρονα. 
// Ο πελάτης Β που θέλει να συνδεθεί μένει στην ουρά του λειτουργικού συστήματος, 
// "κολλημένος", μέχρι ο Α να στείλει EXIT και να κλείσει η σύνδεσή του.



// Τύπος 3 — "Ποια τμήματα υλοποιούν..."
// "Ποιο τμήμα του παραπάνω [iterative] κώδικα εμποδίζει την ταυτόχρονη εξυπηρέτηση πολλών πελατών;"
// → Η ίδια απάντηση: ο βρόχος επικοινωνίας (readLine/processRequest/println) είναι στο ίδιο νήμα με το accept().

// Τύπος 4 — Βηματική εκτέλεση (σενάριο)
// "Έστω ότι ο πελάτης Α συνδέεται στις 10:00 αλλά αργεί να στείλει μήνυμα, και ο πελάτης Β προσπαθεί να συνδεθεί στις 10:01. 
// Περιγράψτε τι συμβαίνει βήμα-βήμα."
// → Αυτό είναι το ακριβές σενάριο που είδαμε πριν με το telnet σου.
// Βήμα-βήμα:
// [10:00] [ACCEPT]   Server δέχεται τον Α (accept() ξεμπλοκάρει)
// [10:00] [READLINE] Server: in.readLine() -> ΜΠΛΟΚΑΡΕΙ, περιμένει μήνυμα Α

// [10:01] [CONNECT]  Β: connect() -> το TCP handshake ολοκληρώνεται σε επίπεδο OS,
//                    ΑΝΕΞΑΡΤΗΤΑ αν η εφαρμογή έχει καλέσει accept() -> Β μπαίνει σε ουρά (backlog)
// [10:01] [BUFFER]   Β -> "β", "ββ", "ββββ" -> ΔΕΝ χάνονται, αποθηκεύονται σε OS buffer,
//                    διατηρώντας τη ΣΕΙΡΑ αποστολής (TCP = ordered delivery)

// [10:02] [READLINE] Α -> "α"    -> server απαντάει -> ξαναμπλοκάρει σε readLine (ίδιο while του Α)
// [10:03] [READLINE] Α -> "αα"   -> server απαντάει -> ξαναμπλοκάρει
// [10:04] [READLINE] Α -> "ααα"  -> server απαντάει -> ξαναμπλοκάρει
// [10:05] [READLINE] Α -> "αααα" -> server απαντάει -> ξαναμπλοκάρει
// [10:06] [CLOSE]    Α -> EXIT -> dataSocket.close() -> server επιστρέφει στην κορυφή του while(true)

// [10:06] [ACCEPT]   ΜΟΝΟ ΤΩΡΑ server accept()-άρει τον Β (τον "παραλαμβάνει" επίσημα)
// [10:06] [READLINE] Server διαβάζει ΑΜΕΣΩΣ ό,τι ήταν ήδη στο buffer, ΜΕ ΤΗ ΣΕΙΡΑ: "β", "ββ", "ββββ"