package couponsweb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import coupon_system.CouponSystem;
import exception.CouponException;
import facades.ClientType;
import facades.CustomerFacade;
import javabeans.Coupon;
import javabeans.CouponType;

@Path("/customer")
public class CustomerService {

	private CustomerFacade getCustomerFacadeFromSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		CustomerFacade customerFacade = (CustomerFacade) session.getAttribute("customer");
		String sessionName = session.getId();
		// System.out.println(sessionName);
		return customerFacade;
	}

	@GET
	@Path("/login/{username}/{password}")
	@Produces(MediaType.APPLICATION_JSON)
	public ClientDTO customerLogin(@PathParam("username") String username, @PathParam("password") String password,
			@Context HttpServletRequest request) throws CouponException {
		CouponSystem couponSystem = CouponSystem.getInstance();
		CustomerFacade customerFacade = (CustomerFacade) couponSystem.login(username, password, ClientType.CUSTOMER);
		HttpSession session = request.getSession(true);
		String sessionName = session.getId();
		// System.out.println(sessionName);
		session.setAttribute("customer", customerFacade);
		// System.out.println("CstomerService.login()");
		ClientDTO clientDTO = new ClientDTO(ClientType.CUSTOMER, username, password);
		// System.out.println("CstomerService.customerLogin() " + sessionName);
		return clientDTO;
	}

	@Path("/logout")
	@GET
	public void customerLogout(@Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			// System.out.println("CstomerService.customerLogout()");
			session.invalidate();
		}
	}

	@Path("/purchasecoupon")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon purchaseCoupon(Coupon couponToPurchase, @Context HttpServletRequest request) throws CouponException {
		getCustomerFacadeFromSession(request).purchaseCoupon(couponToPurchase);
		// System.out.println("CstomerService.purchaseCoupon() " +
		// couponToPurchase.getTitle());
		return couponToPurchase;
	}

	@Path("/getallpurchasedcoupons")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon[] getAllPurchasedCoupons(@Context HttpServletRequest request) throws CouponException {
		Coupon[] purchasedCoupons;
		purchasedCoupons = getCustomerFacadeFromSession(request).getAllPurchasedCoupons().toArray(new Coupon[0]);
		// System.out.println("CstomerService.getAllPurchasedCoupons() " +
		// Arrays.toString(purchasedCoupons));
		return purchasedCoupons;
	}

	@Path("/getallpurchasedcoupons/bytype/{type}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon[] getAllPurchasedCouponsByType(@PathParam("type") CouponType type,
			@Context HttpServletRequest request) throws CouponException {
		Coupon[] purchasedCouponsByType = getCustomerFacadeFromSession(request).getAllPurchasedCouponsByType(type)
				.toArray(new Coupon[0]);
		// System.out.println("CstomerService.getAllPurchasedCouponsByType() " +
		// Arrays.toString(purchasedCouponsByType));
		return purchasedCouponsByType;
	}

	@Path("/getallpurchasedcoupons/byprice/{price}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon[] getAllPurchasedCouponsByPrice(@PathParam("price") double price, @Context HttpServletRequest request)
			throws CouponException {
		Coupon[] purchasedCouponsByPrice = getCustomerFacadeFromSession(request).getAllPurchasedCouponsByPrice(price)
				.toArray(new Coupon[0]);
		// System.out
		// .println("CstomerService.getAllPurchasedCouponsByPrice() " +
		// Arrays.toString(purchasedCouponsByPrice));
		return purchasedCouponsByPrice;
	}

	@Path("/getallavailabledcoupons")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon[] getAllAvailableCoupons(@Context HttpServletRequest request) throws CouponException {
		Coupon[] allCoupons;
		allCoupons = getCustomerFacadeFromSession(request).getAllCouponsForSale().toArray(new Coupon[0]);
		// System.out.println("CstomerService.getAllAvailableCoupons() " +
		// Arrays.toString(allCoupons));
		return allCoupons;
	}

	@Path("/getallavailabledcoupons/bytype/{type}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon[] getAllAvailableCouponsByType(@PathParam("type") CouponType type,
			@Context HttpServletRequest request) throws CouponException {
		Coupon[] allCouponsByType = getCustomerFacadeFromSession(request).getAllCouponsForSaleByType(type)
				.toArray(new Coupon[0]);
		// System.out.println("CstomerService.getAllAvailableCouponsByType() " +
		// Arrays.toString(allCouponsByType));
		return allCouponsByType;
	}

	@Path("/getallavailabledcoupons/byprice/{price}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon[] getAllAvailableCouponsByPrice(@PathParam("price") double price, @Context HttpServletRequest request)
			throws CouponException {
		Coupon[] allCouponsByPrice = getCustomerFacadeFromSession(request).getAllCouponsForSaleByPrice(price)
				.toArray(new Coupon[0]);
		// System.out.println("CstomerService.getAllAvailableCouponsByPrice() " +
		// Arrays.toString(allCouponsByPrice));
		return allCouponsByPrice;
	}
}
