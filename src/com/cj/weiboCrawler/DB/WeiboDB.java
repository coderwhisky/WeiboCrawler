package com.cj.weiboCrawler.DB;

/**
 * 将抓下来的数据存入数据库，当前只支持mysql
 * @author 北邮君君  weibobee@gmail.com   2011-9-1
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cj.weiboCrawler.congifure.ParasSet;
import com.cj.weiboCrawler.status.Status;
import com.cj.weiboCrawler.user.CreateTime;
import com.cj.weiboCrawler.user.FansAndFollsList;
import com.cj.weiboCrawler.user.UserProfile;

public class WeiboDB {
	public Connection connect = null;
	PreparedStatement insert_pst = null;
	PreparedStatement query_pst = null;
	public int statusNum = 0;

	public WeiboDB(String dbName, String table) {
		this.reginster();
		this.conDB(dbName);
	}

	public Connection getConnect() {
		return connect;
	}

	public void setConnect(Connection connect) {
		this.connect = connect;
	}

	public PreparedStatement getInsert_pst() {
		return insert_pst;
	}

	public void setInsert_pst(PreparedStatement insert_pst) {
		this.insert_pst = insert_pst;
	}

	public PreparedStatement getQuery_pst() {
		return query_pst;
	}

	public void setQuery_pst(PreparedStatement query_pst) {
		this.query_pst = query_pst;
	}

	/**
	 * 加载mysql驱动
	 */
	public void reginster() {
		try {
			Class.forName("com.mysql.jdbc.Driver"); // 加载MYSQL JDBC驱动程序
			// System.out.println("Success loading Mysql Driver!");
		} catch (Exception e) {
			System.out.print("Error loading Mysql Driver!");
			e.printStackTrace();
		}
	}

	/**
	 * 连接数据库
	 * 
	 * @return 连接对象
	 */
	public Connection conDB(String dbName) {
		Connection connect = null;
		try {
			connect = DriverManager.getConnection("jdbc:mysql://localhost/"
					+ dbName + "?useUnicode=true&characterEncoding=utf-8",
					"root", "123456");
			// 连接URL为 jdbc:mysql//服务器地址/数据库名
			// 后面的2个参数分别是登陆用户名和密码
			System.out.println("Success connect Mysql server!");
		} catch (Exception e) {
			System.out.print("get data error!");
			e.printStackTrace();
		}
		this.connect = connect;
		return connect;
	}

	/**
	 * 
	 * @param查询table里的信息 条件是situation
	 * @param table
	 *            要插入的表
	 * @return 结果ResultSet对象
	 */
	private ResultSet QueryStatus(String mid) {
		ResultSet r = null;
		try {
			query_pst.clearBatch();
			query_pst.setString(1, mid);
			r = query_pst.executeQuery();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return r;
	}

	/**
	 * 
	 * @param查询table里的信息 条件是situation
	 * @param table
	 *            要插入的表
	 * @return 结果ResultSet对象
	 */
	public ResultSet QueryUser(String uid) {
		ResultSet r = null;
		try {
			query_pst.clearBatch();
			query_pst.setString(1, uid);
			r = query_pst.executeQuery();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return r;
	}

	/**
	 * 插入微博
	 * 
	 * @param bib
	 * @param table
	 */
	public void insertStatus(Status status) {
		ResultSet r = QueryStatus(status.getMid());
		try {
			if (r.next() == false) {
				try {
					insert_pst.clearBatch();
					insert_pst.setString(1, status.getMid());
					insert_pst.setString(2, status.getEncrypted_mid());
					insert_pst.setInt(3, status.getIsReposted());
					insert_pst.setString(4, status.getUserId());
					insert_pst.setString(5, status.getUserName());
					insert_pst.setString(6, status.getRootUserId());
					insert_pst.setString(7, status.getRootUserName());
					insert_pst.setString(8, status.getRootMid());
					insert_pst.setString(9, status.getEncrypted_rootMid());
					insert_pst.setString(10, status.getStatusText());
					insert_pst.setInt(11, status.getHasPic());
					insert_pst.setInt(12, status.getPicsNum());
					insert_pst.setString(13, status.getMultiPicsUrl());
					insert_pst.setString(14, status.getSigPicsUrl());
					insert_pst.setLong(15, status.getRootPraiseNum());
					insert_pst.setLong(16, status.getRootRepostNum());
					insert_pst.setLong(17, status.getRootCommentNum());
					insert_pst.setString(18, status.getRepostReson());
					insert_pst.setLong(19, status.getPraiseNum());
					insert_pst.setLong(20, status.getRepostNum());
					insert_pst.setLong(21, status.getCommentNum());
					insert_pst.setInt(22, status.getFavouriteNum());
					insert_pst.setString(23, status.getCreateTime());
					insert_pst.setLong(24, status.getTimeStamp());
					insert_pst.setString(25, status.getSource());
					insert_pst.setLong(26, status.getFetchTime());
					insert_pst.setString(27, status.getFetchTimeFormat());
					insert_pst.setString(28, ParasSet.userType);

					int update_num = insert_pst.executeUpdate();

				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println(insert_pst.getUpdateCount());
				} finally {
					if (insert_pst != null) {
						// insert_pst.close();
					}
				}
			} else {
				System.out.println("\n————该条微博已经存在于数据库————\n");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			System.out.println("存储微博正常！");
			try {
				if (r != null) {
					r.close();
					r = null;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 插入用户信息
	 * 
	 * @param
	 * @param
	 */
	public void insertUser(UserProfile profile, FansAndFollsList faf,
			CreateTime ct) {
		try {
			insert_pst.clearBatch();
			insert_pst.setString(1, profile.getUid());
			insert_pst.setString(2, profile.getName());
			insert_pst.setInt(3, profile.getSex());
			insert_pst.setString(4, profile.getLocation());
			insert_pst.setInt(5, profile.getIsOnline());
			insert_pst.setInt(6, profile.getuRank());
			insert_pst.setInt(7, profile.getIsMember());
			insert_pst.setInt(8, profile.getIsDaren());
			insert_pst.setInt(9, profile.getIsVerified());
			insert_pst.setString(10, profile.getVerifiedReson());
			insert_pst.setString(11, profile.getDescription());
			insert_pst.setString(12, ct.getCreateTime());
			insert_pst.setLong(13, ct.getTimeStamp());
			insert_pst.setLong(14, profile.getStatuseNum());
			insert_pst.setLong(15, profile.getFriendNum());
			insert_pst.setLong(16, profile.getFollowerNum());
			insert_pst.setInt(17, profile.getMemRank());
			insert_pst.setInt(18, profile.getMedalNum());
			insert_pst.setString(19, profile.getBirthday());
			insert_pst.setString(20, profile.getSexPrefer());
			insert_pst.setString(21, profile.getFeeling());
			insert_pst.setString(22, profile.getInterest());
			if (ct.getTags() == null) {
				insert_pst.setString(23, profile.getTag());
			} else {
				insert_pst.setString(23, ct.getTags());
			}
			insert_pst.setString(24, profile.getDomain());
			insert_pst.setString(25, profile.getExperience());
			insert_pst.setString(26, profile.getAvatar());
			insert_pst.setLong(27, profile.getFetchTime());
			insert_pst.setString(28, faf.getFriendsList());
			insert_pst.setString(29, faf.getFollowersList());
			insert_pst.setString(30, ParasSet.userType);

			int update_num = insert_pst.executeUpdate();
			System.out.println("存储用户资料正常！");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			// System.out.println(insert_pst.getUpdateCount());
		} finally {
			if (insert_pst != null) {
				// insert_pst.close();
			}
		}
	}

	/*
	 * public void insertUser(UserProfile profile, FansAndFollsList faf,
	 * CreateTime ct,String table,String userType) { ResultSet r =
	 * QueryUser(profile.getUid()); try { if (r.next() == false) try {
	 * insert_pst.clearBatch(); insert_pst.setString(1, profile.getUid());
	 * insert_pst.setString(2, profile.getName()); insert_pst.setInt(3,
	 * profile.getSex()); insert_pst.setString(4, profile.getLocation());
	 * insert_pst.setInt(5, profile.getIsOnline()); insert_pst.setInt(6,
	 * profile.getuRank()); insert_pst.setInt(7, profile.getIsMember());
	 * insert_pst.setInt(8, profile.getIsDaren()); insert_pst.setInt(9,
	 * profile.getIsVerified()); insert_pst.setString(10,
	 * profile.getVerifiedReson()); insert_pst.setString(11,
	 * profile.getDescription()); insert_pst.setString(12, ct.getCreateTime());
	 * insert_pst.setLong(13, ct.getTimeStamp()); insert_pst.setLong(14,
	 * profile.getStatuseNum()); insert_pst.setLong(15, profile.getFriendNum());
	 * insert_pst.setLong(16, profile.getFollowerNum()); insert_pst.setInt(17,
	 * profile.getMemRank()); insert_pst.setInt(18, profile.getMedalNum());
	 * insert_pst.setString(19, profile.getBirthday()); insert_pst.setString(20,
	 * profile.getSexPrefer()); insert_pst.setString(21, profile.getFeeling());
	 * insert_pst.setString(22, profile.getInterest()); if(ct.getTags()==null){
	 * insert_pst.setString(23, profile.getTag()); }else{
	 * insert_pst.setString(23, ct.getTags()); } insert_pst.setString(24,
	 * profile.getDomain()); insert_pst.setString(25, profile.getExperience());
	 * insert_pst.setString(26, profile.getAvatar()); insert_pst.setLong(27,
	 * profile.getFetchTime()); insert_pst.setString(28, faf.getFriendsList());
	 * insert_pst.setString(29, faf.getFollowersList());
	 * insert_pst.setString(30, userType);
	 * 
	 * int update_num = insert_pst.executeUpdate();
	 * System.out.println("存储用户资料正常！"); } catch (SQLException e1) { // TODO
	 * Auto-generated catch block e1.printStackTrace();
	 * System.out.println(insert_pst.getUpdateCount()); } finally { if
	 * (insert_pst != null) { // insert_pst.close(); } } } catch (SQLException
	 * e) { // TODO Auto-generated catch block e.printStackTrace(); } finally {
	 * try { if (r != null) { r.close(); r = null; } } catch (SQLException e) {
	 * // TODO Auto-generated catch block e.printStackTrace(); } } }
	 */

	/**
	 * 取得现在总共有多少信息
	 * 
	 * @return
	 */
	public int getTotal(String table) {

		String sql = "select count(*) from " + table;
		int ret = -1;
		ResultSet r = null;
		try {
			query_pst.clearBatch();
			r = query_pst.executeQuery(sql);
			if (r.next())
				ret = r.getInt(1);
			return ret;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return -1;
		}
	}

	public int getStatusNum() {
		return statusNum;
	}

	public void setStatusNum(int constant) {
		statusNum = constant;
	}
}
