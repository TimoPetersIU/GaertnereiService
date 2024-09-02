package peters.iu.programmierenvonwebanwendungen_peters.service;

import org.springframework.stereotype.Component;
import peters.iu.programmierenvonwebanwendungen_peters.entity.*;

@Component
public class KundenFactory {

    public Kunde erstelleKunde(Kundentyp kundentyp) {
        int kundentypId = kundentyp.getTyp();

        switch (kundentypId) {
            case 1:
                return new Privatkunde(kundentyp);
            case 2:
                return new Geschaeftskunde(kundentyp);
            case 3:
                return new Grosskunde(kundentyp);
            case 4:
                return new Neukunde(kundentyp);
            case 5:
                return new Stammkunde(kundentyp);
            default:
                throw new IllegalArgumentException("Ungültiger Kundentyp: " + kundentypId);
        }
    }
}