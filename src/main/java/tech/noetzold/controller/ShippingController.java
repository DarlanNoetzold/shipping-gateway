package tech.noetzold.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;
import tech.noetzold.model.ShippingModel;
import tech.noetzold.service.ShippingService;

import java.util.UUID;

@Path("/api/shipping/v1/shipping")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShippingController {

    @Inject
    ShippingService addressService;

    @Channel("shippings")
    Emitter<ShippingModel> quoteRequestEmitter;

    private static final Logger logger = Logger.getLogger(ShippingController.class);

    @GET
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response getShippingModelByUserId(@PathParam("id") String id){

        ShippingModel addressModel = addressService.findShippingModelById(UUID.fromString(id));

        if(addressModel == null){
            logger.error("There is no address with userId: " + id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(addressModel).build();
    }

    @POST
    @RolesAllowed("admin")
    public Response saveShippingModel(ShippingModel addressModel){
        try {
            if (addressModel.getUserId() == null) {
                logger.error("Error to create Shipping withou userId: " + addressModel.getShippingId());
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            addressModel.setShippingId(null);
            quoteRequestEmitter.send(addressModel);
            logger.info("Create " + addressModel.getShippingId());
            return Response.status(Response.Status.CREATED).entity(addressModel).build();
        } catch (Exception e) {
            logger.error("Error to create addressModel: " + addressModel.getShippingId());
            e.printStackTrace();
        }
        logger.error("Error to create addressModel: " + addressModel.getShippingId());
        return Response.status(Response.Status.BAD_REQUEST).entity(addressModel).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response updateShippingModel(@PathParam("id") String id, ShippingModel updatedShippingModel) {
        if (id.isBlank() || updatedShippingModel == null) {
            logger.warn("Error to update addressModel: " + id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        ShippingModel existingShippingModel = addressService.findShippingModelById(UUID.fromString(id));
        if (existingShippingModel == null) {
            logger.warn("Error to update addressModel: " + id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        addressService.updateShippingModel(updatedShippingModel);

        return Response.ok(updatedShippingModel).build();
    }

    @DELETE
    @RolesAllowed("admin")
    public Response deleteShippingModel(@PathParam("id") String id){
        if (id.isBlank()) {
            logger.warn("Error to delete addressModel: " + id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        UUID uuid = UUID.fromString(id);

        addressService.deleteShippingModelById(uuid);

        return Response.ok().build();
    }
}
