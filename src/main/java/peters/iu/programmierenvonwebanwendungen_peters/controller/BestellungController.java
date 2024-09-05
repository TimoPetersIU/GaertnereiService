package peters.iu.programmierenvonwebanwendungen_peters.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import peters.iu.programmierenvonwebanwendungen_peters.entity.bestellung.Bestellposition;
import peters.iu.programmierenvonwebanwendungen_peters.entity.bestellung.Bestellung;
import peters.iu.programmierenvonwebanwendungen_peters.entity.kunde.Kunde;
import peters.iu.programmierenvonwebanwendungen_peters.entity.produkt.Produkt;
import peters.iu.programmierenvonwebanwendungen_peters.repository.BestellpositionRepository;
import peters.iu.programmierenvonwebanwendungen_peters.repository.BestellungRepository;
import peters.iu.programmierenvonwebanwendungen_peters.repository.KundenRepository;
import peters.iu.programmierenvonwebanwendungen_peters.repository.ProduktRepository;
import peters.iu.programmierenvonwebanwendungen_peters.service.bestellprozess.Bestellprozess;
import peters.iu.programmierenvonwebanwendungen_peters.service.bestellprozess.BestellprozessFactory;
import peters.iu.programmierenvonwebanwendungen_peters.service.BestellungService;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Timo Peters - IU Hamburg
 * Dieser Controller verwaltet die CRUD-Operationen für Bestellungen.
 */
@Controller
public class BestellungController {

    private static final Logger log = LoggerFactory.getLogger(BestellungController.class);

    @Autowired
    private BestellungRepository bestellungRepository;

    @Autowired
    private BestellungService bestellungService;

    @Autowired
    private KundenRepository kundenRepository;

    @Autowired
    private ProduktRepository produktRepository;

    @Autowired
    private BestellpositionRepository bestellpositionRepository;

    @Autowired
    private BestellprozessFactory bestellprozessFactory;

    /**
     * Zeigt alle Bestellungen eines bestimmten Kunden an.
     *
     * @param kundennummer die Kundennummer des Kunden
     * @param model        das Model, das an die View übergeben wird
     * @return der Name der View zur Anzeige der Bestellungen
     */
    @GetMapping("/kunden/{kundennummer}/bestellungen")
    public String alleBestellungenAnzeigen(@PathVariable Long kundennummer, Model model) {
        // Kunde anhand der Kundennummer finden
        Kunde kunde = kundenRepository.findById(kundennummer).orElseThrow(() -> new IllegalArgumentException("Ungültige Kundennummer: " + kundennummer));

        // Kunde und dessen Bestellungen an das Model übergeben
        model.addAttribute("kunde", kunde);
        model.addAttribute("bestellungen", kunde.getBestellungen());

        log.info("Alle Bestellungen für Kunde {} angezeigt", kundennummer);
        return "bestellungsliste";
    }

    /**
     * Zeigt das Formular zur Erstellung einer neuen Bestellung für einen bestimmten Kunden an.
     *
     * @param kundennummer die Kundennummer des Kunden
     * @param model        das Model, das an die View übergeben wird
     * @return der Name der View zum Erstellen einer neuen Bestellung
     */
    @GetMapping("/kunden/{kundennummer}/bestellungen/neu")
    public String neueBestellungFormular(@PathVariable Long kundennummer, Model model) {
        // Kunde anhand der Kundennummer finden
        Kunde kunde = kundenRepository.findById(kundennummer).orElseThrow(() -> new IllegalArgumentException("Ungültige Kundennummer: " + kundennummer));

        // Neues Bestellobjekt und Liste aller Produkte an das Model übergeben
        model.addAttribute("bestellung", new Bestellung());
        model.addAttribute("kunde", kunde);
        model.addAttribute("produkte", produktRepository.findAll());

        log.info("Formular für neue Bestellung für Kunde {} angezeigt", kundennummer);
        return "bestellungneu";
    }

