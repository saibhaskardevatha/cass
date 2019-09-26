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
import com.dao.BalanceDaoUtil;

/**
 * Servlet implementation class IssueStockSplitServlet
 */
@WebServlet("/IssueStockSplit")
public class IssueStockSplitServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Stock Split");
		CorporateActions action = new CorporateActions();
		BalanceDaoUtil balanceDao = new BalanceDaoUtil();
		List<Balance> memberBalance = balanceDao.getAllBalances();
		System.out.println(memberBalance);
		List<Balance> balances = action.issueStockSplit(0.5, memberBalance , "Apple");
		System.out.println(balances);
		
		balanceDao.updateAllBalancesBySecurity(balances, "Apple");
		
		
		System.out.println("Stocks updated");
	
	}

}