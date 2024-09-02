package peters.iu.programmierenvonwebanwendungen_peters.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Grosskunde")
public class Grosskunde extends Kunde {
    public Grosskunde(Kundentyp kundentyp) {
        super(kundentyp);
    }

    public Grosskunde() {

    }
}
