package peters.iu.programmierenvonwebanwendungen_peters.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Bestellposition;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Bestellung;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Kunde;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Produkt;
import peters.iu.programmierenvonwebanwendungen_peters.repository.BestellpositionRepository;
import peters.iu.programmierenvonwebanwendungen_peters.repository.BestellungRepository;
import peters.iu.programmierenvonwebanwendungen_peters.repository.KundenRepository;
import peters.iu.programmierenvonwebanwendungen_peters.repository.ProduktRepository;
import peters.iu.programmierenvonwebanwendungen_peters.service.Bestellprozess;
import peters.iu.programmierenvonwebanwendungen_peters.service.BestellprozessFactory;
import peters.iu.programmierenvonwebanwendungen_peters.service.BestellungService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class BestellungController {

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

    // Alle Bestellungen eines Kunden anzeigen
    @GetMapping("/kunden/{kundennummer}/bestellungen")
    public String alleBestellungenAnzeigen(@PathVariable Long kundennummer, Model model) {
        Kunde kunde = kundenRepository.findById(kundennummer).orElseThrow(() -> new IllegalArgumentException("Ungültige Kundennummer: " + kundennummer));
        model.addAttribute("kunde", kunde);
        model.addAttribute("bestellungen", kunde.getBestellungen());
        return "bestellungsliste";
    }

    // Formular für neue Bestellung eines Kunden anzeigen
    @GetMapping("/kunden/{kundennummer}/bestellungen/neu")
    public String neueBestellungFormular(@PathVariable Long kundennummer, Model model) {
        Kunde kunde = kundenRepository.findById(kundennummer).orElseThrow(() -> new IllegalArgumentException("Ungültige Kundennummer: " + kundennummer));

        LocalDate today = LocalDate.now();
        model.addAttribute("bestellung", new Bestellung());
        model.addAttribute("kunde", kunde);
        model.addAttribute("produkte", produktRepository.findAll());
        return "bestellungneu";
    }

    @PostMapping("/kunden/{kundennummer}/bestellungen")
    public String neueBestellungErstellen(@PathVariable Long kundennummer,
                                          @ModelAttribute Bestellung bestellung,
                                          @RequestParam("bestellprozess") String bestellprozessTyp,
                                          @RequestParam("produktIds") List<Long> produktIds,
                                          @RequestParam("mengen") List<Integer> mengen) {
        Kunde kunde = kundenRepository.findById(kundennummer)
                .orElseThrow(() -> new IllegalArgumentException("Ungültige Kundennummer: " + kundennummer));
        bestellung.setKunde(kunde);

        // Bestellprozess auswählen und starten
        Bestellprozess bestellprozess = bestellprozessFactory.getBestellprozess(bestellprozessTyp);
        bestellprozess.bestellungVerarbeiten(bestellung);

        // Erstellen der Bestellpositionen und Berechnen des Gesamtpreises
        List<Bestellposition> bestellpositionen = bestellungService.erzeugeBestellpositionen(produktIds, mengen, bestellung);
        bestellung.setBestellpositionen(bestellpositionen);

        // Berechne den Preis basierend auf den Bestellpositionen
        BigDecimal gesamtPreis = bestellungService.berechneGesamtpreis(bestellpositionen);
        bestellung.setPreis(gesamtPreis);

        bestellungRepository.save(bestellung);
        return "redirect:/kunden/" + kundennummer + "/bestellungen";
    }



    @GetMapping("/kunden/{kundennummer}/bestellungen/{bestellnummer}/bearbeiten")
    public String bestellungBearbeitenFormular(@PathVariable Long kundennummer,
                                               @PathVariable Long bestellnummer,
                                               Model model) {
        // Hole den Kunden und die Bestellung aus der Datenbank
        Kunde kunde = kundenRepository.findById(kundennummer)
                .orElseThrow(() -> new IllegalArgumentException("Ungültige Kundennummer: " + kundennummer));
        Bestellung bestellung = bestellungRepository.findById(bestellnummer)
                .orElseThrow(() -> new IllegalArgumentException("Ungültige Bestellnummer: " + bestellnummer));

        // Überprüfen, ob die Bestellung zum Kunden gehört
        if (!bestellung.getKunde().equals(kunde)) {
            throw new IllegalArgumentException("Bestellung gehört nicht zu diesem Kunden");
        }

        // Formatieren des Bestelldatums
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedBestelldatum = bestellung.getBestelldatum() != null
                ? bestellung.getBestelldatum().format(formatter)
                : ""; // Sicherstellen, dass der Wert nicht null ist

        // Finde alle Produkte, die nicht in den Bestellpositionen enthalten sind
        List<Bestellposition> bestehendeBestellpositionen = bestellung.getBestellpositionen();
        Set<Long> produktIdsInBestellpositionen = bestehendeBestellpositionen.stream()
                .map(bp -> bp.getProdukt().getId())
                .collect(Collectors.toSet());

        // Finde alle Produkte, die nicht in den Bestellpositionen enthalten sind
        List<Produkt> alleProdukte = produktRepository.findAll();
        List<Produkt> verfügbareProdukte = alleProdukte.stream()
                .filter(produkt -> !produktIdsInBestellpositionen.contains(produkt.getId()))
                .collect(Collectors.toList());

        // Hinzufügen der Attribute zum Modell
        model.addAttribute("kunde", kunde);
        model.addAttribute("bestellung", bestellung);
        model.addAttribute("produkte", verfügbareProdukte); // Nur verfügbare Produkte hinzufügen
        model.addAttribute("formattedBestelldatum", formattedBestelldatum); // Hinzufügen des formatierten Datums

        return "bestellungbearbeiten";
    }



    @PostMapping("/kunden/{kundennummer}/bestellungen/{bestellnummer}")
    public String bestellungBearbeiten(@PathVariable Long kundennummer,
                                       @PathVariable Long bestellnummer,
                                       @ModelAttribute Bestellung bestellung,
                                       @RequestParam(value = "produktIds", required = false) String produktIds,
                                       @RequestParam(value = "mengen", required = false) String mengen) {

        // Logge die Parameter für Debugging-Zwecke
        System.out.println("Produkt IDs: " + produktIds);
        System.out.println("Mengen: " + mengen);

        // Kunden- und Bestellungsabruf
        Kunde kunde = kundenRepository.findById(kundennummer)
                .orElseThrow(() -> new IllegalArgumentException("Ungültige Kundennummer: " + kundennummer));
        Bestellung bestehendeBestellung = bestellungRepository.findById(bestellnummer)
                .orElseThrow(() -> new IllegalArgumentException("Ungültige Bestellnummer: " + bestellnummer));

        if (!bestehendeBestellung.getKunde().equals(kunde)) {
            throw new IllegalArgumentException("Bestellung gehört nicht zu diesem Kunden");
        }

        // Aktualisieren Sie die Eigenschaften der bestehenden Bestellung
        bestehendeBestellung.setBeschreibung(bestellung.getBeschreibung());
        bestehendeBestellung.setBestelldatum(bestellung.getBestelldatum());
        bestehendeBestellung.setLieferstatus(bestellung.getLieferstatus());

        // Lade die bestehenden Bestellpositionen
        List<Bestellposition> bestehendeBestellpositionen = bestehendeBestellung.getBestellpositionen();

        // Konvertiere die übergebenen Strings in Listen
        List<Long> produktIdList = Arrays.stream(produktIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<Integer> mengeList = Arrays.stream(mengen.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        System.out.println("Konvertierte Produkt IDs: " + produktIdList);
        System.out.println("Konvertierte Mengen: " + mengeList);

        // Map für schnelle Abgleichung
        Map<Long, Bestellposition> positionMap = bestehendeBestellpositionen.stream()
                .collect(Collectors.toMap(bp -> bp.getProdukt().getId(), bp -> bp));

        System.out.println("Bestehende Bestellpositionen (Map): " + positionMap);

        // Verarbeite die neuen Bestellpositionen
        for (int i = 0; i < produktIdList.size(); i++) {
            Long produktId = produktIdList.get(i);
            Integer menge = mengeList.get(i);

            System.out.println("Verarbeite Produkt ID: " + produktId + " mit Menge: " + menge);

            Bestellposition bestehendePosition = positionMap.get(produktId);
            if (bestehendePosition != null) {
                // Bestehende Position aktualisieren
                System.out.println("Aktualisiere bestehende Position: " + bestehendePosition);
                bestehendePosition.setMenge(menge);
            } else {
                // Neue Position hinzufügen
                Produkt produkt = produktRepository.findById(produktId)
                        .orElseThrow(() -> new IllegalArgumentException("Ungültige Produkt-ID: " + produktId));

                System.out.println("Erstelle neue Bestellposition für Produkt ID: " + produktId);
                Bestellposition neueBestellposition = new Bestellposition();
                neueBestellposition.setBestellung(bestehendeBestellung);
                neueBestellposition.setProdukt(produkt);
                neueBestellposition.setMenge(menge);
                bestehendeBestellpositionen.add(neueBestellposition);
            }
        }

        // Entferne Bestellpositionen, die nicht mehr vorhanden sind
        List<Bestellposition> zuEntfernen = bestehendeBestellpositionen.stream()
                .filter(bp -> !produktIdList.contains(bp.getProdukt().getId()))
                .collect(Collectors.toList());

        System.out.println("Zu entfernende Bestellpositionen: " + zuEntfernen);

        bestehendeBestellpositionen.removeAll(zuEntfernen);

        // Berechnung des neuen Preises
        BigDecimal neuerPreis = bestehendeBestellpositionen.stream()
                .map(bp -> bp.getProdukt().getPreis().multiply(BigDecimal.valueOf(bp.getMenge())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Setze den neuen Preis
        bestehendeBestellung.setPreis(neuerPreis);

        // Speichern der aktualisierten Bestellung
        System.out.println("Speichere Bestellung: " + bestehendeBestellung);
        bestellungRepository.save(bestehendeBestellung);

        return "redirect:/kunden/" + kundennummer + "/bestellungen";
    }





    // Bestellung löschen
    @PostMapping("/kunden/{kundennummer}/bestellungen/{bestellnummer}/loeschen")
    public String bestellungLoeschen(@PathVariable Long kundennummer, @PathVariable Long bestellnummer) {
        Kunde kunde = kundenRepository.findById(kundennummer).orElseThrow(() -> new IllegalArgumentException("Ungültige Kundennummer: " + kundennummer));
        Bestellung bestellung = bestellungRepository.findById(bestellnummer).orElseThrow(() -> new IllegalArgumentException("Ungültige Bestellnummer: " + bestellnummer));

        // Überprüfen, ob die Bestellung zum Kunden gehört
        if (!bestellung.getKunde().equals(kunde)) {
            throw new IllegalArgumentException("Bestellung gehört nicht zu diesem Kunden");
        }

        bestellungRepository.delete(bestellung);
        return "redirect:/kunden/" + kundennummer + "/bestellungen";
    }

}