package vn.edu.iuh.fit.backend.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import vn.edu.iuh.fit.backend.entities.Employee;
import vn.edu.iuh.fit.backend.services.EmployeeService;

import java.util.List;
import java.util.Optional;

@Path("/employee")
public class EmployeeResource {
    private final EmployeeService employeeService = new EmployeeService();

    public EmployeeResource() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(Employee employee) {
        employeeService.insertEmployee(employee);
        return okResponse(employee);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") long id, Employee employee) {
        Optional<Employee> emp = employeeService.findEmployee(id);
        if (emp.isEmpty()) {
            return notFoundResponse();
        }
        boolean update = employeeService.updateEmployee(employee);
        if (!update) {
            return notFoundResponse();
        }
        return okResponse(employee);
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByID(@PathParam("id") long id) {
        Optional<Employee> optionalEmployee = employeeService.findEmployee(id);

        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            return okResponse(employee);
        } else {
            return notFoundResponse();
        }
    }

    @GET
    @Path("/all")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Employee> list = employeeService.getAllEmployee();
        return okResponse(list);
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") long id) {
        boolean delete = employeeService.deleteEmployee(id);
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