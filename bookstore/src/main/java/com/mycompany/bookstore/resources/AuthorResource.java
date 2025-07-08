package com.mycompany.bookstore.resources;

import com.mycompany.bookstore.model.Author;
import com.mycompany.bookstore.model.Book;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {

    private static Map<Integer, Author> authorDB = new HashMap<>();

    public static Map<Integer, Author> getAuthorDB() {
        return authorDB;
    }

    @POST
    public Response addAuthor(Author author, @Context UriInfo uriInfo) {
        if (author == null || !author.isValid()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("error", "Invalid input"))
                    .build();
        }

        if (authorDB.containsKey(author.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Collections.singletonMap("error", "Author ID already exists"))
                    .build();
        }

        authorDB.put(author.getId(), author);

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(Integer.toString(author.getId()));
        return Response.created(builder.build()).entity(author).build();
    }

    @GET
    public Collection<Author> getAllAuthors() {
        return authorDB.values();
    }

    @GET
    @Path("/{id}")
    public Response getAuthorById(@PathParam("id") int id) {
        Author author = authorDB.get(id);
        if (author == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Collections.singletonMap("error", "Author not found"))
                    .build();
        }
        return Response.ok(author).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") int id, Author updatedAuthor) {
        if (updatedAuthor == null || !updatedAuthor.isValid()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("error", "Invalid input"))
                    .build();
        }

        Author existingAuthor = authorDB.get(id);
        if (existingAuthor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Collections.singletonMap("error", "Author not found"))
                    .build();
        }

        updatedAuthor.setId(id); // Force ID to stay the same
        authorDB.put(id, updatedAuthor);
        return Response.ok(updatedAuthor).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id) {
        Author removedAuthor = authorDB.remove(id);
        if (removedAuthor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Collections.singletonMap("error", "Author not found"))
                    .build();
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/books")
    public Response getBooksByAuthor(@PathParam("id") int id) {
        Author author = authorDB.get(id);
        if (author == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Collections.singletonMap("error", "Author not found"))
                    .build();
        }

        List<Book> booksByAuthor = new ArrayList<>();
        for (Book book : BookResource.getBookDB().values()) {
            if (book.getAuthor().equalsIgnoreCase(author.getName())) {
                booksByAuthor.add(book);
            }
        }

        return Response.ok(booksByAuthor).build();
    }
}
