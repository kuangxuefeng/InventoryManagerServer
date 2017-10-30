package com.kxf.ims.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kxf.ims.entity.FileBean;
import com.kxf.ims.entity.HttpEntity;
import com.kxf.ims.utils.EncUtil;
import com.kxf.ims.utils.FileUtils;
import com.kxf.ims.utils.StringUtils;

public class FileServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/plain; charset=utf-8");
//		BufferedReader reader = req.getReader();
		String filePath = req.getHeader("File_path");
		log("filePath=" + filePath);
		log("请求者地址" + req.getRemoteAddr());
		String respStr = "-1000";
		
		File f = new File("D:\\Log\\s"+ File.separator + filePath);
		FileUtils.creatFile(f);
		FileOutputStream outF = new FileOutputStream(f);
		ServletInputStream in = req.getInputStream();
		byte[] bs = new byte[1024];
        while (in.read(bs)>=0) {
        	outF.write(bs);
		}
        in.close();
		outF.flush();
		outF.close();
		respStr = "0000";
		
		ServletOutputStream out = resp.getOutputStream();
		out.write(respStr.getBytes("utf-8"));
		out.flush();
		out.close();
	}

}