    /**
     * Erstellt eine neue Bestellung für einen bestimmten Kunden.
     *
     * @param kundennummer      die Kundennummer des Kunden
     * @param bestellung        die Bestellung, die erstellt werden soll
     * @param bestellprozessTyp der Typ des Bestellprozesses
     * @param produktIds        die IDs der Produkte in der Bestellung
     * @param mengen            die Mengen der Produkte in der Bestellung
     * @return die Weiterleitung zur Liste der Bestellungen des Kunden
     */
    @PostMapping("/kunden/{kundennummer}/bestellungen")
    public String neueBestellungErstellen(@PathVariable Long kundennummer, @ModelAttribute Bestellung bestellung, @RequestParam("bestellprozess") String bestellprozessTyp, @RequestParam("produktIds") List<Long> produktIds, @RequestParam("mengen") List<Integer> mengen) {
        // Kunde anhand der Kundennummer finden
        Kunde kunde = kundenRepository.findById(kundennummer).orElseThrow(() -> new IllegalArgumentException("Ungültige Kundennummer: " + kundennummer));
        bestellung.setKunde(kunde);

        // Bestellprozess anhand des Typs erstellen und anwenden
        Bestellprozess bestellprozess = bestellprozessFactory.getBestellprozess(bestellprozessTyp);
        bestellprozess.bestellungVerarbeiten(bestellung);
        log.info("Bestellprozess '{}' angewendet auf Bestellung {}", bestellprozessTyp, bestellung.getBestellnummer());

        // Liste für Bestellpositionen vorbereiten
        List<Bestellposition> bestellpositionen = new ArrayList<>();

        // Überprüfen und Erstellen von Bestellpositionen
        for (int i = 0; i < produktIds.size(); i++) {
            Long produktId = produktIds.get(i);
            Integer menge = mengen.get(i);

            // Produkt anhand der Produkt-ID finden
            Produkt produkt = produktRepository.findById(produktId).orElseThrow(() -> new IllegalArgumentException("Ungültige Produkt-ID: " + produktId));

            // Überprüfen, ob die Menge den verfügbaren Bestand überschreitet
            if (menge > produkt.getBestand()) {
                log.error("Die Menge {} für Produkt ID {} überschreitet den verfügbaren Bestand {}", menge, produktId, produkt.getBestand());
                throw new IllegalArgumentException("Die Menge überschreitet den verfügbaren Bestand des Produkts.");
            }

            // Bestand des Produkts aktualisieren
            produkt.setBestand(produkt.getBestand() - menge);
            produktRepository.save(produkt);

            // Bestellposition erstellen und zur Liste hinzufügen
            Bestellposition bestellposition = new Bestellposition();
            bestellposition.setBestellung(bestellung);
            bestellposition.setProdukt(produkt);
            bestellposition.setMenge(menge);
            bestellpositionen.add(bestellposition);
            log.info("Bestellposition für Produkt ID {} mit Menge {} erstellt", produktId, menge);
        }

        // Bestellpositionen zur Bestellung hinzufügen
        bestellung.setBestellpositionen(bestellpositionen);

        // Gesamtpreis der Bestellung berechnen
        BigDecimal gesamtPreis = bestellungService.berechneGesamtpreis(bestellpositionen);
        bestellung.setPreis(gesamtPreis);
        log.info("Gesamtpreis für Bestellung {} berechnet: {}", bestellung.getBestellnummer(), gesamtPreis);

        // Bestellung speichern
        bestellungRepository.save(bestellung);
        log.info("Bestellung {} gespeichert", bestellung.getBestellnummer());
        return "redirect:/kunden/" + kundennummer + "/bestellungen";
    }

