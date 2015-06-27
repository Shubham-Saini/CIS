package com.drive.oauth;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class DatabaseConnect 
{
	private static Connection conn = null;
	public static Connection connect()
	{
		String dbUrl = "jdbc:sqlserver://localhost;databaseName=DriveProject";
		String userName = "sa";
		String password = "michael";
		if(conn ==null)
		{
			try
			{
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				conn = DriverManager.getConnection(dbUrl, userName, password);
			}
			catch(SQLException e)
			{
				System.out.println("Trouble Connecting to the database");
				e.printStackTrace();
			}
			catch(ClassNotFoundException ee)
			{
				System.out.println("Could not found the class in database");
				ee.printStackTrace();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return conn;
	}
	public static List<DriveUser> getAccounts(String userId)
	{
		List<DriveUser> users = new ArrayList<DriveUser>();
		try
		{
			if(conn == null) connect();
			String query = "select * from DriveDetail where UserId=?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1,userId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				String email = rs.getString("Email");
				String server = rs.getString("ServerName");
				String accessToken = rs.getString("AccessToken");
				String refreshToken = rs.getString("RefreshToken");
				String name = rs.getString("Name");
				DriveUser user = new DriveUser(name,userId,email,accessToken,refreshToken,server);
				users.add(user);
			}
		}
		catch(Exception e)
		{
			System.out.println("Trouble getting account details.");
			e.printStackTrace();
		}
		return users;
	}
	public static DriveUser getAccount(String Uname)
	{
		DriveUser user = null;
		try
		{
			if(conn == null) connect();
			String query = "select * from DriveDetail where Name=?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1,Uname);
			ResultSet rs = stmt.executeQuery();
			String userId=null;
			if(rs.next())
			{
				String email = rs.getString("Email");
				String server = rs.getString("ServerName");
				String accessToken = rs.getString("AccessToken");
				String refreshToken = rs.getString("RefreshToken");
				String name = rs.getString("Name");
				user = new DriveUser(name,userId,email,accessToken,refreshToken,server);
			}
		}
		catch(Exception e)
		{
			System.out.println("Trouble getting account details.");
			e.printStackTrace();
		}
		return user;
	}
	public static void queryString(String query)
	{
		try
		{
			if(conn==null)connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnSize = rsmd.getColumnCount();
			for(int i=1;i<=columnSize;i++)
			{
				System.out.printf("%s ",rsmd.getColumnName(i));
			}
			System.out.printf("\n");
			while(rs.next())
			{
				for(int i=1;i<=columnSize;i++)
					System.out.printf("%s ",rs.getString(i));
				System.out.println();
			}
		}
		catch(Exception e)
		{
			System.out.println("Trouble Retrieving results..");
			e.printStackTrace();
		}
	}
	public static int register(String username,String email,String password)
	{
		try
		{
			if(conn==null) connect();
			System.out.println("username is " + username);
			int random=12;
			String query = "select * from Login where Email=?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1,email);
			ResultSet rs = stmt.executeQuery();
			String hashPassword = PasswordHash.createHash(password);
			if(rs.next())
			{
				System.out.println("The provided email id is already registered with us.");
				return 0;
			}
			query="insert into Login values(?,?,?,?,?,?)";
			stmt = conn.prepareStatement(query);
			stmt.setString(1,email); stmt.setString(2,hashPassword); stmt.setInt(3, random);
			stmt.setString(4,username); stmt.setString(5, null); stmt.setString(6,null);
			stmt.executeUpdate();
			return 1;
		}
		catch(Exception e)
		{
			System.out.println("Trouble inserting rescord in Login Table");
			e.printStackTrace();
		}
		return 2;
	}
	public static boolean updatePassword(String email,String password)
	{
		boolean res = false;
		try
		{
			if(conn == null) connect();
			String hashPassword = PasswordHash.createHash(password);
			String query = "update Login set Pass=? where Email=?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, hashPassword); stmt.setString(2, email);
			stmt.executeUpdate();
			return true;
		}
		catch(Exception e)
		{
			System.out.println("Trouble in updating password");
			e.printStackTrace();
		}
		return res;
	}
	public static boolean authenticate(String email,String password,String username)
	{
		try
		{
			if(conn==null) connect();
			String query = "select * from Login where Email=?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, email);
			String correctHash = null;
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
			{
				username = rs.getString("UserName");
				correctHash = rs.getString("Pass");
				if(PasswordHash.validatePassword(password, correctHash))
					return true;
			}
		}
		catch(Exception e)
		{
			System.out.println("Trouble authenticating the user");
			e.printStackTrace();
		}
		return false;
	}
	public static boolean storeTokens(DriveUser user)
	{
		if(conn==null) connect();
		String email = user.getEmail();
		String name = user.getName();
		String accessToken = user.getAccessToken();
		String refreshToken = user.getRefreshToken();
		String userId = user.getUserId();
		String server = user.getServer();
		try
		{
			
			String query ="insert into DriveDetail values(?,?,?,?,?,?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1,userId); stmt.setString(2,email); stmt.setString(3,server);
			stmt.setString(4,name); stmt.setString(5,accessToken); stmt.setString(6,refreshToken);
			int res = stmt.executeUpdate();
			System.out.println("refresh token in database is " + refreshToken);
			return (res>0)?true:false;
		}
		catch(Exception e)
		{
			System.out.println("Trouble storing tokenss.");
			e.printStackTrace();
		}
		return false;
	}
	public static boolean updateTokens(String uname,String accessToken)
	{
		boolean result = false;
		if(conn==null) connect();
		try
		{
			String query = "update DriveDetail set AccessToken=? where Name=?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1,accessToken); stmt.setString(2,uname);
			int res = stmt.executeUpdate();
			if(res>0)
			{
				result=true;
			}
		}
		catch(Exception e)
		{
			System.out.println("Trouble updating token");
			e.printStackTrace();
		}
		return result;
		
	}
	public static String generateCode(String email)
	{
		String result=null;
		if(conn==null) connect();
		String query = "select Email from Login where Email=?";
		try
		{
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, email);
		ResultSet rs = stmt.executeQuery();
		if(rs.next())
		{
			String authCode = PasswordHash.generateSecureRandom();
			query = "update Login set ActivationCode=?, isCodeActive=1 where Email=?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, authCode); stmt.setString(2, email);
			int res = stmt.executeUpdate();
			if(res>0)
			{
				System.out.println("Code stored successfully");
				result = "1:An Email has been sent to your account:"+authCode;
			}
		}
		else
		{
			System.out.println("No such email exists");
			result = "0:No such account exists. Try again.";
		}
		}
		catch(Exception e)
		{
			System.out.println("Trouble in generating tokens");
			result = "0:Ecxeption occured";
			e.printStackTrace();
		}		
		return result;
	}
	public static String removeCode(String authCode)
	{
		if(conn==null) connect();
		String query = "select Email from Login where ActivationCode=? and IsCodeActive=1";
		String result = null;
		try
		{
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, authCode);
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
			{
				query = "update Login set ActivationCode=null, isCodeActive=0 where Email=?";
				String email = rs.getString("Email");
				stmt = conn.prepareStatement(query); stmt.setString(1, email);
				int res = stmt.executeUpdate();
				if(res>0)
				{
					System.out.println("Code successfully removed");
					result = "1:Code is successfully removed:"+email;
				}
			}
			else
			{
				System.out.println("Expired link or bad request");
				result="0:Expired link or bad request";
			}
		}
		catch(Exception e)
		{
			System.out.println("Trouble removing code");
			result = "0:Exception occured";
			e.printStackTrace();
		}
		return result;
	}
	
//	public static List<User> getTokens(String email,String clientId,String clientSecret)
//	{
//		if(conn==null)connect();
//		List<User> list = new ArrayList<User>();
//		try
//		{
//			String query = "select * from DriveDetail where Email=?";
//			PreparedStatement stmt = conn.prepareStatement(query);
//			stmt.setString(1,email);
//			ResultSet rs = stmt.executeQuery();
//			while(rs.next())
//			{
//				String emailRef = rs.getString("Email_ref");
//				String name = rs.getString("Name");
//				String accessToken = rs.getString("AccessToken");
//				String refreshToken = rs.getString("RefreshToken");
//				String server = rs.getString("ServerName");
//				User user = new User(name,email,emailRef,accessToken,refreshToken,server);
//				user.setClientId(clientId); user.setClientSecret(clientSecret);
//				list.add(user);
//			}
//			return list;
//		}
//		catch(Exception e)
//		{
//			System.out.println("Trouble getting tokens");
//			e.printStackTrace();
//		}
//		return list;
//	}
}
