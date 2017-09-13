package com.kxf.ims.servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
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
import com.kxf.ims.utils.StringUtils;
import com.kxf.mysqlmanage.DBWhereBuilder;

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
		resp.setContentType("application/json");
		BufferedReader reader = req.getReader();
		String reStr = "";
		String line = "";
		while (null != (line = reader.readLine())) {
			reStr += line;
		}
		reader.close();
		log("reStr=" + reStr);
		Gson gson = new Gson();
		Type typeOfT = new TypeToken<HttpEntity<User>>() {
		}.getType();
		HttpEntity<User> he = gson.fromJson(reStr, typeOfT);
		log("he=" + he);

		if (null == he) {
			he.setResponseCode("-9999");
			he.setResponseMsg("参数错误");
		} else {
			MyDBManage db = new MyDBManage();
			if ("1000".equals(he.getRequestCode())) {// 查询单个
				User[] us = he.getTs();
				if (null == us || us.length != 1
						|| StringUtils.isEmpty(us[0].getName())) {
					he.setResponseCode("-9999");
					he.setResponseMsg("参数错误");
				} else {
					DBWhereBuilder dbw = new DBWhereBuilder("name", "=",
							us[0].getName());
					List<User> ls = db.find(User.class, dbw);
					if (null == ls || ls.size() < 1) {
						if ("root".equals(us[0].getName())) {// "root".equals(us[0].getName())
							User user = new User();
							user.setName("root");
							user.setPw("qazwsx");
							user.setPermissions(2);
							db.save(user);
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
					List<User> ls = db.find(User.class, dbw);
					if (null == ls || ls.size() < 1) {
						int i = db.save(us[0]);
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