    /**
     * Zeigt das Formular zur Bearbeitung einer bestehenden Bestellung an.
     *
     * @param kundennummer  die Kundennummer des Kunden
     * @param bestellnummer die Bestellnummer der zu bearbeitenden Bestellung
     * @param model         das Model, das an die View übergeben wird
     * @return der Name der View zur Bearbeitung der Bestellung
     */
    @GetMapping("/kunden/{kundennummer}/bestellungen/{bestellnummer}/bearbeiten")
    public String bestellungBearbeitenFormular(@PathVariable Long kundennummer, @PathVariable Long bestellnummer, Model model) {
        // Kunde anhand der Kundennummer finden
        Kunde kunde = kundenRepository.findById(kundennummer).orElseThrow(() -> new IllegalArgumentException("Ungültige Kundennummer: " + kundennummer));

        // Bestellung anhand der Bestellnummer finden
        Bestellung bestellung = bestellungRepository.findById(bestellnummer).orElseThrow(() -> new IllegalArgumentException("Ungültige Bestellnummer: " + bestellnummer));

        // Überprüfen, ob die Bestellung zum richtigen Kunden gehört
        if (!bestellung.getKunde().equals(kunde)) {
            throw new IllegalArgumentException("Bestellung gehört nicht zu diesem Kunden");
        }

        // Formatierer für das Bestelldatum erstellen
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedBestelldatum = bestellung.getBestelldatum() != null ? bestellung.getBestelldatum().format(formatter) : "";

        // IDs der Produkte in den bestehenden Bestellpositionen sammeln
        List<Bestellposition> bestehendeBestellpositionen = bestellung.getBestellpositionen();
        Set<Long> produktIdsInBestellpositionen = bestehendeBestellpositionen.stream().map(bp -> bp.getProdukt().getId()).collect(Collectors.toSet());

        // Alle Produkte abrufen und verfügbare Produkte filtern
        List<Produkt> alleProdukte = produktRepository.findAll();
        List<Produkt> verfügbareProdukte = alleProdukte.stream().filter(produkt -> !produktIdsInBestellpositionen.contains(produkt.getId())).collect(Collectors.toList());

        // Kunde, Bestellung, verfügbare Produkte und formatiertes Bestelldatum an das Model übergeben
        model.addAttribute("kunde", kunde);
        model.addAttribute("bestellung", bestellung);
        model.addAttribute("produkte", verfügbareProdukte);
        model.addAttribute("formattedBestelldatum", formattedBestelldatum);

        log.info("Bestellung {} zum Bearbeiten für Kunde {} geladen", bestellnummer, kundennummer);
        return "bestellungbearbeiten";
    }

