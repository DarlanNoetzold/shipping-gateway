package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.AddressModel;

@ApplicationScoped
public class AddressRepository implements PanacheRepository<AddressModel> {
}
