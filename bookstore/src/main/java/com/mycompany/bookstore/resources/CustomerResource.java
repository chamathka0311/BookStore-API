package com.mycompany.bookstore.resources;

import com.mycompany.bookstore.model.Customer;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    private static Map<Integer, Customer> customerDB = new HashMap<>();

    public static Map<Integer, Customer> getCustomerDB() {
        return customerDB;
    }

    @POST
    public Response addCustomer(Customer customer, @Context UriInfo uriInfo) {
        if (customer == null || !customer.isValid()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("error", "Invalid input"))
                    .build();
        }

        if (customerDB.containsKey(customer.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Collections.singletonMap("error", "Customer ID already exists"))
                    .build();
        }

        customerDB.put(customer.getId(), customer);

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(Integer.toString(customer.getId()));
        return Response.created(builder.build()).entity(customer).build();
    }

    @GET
    public Collection<Customer> getAllCustomers() {
        return customerDB.values();
    }

    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") int id) {
        Customer customer = customerDB.get(id);
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Collections.singletonMap("error", "Customer not found"))
                    .build();
        }
        return Response.ok(customer).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") int id, Customer updatedCustomer) {
        if (updatedCustomer == null || !updatedCustomer.isValid()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("error", "Invalid input"))
                    .build();
        }

        if (!customerDB.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Collections.singletonMap("error", "Customer not found"))
                    .build();
        }

        updatedCustomer.setId(id); // Keep same ID
        customerDB.put(id, updatedCustomer);
        return Response.ok(updatedCustomer).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        Customer removedCustomer = customerDB.remove(id);
        if (removedCustomer == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Collections.singletonMap("error", "Customer not found"))
                    .build();
        }
        return Response.noContent().build();
    }
}
