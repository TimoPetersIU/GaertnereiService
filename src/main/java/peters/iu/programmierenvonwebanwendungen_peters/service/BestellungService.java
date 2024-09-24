package peters.iu.programmierenvonwebanwendungen_peters.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import peters.iu.programmierenvonwebanwendungen_peters.controller.BestellungController;
import peters.iu.programmierenvonwebanwendungen_peters.entity.bestellung.Bestellposition;
import peters.iu.programmierenvonwebanwendungen_peters.entity.bestellung.Bestellung;
import peters.iu.programmierenvonwebanwendungen_peters.entity.produkt.Produkt;
import peters.iu.programmierenvonwebanwendungen_peters.repository.ProduktRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service-Klasse zur Verwaltung von Bestellungen.
 * <p>
 * Diese Klasse enthält Methoden zur Erzeugung von Bestellpositionen und zur Berechnung des Gesamtpreises
 * einer Bestellung. Die {@code erzeugeBestellpositionen} Methode erstellt Bestellpositionen basierend
 * auf den übergebenen Produkt-IDs und Mengen. Die {@code berechneGesamtpreis} Methode berechnet den Gesamtpreis
 * der übergebenen Bestellpositionen.
 * </p>
 *
 * @author Timo Peters - IU Hamburg
 */
@Service
public class BestellungService {

    private static final Logger log = LoggerFactory.getLogger(BestellungController.class);

    @Autowired
    private ProduktRepository produktRepository;

    /**
     * Verarbeitet eine neue Bestellung, erstellt Bestellpositionen, prüft und aktualisiert den Bestand
     * und berechnet den Gesamtpreis.
     *
     * @param bestellung die zu verarbeitende Bestellung
     * @param produktIds die Liste der Produkt-IDs
     * @param mengen     die Liste der Mengen für die jeweiligen Produkte
     */
    public void verarbeiteNeueBestellung(Bestellung bestellung, List<Long> produktIds, List<Integer> mengen) {
        log.info("Beginne Verarbeitung einer neuen Bestellung");

        // Erstelle Bestellpositionen basierend auf den übergebenen Produkt-IDs und Mengen
        List<Bestellposition> bestellpositionen = erzeugeBestellpositionen(produktIds, mengen, bestellung);

        // Füge die erstellten Bestellpositionen zur Bestellung hinzu
        bestellung.setBestellpositionen(bestellpositionen);

        // Berechne den Gesamtpreis der Bestellung basierend auf den Bestellpositionen
        BigDecimal gesamtPreis = berechneGesamtpreis(bestellpositionen);

        // Setze den berechneten Gesamtpreis für die Bestellung
        bestellung.setPreis(gesamtPreis);

        log.info("Gesamtpreis für die Bestellung berechnet: {}", gesamtPreis);
        log.info("Verarbeitung der neuen Bestellung abgeschlossen");
    }

    /**
     * Bearbeitet eine bestehende Bestellung, aktualisiert Bestellpositionen, prüft und aktualisiert den Bestand
     * und berechnet den neuen Gesamtpreis.
     *
     * @param bestehendeBestellung die zu bearbeitende Bestellung
     * @param bestellung           die aktualisierten Bestelldaten
     * @param produktIdList        die Liste der Produkt-IDs
     * @param mengeList            die Liste der Mengen für die jeweiligen Produkte
     */
    public void bearbeiteBestellung(Bestellung bestehendeBestellung, Bestellung bestellung, List<Long> produktIdList, List<Integer> mengeList) {
        log.info("Beginne Bearbeitung der Bestellung {}", bestehendeBestellung.getBestellnummer());

        // Aktualisiere allgemeine Bestelldaten (z.B. Lieferadresse, Zahlungsmethode)
        aktualisiereBestelldaten(bestehendeBestellung, bestellung);

        // Hole bestehende Bestellpositionen und erstelle eine Map für schnelleren Zugriff
        List<Bestellposition> bestehendeBestellpositionen = bestehendeBestellung.getBestellpositionen();
        Map<Long, Bestellposition> positionMap = erstellePositionMap(bestehendeBestellpositionen);

        // Bearbeite bestehende Bestellpositionen oder füge neue hinzu
        bearbeiteBestellpositionen(bestehendeBestellung, bestehendeBestellpositionen, positionMap, produktIdList, mengeList);

        // Entferne Bestellpositionen, die nicht mehr in der aktualisierten Bestellung enthalten sind
        entferneNichtBenoetigteBestellpositionen(bestehendeBestellung, bestehendeBestellpositionen, produktIdList);

        // Berechne den neuen Gesamtpreis basierend auf den aktualisierten Bestellpositionen
        BigDecimal neuerPreis = berechneGesamtpreis(bestehendeBestellpositionen);
        bestehendeBestellung.setPreis(neuerPreis);
        log.info("Neuer Preis für Bestellung {} berechnet: {}", bestehendeBestellung.getBestellnummer(), neuerPreis);

        log.info("Bearbeitung der Bestellung {} abgeschlossen", bestehendeBestellung.getBestellnummer());
    }

