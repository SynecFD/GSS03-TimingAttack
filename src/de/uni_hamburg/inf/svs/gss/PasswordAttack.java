package de.uni_hamburg.inf.svs.gss;

import static de.uni_hamburg.inf.svs.gss.PasswordCompare.passwordCompare;

public class PasswordAttack {

	// Anzahl der Iterationen, die man denselben Angriff ausführen möchte, um
	// verlässliche Mittelwerte zu erhalten
	private final long iterations = 1000000;

	// Geringe Abweichungen zwischen den einzelnen Messwerten müssen erlaubt
	// sein. Hier ist je nach Hardware ein Wert experimentell zu ermitteln.
	private final short offset = 5;

	// Das konkrete Password, dessen Länge erraten werden soll.
	public final static char[] PASSWORD = "W#6u$!2PFL".toCharArray();

	public static void main(String[] args) {
		long timing;

		timing = System.nanoTime();

		int pwLength = new PasswordAttack().findLength();

		timing = (System.nanoTime() - timing) / 1000000000;

		System.out.println("Benötigte Zeit: " + timing + " sec");
		System.out.println("Errechnete Passwortlänge: " + pwLength);

		if (pwLength == PASSWORD.length) {
			System.out.println("Korrekt!");
		} else {
			System.out.println("Nicht korrekt, richtige Länge: "
					+ PASSWORD.length);
		}

		timing = System.nanoTime();

		char[] passwordguess = new PasswordAttack().findPassword(pwLength);

		timing = (System.nanoTime() - timing) / 1000000000;

		System.out.println("Benötigte Zeit: " + timing + " sec");
		System.out.println("Errechnetes Passwort: "
				+ (new String(passwordguess)));

		if ((new String(PASSWORD)).equals(new String(passwordguess))) {
			System.out.println("Korrektes Password!");
		} else {
			System.out.println("Nicht korrekt, falsches Passwort: "
					+ (new String(passwordguess)) + " != "
					+ (new String(PASSWORD)));
		}

	}

	private int findLength() {
		long timer;
		long oldTime = 0;
		long midTime = 0;
		char[] tryPass = "".toCharArray();

		// Abweichungen von <code>offset</code> ns sind im Bereich der Toleranz
		while ((oldTime < midTime - offset) || (oldTime < midTime + offset)) {
			oldTime = midTime;
			midTime = 0;
			tryPass = (new String(tryPass) + "a").toCharArray();
			for (int i = 0; i < iterations; ++i) {
				timer = System.nanoTime();
				passwordCompare(PASSWORD, tryPass);
				timer = System.nanoTime() - timer;
				midTime = midTime + timer;
			}
			midTime = midTime / iterations;
			// System.out.println("" + midTime);
		}

		// -1, da die while-Schleife erst im nächsthöheren Durchlauf entdeckt,
		// dass eine Abweichung stattgefunden hat.
		return tryPass.length - 1;
	}

	private char[] findPassword(int pwlength) {
		long timer;
		long oldTime;
		long midTime;
		char[] tryPass = new char[pwlength];
		for (int i = 0; i < pwlength; ++i) {
			tryPass[i] = ' ';
		}

		for (int j = 0; j < pwlength; ++j) {
			oldTime = 0;
			midTime = 0;
			while ((oldTime < midTime - offset) || (oldTime < midTime + offset)) {
				oldTime = midTime;
				midTime = 0;
				tryPass[j] += 1;

				for (int i = 0; i < iterations; ++i) {
					timer = System.nanoTime();
					passwordCompare(PASSWORD, tryPass);
					timer = System.nanoTime() - timer;
					midTime = midTime + timer;
				}

				midTime = midTime / iterations;
				// System.out.println("" + midTime + " " + (new
				// String(tryPass)));
			}
			tryPass[j] -= 1;
			if (passwordCompare(PASSWORD, tryPass)) {
				return tryPass;
			}
		}

		return tryPass;
	}

}
