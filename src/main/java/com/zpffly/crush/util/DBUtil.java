package com.zpffly.crush.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Properties;
import java.util.Random;

public class DBUtil {

	
	public static Connection getConn() throws Exception{
		String url = "jdbc:mysql://127.0.0.1:3306/crush?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8";
		String username = "root";
		String password = "123456";
		String driver = "com.mysql.cj.jdbc.Driver";
		Class.forName(driver);
		return DriverManager.getConnection(url,username, password);
	}

	public static void main(String[] args) throws Exception {
		Connection conn = getConn();
		PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO test_order VALUES (NULL,?, ?,?)");
		Random random = new Random();
		for (int i = 0; i < 100000; i++) {
			preparedStatement.setInt(1, i);
			preparedStatement.setInt(2, i);
			preparedStatement.setInt(3, i);
//			statement.execute("INSERT INTO test_order VALUES (NULL,?, ?,1 )");
			preparedStatement.execute();
		}
	}
}
