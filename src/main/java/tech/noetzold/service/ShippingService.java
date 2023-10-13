package tech.noetzold.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tech.noetzold.repository.ShippingRepository;

@ApplicationScoped
public class ShippingService {

    @Inject
    ShippingRepository shippingRepository;

}
