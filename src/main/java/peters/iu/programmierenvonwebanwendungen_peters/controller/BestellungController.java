package peters.iu.programmierenvonwebanwendungen_peters.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Bestellposition;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Bestellung;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Kunde;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Produkt;
import peters.iu.programmierenvonwebanwendungen_peters.repository.BestellungRepository;
import peters.iu.programmierenvonwebanwendungen_peters.repository.KundenRepository;
import peters.iu.programmierenvonwebanwendungen_peters.repository.ProduktRepository;
import peters.iu.programmierenvonwebanwendungen_peters.service.Bestellprozess;
import peters.iu.programmierenvonwebanwendungen_peters.service.BestellprozessFactory;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BestellungController {

    @Autowired
    private BestellungRepository bestellungRepository;

    @Autowired
    private KundenRepository kundenRepository;

    @Autowired
    private ProduktRepository produktRepository;

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

        model.addAttribute("kunde", kunde);
        model.addAttribute("bestellung", new Bestellung());
        model.addAttribute("produkte", produktRepository.findAll());
        return "bestellungneu";
    }

    @PostMapping("/kunden/{kundennummer}/bestellungen")
    public String neueBestellungErstellen(@PathVariable Long kundennummer, @ModelAttribute Bestellung bestellung, @RequestParam("bestellprozess") String bestellprozessTyp, @RequestParam("produktIds") List<Long> produktIds, @RequestParam("mengen") List<Integer> mengen) {
        Kunde kunde = kundenRepository.findById(kundennummer).orElseThrow(() -> new IllegalArgumentException("Ungültige Kundennummer: " + kundennummer));
        bestellung.setKunde(kunde);

        // Bestellprozess auswählen und starten
        Bestellprozess bestellprozess = bestellprozessFactory.getBestellprozess(bestellprozessTyp);
        bestellprozess.bestellungVerarbeiten(bestellung);

        // Bestellpositionen erstellen und hinzufügen
        for (int i = 0; i < produktIds.size(); i++) {
            Long produktId = produktIds.get(i);
            int menge = mengen.get(i);

            Produkt produkt = produktRepository.findById(produktId).orElseThrow(() -> new IllegalArgumentException("Ungültige Produkt-ID: " + produktId));

            Bestellposition bestellposition = new Bestellposition();
            bestellposition.setBestellung(bestellung);
            bestellposition.setProdukt(produkt);
            bestellposition.setMenge(menge);

            bestellung.getBestellpositionen().add(bestellposition);
        }

        bestellungRepository.save(bestellung);
        return "redirect:/kunden/" + kundennummer + "/bestellungen";
    }

    // Bestellung bearbeiten
    @GetMapping("/kunden/{kundennummer}/bestellungen/{bestellnummer}/bearbeiten")
    public String bestellungBearbeitenFormular(@PathVariable Long kundennummer, @PathVariable Long bestellnummer, Model model) {
        Kunde kunde = kundenRepository.findById(kundennummer).orElseThrow(() -> new IllegalArgumentException("Ungültige Kundennummer: " + kundennummer));
        Bestellung bestellung = bestellungRepository.findById(bestellnummer).orElseThrow(() -> new IllegalArgumentException("Ungültige Bestellnummer: " + bestellnummer));

        // Überprüfen, ob die Bestellung zum Kunden gehört
        if (!bestellung.getKunde().equals(kunde)) {
            throw new IllegalArgumentException("Bestellung gehört nicht zu diesem Kunden");
        }

        model.addAttribute("kunde", kunde);
        model.addAttribute("bestellung", bestellung);
        model.addAttribute("produkte", produktRepository.findAll()); // Alle Produkte hinzufügen

        return "bestellungbearbeiten";
    }

    @PostMapping("/kunden/{kundennummer}/bestellungen/{bestellnummer}")
    public String bestellungBearbeiten(@PathVariable Long kundennummer,
                                       @PathVariable Long bestellnummer,
                                       @ModelAttribute Bestellung bestellung,
                                       @RequestParam("bestellprozess") String bestellprozessTyp,
                                       @RequestParam("produktIds") List<Long> produktIds,
                                       @RequestParam("mengen") List<Integer> mengen,
                                       @RequestParam(value = "neueProduktIds", required = false) List<Long> neueProduktIds,
                                       @RequestParam(value = "neueMengen", required = false) List<Integer> neueMengen) {

        Kunde kunde = kundenRepository.findById(kundennummer)
                .orElseThrow(() -> new IllegalArgumentException("Ungültige Kundennummer: " + kundennummer));
        Bestellung bestehendeBestellung = bestellungRepository.findById(bestellnummer)
                .orElseThrow(() -> new IllegalArgumentException("Ungültige Bestellnummer: " + bestellnummer));

        // Überprüfen, ob die Bestellung zum Kunden gehört
        if (!bestehendeBestellung.getKunde().equals(kunde)) {
            throw new IllegalArgumentException("Bestellung gehört nicht zu diesem Kunden");
        }

        // Aktualisieren Sie die Eigenschaften der bestehenden Bestellung mit den neuen Werten aus dem Formular
        bestehendeBestellung.setBeschreibung(bestellung.getBeschreibung());
        bestehendeBestellung.setPreis(bestellung.getPreis());
        bestehendeBestellung.setBestelldatum(bestellung.getBestelldatum());
        bestehendeBestellung.setLieferstatus(bestellung.getLieferstatus());
        bestehendeBestellung.setBestellprozessTyp(bestellprozessTyp);

        // Bestellpositionen aktualisieren oder neu erstellen
        List<Bestellposition> bestehendeBestellpositionen = bestehendeBestellung.getBestellpositionen();

        for (int i = 0; i < produktIds.size(); i++) {
            Long produktId = produktIds.get(i);
            int menge = mengen.get(i);

            Produkt produkt = produktRepository.findById(produktId)
                    .orElseThrow(() -> new IllegalArgumentException("Ungültige Produkt-ID: " + produktId));

            // Überprüfen, ob eine Bestellposition für dieses Produkt bereits existiert
            Bestellposition bestehendeBestellposition = bestehendeBestellpositionen.stream()
                    .filter(bp -> bp.getProdukt().getId().equals(produktId))
                    .findFirst()
                    .orElse(null);

            if (bestehendeBestellposition != null) {
                // Wenn ja, aktualisieren Sie die Menge
                bestehendeBestellposition.setMenge(menge);
            } else {
                // Wenn nein, erstellen Sie eine neue Bestellposition
                Bestellposition neueBestellposition = new Bestellposition();
                neueBestellposition.setBestellung(bestehendeBestellung);
                neueBestellposition.setProdukt(produkt);
                neueBestellposition.setMenge(menge);
                bestehendeBestellpositionen.add(neueBestellposition);
            }
        }

        // Neue Bestellpositionen hinzufügen (falls neue Produkte ausgewählt wurden)
        if (neueProduktIds != null && neueMengen != null) {
            for (int i = 0; i < neueProduktIds.size(); i++) {
                Long produktId = neueProduktIds.get(i);
                int menge = neueMengen.get(i);

                Produkt produkt = produktRepository.findById(produktId)
                        .orElseThrow(() -> new IllegalArgumentException("Ungültige Produkt-ID: " + produktId));

                Bestellposition neueBestellposition = new Bestellposition();
                neueBestellposition.setBestellung(bestehendeBestellung);
                neueBestellposition.setProdukt(produkt);
                neueBestellposition.setMenge(menge);
                bestehendeBestellpositionen.add(neueBestellposition);
            }
        }

        // Alte Bestellpositionen entfernen, die nicht mehr ausgewählt wurden
        // Das wird automatisch durch orphanRemoval = true in der @OneToMany-Annotation erledigt

        // Bestellprozess auswählen und starten (falls erforderlich)
        if (bestellprozessTyp != null && !bestellprozessTyp.isEmpty()) {
            Bestellprozess bestellprozess = bestellprozessFactory.getBestellprozess(bestellprozessTyp);
            bestellprozess.bestellungVerarbeiten(bestehendeBestellung);
        }

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