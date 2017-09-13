package com.kxf.ims.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import com.kxf.mysqlmanage.BaseDBManage;
import com.kxf.mysqlmanage.LogUtils;

public class MyDBManage extends BaseDBManage {

	@Override
	public Connection openConnection() {
		Properties prop = new Properties();
		String driver = null;
		String url = null;
		String username = null;
		String password = null;
		try {
			prop.load(this.getClass().getClassLoader()
					.getResourceAsStream("DBConfig.properties"));
			driver = prop.getProperty("driver");
			url = prop.getProperty("url");
			username = prop.getProperty("username");
			password = prop.getProperty("password");
			Class.forName(driver);
			return DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			LogUtils.e(e.toString());
		}
		return null;
	}

}
