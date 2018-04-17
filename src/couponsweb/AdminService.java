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
import facades.AdminFacade;
import facades.ClientType;
import javabeans.Company;
import javabeans.Customer;

@Path("/admin")
public class AdminService {

	private AdminFacade getAadminFacadeFromSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		AdminFacade adminFacade = (AdminFacade) session.getAttribute("admin");
		String sessionName = session.getId();
		// System.out.println(sessionName);
		return adminFacade;
	}

	@Path("/login/{username}/{password}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ClientDTO adminLogin(@PathParam("username") String username, @PathParam("password") String password,
			@Context HttpServletRequest request) throws CouponException {
		CouponSystem couponSystem = CouponSystem.getInstance();
		AdminFacade adminFacade = (AdminFacade) couponSystem.login(username, password, ClientType.ADMIN);
		HttpSession session = request.getSession(true);
		String sessionName = session.getId();
		// System.out.println(sessionName);
		session.setAttribute("admin", adminFacade);
		// System.out.println("AdminService.adminLogin()");
		ClientDTO clientDTO = new ClientDTO(ClientType.ADMIN, username, password);
		// System.out.println("AdminService.adminLogin() " + sessionName);
		return clientDTO;
	}

	@Path("/logout")
	@GET
	public void adminLogout(@Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			// System.out.println("AdminService.adminLogout()" + session.getId());
			session.invalidate();
		}

	}

	@POST
	@Path("/companies")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Company createCompany(Company newCompanyToCreate, @Context HttpServletRequest request)
			throws CouponException {
		getAadminFacadeFromSession(request).createCompany(newCompanyToCreate);
		// System.out.println("AdminService.createCompany() " +
		// newCompanyToCreate.getCompName());
		return newCompanyToCreate;
	}

	@PUT
	@Path("/companies/{companyId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateCompany(@PathParam("companyId") long companyId, Company companyToUpdate,
			@Context HttpServletRequest request) throws CouponException {
		if (companyId != companyToUpdate.getId()) {
			throw new CouponException("The Company selected must match to the Company in Database. ");
		} else {
			getAadminFacadeFromSession(request).updateCompany(companyToUpdate);
			// System.out.println("AdminService.updateCompany() " +
			// companyToUpdate.getCompName());
		}
	}

	@DELETE
	@Path("/companies")
	@Consumes(MediaType.APPLICATION_JSON)
	public void removeCompany(Company companyToRemove, @Context HttpServletRequest request) throws CouponException {
		// System.out.println("AdminService.removeCompany() " +
		// companyToRemove.getCompName());
		getAadminFacadeFromSession(request).removeCompany(companyToRemove);
	}

	@GET
	@Path("/companies/{companyId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Company getCompany(@PathParam("companyId") long companyId, @Context HttpServletRequest request)
			throws CouponException {
		Company company = getAadminFacadeFromSession(request).getCompany(companyId);
		// System.out.println("AdminService.getCompany() " + companyId);
		return company;
	}

	@GET
	@Path("/companies")
	@Produces(MediaType.APPLICATION_JSON)
	public Company[] getAllCompanies(@Context HttpServletRequest request) throws CouponException {
		Company[] allCompanies = getAadminFacadeFromSession(request).getAllCompanies().toArray(new Company[0]);
		// System.out.println("AdminService.getAllCompanies() " +
		// Arrays.toString(allCompanies));
		return allCompanies;
	}

	@Path("/customers")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Customer createCustomer(Customer customerToCreate, @Context HttpServletRequest request)
			throws CouponException {
		getAadminFacadeFromSession(request).createCustomer(customerToCreate);
		// System.out.println("AdminService.createCustomer() " +
		// customerToCreate.getCustName());
		return customerToCreate;
	}

	@Path("/customers/{customerId}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateCustomer(@PathParam("customerId") long customerId, Customer customerToUpdate,
			@Context HttpServletRequest request) throws CouponException {
		if (customerId != customerToUpdate.getId()) {
			throw new CouponException("The Customer selected must match to the Customer in Database. ");
		} else {
			getAadminFacadeFromSession(request).updateCustomer(customerToUpdate);
			// System.out.println("AdminService.updateCustomer() " +
			// customerToUpdate.getCustName());
		}
	}

	@Path("/customers")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public void removeCustomer(Customer customerToRemove, @Context HttpServletRequest request) throws CouponException {
		// System.out.println("AdminService.removeCustomer() " +
		// customerToRemove.getCustName());
		getAadminFacadeFromSession(request).removeCustomer(customerToRemove);
	}

	@Path("/customers/{customerId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Customer getCustomer(@PathParam("customerId") long customerId, @Context HttpServletRequest request)
			throws CouponException {
		Customer customer = getAadminFacadeFromSession(request).getCustomer(customerId);
		// System.out.println("AdminService.getCustomer() " + customerId);
		return customer;
	}

	@GET
	@Path("/customers")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer[] getAllCustomers(@Context HttpServletRequest request) throws CouponException {
		Customer[] allCustomers = getAadminFacadeFromSession(request).getAllCustomers().toArray(new Customer[0]);
		// System.out.println("AdminService.getAllCustomers() " +
		// Arrays.toString(allCustomers));
		return allCustomers;
	}

}
