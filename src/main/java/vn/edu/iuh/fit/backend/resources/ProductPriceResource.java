package vn.edu.iuh.fit.backend.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import vn.edu.iuh.fit.backend.entities.ProductPrice;
import vn.edu.iuh.fit.backend.services.ProductPriceService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Path("/productPrices")
public class ProductPriceResource {
    private final ProductPriceService productPriceService = new ProductPriceService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @GET
    @Path("/all")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<ProductPrice> list = productPriceService.getAllProductPrice();
        return okResponse(list);
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findProductPricesByProductId(@PathParam("id") long id) {
        return productPriceService.findProductPricesByProductId(id).map(this::okResponse).orElseGet(this::notFoundResponse);
    }

    @GET
    @Path("/last/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findLatestProductPrice(@PathParam("id") long id) {
        Double latestPrice = productPriceService.findLatestProductPrice(id);
        return latestPrice != null ? okResponse(latestPrice) : notFoundResponse();
    }

    @GET
    @Path("/{id}/{date}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByID(@PathParam("id") long id, @PathParam("date") String date) {
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        return productPriceService.findProductPrice(id, dateTime).map(this::okResponse).orElseGet(this::notFoundResponse);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(ProductPrice productPrice) {
        productPriceService.insertProductPrice(productPrice);
        return okResponse(productPrice);
    }

    @PUT
    @Path("/{id}/{date}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") long id, @PathParam("date") String date, ProductPrice productPrice) {
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        if (productPriceService.findProductPrice(id, dateTime).isEmpty())
            return notFoundResponse();
        boolean update = productPriceService.updateProductPrice(productPrice);
        return update ? okResponse(productPrice) : notFoundResponse();
    }

    @DELETE
    @Path("/{id}/{date}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") long id, @PathParam("date") String date) {
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        boolean delete = productPriceService.deleteProductPrice(id, dateTime);
        return delete ? okResponse(id) : notFoundResponse();
    }

    private Response okResponse(Object entity) {
        return Response.ok(entity).build();
    }

    private Response notFoundResponse() {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}