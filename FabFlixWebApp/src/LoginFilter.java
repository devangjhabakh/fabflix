
import java.awt.Window;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter(filterName = "/LoginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {

    /**
     * Default constructor. 
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		if(this.canAccessWithoutLogin(req.getRequestURI())) {
			chain.doFilter(request, response);
			return;
		}
		
		if(req.getSession().getAttribute("user") == null) {
			System.out.println("There is no user!");
			res.sendRedirect("login.html");
		}
		else {
			chain.doFilter(request, response);
			return;
		}
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}
	
	private boolean canAccessWithoutLogin(String URI) {
		URI = URI.toLowerCase();
		return URI.endsWith("payment/pay") || URI.endsWith("login.html") || URI.endsWith("login.js") || URI.endsWith("api/table_metadata") || URI.endsWith("add_movie/add") || URI.endsWith("api/login") || URI.endsWith("form-recaptcha") || URI.endsWith("_dashboard.html") || URI.endsWith("_dashboard.js") || URI.endsWith("_dashboard") || URI.endsWith("api/dashboard_login");
	}
	
}
