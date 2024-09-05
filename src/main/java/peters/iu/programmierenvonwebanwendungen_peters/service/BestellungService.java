package peters.iu.programmierenvonwebanwendungen_peters.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Bestellposition;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Bestellung;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Produkt;
import peters.iu.programmierenvonwebanwendungen_peters.repository.ProduktRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class BestellungService {

    @Autowired
    private ProduktRepository produktRepository;

    public List<Bestellposition> erzeugeBestellpositionen(List<Long> produktIds, List<Integer> mengen, Bestellung bestellung) {
        List<Bestellposition> bestellpositionen = new ArrayList<>();
        for (int i = 0; i < produktIds.size(); i++) {
            Long produktId = produktIds.get(i);
            int menge = mengen.get(i);

            Produkt produkt = produktRepository.findById(produktId)
                    .orElseThrow(() -> new IllegalArgumentException("Ungültige Produkt-ID: " + produktId));

            Bestellposition bestellposition = new Bestellposition();
            bestellposition.setBestellung(bestellung);
            bestellposition.setProdukt(produkt);
            bestellposition.setMenge(menge);

            bestellpositionen.add(bestellposition);
        }
        return bestellpositionen;
    }

    public BigDecimal berechneGesamtpreis(List<Bestellposition> bestellpositionen) {
        return bestellpositionen.stream()
                .map(bp -> bp.getProdukt().getPreis().multiply(BigDecimal.valueOf(bp.getMenge())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
