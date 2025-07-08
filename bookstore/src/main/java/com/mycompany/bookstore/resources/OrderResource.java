package com.mycompany.bookstore.resources;

import com.mycompany.bookstore.model.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.GET; 
import javax.ws.rs.Path; 
import javax.ws.rs.Produces; 
import javax.ws.rs.core.MediaType; 

import java.util.*;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    private static final Map<Integer, List<Order>> orderDB = new HashMap<>();
    private static int orderIdCounter = 1;

    @POST
    public Response placeOrder(@PathParam("customerId") int customerId) {
        List<CartItem> cartItems = CartResource.getCartDB().get(customerId);

        if (cartItems == null || cartItems.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Cart is empty").build();
        }

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        for (CartItem cartItem : cartItems) {
            Book book = cartItem.getBook();
            int qty = cartItem.getQuantity();

            // Reduce stock
            if (book.getStock() < qty) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Not enough stock for book: " + book.getTitle()).build();
            }
            book.setStock(book.getStock() - qty);

            orderItems.add(new OrderItem(book, qty));
            total += book.getPrice() * qty;
        }

        Order order = new Order(orderIdCounter++, customerId, orderItems, total);
        orderDB.putIfAbsent(customerId, new ArrayList<>());
        orderDB.get(customerId).add(order);

        // Clear the cart
        CartResource.getCartDB().remove(customerId);

        return Response.status(Response.Status.CREATED).entity(order).build();
    }

    @GET
    public Response getAllOrders(@PathParam("customerId") int customerId) {
        List<Order> orders = orderDB.getOrDefault(customerId, new ArrayList<>());
        return Response.ok(orders).build();
    }

    @GET
    @Path("/{orderId}")
    public Response getOrderById(@PathParam("customerId") int customerId, @PathParam("orderId") int orderId) {
        List<Order> orders = orderDB.get(customerId);
        if (orders != null) {
            for (Order order : orders) {
                if (order.getOrderId() == orderId) {
                    return Response.ok(order).build();
                }
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Order not found").build();
    }
}