    /**
     * Erzeugt eine Liste von {@link Bestellposition} Objekten basierend auf den übergebenen Produkt-IDs und Mengen.
     * <p>
     * Diese Methode verwendet die Produkt-IDs, um die zugehörigen {@link Produkt} Entitäten aus dem
     * {@link ProduktRepository} abzurufen. Anschließend erstellt sie für jede Produkt-ID und Menge
     * eine {@link Bestellposition} und fügt sie der Liste der Bestellpositionen hinzu.
     * </p>
     *
     * @param produktIds die Liste von IDs der Produkte, die in die Bestellung aufgenommen werden sollen
     * @param mengen     die Liste der Mengen, die für die jeweiligen Produkte bestellt werden
     * @param bestellung die Bestellung, zu der die Bestellpositionen gehören
     * @return eine Liste von {@link Bestellposition} Objekten
     * @throws IllegalArgumentException wenn eine Produkt-ID ungültig ist
     */
    private List<Bestellposition> erzeugeBestellpositionen(List<Long> produktIds, List<Integer> mengen, Bestellung bestellung) {
        // Erstelle eine leere Liste, um die Bestellpositionen zu speichern
        List<Bestellposition> bestellpositionen = new ArrayList<>();

        // Iteriere über alle Produkt-IDs und Mengen
        for (int i = 0; i < produktIds.size(); i++) {
            // Hole die Produkt-ID und die entsprechende Menge aus den Listen
            Long produktId = produktIds.get(i);
            int menge = mengen.get(i);

            log.info("Verarbeite Produkt mit ID: {} und Menge: {}", produktId, menge);

            // Suche das Produkt anhand der ID in der Datenbank
            Produkt produkt = produktRepository.findById(produktId).orElseThrow(() -> new IllegalArgumentException("Ungültige Produkt-ID: " + produktId));

            // Überprüfe, ob genügend Bestand für die gewünschte Menge vorhanden ist
            if (menge > produkt.getBestand()) {
                log.error("Nicht genügend Bestand für Produkt ID: {}. Benötigte Menge: {}, verfügbarer Bestand: {}", produktId, menge, produkt.getBestand());
                throw new IllegalArgumentException("Die Menge überschreitet den verfügbaren Bestand des Produkts.");
            }

            // Erstelle eine neue Bestellposition mit den Informationen zu Bestellung, Produkt und Menge
            Bestellposition bestellposition = erstelleBestellposition(bestellung, produkt, menge);

            // Füge die erstellte Bestellposition zur Liste hinzu
            bestellpositionen.add(bestellposition);
        }

        // Gib die Liste der erstellten Bestellpositionen zurück
        return bestellpositionen;
    }

    /**
     * Erstellt eine neue Bestellposition für das angegebene Produkt und die Menge,
     * aktualisiert den Produktbestand und gibt die erstellte Bestellposition zurück
     *
     * @param bestellung die Bestellung, zu der die Bestellposition gehört
     * @param produkt    das Produkt für die Bestellposition
     * @param menge      die Menge des Produkts in der Bestellposition
     * @return die erstellte Bestellposition
     */
    private Bestellposition erstelleBestellposition(Bestellung bestellung, Produkt produkt, Integer menge) {
        // Reduziere den Bestand des Produkts um die bestellte Menge
        produkt.setBestand(produkt.getBestand() - menge);

        // Speichere das aktualisierte Produkt in der Datenbank
        produktRepository.save(produkt);
        log.info("Bestand für Produkt ID: {} aktualisiert. Neuer Bestand: {}", produkt.getId(), produkt.getBestand());

        // Erstelle eine neue Bestellposition
        Bestellposition bestellposition = new Bestellposition();

        // Setze die zugehörige Bestellung, das Produkt und die bestellte Menge in der Bestellposition
        bestellposition.setBestellung(bestellung);
        bestellposition.setProdukt(produkt);
        bestellposition.setMenge(menge);
        log.info("Bestellposition für Produkt ID: {} mit Menge: {} erstellt", produkt.getId(), menge);

        // Gib die erstellte Bestellposition zurück
        return bestellposition;
    }

