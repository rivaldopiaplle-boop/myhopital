package com.rivaldo.hopital.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MotDePasseTest {

    @Test
    void leHacheNeContientPasLeMotDePasse() {
        String hache = MotDePasse.hacher("admin");
        assertTrue(hache.startsWith("pbkdf2$"));
        assertFalse(hache.contains("admin"));
    }

    @Test
    void leBonMotDePasseEstAccepteLeMauvaisRefuse() {
        String hache = MotDePasse.hacher("secret");
        assertTrue(MotDePasse.verifier("secret", hache));
        assertFalse(MotDePasse.verifier("Secret", hache));
        assertFalse(MotDePasse.verifier("", hache));
        assertFalse(MotDePasse.verifier(null, hache));
    }

    @Test
    void deuxHachesDuMemeMotDePasseDifferent() {
        // Un sel par mot de passe : deux comptes au meme mot de passe ne se trahissent pas.
        assertNotEquals(MotDePasse.hacher("admin"), MotDePasse.hacher("admin"));
    }

    @Test
    void uneValeurEncoreEnClairResteVerifiable() {
        assertFalse(MotDePasse.estHache("admin"));
        assertTrue(MotDePasse.verifier("admin", "admin"));
        assertFalse(MotDePasse.verifier("autre", "admin"));
    }

    @Test
    void uneValeurMalFormeeEstRefusee() {
        assertFalse(MotDePasse.verifier("admin", "pbkdf2$abime"));
    }
}
