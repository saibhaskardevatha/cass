package com.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.algorithms.CorporateActions;
import com.beans.Balance;
import com.beans.Security;
import com.dao.BalanceDaoUtil;
import com.dao.SecurityDaoUtil;

/**
 * Servlet implementation class IssueCashDividendServlet
 */
@WebServlet("/IssueCashDividend")
public class IssueCashDividendServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("Stock Split");
		CorporateActions action = new CorporateActions();
		BalanceDaoUtil balanceDao = new BalanceDaoUtil();
		List<Balance> memberBalance = balanceDao.getAllBalances();
		System.out.println(memberBalance);
		
		SecurityDaoUtil securityDao = new SecurityDaoUtil();
		Security security = new Security();
		security = securityDao.getSecurityByName("Apple");
		System.out.println(security);
		
		List<Balance> balances = action.issueCashDividend(10, memberBalance , security);
		System.out.println(balances);
		
		balanceDao.updateFundBalance(balances);
		
		
		System.out.println("Stocks updated");
	}


}
