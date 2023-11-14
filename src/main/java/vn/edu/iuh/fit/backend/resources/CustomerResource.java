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

    public CustomerResource() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(Customer customer) {
        customerService.insertCust(customer);
        return Response.ok(customer).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") long id, Customer customer) {
        Customer cus = customerService.findCustomer(id).get();
        if (cus == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        customerService.updateCust(customer);
        return Response.ok(customer).build();
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByID(@PathParam("id") long id) {
        Customer customer = customerService.findCustomer(id).get();
        if (customer == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(customer).build();
    }

    @GET
    @Path("/all")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Customer> list = customerService.getAll();
        return Response.ok(list).build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") long id) {
        customerService.deleteCust(id);
        return Response.ok(id).build();
    }
}