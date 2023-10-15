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
import tech.noetzold.service.AddressService;

@ApplicationScoped
public class AddressConsumer {

    @Inject
    AddressService shippingService;

    private static final Logger logger = LoggerFactory.getLogger(AddressConsumer.class);

    @Incoming("addresses")
    @Merge
    @Blocking
    public AddressModel process(JsonObject incomingAddressModelInJson) {

        AddressModel incomingAddressModel = incomingAddressModelInJson.mapTo(AddressModel.class);

        shippingService.saveAddressModel(incomingAddressModel);
        logger.info("Create Address " + incomingAddressModel.getAddressId() + ".");

        return incomingAddressModel;
    }
}
