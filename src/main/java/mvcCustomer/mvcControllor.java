package mvcCustomer;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/member/*")
public class mvcControllor extends HttpServlet {
	private static final long serialVersionUID = 1L;
	MemberDAO memberDAO;

	@Override
	public void init() throws ServletException {
		memberDAO = new MemberDAO();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		String nextPage = null;
		String action = request.getPathInfo();

		if (action == null || action.equals("listMembers.do")) {
			// 리스트 멤버 함수로 받은 list 를 MemberVO 타입
			List<MemberVO> membersList = memberDAO.listMembers();
			request.setAttribute("membersList", membersList);
			nextPage = "listMembers.jsp";
		} else if (action.equals("/addmember.do")) {
			String id = request.getParameter("id");
			String pwd = request.getParameter("pwd");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			MemberVO memberVO = new MemberVO(id, pwd, name, email);
			memberDAO.addMember(memberVO);
			nextPage = "/member/listMembers.do";
		}else if(action.equals("/memberForm.do")) {
			nextPage ="/memberForm.jsp";
		}else
		{
			List<MemberVO> membersList = memberDAO.listMembers();
			request.setAttribute("membersList", membersList);
			nextPage = "/listMembers.jsp";
		}
		RequestDispatcher dispath = request.getRequestDispatcher(nextPage);
		
		dispath.forward(request, response);
	}

}
