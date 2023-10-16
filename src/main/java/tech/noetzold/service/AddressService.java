package tech.noetzold.service;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tech.noetzold.model.AddressModel;
import tech.noetzold.model.ShippingModel;
import tech.noetzold.repository.AddressRepository;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class AddressService {

    @Inject
    AddressRepository addressRepository;

    @Transactional
    @CacheResult(cacheName = "address")
    public AddressModel findAddressModelById(UUID id){
        Optional<AddressModel> optionalAddressModel = addressRepository.findByIdOptional(id);
        return optionalAddressModel.orElse(new AddressModel());
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "address")
    public AddressModel saveAddressModel(AddressModel addressModel){
        addressRepository.persist(addressModel);
        addressRepository.flush();
        return addressModel;
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "address")
    public void updateAddressModel(AddressModel addressModel){
        if (addressModel == null || addressModel.getAddressId() == null) {
            throw new WebApplicationException("Invalid data for addressModel update", Response.Status.BAD_REQUEST);
        }

        AddressModel existingAddressModel = findAddressModelById(addressModel.getAddressId());
        if (existingAddressModel == null) {
            throw new WebApplicationException("addressModel not found", Response.Status.NOT_FOUND);
        }

        existingAddressModel.setAddress1(addressModel.getAddress1());
        existingAddressModel.setAddress2(addressModel.getAddress2());
        existingAddressModel.setCity(addressModel.getCity());
        existingAddressModel.setUf(addressModel.getUf());
        existingAddressModel.setCountry(addressModel.getCountry());
        existingAddressModel.setDeliveryDate(addressModel.getDeliveryDate());
        existingAddressModel.setFinalShippingCost(addressModel.getFinalShippingCost());
        existingAddressModel.setPhoneNumber(addressModel.getPhoneNumber());
        existingAddressModel.setRiskArea(addressModel.isRiskArea());
        existingAddressModel.setPostaCode(addressModel.getPostaCode());
        existingAddressModel.setFirstName(addressModel.getFirstName());
        existingAddressModel.setLastName(addressModel.getLastName());

        addressRepository.persist(existingAddressModel);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "address")
    public void deleteAddressModelById(UUID id){
        addressRepository.deleteById(id);
    }
}
