package com.enation.app.shop.statistics.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.statistics.service.IB2b2cMemberStatisticsManager;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.enums.PayStatus;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;

@Service
public class B2b2cMemberStatisticsManager implements IB2b2cMemberStatisticsManager {

	@Autowired
	private IDaoSupport daoSupport;

	/**
	 * 会员下单量统计
	 * @author LSJ
	 * @param top_num 排名名次
	 * @param startDateStamp 开始时间
	 * @param endDateStamp 结束时间
	 * @param store_id 店铺ID
	 * @return 会员下单量 list
	 * 2016年12月6日上午11:38
	 */
	@Override
	public List<Map<String, Object>> getOrderNumTop(int topNum, String startDate, String endDate, String store_id) {

		// 如果排名没有值
		if (topNum == 0) {
			topNum = 15;
		}

		String dateWhere = ""; // 时间条件

		// 如果包含开始时间条件
		if (startDate != null && !"".equals(startDate)) {

			dateWhere += "AND o.create_time >= " + startDate;
		}
		// 如果包含结束时间条件
		if (endDate != null && !"".equals(endDate)) {

			dateWhere += " AND o.create_time <= " + endDate;
		}
		String storeWhere = "";
		if (store_id != null && !"0".equals(store_id)) {
			storeWhere = " AND  o.seller_id = " + store_id;
		}

		// 拼接Sql 条件是 网上支付且已经付款的 或者 货到付款且订单完成的 有效订单
		String sql = "SELECT m.member_id,m.name,m.nickname,count(o.order_id) AS num " + "FROM es_order o "
				+ "LEFT JOIN es_member m ON o.member_id = m.member_id WHERE m.disabled!=1 AND o.disabled = 0 AND ((o.payment_method_id = 3  AND o.order_status = '"
				+ OrderStatus.COMPLETE + "')	OR ( o.pay_status = '" + PayStatus.PAY_YES + "'))" + " " + dateWhere
				+ " " + storeWhere + " " + " GROUP BY m.member_id, m.name, m.nickname ";

		String mainSql = sql + "ORDER BY count(o.order_id) DESC";

		String countSql = "SELECT COUNT(*) FROM (" + sql + ") t0";
		Page page = this.daoSupport.queryForPage(mainSql, countSql, 1, topNum);
		List<Map<String, Object>> list = (List<Map<String, Object>>) page.getResult();

		return list;
	}

	/**
	 * 会员下单商品数量
	 * @author LSJ
	 * @param top_num 排行名次
	 * @param startDateStamp 开始时间
	 * @param endDateStamp 结束时间
	 * @param store_id 店铺ID
	 * @return 会员下单商品数量
	 * 2016年12月6日上午11:38
	 */
	@Override
	public List<Map<String, Object>> getGoodsNumTop(int topNum, String startDate, String endDate, String store_id) {

		// 如果排名没有值
		if (topNum == 0) {
			topNum = 15;
		}

		String dateWhere = ""; // 时间条件

		// 如果包含开始时间条件
		if (startDate != null && !"".equals(startDate)) {

			dateWhere += "AND o.create_time >= " + startDate;
		}
		// 如果包含结束时间条件
		if (endDate != null && !"".equals(endDate)) {

			dateWhere += " AND o.create_time <= " + endDate;
		}
		String storeWhere = "";
		if (store_id != null && !"0".equals(store_id)) {
			storeWhere = " AND o.seller_id = " + store_id;
		}

		// 拼接Sql 条件是 网上支付且已经付款的 或者 货到付款且订单完成的 订单
		String sql = "SELECT m.member_id, m.name, m.nickname, sum(i.num) AS num FROM es_order o"
				+ " LEFT JOIN es_member m ON o.member_id = m.member_id "
				+ "LEFT JOIN es_order_items i ON i.order_sn = o.sn  "
				+ "WHERE m.disabled!=1 AND o.disabled = 0 AND( (o.payment_method_id = 3 AND o.order_status = '"
				+ OrderStatus.COMPLETE + "') OR (o.pay_status = '" + PayStatus.PAY_YES + "'))" + " " + dateWhere
				+ " " + storeWhere + " GROUP BY m.member_id, m.name, m.nickname ";

		String mainSql = sql + " ORDER BY sum(i.num) DESC ";
		String countSql = "SELECT COUNT(*) FROM (" + sql + ") t0";

		Page page = this.daoSupport.queryForPage(mainSql, countSql, 1, topNum);

		return (List<Map<String, Object>>) page.getResult();
	}

