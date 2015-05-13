package de.uni_hamburg.inf.svs.gss;
public class PasswordCompare {

	/**
	 * Einfacher break-on-inequality Vergleich zweier Passwörter
	 * 
	 * @param a
	 *            tatsächliches Passwort aus dem TPM
	 * @param b
	 *            übergebenes Passwort, was es zu prüfen gilt
	 * @return true, falls die Passwörter a und b übereinstimmen, false sonst
	 */
	public static boolean passwordCompare(char[] a, char[] b) {
		int i;

		if (a.length != b.length)
			return false;

		for (i = 0; i < a.length && a[i] == b[i]; i++);

		return i == a.length;
	}
}
