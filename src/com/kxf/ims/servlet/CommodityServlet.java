package com.kxf.ims.servlet;

import java.io.BufferedReader;
import java.io.IOException;
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
import com.kxf.ims.entity.Commodity;
import com.kxf.ims.entity.HttpEntity;
import com.kxf.ims.entity.User;
import com.kxf.ims.utils.EncUtil;
import com.kxf.ims.utils.StringUtils;
import com.kxf.ims.utils.ZipUtils;
import com.kxf.mysqlmanage.DBWhereBuilder;
import com.kxf.mysqlmanage.LogUtils.LogListener;
import com.kxf.mysqlmanage.MySqlManagerException;

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
		resp.setContentType("text/plain; charset=utf-8");
		BufferedReader reader = req.getReader();
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
		Type typeOfT = new TypeToken<HttpEntity<Commodity>>() {
		}.getType();
		HttpEntity<Commodity> he = gson.fromJson(reStr, typeOfT);
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
			if ("1000".equals(he.getRequestCode())) {// 查询单个，按二维码查询
				Commodity[] com = he.getTs();
				if (null == com || com.length != 1
						|| StringUtils.isEmpty(com[0].getQcode())) {
					he.setResponseCode("-9999");
					he.setResponseMsg("参数错误");
				} else {
					DBWhereBuilder dbw = new DBWhereBuilder("qcode", "=",
							com[0].getQcode());
					List<Commodity> ls = null;
					try {
						ls = db.find(Commodity.class, dbw);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MySqlManagerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
					List<Commodity> ls = null;
					try {
						ls = db.find(Commodity.class, dbw);
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
							i = db.save(com[0]);
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
							he.setResponseMsg("添加失败");
						}
					} else {
						he.setResponseCode("-9996");
						he.setResponseMsg("已存在");
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
					List<Commodity> ls = null;
					try {
						ls = db.find(Commodity.class, dbw);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MySqlManagerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					he.setResponseCode("0000");
					he.setResponseMsg("成功");
					he.setTs(ls.toArray(new Commodity[0]));
				}
			} else if ("1003".equals(he.getRequestCode())) {//通过qcode修改状态
				Commodity[] com = he.getTs();
				if (null == com || com.length != 1
						|| StringUtils.isEmpty(com[0].getQcode())) {
					he.setResponseCode("-9999");
					he.setResponseMsg("参数错误");
				} else {
					DBWhereBuilder dbw = new DBWhereBuilder("qcode", "=",
							com[0].getQcode());
					List<Commodity> ls = null;
					try {
						ls = db.find(Commodity.class, dbw);
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (MySqlManagerException e) {
						e.printStackTrace();
					}
					if (null == ls || ls.size()<1) {
						he.setResponseCode("-9994");
						he.setResponseMsg("要修改的数据未找到");
					}else {
						Commodity commodity = ls.get(0);
						commodity.setState(com[0].getState());
						int i = -1;
						try {
							i = db.update(commodity, dbw);
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
						}else {
							he.setResponseCode("-9993");
							he.setResponseMsg("修改失败");
						}
					}
					
				}
			} else if ("1004".equals(he.getRequestCode())) {//查询多个,按照权限查询
				Commodity[] com = he.getTs();
				if (null == com || com.length != 1
						|| StringUtils.isEmpty(com[0].getUserId() + "")) {
					he.setResponseCode("-9999");
					he.setResponseMsg("参数错误");
				} else {
					DBWhereBuilder dbw = new DBWhereBuilder("userId", "=",
							com[0].getUserId());
					List<Commodity> ls = null;
					try {
						ls = db.find(Commodity.class, dbw);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MySqlManagerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					he.setResponseCode("0000");
					he.setResponseMsg("成功");
					DBWhereBuilder dbw1 = new DBWhereBuilder("id", "=",
							com[0].getUserId());
					List<User> us = null;
					try {
						us = db.find(User.class, dbw1);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MySqlManagerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (null != us && us.size()>0) {
						DBWhereBuilder dbw2 = new DBWhereBuilder("permissions", "<",
								us.get(0).getPermissions());
						List<User> usLow = null;
						try {
							usLow = db.find(User.class, dbw2);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (MySqlManagerException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (null != usLow && usLow.size()>0) {
							for (User user : usLow) {
								DBWhereBuilder dbw4 = new DBWhereBuilder("userId", "=",
										user.getId());
								List<Commodity> ls4 = null;
								try {
									ls4 = db.find(Commodity.class, dbw4);
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (MySqlManagerException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (null != ls4 && ls4.size()>0) {
									ls.addAll(ls4);
								}
							}
						}
					}
					he.setTs(ls.toArray(new Commodity[0]));
				}
			} else if ("1005".equals(he.getRequestCode())) {//查询全部
				List<Commodity> ls = null;
				try {
					ls = db.findAll(Commodity.class);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MySqlManagerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				he.setTs(ls.toArray(new Commodity[0]));
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