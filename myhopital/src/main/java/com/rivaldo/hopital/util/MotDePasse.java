package com.rivaldo.hopital.util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * @Objectif Hacher et verifier les mots de passe, sans jamais les stocker en clair.
 *
 * PBKDF2 avec HMAC-SHA256, fourni par Java : aucune dependance a ajouter.
 * Chaque mot de passe recoit son propre sel, et le nombre d'iterations est
 * range avec le hache, pour pouvoir l'augmenter plus tard sans casser les
 * comptes existants. Forme stockee : pbkdf2$iterations$sel$hache (Base64).
 */
public final class MotDePasse {

    private static final String PREFIXE = "pbkdf2$";
    private static final String ALGORITHME = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 210_000; // recommandation OWASP pour PBKDF2-SHA256
    private static final int OCTETS_SEL = 16;
    private static final int BITS_HACHE = 256;
    private static final SecureRandom ALEA = new SecureRandom();

    private MotDePasse() {
    }

    /**
     * @Objectif Produire la forme stockable d'un mot de passe.
     * @param motDePasse mot de passe saisi
     * @return pbkdf2$iterations$sel$hache
     */
    public static String hacher(String motDePasse) {
        byte[] sel = new byte[OCTETS_SEL];
        ALEA.nextBytes(sel);
        byte[] hache = deriver(motDePasse, sel, ITERATIONS);
        Base64.Encoder b64 = Base64.getEncoder();
        return PREFIXE + ITERATIONS + "$" + b64.encodeToString(sel) + "$" + b64.encodeToString(hache);
    }

    /**
     * @Objectif Dire si une valeur stockee est deja hachee.
     * @param stocke valeur lue en base
     * @return true si elle a la forme pbkdf2$...
     */
    public static boolean estHache(String stocke) {
        return stocke != null && stocke.startsWith(PREFIXE);
    }

    /**
     * @Objectif Verifier un mot de passe saisi contre la valeur stockee.
     *
     * Une valeur encore en clair (base creee avant le hachage) est comparee
     * telle quelle : AuthService la remplace par son hache a la premiere
     * connexion reussie.
     *
     * @param saisi mot de passe saisi
     * @param stocke valeur lue en base
     * @return true si le mot de passe est le bon
     */
    public static boolean verifier(String saisi, String stocke) {
        if (saisi == null || stocke == null) {
            return false;
        }
        if (!estHache(stocke)) {
            return MessageDigest.isEqual(saisi.getBytes(), stocke.trim().getBytes());
        }
        String[] parties = stocke.split("\\$");
        if (parties.length != 4) {
            return false;
        }
        int iterations = Integer.parseInt(parties[1]);
        byte[] sel = Base64.getDecoder().decode(parties[2]);
        byte[] attendu = Base64.getDecoder().decode(parties[3]);
        // Comparaison en temps constant : la duree ne revele pas combien d'octets concordent.
        return MessageDigest.isEqual(attendu, deriver(saisi, sel, iterations));
    }

    private static byte[] deriver(String motDePasse, byte[] sel, int iterations) {
        PBEKeySpec spec = new PBEKeySpec(motDePasse.toCharArray(), sel, iterations, BITS_HACHE);
        try {
            return SecretKeyFactory.getInstance(ALGORITHME).generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("PBKDF2 indisponible", e);
        } finally {
            spec.clearPassword();
        }
    }
}