    /**
     * Berechnet den Gesamtpreis der angegebenen {@link Bestellposition} Objekte.
     * <p>
     * Die Methode summiert die Preise aller Produkte in den Bestellpositionen, multipliziert mit der jeweiligen
     * Menge und gibt den Gesamtpreis als {@link BigDecimal} zurück.
     * </p>
     *
     * @param bestellpositionen die Liste der Bestellpositionen, deren Gesamtpreis berechnet werden soll
     * @return der Gesamtpreis als {@link BigDecimal}
     */
    private BigDecimal berechneGesamtpreis(List<Bestellposition> bestellpositionen) {
        return bestellpositionen.stream().map(bp -> bp.getProdukt().getPreis().multiply(BigDecimal.valueOf(bp.getMenge()))) // Preis der Bestellposition berechnen
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Alle Preise summieren
    }

    /**
     * Aktualisiert die Bestelldaten einer bestehenden Bestellung.
     *
     * @param bestehendeBestellung die zu aktualisierende Bestellung
     * @param bestellung           die neuen Bestelldaten
     */
    private void aktualisiereBestelldaten(Bestellung bestehendeBestellung, Bestellung bestellung) {
        bestehendeBestellung.setBeschreibung(bestellung.getBeschreibung());
        bestehendeBestellung.setBestelldatum(bestellung.getBestelldatum());
        bestehendeBestellung.setLieferstatus(bestellung.getLieferstatus());
        log.info("Bestelldaten aktualisiert für Bestellung: {}", bestehendeBestellung.getBestellnummer());
    }

    /**
     * Erstellt eine Map aus Bestellpositionen, wobei der Schlüssel die Produkt-ID ist.
     *
     * @param bestehendeBestellpositionen die Liste der Bestellpositionen
     * @return eine Map, wobei der Schlüssel die Produkt-ID und der Wert die Bestellposition ist
     */
    private Map<Long, Bestellposition> erstellePositionMap(List<Bestellposition> bestehendeBestellpositionen) {
        return bestehendeBestellpositionen.stream().collect(Collectors.toMap(bp -> bp.getProdukt().getId(), bp -> bp));
    }

    /**
     * Bearbeitet die Bestellpositionen einer Bestellung, indem bestehende Positionen aktualisiert
     * oder neue hinzugefügt werden und der Produktbestand entsprechend angepasst wird
     *
     * @param bestehendeBestellung        die zu bearbeitende Bestellung
     * @param bestehendeBestellpositionen die Liste der bestehenden Bestellpositionen
     * @param positionMap                 die Map der bestehenden Bestellpositionen, indexiert nach Produkt-ID
     * @param produktIdList               die Liste der Produkt-IDs
     * @param mengeList                   die Liste der Mengen für die jeweiligen Produkte
     */
    private void bearbeiteBestellpositionen(Bestellung bestehendeBestellung, List<Bestellposition> bestehendeBestellpositionen, Map<Long, Bestellposition> positionMap, List<Long> produktIdList, List<Integer> mengeList) {
        // Iteriere über alle Produkt-IDs und ihre Mengen in der aktualisierten Bestellung
        for (int i = 0; i < produktIdList.size(); i++) {
            Long produktId = produktIdList.get(i);        // Hole die Produkt-ID
            Integer menge = mengeList.get(i);             // Hole die gewünschte Menge

            log.info("Verarbeite Produkt mit ID: {} und Menge: {} bei Bestellbearbeitung", produktId, menge);

            // Lade das Produkt aus der Datenbank
            Produkt produkt = produktRepository.findById(produktId).orElseThrow(() -> new IllegalArgumentException("Ungültige Produkt-ID: " + produktId));

            // Überprüfe, ob genügend Bestand vorhanden ist
            if (menge > produkt.getBestand()) {
                log.error("Nicht genügend Bestand für Produkt ID: {}. Benötigte Menge: {}, verfügbarer Bestand: {}", produktId, menge, produkt.getBestand());
                throw new IllegalArgumentException("Die Menge überschreitet den verfügbaren Bestand des Produkts.");
            }

            // Suche nach einer bestehenden Bestellposition für dieses Produkt
            Bestellposition bestehendePosition = positionMap.get(produktId);

            if (bestehendePosition != null) {
                // Wenn eine bestehende Position gefunden wurde, aktualisiere sie
                aktualisiereBestellposition(bestehendePosition, menge, produkt);
            } else {
                // Ansonsten erstelle eine neue Bestellposition
                erstelleNeueBestellposition(bestehendeBestellung, bestehendeBestellpositionen, produkt, menge);
            }
        }
    }

