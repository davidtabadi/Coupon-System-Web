package couponsweb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import coupon_system.CouponSystem;
import exception.CouponException;
import facades.ClientType;
import facades.CompanyFacade;
import javabeans.Coupon;
import javabeans.CouponType;

@Path("/company")
public class CompanyService {

	private CompanyFacade getCompanyFacadeFromSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		CompanyFacade companyFacade = (CompanyFacade) session.getAttribute("company");
		String sessionName = session.getId();
		// System.out.println(sessionName);
		return companyFacade;
	}

	@GET
	@Path("/login/{username}/{password}")
	@Produces(MediaType.APPLICATION_JSON)
	public ClientDTO companyLogin(@PathParam("username") String username, @PathParam("password") String password,
			@Context HttpServletRequest request) throws CouponException {
		CouponSystem couponSystem = CouponSystem.getInstance();
		CompanyFacade companyFacade = (CompanyFacade) couponSystem.login(username, password, ClientType.COMPANY);
		HttpSession session = request.getSession(true);
		String sessionName = session.getId();
		// System.out.println(sessionName);
		session.setAttribute("company", companyFacade);
		ClientDTO clientDTO = new ClientDTO(ClientType.COMPANY, username, password);
		// System.out.println("CompanyService.login() " + sessionName);
		return clientDTO;
	}

	@Path("/logout")
	@GET
	public void companyLogout(@Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			// System.out.println("CompanyService.companyLogout()");
			session.invalidate();
		}
	}

	@Path("/createcoupon")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon createCoupon(Coupon newCouponToCreate, @Context HttpServletRequest request) throws CouponException {
		getCompanyFacadeFromSession(request).createCuopon(newCouponToCreate);
		// System.out.println("CompanyService.createCoupon() " +
		// newCouponToCreate.getTitle());
		return newCouponToCreate;
	}

	@Path("/updatecoupon/{couponId}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateCoupon(@PathParam("couponId") long couponId, Coupon couponToUpdate,
			@Context HttpServletRequest request) throws CouponException {
		if (couponId != couponToUpdate.getCouponId()) {
			throw new CouponException("The Coupon selected must match to the Coupon in Database.");
		} else {
			getCompanyFacadeFromSession(request).updateCoupon(couponToUpdate);
			// System.out.println("CompanyService.updateCoupon() " +
			// couponToUpdate.getTitle());
		}
	}

	@Path("/removecoupon")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public void removeCoupon(Coupon couponToRemove, @Context HttpServletRequest request) throws CouponException {
		// System.out.println("CompanyService.removeCoupon() " +
		// couponToRemove.getTitle());
		getCompanyFacadeFromSession(request).removeCoupon(couponToRemove);
	}

	@Path("/getcoupon/{couponId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon getCoupon(@PathParam("couponId") long couponId, @Context HttpServletRequest request)
			throws CouponException {
		// System.out.println("CompanyService.getCoupon() " + couponId);
		return getCompanyFacadeFromSession(request).getCoupon(couponId);
	}

	@Path("/getallcoupons")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon[] getCompanyCoupons(@Context HttpServletRequest request) throws CouponException {
		Coupon[] companyCoupons = getCompanyFacadeFromSession(request).getAllCompanyCoupons().toArray(new Coupon[0]);
		// System.out.println("CompanyService.getCompanyCoupons() " +
		// Arrays.toString(companyCoupons));
		return companyCoupons;
	}

	@Path("/getallcoupons/byprice/{price}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon[] getCompanyCouponsByPrice(@PathParam("price") double price, @Context HttpServletRequest request)
			throws CouponException {
		Coupon[] companyCouponsByPrice = getCompanyFacadeFromSession(request).getCouponsUntilPrice(price)
				.toArray(new Coupon[0]);
		// System.out.println("CompanyService.getCompanyCouponsByPrice() " +
		// Arrays.toString(companyCouponsByPrice));
		return companyCouponsByPrice;
	}

	@Path("/getallcoupons/bytype/{type}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon[] getCompanyCouponsByType(@PathParam("type") CouponType type, @Context HttpServletRequest request)
			throws CouponException {
		Coupon[] companyCouponsByType = getCompanyFacadeFromSession(request).getCouponsByType(type)
				.toArray(new Coupon[0]);
		// System.out.println("CompanyService.getCompanyCouponsByType() " +
		// Arrays.toString(companyCouponsByType));
		return companyCouponsByType;
	}

	// There is no need for the show getCompanyCouponsByEndDate in angularjs

	// @Path("/getallcoupons/bydate/{date}")
	// @GET
	// @Produces(MediaType.APPLICATION_JSON)
	// public Coupon[] getCompanyCouponsByEndDate(@PathVariable("date") String date,
	// HttpSession session)
	// throws CouponException, ParseException {
	// CompanyFacade companyfacade = getCompanyFacadeFromSession(session);
	// DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	// Date newDate = format.parse(date);
	// Coupon[] companyCouponsByEndDate =
	// companyfacade.getCouponsBeforeEndDate(newDate).toArray(new Coupon[0]);
	// System.out.println("CompanyService.getCouponByDate() " + newDate);
	// return companyCouponsByEndDate;
	// }

	// @Path("/getallcoupons/bydate/{date}")
	// @GET
	// @Produces(MediaType.APPLICATION_JSON)
	// public Coupon[] getCompanyCouponsByEndDate(@PathParam("date") Date date,
	// @Context HttpServletRequest request)
	// throws CouponException {
	// Coupon[] companyCouponsByEndDate =
	// getCompanyFacadeFromSession(request).getCouponsBeforeEndDate(date)
	// .toArray(new Coupon[0]);
	// System.out.println("CompanyService.getCompanyCouponsByEndDate() " +
	// Arrays.toString(companyCouponsByEndDate));
	// return companyCouponsByEndDate;
	// }

}
