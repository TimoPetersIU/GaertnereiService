package peters.iu.programmierenvonwebanwendungen_peters.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Neukunde")
public class Neukunde extends Kunde {
    public Neukunde(Kundentyp kundentyp) {
        super(kundentyp);
    }

    public Neukunde() {

    }
}
