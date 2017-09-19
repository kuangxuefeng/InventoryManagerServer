package com.kxf.ims.servlet;

import java.io.BufferedReader;
import java.io.IOException;
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
import com.kxf.ims.entity.Commodity;
import com.kxf.ims.entity.HttpEntity;
import com.kxf.ims.utils.StringUtils;
import com.kxf.mysqlmanage.DBWhereBuilder;

public class CommodityServlet extends HttpServlet {
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
		Type typeOfT = new TypeToken<HttpEntity<Commodity>>() {
		}.getType();
		HttpEntity<Commodity> he = gson.fromJson(reStr, typeOfT);
		log("he=" + he);

		if (null == he) {
			he.setResponseCode("-9999");
			he.setResponseMsg("参数错误");
		} else {
			MyDBManage db = new MyDBManage();
			if ("1000".equals(he.getRequestCode())) {// 查询单个，按二维码查询
				Commodity[] com = he.getTs();
				if (null == com || com.length != 1
						|| StringUtils.isEmpty(com[0].getQcode())) {
					he.setResponseCode("-9999");
					he.setResponseMsg("参数错误");
				} else {
					DBWhereBuilder dbw = new DBWhereBuilder("qcode", "=",
							com[0].getQcode());
					List<Commodity> ls = db.find(Commodity.class, dbw);
					if (null == ls || ls.size() < 1) {
						he.setResponseCode("-9997");
						he.setResponseMsg("未查到");
					} else {
						he.setResponseCode("0000");
						he.setResponseMsg("成功");
						he.setTs(ls.toArray(new Commodity[0]));
					}
				}
			} else if ("1001".equals(he.getRequestCode())) {//添加
				Commodity[] com = he.getTs();
				if (null == com || com.length != 1
						|| StringUtils.isEmpty(com[0].getQcode())) {
					he.setResponseCode("-9999");
					he.setResponseMsg("参数错误");
				} else {
					DBWhereBuilder dbw = new DBWhereBuilder("qcode", "=",
							com[0].getQcode());
					List<Commodity> ls = db.find(Commodity.class, dbw);
					if (null == ls || ls.size() < 1) {
						int i = db.save(com[0]);
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
						he.setTs(ls.toArray(new Commodity[0]));
					}
				}
			} else if ("1002".equals(he.getRequestCode())) {//查询多个,按照userId查询
				Commodity[] com = he.getTs();
				if (null == com || com.length != 1
						|| StringUtils.isEmpty(com[0].getUserId() + "")) {
					he.setResponseCode("-9999");
					he.setResponseMsg("参数错误");
				} else {
					DBWhereBuilder dbw = new DBWhereBuilder("userId", "=",
							com[0].getUserId());
					List<Commodity> ls = db.find(Commodity.class, dbw);
					he.setResponseCode("0000");
					he.setResponseMsg("成功");
					he.setTs(ls.toArray(new Commodity[0]));
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