    /**
     * Bearbeitet eine bestehende Bestellung für einen bestimmten Kunden.
     *
     * @param kundennummer  die Kundennummer des Kunden
     * @param bestellnummer die Bestellnummer der zu bearbeitenden Bestellung
     * @param bestellung    die Bestellung, die aktualisiert werden soll
     * @param produktIds    die IDs der Produkte in der Bestellung
     * @param mengen        die Mengen der Produkte in der Bestellung
     * @return die Weiterleitung zur Liste der Bestellungen des Kunden
     */
    @PostMapping("/kunden/{kundennummer}/bestellungen/{bestellnummer}")
    public String bestellungBearbeiten(@PathVariable Long kundennummer, @PathVariable Long bestellnummer, @ModelAttribute Bestellung bestellung, @RequestParam(value = "produktIds", required = false) String produktIds, @RequestParam(value = "mengen", required = false) String mengen) {

        log.info("Bearbeitung der Bestellung {} für Kunde {}", bestellnummer, kundennummer);

        // Kunde und bestehende Bestellung anhand der IDs finden
        Kunde kunde = kundenRepository.findById(kundennummer).orElseThrow(() -> new IllegalArgumentException("Ungültige Kundennummer: " + kundennummer));
        Bestellung bestehendeBestellung = bestellungRepository.findById(bestellnummer).orElseThrow(() -> new IllegalArgumentException("Ungültige Bestellnummer: " + bestellnummer));

        // Überprüfen, ob die Bestellung zum richtigen Kunden gehört
        if (!bestehendeBestellung.getKunde().equals(kunde)) {
            throw new IllegalArgumentException("Bestellung gehört nicht zu diesem Kunden");
        }

        // Bestehende Bestellung mit neuen Werten aktualisieren
        bestehendeBestellung.setBeschreibung(bestellung.getBeschreibung());
        bestehendeBestellung.setBestelldatum(bestellung.getBestelldatum());
        bestehendeBestellung.setLieferstatus(bestellung.getLieferstatus());

        // Vorhandene Bestellpositionen in eine Map umwandeln
        List<Bestellposition> bestehendeBestellpositionen = bestehendeBestellung.getBestellpositionen();
        Map<Long, Bestellposition> positionMap = bestehendeBestellpositionen.stream().collect(Collectors.toMap(bp -> bp.getProdukt().getId(), bp -> bp));

        // Produkt-IDs und Mengen aus den Request-Parametern einlesen
        List<Long> produktIdList = Arrays.stream(produktIds.split(",")).map(Long::parseLong).collect(Collectors.toList());
        List<Integer> mengeList = Arrays.stream(mengen.split(",")).map(Integer::parseInt).collect(Collectors.toList());

        log.info("Produkt IDs: {}", produktIdList);
        log.info("Mengen: {}", mengeList);

        // Überprüfen und Aktualisieren oder Hinzufügen von Bestellpositionen
        for (int i = 0; i < produktIdList.size(); i++) {
            Long produktId = produktIdList.get(i);
            Integer menge = mengeList.get(i);

            Produkt produkt = produktRepository.findById(produktId).orElseThrow(() -> new IllegalArgumentException("Ungültige Produkt-ID: " + produktId));

            // Überprüfen, ob die Menge den verfügbaren Bestand überschreitet
            if (menge > produkt.getBestand()) {
                log.error("Die Menge {} für Produkt ID {} überschreitet den verfügbaren Bestand {}", menge, produktId, produkt.getBestand());
                throw new IllegalArgumentException("Die Menge überschreitet den verfügbaren Bestand des Produkts.");
            }

            Bestellposition bestehendePosition = positionMap.get(produktId);
            if (bestehendePosition != null) {
                // Bestehende Position aktualisieren
                produkt.setBestand(produkt.getBestand() + bestehendePosition.getMenge() - menge);
                bestehendePosition.setMenge(menge);
                log.info("Bestehende Position für Produkt ID {} aktualisiert auf Menge {}", produktId, menge);
            } else {
                // Neue Position erstellen
                Bestellposition neueBestellposition = new Bestellposition();
                neueBestellposition.setBestellung(bestehendeBestellung);
                neueBestellposition.setProdukt(produkt);
                neueBestellposition.setMenge(menge);
                bestehendeBestellpositionen.add(neueBestellposition);
                produkt.setBestand(produkt.getBestand() - menge);
                log.info("Neue Bestellposition für Produkt ID {} mit Menge {} erstellt", produktId, menge);
            }
            produktRepository.save(produkt); // Produkt mit aktualisiertem Bestand speichern
        }

        // Bestellpositionen entfernen, die nicht mehr enthalten sind
        List<Bestellposition> zuEntfernen = bestehendeBestellpositionen.stream().filter(bp -> !produktIdList.contains(bp.getProdukt().getId())).collect(Collectors.toList());

        for (Bestellposition bp : zuEntfernen) {
            // Bestand zurücksetzen, wenn Position entfernt wird
            Produkt produkt = bp.getProdukt();
            produkt.setBestand(produkt.getBestand() + bp.getMenge());
            produktRepository.save(produkt);
            bestehendeBestellpositionen.remove(bp);
            log.info("Bestellposition für Produkt ID {} entfernt und Bestand zurückgesetzt", bp.getProdukt().getId());
        }

        // Neuen Preis der Bestellung berechnen
        BigDecimal neuerPreis = bestehendeBestellpositionen.stream().map(bp -> bp.getProdukt().getPreis().multiply(BigDecimal.valueOf(bp.getMenge()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        bestehendeBestellung.setPreis(neuerPreis);
        log.info("Neuer Preis für Bestellung {} berechnet: {}", bestellnummer, neuerPreis);

        // Aktualisierte Bestellung speichern
        bestellungRepository.save(bestehendeBestellung);
        log.info("Bestellung {} aktualisiert und gespeichert", bestellnummer);

        return "redirect:/kunden/" + kundennummer + "/bestellungen";
    }


    /**
     * Löscht eine bestehende Bestellung für einen bestimmten Kunden.
     *
     * @param kundennummer  die Kundennummer des Kunden
     * @param bestellnummer die Bestellnummer der zu löschenden Bestellung
     * @return die Weiterleitung zur Liste der Bestellungen des Kunden
     */
    @PostMapping("/kunden/{kundennummer}/bestellungen/{bestellnummer}/loeschen")
    public String bestellungLoeschen(@PathVariable Long kundennummer, @PathVariable Long bestellnummer) {
        // Kunde und Bestellung anhand der IDs finden
        Kunde kunde = kundenRepository.findById(kundennummer).orElseThrow(() -> new IllegalArgumentException("Ungültige Kundennummer: " + kundennummer));
        Bestellung bestellung = bestellungRepository.findById(bestellnummer).orElseThrow(() -> new IllegalArgumentException("Ungültige Bestellnummer: " + bestellnummer));

        // Überprüfen, ob die Bestellung zum richtigen Kunden gehört
        if (!bestellung.getKunde().equals(kunde)) {
            throw new IllegalArgumentException("Bestellung gehört nicht zu diesem Kunden");
        }

        // Bestellung löschen
        bestellungRepository.delete(bestellung);
        log.info("Bestellung {} gelöscht", bestellnummer);

        return "redirect:/kunden/" + kundennummer + "/bestellungen";
    }
}
