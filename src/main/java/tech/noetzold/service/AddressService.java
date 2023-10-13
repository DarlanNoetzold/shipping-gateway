package tech.noetzold.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tech.noetzold.repository.AddressRepository;

@ApplicationScoped
public class AddressService {

    @Inject
    AddressRepository addressRepository;
}
