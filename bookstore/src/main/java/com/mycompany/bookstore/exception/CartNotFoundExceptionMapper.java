package com.mycompany.bookstore.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class CartNotFoundExceptionMapper implements ExceptionMapper<CartNotFoundException> {

    @Override
    public Response toResponse(CartNotFoundException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("error", exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }
}
