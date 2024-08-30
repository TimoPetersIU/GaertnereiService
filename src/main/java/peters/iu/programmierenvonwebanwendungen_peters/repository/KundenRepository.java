package peters.iu.programmierenvonwebanwendungen_peters.repository;

import peters.iu.programmierenvonwebanwendungen_peters.entity.Kunde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KundenRepository extends JpaRepository<Kunde, Long> {
    // Hier können Sie bei Bedarf benutzerdefinierte Abfragemethoden hinzufügen
}
