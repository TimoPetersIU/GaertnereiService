package peters.iu.programmierenvonwebanwendungen_peters.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import peters.iu.programmierenvonwebanwendungen_peters.repository.BestellungRepository;
import peters.iu.programmierenvonwebanwendungen_peters.repository.KundenRepository;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Bestellung;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Kunde;
import peters.iu.programmierenvonwebanwendungen_peters.service.AbholungBestellprozess;
import peters.iu.programmierenvonwebanwendungen_peters.service.Bestellprozess;
import peters.iu.programmierenvonwebanwendungen_peters.service.ExpressBestellprozess;
import peters.iu.programmierenvonwebanwendungen_peters.service.StandardBestellprozess;

@Controller
public class BestellungController {

    @Autowired
    private BestellungRepository bestellungRepository;

    @Autowired
    private KundenRepository kundenRepository;

    // Methoden für CRUD-Operationen und andere Funktionen
    @GetMapping("/kunden/{kundennummer}/bestellungen")
    public String alleBestellungenAnzeigen(@PathVariable Long kundennummer, Model model) {
        Kunde kunde = kundenRepository.findById(kundennummer)
                .orElseThrow(() -> new IllegalArgumentException("Ungültige Kundennummer: " + kundennummer));
        model.addAttribute("kunde", kunde);
        model.addAttribute("bestellungen", kunde.getBestellungen());
        return "bestellungsliste"; // Thymeleaf-Template zur Anzeige der Bestellungen eines Kunden
    }

    @GetMapping("/kunden/{kundennummer}/bestellungen/neu")
    public String neueBestellungFormular(@PathVariable Long kundennummer, Model model) {
        Kunde kunde = kundenRepository.findById(kundennummer)
                .orElseThrow(() -> new IllegalArgumentException("Ungültige Kundennummer: " + kundennummer));
        model.addAttribute("kunde", kunde);
        model.addAttribute("bestellung", new Bestellung());
        return "bestellungneu";
    }

    @PostMapping("/kunden/{kundennummer}/bestellungen")
    public String neueBestellungErstellen(@PathVariable Long kundennummer, @ModelAttribute Bestellung bestellung,  @RequestParam("bestellprozess") String bestellprozessTyp) {
        Kunde kunde = kundenRepository.findById(kundennummer)
                .orElseThrow(() -> new IllegalArgumentException("Ungültige Kundennummer: " + kundennummer));
        bestellung.setKunde(kunde);

        // Bestellprozess auswählen und starten
        Bestellprozess bestellprozess;
        if (bestellprozessTyp.equals("express")) {
            bestellprozess = new ExpressBestellprozess();
        } else if (bestellprozessTyp.equals("abholung")) {
            bestellprozess = new AbholungBestellprozess();
        } else {
            bestellprozess = new StandardBestellprozess();
        }
        bestellprozess.bestellungVerarbeiten(bestellung);

        bestellungRepository.save(bestellung);
        return "redirect:/kunden/" + kundennummer + "/bestellungen";
    }

    // Bestellung bearbeiten
    @GetMapping("/kunden/{kundennummer}/bestellungen/{bestellnummer}/bearbeiten")
    public String bestellungBearbeitenFormular(@PathVariable Long kundennummer, @PathVariable Long bestellnummer, Model model) {
        Kunde kunde = kundenRepository.findById(kundennummer)
                .orElseThrow(() -> new IllegalArgumentException("Ungültige Kundennummer: " + kundennummer));
        Bestellung bestellung = bestellungRepository.findById(bestellnummer)
                .orElseThrow(() -> new IllegalArgumentException("Ungültige Bestellnummer: " + bestellnummer));

        // Überprüfen, ob die Bestellung zum Kunden gehört
        if (!bestellung.getKunde().equals(kunde)) {
            throw new IllegalArgumentException("Bestellung gehört nicht zu diesem Kunden");
        }

        model.addAttribute("kunde", kunde);
        model.addAttribute("bestellung", bestellung);
        return "bestellungbearbeiten";
    }

    @PostMapping("/kunden/{kundennummer}/bestellungen/{bestellnummer}")
    public String bestellungBearbeiten(@PathVariable Long kundennummer, @PathVariable Long bestellnummer, @ModelAttribute Bestellung bestellung) {
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

        bestellungRepository.save(bestehendeBestellung);
        return "redirect:/kunden/" + kundennummer + "/bestellungen";
    }

    // Bestellung löschen
    @PostMapping("/kunden/{kundennummer}/bestellungen/{bestellnummer}/loeschen")
    public String bestellungLoeschen(@PathVariable Long kundennummer, @PathVariable Long bestellnummer) {
        Kunde kunde = kundenRepository.findById(kundennummer)
                .orElseThrow(() -> new IllegalArgumentException("Ungültige Kundennummer: " + kundennummer));
        Bestellung bestellung = bestellungRepository.findById(bestellnummer)
                .orElseThrow(() -> new IllegalArgumentException("Ungültige Bestellnummer: " + bestellnummer));

        // Überprüfen, ob die Bestellung zum Kunden gehört
        if (!bestellung.getKunde().equals(kunde)) {
            throw new IllegalArgumentException("Bestellung gehört nicht zu diesem Kunden");
        }

        bestellungRepository.delete(bestellung);
        return "redirect:/kunden/" + kundennummer + "/bestellungen";
    }

}