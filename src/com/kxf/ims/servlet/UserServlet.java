package com.kxf.ims.servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kxf.ims.entity.HttpEntity;
import com.kxf.ims.entity.User;


public class UserServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
//		super.doPost(req, resp);
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
		Type typeOfT = new TypeToken<HttpEntity<User>>(){}.getType();
		HttpEntity<User> he = gson.fromJson(reStr, typeOfT);
		log("he=" + he);
		
		he.setResponseCode("0000");
		log("he=" + he);
		String respStr = gson.toJson(he);
		log("respStr=" + respStr);
//		PrintWriter bw = resp.getWriter();
//		bw.write(respStr);
//		bw.flush();
//		bw.close();
		ServletOutputStream out = resp.getOutputStream();
		out.write(respStr.getBytes("utf-8"));
		out.flush();
		out.close();
	}
}
