package tech.noetzold.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;
import tech.noetzold.model.AddressModel;
import tech.noetzold.service.AddressService;

import java.util.UUID;

@Path("/api/shipping/v1/address")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AddressController {
    @Inject
    AddressService addressService;

    @Channel("addresses-out")
    Emitter<AddressModel> quoteRequestEmitter;

    private static final Logger logger = Logger.getLogger(AddressController.class);

    @GET
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response getAddressModelByUserId(@PathParam("id") String id){

        AddressModel addressModel = addressService.findAddressModelById(UUID.fromString(id));

        if(addressModel == null){
            logger.error("There is no address with userId: " + id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(addressModel).build();
    }

    @POST
    @RolesAllowed("admin")
    public Response saveAddressModel(AddressModel addressModel){
        try {
            if (addressModel.getUserId() == null) {
                logger.error("Error to create Address withou userId: " + addressModel.getAddressId());
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            addressModel.setAddressId(null);
            quoteRequestEmitter.send(addressModel);
            logger.info("Create " + addressModel.getAddressId());
            return Response.status(Response.Status.CREATED).entity(addressModel).build();
        } catch (Exception e) {
            logger.error("Error to create addressModel: " + addressModel.getAddressId());
            e.printStackTrace();
        }
        logger.error("Error to create addressModel: " + addressModel.getAddressId());
        return Response.status(Response.Status.BAD_REQUEST).entity(addressModel).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response updateAddressModel(@PathParam("id") String id, AddressModel updatedAddressModel) {
        if (id.isBlank() || updatedAddressModel == null) {
            logger.warn("Error to update addressModel: " + id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        AddressModel existingAddressModel = addressService.findAddressModelById(UUID.fromString(id));
        if (existingAddressModel == null) {
            logger.warn("Error to update addressModel: " + id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        addressService.updateAddressModel(updatedAddressModel);

        return Response.ok(updatedAddressModel).build();
    }

    @DELETE
    @RolesAllowed("admin")
    public Response deleteAddressModel(@PathParam("id") String id){
        if (id.isBlank()) {
            logger.warn("Error to delete addressModel: " + id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        UUID uuid = UUID.fromString(id);

        addressService.deleteAddressModelById(uuid);

        return Response.ok().build();
    }
}
