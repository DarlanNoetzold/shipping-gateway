package tech.noetzold.service;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tech.noetzold.model.ShippingModel;
import tech.noetzold.repository.ShippingRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ShippingService {

    @Inject
    ShippingRepository shippingRepository;

    @Transactional
    @CacheResult(cacheName = "shipping")
    public ShippingModel findShippingModelById(UUID id){
        Optional<ShippingModel> optionalShippingModel = shippingRepository.findByIdOptional(id);
        return optionalShippingModel.orElse(new ShippingModel());
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "shipping")
    public ShippingModel saveShippingModel(ShippingModel shippingModel){
        shippingRepository.persist(shippingModel);
        shippingRepository.flush();
        return shippingModel;
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "shipping")
    public void updateShippingModel(ShippingModel shippingModel){
        if (shippingModel == null || shippingModel.getShippingId() == null) {
            throw new WebApplicationException("Invalid data for shippingModel update", Response.Status.BAD_REQUEST);
        }

        ShippingModel existingShippingModel = findShippingModelById(shippingModel.getShippingId());
        if (existingShippingModel == null) {
            throw new WebApplicationException("shippingModel not found", Response.Status.NOT_FOUND);
        }

        existingShippingModel.setAddressModel(shippingModel.getAddressModel());
        existingShippingModel.setShippingMethod(shippingModel.getShippingMethod());
        existingShippingModel.setState(shippingModel.getState());
        existingShippingModel.setOrderId(shippingModel.getOrderId());
        existingShippingModel.setUserId(shippingModel.getUserId());
        existingShippingModel.setCarrierCode(shippingModel.getCarrierCode());
        existingShippingModel.setTrackingUrl(shippingModel.getTrackingUrl());

        shippingRepository.persist(existingShippingModel);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "shipping")
    public void deleteShippingModelById(UUID id){
        shippingRepository.deleteById(id);
    }

    public List<ShippingModel> findShippingModelByOrderId(String orderId) {
        return shippingRepository.findByOrderId(orderId);
    }
}
