package com.enation.app.shop.component.payment;

import org.springframework.beans.factory.annotation.Autowired;

import com.enation.app.base.core.plugin.job.IEveryHourExecuteEvent;
import com.enation.app.shop.payment.service.IOrderPayManager;
import com.enation.framework.plugin.AutoRegister;
import com.enation.framework.plugin.AutoRegisterPlugin;
/**
 * 
 * 查询退款进度
 * @author fk
 * @version v6.5.0
 * @since v6.5.1
 * 2017年8月2日11:03:12
 */
@AutoRegister
public class RefundQueryPlugin extends AutoRegisterPlugin implements IEveryHourExecuteEvent{

	@Autowired
	private  IOrderPayManager orderPayManager;

	@Override
	public void everyHour() {
		
		this.orderPayManager.queryRefundOrderStatus();
	}
	
	
}
