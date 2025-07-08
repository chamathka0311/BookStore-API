package com.mycompany.bookstore.resources;

import com.mycompany.bookstore.model.Book;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    private static Map<Integer, Book> books = new HashMap<>();
    private static int currentId = 1;

    public static Map<Integer, Book> getBookDB() {
        return books;
    }

    @GET
    public Collection<Book> getAllBooks() {
        return books.values();
    }

    @GET
    @Path("/{id}")
    public Response getBook(@PathParam("id") int id) {
        Book book = books.get(id);
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Collections.singletonMap("error", "Book not found"))
                    .build();
        }
        return Response.ok(book).build();
    }

    @POST
    public Response createBook(Book book, @Context UriInfo uriInfo) {
        if (book == null || !book.isValid()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("error", "Invalid input"))
                    .build();
        }

        book.setId(currentId++);
        books.put(book.getId(), book);

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(Integer.toString(book.getId()));
        return Response.created(builder.build()).entity(book).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") int id, Book updatedBook) {
        if (updatedBook == null || !updatedBook.isValid()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("error", "Invalid input"))
                    .build();
        }

        Book existingBook = books.get(id);
        if (existingBook == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Collections.singletonMap("error", "Book not found"))
                    .build();
        }

        updatedBook.setId(id);
        books.put(id, updatedBook);
        return Response.ok(updatedBook).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        Book removedBook = books.remove(id);
        if (removedBook == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Collections.singletonMap("error", "Book not found"))
                    .build();
        }
        return Response.noContent().build();
    }
}
