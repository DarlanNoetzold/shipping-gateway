package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.AddressModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class AddressRepository implements PanacheRepository<AddressModel> {

    public Optional<AddressModel> findByIdOptional(UUID id) {
        return find("addressId", id).firstResultOptional();
    }

    public void deleteById(UUID id) {
        delete("addressId", id);
    }

    public List<AddressModel> findByUserId(String userId) {
        return list("userId", userId);
    }
}
