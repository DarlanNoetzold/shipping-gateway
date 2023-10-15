package tech.noetzold.consumer;

import io.smallrye.reactive.messaging.annotations.Blocking;
import io.smallrye.reactive.messaging.annotations.Merge;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.noetzold.model.AddressModel;
import tech.noetzold.model.ShippingModel;
import tech.noetzold.service.AddressService;
import tech.noetzold.service.ShippingService;

@ApplicationScoped
public class ShippingConsumer {

    @Inject
    ShippingService shippingService;

    @Inject
    AddressService addressService;

    private static final Logger logger = LoggerFactory.getLogger(ShippingConsumer.class);

    @Incoming("shippings")
    @Merge
    @Blocking
    public ShippingModel process(JsonObject incomingShippingModelInJson) {

        ShippingModel incomingShippingModel = incomingShippingModelInJson.mapTo(ShippingModel.class);

        AddressModel addressModel = addressService.findAddressModelById(incomingShippingModel.getAddressModel().getAddressId());

        if(addressModel == null){
            addressModel = addressService.saveAddressModel(incomingShippingModel.getAddressModel());
        }

        incomingShippingModel.setAddressModel(addressModel);
        shippingService.saveShippingModel(incomingShippingModel);
        logger.info("Create Shipping " + incomingShippingModel.getShippingId() + ".");

        return incomingShippingModel;
    }
}
