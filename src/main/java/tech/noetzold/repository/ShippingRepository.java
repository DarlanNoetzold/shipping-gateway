package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.ShippingModel;

@ApplicationScoped
public class ShippingRepository implements PanacheRepository<ShippingModel> {
}
