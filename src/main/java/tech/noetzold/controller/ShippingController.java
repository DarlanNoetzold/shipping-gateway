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

import java.util.List;
import java.util.UUID;

@Path("/api/shipping/v1/shipping")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShippingController {

    @Inject
    ShippingService shippingService;

    @Channel("shippings-out")
    Emitter<ShippingModel> quoteRequestEmitter;

    private static final Logger logger = Logger.getLogger(ShippingController.class);

    @GET
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response getShippingModelById(@PathParam("id") String id){

        ShippingModel shippingModel = shippingService.findShippingModelById(UUID.fromString(id));

        if(shippingModel.getShippingId() == null){
            logger.error("There is no shipping with Id: " + id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(shippingModel).build();
    }

    @GET
    @Path("/order/{orderId}")
    @RolesAllowed("admin")
    public Response getShippingModelByOrderId(@PathParam("orderId") String orderId){

        List<ShippingModel> shippingModel = shippingService.findShippingModelByOrderId(orderId);

        if(shippingModel.isEmpty()){
            logger.error("There is no shipping with orderId: " + orderId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(shippingModel).build();
    }

    @POST
    @RolesAllowed("admin")
    public Response saveShippingModel(ShippingModel shippingModel){
        try {
            if (shippingModel.getUserId() == null) {
                logger.error("Error to create Shipping withou userId: " + shippingModel.getShippingId());
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            shippingModel.setShippingId(null);
            quoteRequestEmitter.send(shippingModel);
            logger.info("Create " + shippingModel.getShippingId());
            return Response.status(Response.Status.CREATED).entity(shippingModel).build();
        } catch (Exception e) {
            logger.error("Error to create shippingModel: " + shippingModel.getShippingId());
            e.printStackTrace();
        }
        logger.error("Error to create shippingModel: " + shippingModel.getShippingId());
        return Response.status(Response.Status.BAD_REQUEST).entity(shippingModel).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response updateShippingModel(@PathParam("id") String id, ShippingModel updatedShippingModel) {
        if (id.isBlank() || updatedShippingModel == null) {
            logger.warn("Error to update shippingModel: " + id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        ShippingModel existingShippingModel = shippingService.findShippingModelById(UUID.fromString(id));
        if (existingShippingModel.getShippingId() == null) {
            logger.warn("Error to update shippingModel: " + id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        updatedShippingModel.setShippingId(existingShippingModel.getShippingId());

        shippingService.updateShippingModel(updatedShippingModel);

        return Response.ok(updatedShippingModel).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response deleteShippingModel(@PathParam("id") String id){
        if (id.isBlank()) {
            logger.warn("Error to delete shippingModel: " + id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        UUID uuid = UUID.fromString(id);

        shippingService.deleteShippingModelById(uuid);

        return Response.ok().build();
    }
}
