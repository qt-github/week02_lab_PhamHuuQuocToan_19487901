package vn.edu.iuh.fit.backend.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import vn.edu.iuh.fit.backend.dto.ProductAndImgDTO;
import vn.edu.iuh.fit.backend.dto.ProductInfoDTO;
import vn.edu.iuh.fit.backend.entities.Product;
import vn.edu.iuh.fit.backend.entities.ProductImage;
import vn.edu.iuh.fit.backend.entities.ProductPrice;
import vn.edu.iuh.fit.backend.services.ProductService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Path("/products")
public class ProductResource {
    private final ProductService productService = new ProductService();

    @GET
    @Path("/all")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Product> list = productService.getAllProduct();
        return okResponse(list);
    }

    @GET
    @Path("/active")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActive() {
        List<ProductInfoDTO> list = productService.getActiveProductInfo();
        return okResponse(list);
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByID(@PathParam("id") long id) {
        return productService.findProduct(id).map(this::okResponse).orElseGet(this::notFoundResponse);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(ProductAndImgDTO dto) {
        try {
            System.out.println(dto);
            Product product = new Product();
            ProductImage productImage = new ProductImage();
            ProductPrice productPrice = new ProductPrice();
            product.setName(dto.getProductName());
            product.setDescription(dto.getDescription());
            product.setManufacturer(dto.getManufacturer());
            product.setUnit(dto.getUnit());
            product.setStatus(dto.getStatus());
            productImage.setProduct(product);
            productImage.setPath(dto.getPath());
            productImage.setAlternative(dto.getAlternative());
            productPrice.setProduct(product);
            productPrice.setPrice(dto.getPrice());
            productPrice.setNote("gia dau tien");
            LocalDateTime ldt = LocalDateTime.now();
            productPrice.setPrice_date_time(ldt);
            boolean success = productService.insertProduct(product, productImage,productPrice);
            if (success) {
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") long id, ProductAndImgDTO dto) {
        try {
            Optional<Product> optionalProduct = productService.findProduct(id);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                List<ProductImage> productImages = product.getProductImageList();
                if (!productImages.isEmpty()) {
                    ProductImage productImage = productImages.get(0);
                    productImage.setPath(dto.getPath());
                    productImage.setAlternative(dto.getAlternative());
                }
                product.setName(dto.getProductName());
                product.setDescription(dto.getDescription());
                product.setManufacturer(dto.getManufacturer());
                product.setUnit(dto.getUnit());
                product.setStatus(dto.getStatus());

                boolean success = productService.updateProduct(product,productImages.get(0));

                if (success) {
                    return Response.ok().build();
                } else {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                }
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") long id) {
        boolean isDeleted = productService.deleteProduct(id);
        return isDeleted ? okResponse(id) : notFoundResponse();
    }

    private Response okResponse(Object entity) {
        return Response.ok(entity).build();
    }

    private Response notFoundResponse() {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}