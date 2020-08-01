

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MovieBrowseServlet
 */
@WebServlet(name = "/MovieBrowseServlet", urlPatterns = "/api/browse")
public class MovieBrowseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().setAttribute("startingLetter", request.getParameter("startingLetter"));
		request.getSession().setAttribute("inputYear", "");
		request.getSession().setAttribute("inputTitle", "");
		request.getSession().setAttribute("inputDirector", "");
		request.getSession().setAttribute("inputStar", "");
		request.getSession().setAttribute("NumResultSelect", request.getParameter("NumResultSelect"));
		request.getSession().setAttribute("sortCriteria", "ratings.rating");
		request.getSession().setAttribute("sortOrder", "desc");
		request.getSession().setAttribute("genre", request.getParameter("genre") == null ? "" : request.getParameter("genre"));
		response.setStatus(200);
	}

}
