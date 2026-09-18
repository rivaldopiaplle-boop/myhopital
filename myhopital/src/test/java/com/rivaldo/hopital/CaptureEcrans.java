package com.rivaldo.hopital;

import com.rivaldo.hopital.config.AppBootstrap;
import com.rivaldo.hopital.config.DatabaseConfig;
import com.rivaldo.hopital.domain.Utilisateur;
import com.rivaldo.hopital.repository.DataStore;
import com.rivaldo.hopital.repository.DatabaseManager;
import com.rivaldo.hopital.repository.DatabaseRepository;
import com.rivaldo.hopital.service.AuthService;
import com.rivaldo.hopital.service.ConsultationService;
import com.rivaldo.hopital.service.DocumentMedicalService;
import com.rivaldo.hopital.service.DossierMedicalService;
import com.rivaldo.hopital.service.MedecinService;
import com.rivaldo.hopital.service.PatientService;
import com.rivaldo.hopital.service.RendezVousService;
import com.rivaldo.hopital.service.ValidationService;
import com.rivaldo.hopital.ui.DashboardView;
import com.rivaldo.hopital.ui.LoginView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 * @Objectif Photographier les ecrans de l'application pour le README et le portfolio.
 *
 * Monte l'application sur une base temporaire (les donnees de demonstration),
 * se connecte comme directeur, puis enregistre l'ecran de connexion et chaque
 * onglet du tableau de bord en PNG. Ce n'est pas un test : un programme a
 * lancer a la main, qui ne touche pas a la base de l'application.
 *
 *   java -cp target/classes:target/test-classes:(dependances) com.rivaldo.hopital.CaptureEcrans captures
 */
public final class CaptureEcrans {

    private CaptureEcrans() {
    }

    public static void main(String[] args) throws Exception {
        Path sortie = Path.of(args.length > 0 ? args[0] : "captures");
        Files.createDirectories(sortie);
        Path base = Files.createTempFile("myhopital-capture", ".db");

        CountDownLatch fin = new CountDownLatch(1);
        Platform.startup(() -> {
            try {
                capturer(sortie, base);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                fin.countDown();
            }
        });
        fin.await();
        Platform.exit();
        Files.deleteIfExists(base);
    }

    private static void capturer(Path sortie, Path base) throws Exception {
        DatabaseManager manager = new DatabaseManager(
            new DatabaseConfig("jdbc:sqlite:" + base, null, null, "org.sqlite.JDBC"));
        manager.connect();
        manager.createTables();
        DatabaseRepository repository = new DatabaseRepository(manager);
        DataStore store = new DataStore();
        ValidationService validation = new ValidationService();
        DossierMedicalService dossiers = new DossierMedicalService(store, repository);
        PatientService patients = new PatientService(store, dossiers, repository);
        MedecinService medecins = new MedecinService(store, repository);
        RendezVousService rendezVous = new RendezVousService(store, repository);
        ConsultationService consultations = new ConsultationService(store, repository);
        DocumentMedicalService documents = new DocumentMedicalService(store, repository);
        AuthService auth = new AuthService(store, validation, repository);
        new AppBootstrap(store, validation, auth, medecins, patients, rendezVous, consultations, documents).load();

        Stage stage = new Stage();
        Scene connexion = scene(new LoginView(auth, u -> { }, () -> { }, () -> { }).createView(), 1100, 720);
        stage.setScene(connexion);
        stage.show();
        enregistrer(connexion, sortie.resolve("connexion.png"));

        Utilisateur directeur = auth.login("admin@hopital.local", "admin").orElseThrow();
        Parent tableau = new DashboardView(directeur, store, patients, medecins, rendezVous, consultations,
            documents, dossiers, auth, () -> { }).createView();
        Scene scene = scene(tableau, 1280, 760);
        stage.setScene(scene);
        TabPane onglets = (TabPane) tableau.lookup(".tab-pane");
        for (Tab onglet : onglets.getTabs()) {
            onglets.getSelectionModel().select(onglet);
            scene.getRoot().applyCss();
            scene.getRoot().layout();
            String nom = onglet.getText().toLowerCase().replaceAll("[^a-z]+", "-");
            enregistrer(scene, sortie.resolve("onglet-" + nom + ".png"));
        }
        stage.close();
        manager.close();
    }

    private static Scene scene(Parent racine, double largeur, double hauteur) {
        Scene scene = new Scene(racine, largeur, hauteur);
        scene.getStylesheets().add(CaptureEcrans.class.getResource("/application.css").toExternalForm());
        return scene;
    }

    /** PNG sans le module javafx-swing : les pixels passent par un BufferedImage. */
    private static void enregistrer(Scene scene, Path fichier) throws Exception {
        WritableImage image = scene.snapshot(null);
        int largeur = (int) image.getWidth();
        int hauteur = (int) image.getHeight();
        PixelReader lecteur = image.getPixelReader();
        BufferedImage png = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                png.setRGB(x, y, lecteur.getArgb(x, y));
            }
        }
        ImageIO.write(png, "png", new File(fichier.toString()));
        System.out.println("capture : " + fichier);
    }
}
