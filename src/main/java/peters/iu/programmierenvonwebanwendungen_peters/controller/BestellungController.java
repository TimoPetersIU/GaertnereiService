package peters.iu.programmierenvonwebanwendungen_peters.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Bestellung;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Kunde;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Produkt;
import peters.iu.programmierenvonwebanwendungen_peters.repository.BestellungRepository;
import peters.iu.programmierenvonwebanwendungen_peters.repository.KundenRepository;
import peters.iu.programmierenvonwebanwendungen_peters.repository.ProduktRepository;
import peters.iu.programmierenvonwebanwendungen_peters.service.Bestellprozess;
import peters.iu.programmierenvonwebanwendungen_peters.service.BestellprozessFactory;

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
    public String neueBestellungErstellen(@PathVariable Long kundennummer, @ModelAttribute Bestellung bestellung, @RequestParam("bestellprozess") String bestellprozessTyp, @RequestParam("produktIds") List<Long> produktIds) {
        Kunde kunde = kundenRepository.findById(kundennummer).orElseThrow(() -> new IllegalArgumentException("Ungültige Kundennummer: " + kundennummer));
        bestellung.setKunde(kunde);

        // Produkte zur Bestellung hinzufügen
        List<Produkt> produkte = produktRepository.findAllById(produktIds);
        bestellung.setProdukte(produkte);

        // Bestellprozess auswählen und starten
        Bestellprozess bestellprozess = bestellprozessFactory.getBestellprozess(bestellprozessTyp);
        bestellprozess.bestellungVerarbeiten(bestellung);

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
        return "bestellungbearbeiten";
    }

    @PostMapping("/kunden/{kundennummer}/bestellungen/{bestellnummer}")
    public String bestellungBearbeiten(@PathVariable Long kundennummer, @PathVariable Long bestellnummer, @ModelAttribute Bestellung bestellung) {
        Kunde kunde = kundenRepository.findById(kundennummer).orElseThrow(() -> new IllegalArgumentException("Ungültige Kundennummer: " + kundennummer));
        Bestellung bestehendeBestellung = bestellungRepository.findById(bestellnummer).orElseThrow(() -> new IllegalArgumentException("Ungültige Bestellnummer: " + bestellnummer));

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