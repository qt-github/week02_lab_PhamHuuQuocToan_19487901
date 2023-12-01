package vn.edu.iuh.fit.backend.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import vn.edu.iuh.fit.backend.entities.Customer;
import vn.edu.iuh.fit.backend.services.CustomerService;

import java.util.List;

@Path("/customers")
public class CustomerResource {
    private final CustomerService customerService = new CustomerService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(Customer customer) {
        customerService.insertCust(customer);
        return okResponse(customer);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") long id, Customer customer) {
        Customer cus = customerService.findCustomer(id).orElse(null);
        if (cus == null)
            return notFoundResponse();
        customerService.updateCust(customer);
        return okResponse(customer);
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByID(@PathParam("id") long id) {
        Customer customer = customerService.findCustomer(id).orElse(null);
        if (customer == null)
            return notFoundResponse();
        return okResponse(customer);
    }

    @GET
    @Path("/all")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Customer> list = customerService.getAll();
        return okResponse(list);
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") long id) {
        boolean delete = customerService.deleteCust(id);
        if (!delete)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(id).build();
    }

    private Response okResponse(Object entity) {
        return Response.ok(entity).build();
    }

    private Response notFoundResponse() {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}