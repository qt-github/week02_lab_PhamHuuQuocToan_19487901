package vn.edu.iuh.fit.backend.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import vn.edu.iuh.fit.backend.entities.ProductImage;
import vn.edu.iuh.fit.backend.services.ProductImageService;

import java.util.List;

@Path("/productImages")
public class ProductImageResource {
    private final ProductImageService productImageService = new ProductImageService();

    @GET
    @Path("/all")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<ProductImage> list = productImageService.getAllProductImages();
        return okResponse(list);
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByID(@PathParam("id") long id) {
        return productImageService.findProductImage(id).map(this::okResponse).orElseGet(this::notFoundResponse);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(ProductImage productImage) {
        productImageService.insertProductImage(productImage);
        return okResponse(productImage);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") long id, ProductImage productImage) {
        if (productImageService.findProductImage(id).isEmpty())
            return notFoundResponse();
        boolean update = productImageService.updateProductImage(productImage);
        if (!update)
            return notFoundResponse();
        return okResponse(productImage);
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") long id) {
        boolean delete = productImageService.deleteProductImage(id);
        if (!delete)
            return notFoundResponse();
        return okResponse(id);
    }

    private Response okResponse(Object entity) {
        return Response.ok(entity).build();
    }

    private Response notFoundResponse() {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}