package com.kxf.ims.servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kxf.ims.db.MyDBManage;
import com.kxf.ims.entity.HttpEntity;
import com.kxf.ims.entity.User;
import com.kxf.ims.utils.EncUtil;
import com.kxf.ims.utils.StringUtils;
import com.kxf.ims.utils.ZipUtils;
import com.kxf.mysqlmanage.DBWhereBuilder;
import com.kxf.mysqlmanage.LogUtils.LogListener;
import com.kxf.mysqlmanage.MySqlManagerException;

public class UserServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// super.doPost(req, resp);
		resp.setContentType("text/plain; charset=utf-8");
		BufferedReader reader = req.getReader();
		log("请求者地址" + req.getRemoteAddr());
		String reStr = "";
		String line = "";
		while (null != (line = reader.readLine())) {
			reStr += line;
		}
		reader.close();
		log("reStr=" + reStr);
		reStr = EncUtil.desEncryptAsString(null, reStr);
		log("解密后  reStr=" + reStr);
		Gson gson = new Gson();
		Type typeOfT = new TypeToken<HttpEntity<User>>() {
		}.getType();
		HttpEntity<User> he = gson.fromJson(reStr, typeOfT);
		log("he=" + he);

		if (null == he) {
			he.setResponseCode("-9999");
			he.setResponseMsg("参数错误");
		} else {
			MyDBManage db = new MyDBManage(){

				@Override
				public LogListener getListener() {
					// TODO Auto-generated method stub
					return new LogListener() {
						
						@Override
						public void w(String s) {
							log("LogListener w s=" + s);
							
						}
						
						@Override
						public void i(String s) {
							log("LogListener i s=" + s);
							
						}
						
						@Override
						public void e(String s) {
							log("LogListener e s=" + s);
							
						}
						
						@Override
						public void d(String s) {
							log("LogListener d s=" + s);
							
						}
					};
				}};
			if ("1000".equals(he.getRequestCode())) {// 查询单个
				User[] us = he.getTs();
				if (null == us || us.length != 1
						|| StringUtils.isEmpty(us[0].getName())) {
					he.setResponseCode("-9999");
					he.setResponseMsg("参数错误");
				} else {
					DBWhereBuilder dbw = new DBWhereBuilder("name", "=",
							us[0].getName());
					List<User> ls = null;
					try {
						ls = db.find(User.class, dbw);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MySqlManagerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (null == ls || ls.size() < 1) {
						if ("root".equals(us[0].getName())) {// "root".equals(us[0].getName())
							User user = new User();
							user.setName("root");
							user.setPw("qazwsx");
							user.setPermissions(2);
							try {
								db.save(user);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MySqlManagerException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							ls = new ArrayList<User>();
							ls.add(user);
							he.setResponseCode("0000");
							he.setResponseMsg("成功");
							he.setTs(ls.toArray(new User[0]));
						} else {
							he.setResponseCode("-9997");
							he.setResponseMsg("未查到");
						}
					} else {
						he.setResponseCode("0000");
						he.setResponseMsg("成功");
						he.setTs(ls.toArray(new User[0]));
					}
				}
			} else if ("1001".equals(he.getRequestCode())) {//添加
				User[] us = he.getTs();
				if (null == us || us.length != 1
						|| StringUtils.isEmpty(us[0].getName())) {
					he.setResponseCode("-9999");
					he.setResponseMsg("参数错误");
				} else {
					DBWhereBuilder dbw = new DBWhereBuilder("name", "=",
							us[0].getName());
					List<User> ls = null;
					try {
						ls = db.find(User.class, dbw);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MySqlManagerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (null == ls || ls.size() < 1) {
						int i = -1;
						try {
							i = db.save(us[0]);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (MySqlManagerException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (1 == i) {
							he.setResponseCode("0000");
							he.setResponseMsg("成功");
						} else {
							he.setResponseCode("-9995");
							he.setResponseMsg("用户添加失败");
						}
					} else {
						he.setResponseCode("-9996");
						he.setResponseMsg("用户已存在");
						he.setTs(ls.toArray(new User[0]));
					}
				}
			} else if ("1002".equals(he.getRequestCode())) {//查询多个
				User[] us = he.getTs();
				if (null == us || us.length != 1
						|| StringUtils.isEmpty(us[0].getPermissions() + "")) {
					he.setResponseCode("-9999");
					he.setResponseMsg("参数错误");
				} else {
					DBWhereBuilder dbw = new DBWhereBuilder("permissions", "<=",
							us[0].getPermissions());
					List<User> ls = null;
					try {
						ls = db.find(User.class, dbw);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MySqlManagerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					he.setResponseCode("0000");
					he.setResponseMsg("成功");
					he.setTs(ls.toArray(new User[0]));
				}
			} else if ("1003".equals(he.getRequestCode())) {//修改信息
				User[] us = he.getTs();
				if (null == us || us.length != 1
						|| StringUtils.isEmpty(us[0].getId() + "")) {
					he.setResponseCode("-9999");
					he.setResponseMsg("参数错误");
				} else {
					DBWhereBuilder dbw = new DBWhereBuilder("id", "=",
							us[0].getId());
					int i = -1;
					try {
						i = db.update(us[0], dbw);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MySqlManagerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (i!=1) {
						he.setResponseCode("-9994");
						he.setResponseMsg("用户修改失败");
					}else {
						he.setResponseCode("0000");
						he.setResponseMsg("成功");
					}
				}
			} else if ("1004".equals(he.getRequestCode())) {//删除信息
				User[] us = he.getTs();
				if (null == us || us.length != 1
						|| StringUtils.isEmpty(us[0].getId() + "")) {
					he.setResponseCode("-9999");
					he.setResponseMsg("参数错误");
				} else {
					DBWhereBuilder dbw = new DBWhereBuilder("id", "=",
							us[0].getId());
					int i = -1;
					try {
						i = db.delete(User.class, dbw);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MySqlManagerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (i!=1) {
						he.setResponseCode("-9993");
						he.setResponseMsg("用户删除失败");
					}else {
						he.setResponseCode("0000");
						he.setResponseMsg("成功");
					}
				}
			} else {
				he.setResponseCode("-9998");
				he.setResponseMsg("请求码不支持");
			}
		}

		log("he=" + he);
		String respStr = gson.toJson(he);
		log("respStr=" + respStr);
		respStr = EncUtil.encryptAsString(null, respStr);
		log("加密后  respStr=" + respStr);
		// PrintWriter bw = resp.getWriter();
		// bw.write(respStr);
		// bw.flush();
		// bw.close();
		ServletOutputStream out = resp.getOutputStream();
		out.write(respStr.getBytes("utf-8"));
		out.flush();
		out.close();
	}
}
