package vn.edu.iuh.fit.backend.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import vn.edu.iuh.fit.backend.dto.OrderDTO;
import vn.edu.iuh.fit.backend.entities.Order;
import vn.edu.iuh.fit.backend.entities.OrderDetail;
import vn.edu.iuh.fit.backend.services.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Path("/orders")
public class OrderResource {
    private final OrderService orderService = new OrderService();
    private final CustomerService customerService = new CustomerService();
    private final EmployeeService employeeService = new EmployeeService();
    private final ProductService productService = new ProductService();
    private final OrderDetailService orderDetailService = new OrderDetailService();

    @GET
    @Path("/all")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return okResponse(orderService.getAllOrders());
    }

    @GET
    @Path("/info")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByID() {
        if (orderService.getInfoOrder().isEmpty())
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(orderService.getInfoOrder()).build();
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderDto(@PathParam("id") long id) {
        return orderService.findOrder(id).map(this::okResponse).orElseGet(this::notFoundResponse);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(List<OrderDTO> orderDtos) {
        List<Order> orders = new ArrayList<>();
        for (OrderDTO orderDto : orderDtos) {
            Optional<Order> existingOrder = orderService.findOrderByCustomerAndEmployee(orderDto.getCusId(), orderDto.getEnoId());
            if (existingOrder.isEmpty()) {
                Order order = new Order();
                OrderDetail orderDetail = new OrderDetail();
                order.setOrderDate(orderDto.getOrderDate());
                order.setCustomer(customerService.findCustomer(orderDto.getCusId()).orElse(null));
                order.setEmployee(employeeService.findEmployee(orderDto.getEnoId()).orElse(null));
                orderDetail.setNote(orderDto.getNote());
                orderDetail.setPrice(orderDto.getPrice());
                orderDetail.setQuantity(orderDto.getQuantity());
                orderDetail.setProduct(productService.findProduct(orderDto.getProductId()).orElse(null));
                orderDetail.setOrder(order);
                orders.add(order);
                orderService.insertOrder(order, orderDetail);
            } else {
                OrderDetail orderDetail = orderDetailService.findOrderDetailByOrderAndProduct(existingOrder.get(), productService.findProduct(orderDto.getProductId()).orElse(null)).orElse(new OrderDetail());
                orderDetail.setNote(orderDto.getNote());
                orderDetail.setPrice(orderDto.getPrice());
                orderDetail.setQuantity(orderDto.getQuantity());
                orderDetail.setProduct(productService.findProduct(orderDto.getProductId()).orElse(null));
                orderDetail.setOrder(existingOrder.get());
                orderDetailService.insertOrderDetail(orderDetail);
            }
        }
        return okResponse(orders);
    }

    @GET
    @Path("/statisticsByDay")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderStatisticsByDay() {
        return okResponse(orderService.getOrderStatisticsByDate());
    }

    @GET
    @Path("/statisticsByEmployeeAndDateRange")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderStatisticsByEmployeeAndDateRange(
            @QueryParam("employeeId") Long employeeId,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate) {
        if (employeeId == null || startDate == null || endDate == null) {
            return badRequestResponse("Employee ID, start date, and end date are required.");
        }
        LocalDate startLocalDate = LocalDate.parse(startDate);
        LocalDate endLocalDate = LocalDate.parse(endDate);
        return okResponse(orderService.getOrderStatisticsByEmployeeAndDateRange(employeeId, startLocalDate, endLocalDate));
    }

    @GET
    @Path("/statisticsByDateRange")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderStatisticsByDateRange(
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate) {
        LocalDate startLocalDate = LocalDate.parse(startDate);
        LocalDate endLocalDate = LocalDate.parse(endDate);
        return okResponse(orderService.getOrderStatisticsByDateRange(startLocalDate, endLocalDate));
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") long id, Order order) {
        boolean isUpdated = orderService.updateOrder(order);
        return isUpdated ? okResponse(order) : notFoundResponse();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") long id) {
        return orderService.deleteOrders(id) ? okResponse(id) : notFoundResponse();
    }

    private Response okResponse(Object entity) {
        return Response.ok(entity).build();
    }

    private Response notFoundResponse() {
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    private Response badRequestResponse(String message) {
        return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
    }
}