	/**
	 * 会员下单总金额统计
	 * @author LSJ
	 * @param top_num 排行名次
	 * @param startDateStamp 开始时间
	 * @param endDateStamp 结束时间
	 * @param store_id 店铺ID
	 * @return 下单金额 list
	 * 2016年12月6日上午11:38
	 */
	@Override
	public List<Map<String, Object>> getOrderPriceTop(int topNum, String startDate, String endDate, String store_id) {

		// 如果排名没有值
		if (topNum == 0) {
			topNum = 15;
		}

		String dateWhere = ""; // 时间条件

		// 如果包含开始时间条件
		if (startDate != null && !"".equals(startDate)) {

			dateWhere += "AND o.create_time >= " + startDate;
		}
		// 如果包含结束时间条件
		if (endDate != null && !"".equals(endDate)) {

			dateWhere += " AND o.create_time <= " + endDate;
		}

		String storeWhere = "";
		if (store_id != null && !"0".equals(store_id)) {
			storeWhere = " AND o.seller_id = " + store_id;
		}

		// 拼接Sql 条件是 网上支付且已经付款的 或者 货到付款且订单完成的 有效订单
		String sql = "SELECT m.member_id,m.name,m.nickname,sum(o.need_pay_money) AS price " + "FROM es_order o "
				+ "LEFT JOIN es_member m ON o.member_id = m.member_id WHERE m.disabled!=1 AND o.disabled = 0 AND ((o.payment_method_id = 3  AND o.order_status = '"
				+ OrderStatus.COMPLETE + "')	OR ( o.pay_status = '" + PayStatus.PAY_YES + "'))" + " " + dateWhere
				+ " " + storeWhere + " GROUP BY m.member_id, m.name, m.nickname ";
		String mainSql = sql + " ORDER BY sum(o.need_pay_money) DESC";
		String countSql = "SELECT COUNT(*) FROM (" + sql + ") tmp0";

		Page page = this.daoSupport.queryForPage(mainSql, countSql, 1, topNum);

		return (List<Map<String, Object>>) page.getResult();
	}

	/**
	 * 客单价分布数据
	 * @author LSJ
	 * @param asList 区间
	 * @param startDateStamp 开始时间
	 * @param endDateStamp 结束时间
	 * @param store_id 店铺ID
	 * @return 客单价分布数据list
	 * 2016年12月6日上午11:38
	 */
	@Override
	public List<Map<String, Object>> getOrderPriceDis(List<Integer> sections, String startDate, String endDate,
			String store_id) {

		// 如果没有区间数据
		if (sections == null || sections.size() == 0) {

			throw new RuntimeException("区间不能为空");
		}

		String interval = ""; // INTERVAL 分组
		String dateWhere = ""; // 时间条件
		String intervalDesc = ""; // INTERVAL 描述
		List<Integer> list = new ArrayList<Integer>(); // 筛选后的区间值

		// 如果包含开始时间条件
		if (startDate != null && !"".equals(startDate)) {

			dateWhere += "AND o.create_time >= " + startDate;
		}
		// 如果包含结束时间条件
		if (endDate != null && !"".equals(endDate)) {

			dateWhere += " AND o.create_time <= " + endDate;
		}
		String storeWhere = "";
		if (store_id != null && !"0".equals(store_id)) {
			storeWhere = " AND o.seller_id = " + store_id;
		}

		// 遍历去除null值
		for (Integer temp : sections) {
			if (temp != null && !"".equals(temp)) {
				list.add(temp);
			}
		}
		/*
		 * Collections.sort(list); //先按照价格从小到大排序
		 * 
		 * //遍历区间数组 拼凑interval分组和描述 for (int i = 0; i < list.size(); i++ ) { int
		 * price = list.get(i); int nextPrice = 0; //下一个区间的价格
		 * 
		 * interval += price + ",";
		 * 
		 * //如果是最后一个区间 if (i == list.size() - 1) {
		 * 
		 * intervalDesc += "'" + price + "+',"; } else{
		 * 
		 * nextPrice = list.get(i + 1); intervalDesc += "'" + price + "~" +
		 * nextPrice + "',"; }
		 * 
		 * }
		 * 
		 * //减去最后一个逗号 interval = interval.substring(0, interval.length() - 1);
		 * intervalDesc = intervalDesc.substring(0, intervalDesc.length() - 1);
		 */

		String caseStatement = getOrderPriceDisSqlCaseStatement(list);
		String sql = "SELECT count(o.order_id) AS num, " + caseStatement
				+ " AS elt_data,o.need_pay_money FROM es_order o " + "WHERE 1=1 " + dateWhere + storeWhere
				+ " GROUP BY o.need_pay_money ";

		String mainSql = "SELECT SUM(t1.num) num, t1.elt_data, SUM(t1.need_pay_money) need_pay_money FROM(" + sql
				+ ") t1 " + "GROUP BY t1.elt_data ORDER BY t1.elt_data";

		List<Map<String, Object>> data = this.daoSupport.queryForList(mainSql);

		return data;
	}

