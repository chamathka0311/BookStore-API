package com.mycompany.bookstore.resources;

import com.mycompany.bookstore.model.Book;
import com.mycompany.bookstore.model.CartItem;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.GET; 
import javax.ws.rs.Path; 
import javax.ws.rs.Produces; 
import javax.ws.rs.core.MediaType; 

import java.util.*;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {

    // Key: customerId, Value: List of CartItems
    private static final Map<Integer, List<CartItem>> cartDB = new HashMap<>();

    @POST
    @Path("/items")
    public Response addItemToCart(@PathParam("customerId") int customerId, CartItem newItem) {
        Book book = BookResource.getBookDB().get(newItem.getBook().getId());
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Book not found").build();
        }

        cartDB.putIfAbsent(customerId, new ArrayList<>());
        List<CartItem> cart = cartDB.get(customerId);

        // Check if book already in cart → update quantity
        for (CartItem item : cart) {
            if (item.getBook().getId() == book.getId()) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                return Response.ok(item).build();
            }
        }

        // Add new book
        cart.add(new CartItem(book, newItem.getQuantity()));
        return Response.status(Response.Status.CREATED).entity(newItem).build();
    }

    @GET
    public Response viewCart(@PathParam("customerId") int customerId) {
        List<CartItem> cart = cartDB.getOrDefault(customerId, new ArrayList<>());
        return Response.ok(cart).build();
    }

    @PUT
    @Path("/items/{bookId}")
    public Response updateCartItem(
            @PathParam("customerId") int customerId,
            @PathParam("bookId") int bookId,
            CartItem updatedItem) {

        List<CartItem> cart = cartDB.get(customerId);
        if (cart == null) return Response.status(Response.Status.NOT_FOUND).entity("Cart not found").build();

        for (CartItem item : cart) {
            if (item.getBook().getId() == bookId) {
                item.setQuantity(updatedItem.getQuantity());
                return Response.ok(item).build();
            }
        }

        return Response.status(Response.Status.NOT_FOUND).entity("Book not in cart").build();
    }

    @DELETE
    @Path("/items/{bookId}")
    public Response removeItem(
            @PathParam("customerId") int customerId,
            @PathParam("bookId") int bookId) {

        List<CartItem> cart = cartDB.get(customerId);
        if (cart == null) return Response.status(Response.Status.NOT_FOUND).build();

        boolean removed = cart.removeIf(item -> item.getBook().getId() == bookId);
        if (!removed) return Response.status(Response.Status.NOT_FOUND).entity("Book not in cart").build();

        return Response.noContent().build();
    }

    // Optional: expose cart map if needed by OrderResource
    public static Map<Integer, List<CartItem>> getCartDB() {
        return cartDB;
    }
}
