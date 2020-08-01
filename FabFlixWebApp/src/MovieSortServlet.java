

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MovieSortServlet
 */
@WebServlet(name = "/MovieSortServlet", urlPatterns = "/api/movieSort")
public class MovieSortServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().setAttribute("sortCriteria", request.getParameter("sortCriteria"));
		request.getSession().setAttribute("sortOrder", request.getParameter("sortOrder"));
	}

}
