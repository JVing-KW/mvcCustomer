package mvcCustomer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class MemberDAO {

	private Connection con;

	private DataSource dataFactory;

	private PreparedStatement pstm;

	public MemberDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/mysql");

		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

	public List<MemberVO> listMembers() {
		List<MemberVO> membersList = new ArrayList<MemberVO>();

		try {
			con = dataFactory.getConnection();
			String query = "select * from t_member order by joinDate desc";
			System.out.println("PreparedStatement :" + query);
			pstm = con.prepareStatement(query);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String id = rs.getString("id");
				String pwd = rs.getString("pwd");
				String name = rs.getString("name");
				String email = rs.getString("email");
				Date joinDate = rs.getDate("joinDate");

				MemberVO vo = new MemberVO(id, pwd, name, email, joinDate);

				membersList.add(vo);
			}
			rs.close();
			pstm.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return membersList;
	}

	public void addMember(MemberVO m) {

		try {
			// 서블릿에서 가져온걸 DAO에서 VO로 저정한걸가져옴
			con = dataFactory.getConnection();
			String query = "INSERT INTO t_member values(?, ?, ?, ?, default)";
			System.out.println("PreparedStatement :" + query);
			pstm = con.prepareStatement(query);
			pstm.setString(1, m.getId());
			pstm.setString(2, m.getPwd());
			pstm.setString(3, m.getName());
			pstm.setString(4, m.getEmail());
			pstm.executeUpdate();
			pstm.close();
			con.close();

			//

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void modMember(MemberVO memberVO) {
		String id = memberVO.getId();
		String pwd = memberVO.getPwd();
		String name = memberVO.getName();
		String email = memberVO.getEmail();	
		try {
			con = dataFactory.getConnection();
			String query = "UPDATE t_member set pwd=?,name=?,email=?  where id=?";
			System.out.println(query);
			pstm = con.prepareStatement(query);
			pstm.setString(1, pwd);
			pstm.setString(2, name);
			pstm.setString(3, email);
			pstm.setString(4, id);
			pstm.executeUpdate();
			pstm.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public MemberVO findMember(String _id) {
		MemberVO memInfo = null;
		try {
			con = dataFactory.getConnection();
			String query = "SELECT * FROM  t_member WHERE id=?";
			pstm = con.prepareStatement(query);
			pstm.setString(1, _id);
			System.out.println(query);
			ResultSet rs = pstm.executeQuery();
			rs.next();
			String id = rs.getString("id");
			String pwd = rs.getString("pwd");
			String name = rs.getString("name");
			String email = rs.getString("email");
			Date joinDate = rs.getDate("joinDate");
			memInfo = new MemberVO(id, pwd, name, email, joinDate);
			pstm.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return memInfo;
	}
	public void delMember(String id) {
		try {
			con = dataFactory.getConnection();
			String query = "DELETE FROM t_member WHERE id=?";
			System.out.println(query);
			pstm = con.prepareStatement(query);
			pstm.setString(1,id);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	}
	


