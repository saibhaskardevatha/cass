package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.beans.Balance;
import com.beans.Rights;
import com.interfaces.BalanceDao;

public class BalanceDaoUtil implements BalanceDao{

	@Override
	public List<Balance> getAllBalances() {
		List<Balance> balances = new ArrayList<Balance>();
		RightDaoUtil rightsDao = new RightDaoUtil();
		
		String SQL_GET_ALL_BALANCE = "SELECT * FROM Balances";
		
		
		try(Connection conn = DBConnection.openConnection()){
			Statement ps = conn.createStatement();
			ResultSet rs = ps.executeQuery(SQL_GET_ALL_BALANCE);

			while (rs.next()) {
				Balance balance = new Balance();
				int clearingMemberId = rs.getInt("clearingMemberId");
				System.out.println(clearingMemberId);
				balance.setClearingMemberId(clearingMemberId);
				balance.setFunds(rs.getDouble("funds"));
				Map<String, Integer> securityBalance = new HashMap<String, Integer>();
				securityBalance.put("Facebook", rs.getInt("facebook"));
				securityBalance.put("LinkedIn", rs.getInt("linkedin"));
				securityBalance.put("Amazon", rs.getInt("amazon"));
				securityBalance.put("Apple", rs.getInt("apple"));
				securityBalance.put("Twitter", rs.getInt("twitter"));
				balance.setSecurityBalance(securityBalance);
				List<Rights> rights = rightsDao.getRightsByClearingMember(clearingMemberId);
				balance.setRights(rights);
				System.out.println(balance);
				
				balances.add(balance);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return balances;
	}

	@Override
	public boolean updateAllBalancesBySecurity(List<Balance> balances, String securityName) {
		boolean isUpdated = false;
		String SQL_UPDATE_BALANCES = "UPDATE balances SET "+securityName+"=? WHERE clearingMemberId=?";
		
		try(Connection conn = DBConnection.openConnection()){
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_BALANCES);
			
			for(Balance balance : balances) {
				ps.setInt(1, balance.getSecurityBalance().get(securityName));
				ps.setInt(2, balance.getClearingMemberId());
				
				ps.addBatch();
			}
			int[] rs = ps.executeBatch();
			
			if(rs.length == 6) {
				isUpdated = true;
				conn.commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isUpdated;
	}

	@Override
	public Balance getBalanceByClearingMember(int clearingMemberId) {
		Balance balance = new Balance();
		RightDaoUtil rightsDao = new RightDaoUtil();
		
		String SQL_GET_BALANCE_BY_ID = "SELECT * FROM Balances WHERE clearingMemberId=?";
		
		
		try(Connection conn = DBConnection.openConnection()){
			PreparedStatement ps = conn.prepareStatement(SQL_GET_BALANCE_BY_ID);
			
			ps.setInt(1, clearingMemberId);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				balance.setClearingMemberId(rs.getInt("clearingMemberId"));
				balance.setFunds(rs.getDouble("funds"));
				Map<String, Integer> securityBalance = new HashMap<>();
				securityBalance.put("Facebook", rs.getInt("facebook"));
				securityBalance.put("LinkedIn", rs.getInt("linkedin"));
				securityBalance.put("Amazon", rs.getInt("amazon"));
				securityBalance.put("Apple", rs.getInt("apple"));
				securityBalance.put("Twitter", rs.getInt("Twitter"));
				balance.setSecurityBalance(securityBalance);
				List<Rights> rights = rightsDao.getRightsByClearingMember(clearingMemberId);
				balance.setRights(rights);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return balance;
	}

	@Override
	public boolean updateSecurity(Balance balance) {
		boolean isUpdated = false;
		String SQL_UPDATE_BALANCES = "UPDATE balances SET funds=?,facebook=?,linkedin=?,amazon=?,twitter=?,apple=? WHERE clearingMemberId=?";
		int rows=0;
		try(Connection conn = DBConnection.openConnection()){
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_BALANCES);
			
				ps.setDouble(1, balance.getFunds());
				ps.setInt(2, balance.getSecurityBalance().get("Facebook"));
				ps.setInt(3, balance.getSecurityBalance().get("LinkedIn"));
				ps.setInt(4, balance.getSecurityBalance().get("Amazon"));
				ps.setInt(5, balance.getSecurityBalance().get("Twitter"));
				ps.setInt(6, balance.getSecurityBalance().get("Apple"));
				ps.setInt(7, balance.getClearingMemberId());
				 rows=ps.executeUpdate();
			
			if(rows>0) {
				isUpdated = true;
				conn.commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isUpdated;
	}

	@Override
	public double updateFunds(double funds, int clearingMemberId) {
		// TODO Auto-generated method stub
		
		String SQL_UPDATE_FUNDS = "UPDATE balances SET funds =? WHERE clearingMemberId=?";
				try(Connection conn = DBConnection.openConnection()){
					conn.setAutoCommit(false);
					PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_FUNDS);
					ps.setDouble(1, funds);
					ps.setInt(2, clearingMemberId);
					
					ResultSet rs = ps.executeQuery();
					if(rs.next()) {
						
						conn.commit();
						return funds;
						
					}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		
		return 0;
	}
	
	@Override
	public boolean updateFundBalance(List<Balance> balances) {
		boolean isUpdated = false;
		String SQL_UPDATE_BALANCES = "UPDATE balances SET funds=? WHERE clearingMemberId=?";
		
		try(Connection conn = DBConnection.openConnection()){
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_BALANCES);
			
			for(Balance balance : balances) {
				ps.setDouble(1, balance.getFunds());
				ps.setInt(2, balance.getClearingMemberId());
				
				ps.addBatch();
			}
			int[] rs = ps.executeBatch();
			
			if(rs.length == 6) {
				isUpdated = true;
				conn.commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isUpdated;
	}
	
	public Balance getInitialBalanceByClearingMember(int clearingMemberId) {
		Balance balance = new Balance();
		RightDaoUtil rightsDao = new RightDaoUtil();
		
		String SQL_GET_BALANCE_BY_ID = "SELECT * FROM InitialBalances WHERE clearingMemberId=?";
		
		
		try(Connection conn = DBConnection.openConnection()){
			PreparedStatement ps = conn.prepareStatement(SQL_GET_BALANCE_BY_ID);
			
			ps.setInt(1, clearingMemberId);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				balance.setClearingMemberId(rs.getInt("clearingMemberId"));
				balance.setFunds(rs.getDouble("funds"));
				Map<String, Integer> securityBalance = new HashMap<>();
				securityBalance.put("Facebook", rs.getInt("facebook"));
				securityBalance.put("LinkedIn", rs.getInt("linkedin"));
				securityBalance.put("Amazon", rs.getInt("amazon"));
				securityBalance.put("Apple", rs.getInt("apple"));
				securityBalance.put("Twitter", rs.getInt("twitter"));
				balance.setSecurityBalance(securityBalance);
				List<Rights> rights = rightsDao.getRightsByClearingMember(clearingMemberId);
				balance.setRights(rights);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return balance;
	}
	
	//Not useful
	@Override
	public boolean updateAllBalances(List<Balance> balances) {
		return false;
	}

}