    /**
     * Aktualisiert eine bestehende Bestellposition und passt den Produktbestand entsprechend an.
     *
     * @param bestehendePosition die zu aktualisierende Bestellposition
     * @param neueMenge          die neue Menge für die Bestellposition
     * @param produkt            das zugehörige Produkt
     */
    private void aktualisiereBestellposition(Bestellposition bestehendePosition, Integer neueMenge, Produkt produkt) {
        int bestandsaenderung = bestehendePosition.getMenge() - neueMenge;
        produkt.setBestand(produkt.getBestand() + bestandsaenderung);
        bestehendePosition.setMenge(neueMenge);
        produktRepository.save(produkt);
        log.info("Bestellposition für Produkt ID: {} auf Menge: {} aktualisiert. Bestandsaenderung: {}", produkt.getId(), neueMenge, bestandsaenderung);
    }

    /**
     * Erstellt eine neue Bestellposition und fügt sie der Bestellung hinzu. Passt den Produktbestand entsprechend an.
     *
     * @param bestehendeBestellung        die Bestellung, zu der die neue Position hinzugefügt wird
     * @param bestehendeBestellpositionen die Liste der bestehenden Bestellpositionen
     * @param produkt                     das Produkt für die neue Bestellposition
     * @param menge                       die Menge für die neue Bestellposition
     */
    private void erstelleNeueBestellposition(Bestellung bestehendeBestellung, List<Bestellposition> bestehendeBestellpositionen, Produkt produkt, Integer menge) {
        Bestellposition neueBestellposition = new Bestellposition();
        neueBestellposition.setBestellung(bestehendeBestellung);
        neueBestellposition.setProdukt(produkt);
        neueBestellposition.setMenge(menge);
        bestehendeBestellpositionen.add(neueBestellposition);
        produkt.setBestand(produkt.getBestand() - menge);
        produktRepository.save(produkt);
        log.info("Neue Bestellposition für Produkt ID: {} mit Menge: {} erstellt", produkt.getId(), menge);
    }

    /**
     * Entfernt Bestellpositionen, die nicht mehr in der aktualisierten Bestellung enthalten sind,
     * und setzt den Bestand der entsprechenden Produkte zurück.
     *
     * @param bestehendeBestellung        die zu bearbeitende Bestellung
     * @param bestehendeBestellpositionen die Liste der bestehenden Bestellpositionen
     * @param produktIdList               die Liste der Produkt-IDs in der aktualisierten Bestellung
     */
    private void entferneNichtBenoetigteBestellpositionen(Bestellung bestehendeBestellung, List<Bestellposition> bestehendeBestellpositionen, List<Long> produktIdList) {
        // Finde alle Bestellpositionen, deren Produkt-ID nicht in der aktualisierten Produktliste enthalten ist
        List<Bestellposition> zuEntfernen = bestehendeBestellpositionen.stream()
                .filter(bp -> !produktIdList.contains(bp.getProdukt().getId()))
                .collect(Collectors.toList());

        // Iteriere über die zu entfernenden Bestellpositionen
        for (Bestellposition bp : zuEntfernen) {
            // Hole das zugehörige Produkt
            Produkt produkt = bp.getProdukt();

            // Erhöhe den Produktbestand um die Menge der entfernten Bestellposition
            produkt.setBestand(produkt.getBestand() + bp.getMenge());

            // Speichere das aktualisierte Produkt in der Datenbank
            produktRepository.save(produkt);

            // Entferne die Bestellposition aus der Liste
            bestehendeBestellpositionen.remove(bp);

            log.info("Bestellposition für Produkt ID {} entfernt und Bestand zurückgesetzt", bp.getProdukt().getId());
        }
    }
}
