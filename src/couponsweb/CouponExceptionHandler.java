package couponsweb;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CouponExceptionHandler implements ExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception e) {
		String message = e.getMessage();

		if (message == null) {
			message = "Error while calling this method ";
			return null;
		}

		// return status: 500 with error message as JSON
		return Response.serverError().entity(new ErrorMessage(message)).type(MediaType.APPLICATION_JSON).build();
	}

}
