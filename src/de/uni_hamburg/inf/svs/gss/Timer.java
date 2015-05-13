package de.uni_hamburg.inf.svs.gss;

import static de.uni_hamburg.inf.svs.gss.PasswordCompare.passwordCompare;

public class Timer {

	/**
	 * Timing-Attack, welche die Zeit in Nanosekunden misst, die ein Aufruf von
	 * <code>boolean passwordCompare(char[],char[])</code> bei verschiedenen
	 * Eingaben benötigt.
	 * 
	 * @return Vergleichszeiten der Passwörter in ns als Array (1: gleiche, 2:
	 *         halbgleiche, 3: ungleiche, 4: ungleich aber selbe Länge)
	 */
	public long[] timingAttack() {
		long timer;
		char[] password;
		char[] wrongPass;
		long[] middle = new long[4];

		// Vergleich gleicher Passwörter
		password = "W#6u$!2PFLrx99fQBB".toCharArray();
		timer = System.nanoTime();
		passwordCompare(password, password);
		timer = System.nanoTime() - timer;
		middle[0] = timer;

		// Vergleich halbgleicher Passwörter
		wrongPass = "W#6u$!2PF%9-3a2Taa".toCharArray();
		timer = System.nanoTime();
		passwordCompare(password, wrongPass);
		timer = System.nanoTime() - timer;
		middle[1] = timer;

		// Vergleich ungleicher Passwörter
		wrongPass = "%9-3a2Tauy$".toCharArray();
		timer = System.nanoTime();
		passwordCompare(password, wrongPass);
		timer = System.nanoTime() - timer;
		middle[2] = timer;

		// Vergleich ungleicher Passwörter aber mit derselben Länge
		password = "W#6u$!2PFL".toCharArray();
		wrongPass = "%9-3a2Tauy".toCharArray();
		timer = System.nanoTime();
		passwordCompare(password, wrongPass);
		timer = System.nanoTime() - timer;
		middle[3] = timer;

		return middle;
	}

	public static void main(String[] args) {
		long timing;
		Timer timer = new Timer();
		long[] middle;
		long[] time = new long[4];

		// Anzahl der Iterationen, die man denselben Angriff ausführen möchte,
		// um verlässliche Mittelwerte zu erhalten
		final long iterations = 1000000;

		timing = System.nanoTime();

		// Die Timing-Attack mehrmals ausführen
		for (int i = 1; i <= iterations; ++i) {
			middle = timer.timingAttack();
			time[0] = (time[0] + middle[0]);
			time[1] = (time[1] + middle[1]);
			time[2] = (time[2] + middle[2]);
			time[3] = (time[3] + middle[3]);
		}
		time[0] = time[0] / iterations;
		time[1] = time[1] / iterations;
		time[2] = time[2] / iterations;
		time[3] = time[3] / iterations;

		timing = (System.nanoTime() - timing) / 1000000000;

		System.out.println("Zeit bei Vergleich gleicher Passwörter: " + time[0]
				+ " ns");
		System.out.println("Zeit bei Vergleich halbgleicher Passwörter: "
				+ time[1] + " ns");
		System.out.println("Zeit bei Vergleich ungleicher Passwörter: "
				+ time[2] + " ns");
		System.out
				.println("Zeit bei Vergleich ungleicher Passwörter mit selber Länge: "
						+ time[3] + " ns");
		System.out.println("Benötigte Zeit: " + timing + " sec");
	}

}