	/**
	 * 生成价格销量统计的SQL CASE语句
	 * 
	 * @param ranges
	 *            整数数组
	 * @return SQL CASE Statement
	 */
	private static String getOrderPriceDisSqlCaseStatement(List<Integer> ranges) {
		if (ranges == null || ranges.size() == 0) {
			return "0";
		}
		// 由大到小排序
		Collections.sort(ranges, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				if (o1 > o2) {
					return -1;
				} else if (o1 < o2) {
					return 1;
				}
				return 0;
			}
		});

		StringBuilder sb = new StringBuilder("(case ");
		for (int i = 0; i < ranges.size(); i++) {
			Integer num = ranges.get(i);
			Integer nextNum = 0;
			if (i == 0) {
				sb.append("when o.need_pay_money > ").append(num).append(" then '").append(num).append("+' ");
			}
			if (i < ranges.size() - 1) {
				nextNum = ranges.get(i + 1);
				sb.append("when o.need_pay_money >= ").append(nextNum).append(" and o.need_pay_money <= ").append(num)
						.append(" then '").append(nextNum).append("~").append(num).append("' ");
			}
		}
		sb.append("else '0' end ) ");
		return sb.toString();
	}

	/**
	 * 用户购买频次数据
	 * @author LSJ
	 * @param startDateStamp 开始时间
	 * @param endDateStamp 结束时间
	 * @param store_id 店铺ID
	 * @return 用户购买频次数据 list
	 * 2016年12月6日上午11:38
	 */
	@Override
	public List<Map<String, Object>> getBuyFre(String startDate, String endDate, String store_id) {

		String dateWhere = ""; // 时间条件

		// 如果包含开始时间条件
		if (startDate != null && !"".equals(startDate)) {

			dateWhere += "AND o.create_time >= " + startDate;
		}
		// 如果包含结束时间条件
		if (endDate != null && !"".equals(endDate)) {

			dateWhere += " AND o.create_time <= " + endDate;
		}
		String storeWhere = "";
		if (store_id != null && !"0".equals(store_id)) {
			storeWhere = " AND o.seller_id = " + store_id;
		}

		//   查询购买总人数 sql
		String totalSql = "SELECT count(member_id) AS member_num FROM ("
				+ "SELECT count(o.member_id) AS num, o.member_id FROM es_order o " + "WHERE 1=1 " + dateWhere
				+ storeWhere + " GROUP BY o.member_id) pc";
		int totla = this.daoSupport.queryForInt(totalSql);

		// 正常数据
		String sql = "SELECT " + totla + " AS total_num, num AS buy_num, count(member_id) member_num FROM "
				+ "(SELECT count(o.member_id) AS num, o.member_id FROM es_order o " + "WHERE 1=1 " + dateWhere
				+ storeWhere + " GROUP BY o.member_id) pc GROUP BY num";

		List<Map<String, Object>> data = this.daoSupport.queryForList(sql);

		return data;
	}

	/**
	 * 用户购买时段分布数据
	 * @author LSJ
	 * @param startDateStamp 开始时间
	 * @param endDateStamp 结束时间
	 * @param store_id 店铺ID
	 * @return 用户购买时段分布数据 list
	 * 2016年12月6日上午11:38
	 */
	@Override
	public List<Map<String, Object>> getBuyTimeDis(String startDate, String endDate, String store_id) {
		String dateWhere = ""; // 时间条件

		// 如果包含开始时间条件
		if (startDate != null && !"".equals(startDate)) {

			dateWhere += "AND o.create_time >= " + startDate;
		}
		// 如果包含结束时间条件
		if (endDate != null && !"".equals(endDate)) {

			dateWhere += " AND o.create_time <= " + endDate;
		}
		String storeWhere = "";
		if (store_id != null && !"0".equals(store_id)) {
			storeWhere = " AND o.seller_id = " + store_id;
		}

		String db_type = EopSetting.DBTYPE;
		String dateFunction = "";
		if (db_type.equals("1")) { // mysql
			dateFunction = "CONVERT(FROM_UNIXTIME(o.create_time, '%k'),SIGNED)";
		} else if (db_type.equals("2")) { // oracle
			dateFunction = "TO_NUMBER(" + this.getOracleTimeFormatFunc("o.create_time", "hh24") + ")";
		} else if (db_type.equals("3")) { // sqlserver
			// TODO 数值型（单位：秒）表示的日期格式转换 for SQLServer
			dateFunction = "DATEPART(dd,DATEADD(SECOND, o.create_time, '1970-01-01 08:00:00'))";
		}

		String sql = "SELECT count(o.order_id) AS num, " + dateFunction + " AS hour_num " + "FROM es_order o "
				+ "WHERE 1=1 " + dateWhere + storeWhere + " GROUP BY " + dateFunction
				+ " ORDER BY hour_num";

		List<Map<String, Object>> data = this.daoSupport.queryForList(sql);

		return data;
	}

	/**
	 * 会员新增  按月统计 【当前月份】
	 * @author LSJ
	 * @param startDateStamp
	 * @param endDateStamp
	 * @return 按月统计list
	 * 2016年12月6日上午11:38
	 */
	@Override
	public List<Map<String, Object>> getAddMemberNum(String startDate, String endDate) {

		String dateWhere = ""; // 时间条件

		// 如果包含开始时间条件
		if (startDate != null && !"".equals(startDate)) {

			dateWhere += "AND o.create_time >= " + startDate;
		}
		// 如果包含结束时间条件
		if (endDate != null && !"".equals(endDate)) {

			dateWhere += " AND o.create_time <= " + endDate;
		}

		String db_type = EopSetting.DBTYPE;
		String dateFunction = "";
		if (db_type.equals("1")) { // mysql
			dateFunction = "FROM_UNIXTIME(m.regtime, '%d')";
		} else if (db_type.equals("2")) { // oracle
			dateFunction = this.getOracleTimeFormatFunc("m.regtime", "dd");
		} else if (db_type.equals("3")) { // sqlserver
			// TODO 数值型（单位：秒）表示的日期格式转换 for SQLServer
			dateFunction = "DATEPART(dd,DATEADD(SECOND, m.regtime, '1970-01-01 08:00:00'))";
		}

		// 拼接Sql 条件是 网上支付且已经付款的 或者 货到付款且订单完成的 有效订单
		String sql = "SELECT count(em.member_id) as membernum , em.e_regtime as membertime FROM "
				+ "( SELECT m.member_id,m.regtime, " + dateFunction
				+ " AS e_regtime FROM es_member m where m.disabled!=1 and m.regtime >= " + startDate
				+ " and  m.regtime <= " + endDate + ")  em " + "GROUP BY em.e_regtime";
		List<Map<String, Object>> list = this.daoSupport.queryForList(sql);

		return list;
	}

	/**
	 * 数值型（单位：秒）表示的日期格式转换 for Oracle
	 * 
	 * @param col
	 *            列名，如：r.name
	 * @param pattern
	 *            转换格式如：yyyy-mm-dd hh24:mi:ss
	 * @return SQL语句片段
	 */
	private String getOracleTimeFormatFunc(String col, String pattern) {
		String func = "to_char(" + "TO_DATE('19700101','yyyymmdd') + " + col
				+ "/86400 + TO_NUMBER(SUBSTR(TZ_OFFSET(sessiontimezone),1,3))/24, '" + pattern + "')";
		return func;
	}

	/**
	 * 会员新增 按月统计 【上一个月】
	 * @author LSJ
	 * @param lastStartDate 开始时间
	 * @param lastEndDate 结束时间
	 * @return 按月统计list
	 * 2016年12月6日上午11:38
	 */
	@Override
	public List<Map<String, Object>> getLastAddMemberNum(String lastStartDate, String lastEndDate) {

		String dateWhere = ""; // 时间条件

		// 如果包含开始时间条件
		if (lastStartDate != null && !"".equals(lastStartDate)) {

			dateWhere += "AND o.create_time >= " + lastStartDate;
		}
		// 如果包含结束时间条件
		if (lastEndDate != null && !"".equals(lastEndDate)) {

			dateWhere += " AND o.create_time <= " + lastEndDate;
		}

		String db_type = EopSetting.DBTYPE;
		String dateFunction = "";
		if (db_type.equals("1")) { // mysql
			dateFunction = "FROM_UNIXTIME(m.regtime, '%d')";
		} else if (db_type.equals("2")) { // oracle
			dateFunction = this.getOracleTimeFormatFunc("m.regtime", "dd");
		} else if (db_type.equals("3")) { // sqlserver
			// TODO 数值型（单位：秒）表示的日期格式转换 for SQLServer
			dateFunction = "DATEPART(dd,DATEADD(SECOND, m.regtime, '1970-01-01 08:00:00'))";
		}

		// 拼接Sql 条件是 网上支付且已经付款的 或者 货到付款且订单完成的 有效订单

		String sql = "SELECT count(em.member_id) as membernum , em.e_regtime as membertime FROM "
				+ "( SELECT m.member_id,m.regtime, " + dateFunction
				+ " AS e_regtime FROM es_member m where m.disabled!=1 AND m.regtime >= " + lastStartDate
				+ " and  m.regtime <= " + lastEndDate + ") em " + "GROUP BY em.e_regtime";

		List<Map<String, Object>> list = this.daoSupport.queryForList(sql);

		return list;
	}

	/**
	 * 会员新增  按年统计 【当前年份】
	 * @author LSJ
	 * @param startDateStamp 开始时间 
	 * @param endDateStamp 结束时间
	 * @return 按年统计 list
	 * 2016年12月6日上午11:38
	 */
	@Override
	public List<Map<String, Object>> getAddYearMemberNum(String startDate, String endDate) {

		String dateWhere = ""; // 时间条件

		// 如果包含开始时间条件
		if (startDate != null && !"".equals(startDate)) {

			dateWhere += "AND o.create_time >= " + startDate;
		}
		// 如果包含结束时间条件
		if (endDate != null && !"".equals(endDate)) {

			dateWhere += " AND o.create_time <= " + endDate;
		}

		String db_type = EopSetting.DBTYPE;
		String dateFunction = "";
		if (db_type.equals("1")) { // mysql
			dateFunction = "FROM_UNIXTIME(m.regtime, '%m')";
		} else if (db_type.equals("2")) { // oracle
			dateFunction = this.getOracleTimeFormatFunc("m.regtime", "mm");
		} else if (db_type.equals("3")) { // sqlserver
			// TODO 数值型（单位：秒）表示的日期格式转换 for SQLServer
			dateFunction = "DATEPART(dd,DATEADD(SECOND, m.regtime, '1970-01-01 08:00:00'))";
		}

		// 拼接Sql 条件是 网上支付且已经付款的 或者 货到付款且订单完成的 有效订单

		String sql = "SELECT count(em.member_id) as membernum , em.e_regtime as membertime FROM "
				+ "( SELECT m.member_id,m.regtime, " + dateFunction
				+ " AS e_regtime FROM es_member m where m.disabled!=1 AND m.regtime >= " + startDate
				+ " and  m.regtime <= " + endDate + ") em " + "GROUP BY em.e_regtime";

		List<Map<String, Object>> list = this.daoSupport.queryForList(sql);

		return list;
	}

	/**
	 * 会员新增 按年统计 【上一年】
	 * @author LSJ
	 * @param lastStartDateStamp
	 * @param lastEndDateStamp
	 * @return 按年统计list
	 * 2016年12月6日上午11:38
	 */
	@Override
	public List<Map<String, Object>> getLastAddYearMemberNum(String lastStartDate, String lastEndDate) {

		String dateWhere = ""; // 时间条件

		// 如果包含开始时间条件
		if (lastStartDate != null && !"".equals(lastStartDate)) {

			dateWhere += "AND o.create_time >= " + lastStartDate;
		}
		// 如果包含结束时间条件
		if (lastEndDate != null && !"".equals(lastEndDate)) {

			dateWhere += " AND o.create_time <= " + lastEndDate;
		}

		String db_type = EopSetting.DBTYPE;
		String dateFunction = "";
		if (db_type.equals("1")) { // mysql
			dateFunction = "FROM_UNIXTIME(m.regtime, '%m')";
		} else if (db_type.equals("2")) { // oracle
			dateFunction = this.getOracleTimeFormatFunc("m.regtime", "mm");
		} else if (db_type.equals("3")) { // sqlserver
			// TODO 数值型（单位：秒）表示的日期格式转换 for SQLServer
			dateFunction = "DATEPART(dd,DATEADD(SECOND, m.regtime, '1970-01-01 08:00:00'))";
		}
		// 拼接Sql 条件是 网上支付且已经付款的 或者 货到付款且订单完成的 有效订单

		String sql = "SELECT count(em.member_id) as membernum , em.e_regtime as membertime FROM "
				+ "( SELECT m.member_id,m.regtime, " + dateFunction
				+ " AS e_regtime FROM es_member m where m.disabled!=1 AND m.regtime >= " + lastStartDate
				+ " and  m.regtime <= " + lastEndDate + ") em " + "GROUP BY em.e_regtime";

		List<Map<String, Object>> list = this.daoSupport.queryForList(sql);

		return list;
	}

}
