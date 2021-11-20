package pl.mirekgab.beanvalidation;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author mirek
 */
public interface ClientRepository extends JpaRepository<Client, Long> {
    
}
