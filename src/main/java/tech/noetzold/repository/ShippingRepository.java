package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.ShippingModel;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ShippingRepository implements PanacheRepository<ShippingModel> {

    public Optional<ShippingModel> findByIdOptional(UUID id) {
        return find("shippingId", id).firstResultOptional();
    }

    public void deleteById(UUID id) {
        delete("shippingId", id);
    }
}
