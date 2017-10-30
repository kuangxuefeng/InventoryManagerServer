package com.kxf.ims.servlet;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kxf.ims.utils.EncUtil;
import com.kxf.ims.utils.ZipUtils;

public class TestServlet extends HttpServlet {
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
		String reStr = "";
		String line = "";
		while (null != (line = reader.readLine())) {
			reStr += line;
		}
		reader.close();
		String unZip = EncUtil.desEncryptAsStringBeforeUnZip(null, reStr);
		log("unZip=" + unZip);
//		long millis = (long) (Math.random()*1000*20);
//		log("reStr=" + reStr + ";休眠时间：" + (millis/1000));
//		try {
//			Thread.sleep(millis);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		String respStr = reStr;// + "休眠时间：" + (millis/1000);
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
