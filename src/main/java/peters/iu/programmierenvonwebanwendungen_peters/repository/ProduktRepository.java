package peters.iu.programmierenvonwebanwendungen_peters.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import peters.iu.programmierenvonwebanwendungen_peters.entity.Produkt;

@Repository
public interface ProduktRepository extends JpaRepository<Produkt, Long> {
}