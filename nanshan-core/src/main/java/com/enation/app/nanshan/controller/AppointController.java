package com.enation.app.nanshan.controller;

import com.enation.app.base.core.model.Member;
import com.enation.app.nanshan.model.ArticleExt;
import com.enation.app.nanshan.model.NanShanActReserve;
import com.enation.app.nanshan.service.IActReserveService;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yulong on 18/1/1.
 */
@Api(description="预约活动")
@RestController
@RequestMapping("/activity-operation")
@Validated
public class AppointController {

    @Autowired
    private IActReserveService actReserveService;

    @ApiOperation(value="活动预约", notes="活动预约")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameAccount", value = "游戏帐号", required = true, dataType = "String",paramType="query"),
            @ApiImplicitParam(name = "points", value = "点数", required =true, dataType = "int" ,paramType="query" ),
    })
    @ResponseBody
    @PostMapping(value="/appoint")
    public JsonResult create(int activity_id, String activity_time, String member_name,int num,
                              String phone_number, String email){

        try {

            Member member  = UserConext.getCurrentMember();

            if(member == null){
                return JsonResultUtil.getErrorJson("not login");
            }
            ArticleExt ext= actReserveService.queryArticleExt(activity_id);
            if(ext==null){
            	 return JsonResultUtil.getErrorJson("预约活动不存在");
            }
            if(ext.getReserved_num()>=ext.getReserve_num()){
            	 return JsonResultUtil.getErrorJson("预约人数已满");
            }
            if((ext.getReserved_num() + num) > ext.getReserve_num()){
                return JsonResultUtil.getErrorJson("只剩余" + (ext.getReserve_num() - ext.getReserved_num()) + "个席位!");
            }


            NanShanActReserve tmp = actReserveService.queryReserveByMemberId(member.getMember_id(), activity_id);
            if(tmp != null){
                return JsonResultUtil.getErrorJson("您已经预约该活动");
            }


            NanShanActReserve reserve = new NanShanActReserve();
            reserve.setActivity_id(activity_id);
            if(StringUtils.isNotBlank(activity_time)){
                reserve.setActivity_time(DateUtil.getDateline(activity_time));
            }
            reserve.setAttend_name(member_name);
            reserve.setPhone_number(phone_number);
            reserve.setEmail(email);
            reserve.setMember_id(member.getMember_id());
            reserve.setNum(num);

            actReserveService.reserve(reserve);

        } catch (Exception e) {
            if (!StringUtil.isEmpty(e.getMessage())) {
                return JsonResultUtil.getErrorJson(e.getMessage());
            }
            return JsonResultUtil.getErrorJson("预约失败");
        }
        return JsonResultUtil.getSuccessJson("预约成功!");


    }
    
    @ResponseBody
    @PostMapping(value="/cancel")
    public JsonResult cancel(int activity_id){

        try {
            Member member  = UserConext.getCurrentMember();
            if(member == null){
                return JsonResultUtil.getErrorJson("not login");
            }
            NanShanActReserve reserve = new NanShanActReserve();

            reserve.setMember_id(member.getMember_id());
            reserve.setActivity_id(activity_id);
            actReserveService.cancelReserve(reserve);
        } catch (Exception e) {
            if (!StringUtil.isEmpty(e.getMessage())) {
                return JsonResultUtil.getErrorJson(e.getMessage());
            }
            return JsonResultUtil.getErrorJson("取消预约失败");
        }
        return JsonResultUtil.getSuccessJson("取消预约成功!");

    }

    @ResponseBody
    @PostMapping(value="/isAppoint")
    public JsonResult isAppoint(int activity_id){

        try {
            Member member  = UserConext.getCurrentMember();

            if(member == null){
                return JsonResultUtil.getErrorJson("not login");
            }

            NanShanActReserve tmp = actReserveService.queryReserveByMemberId(member.getMember_id(), activity_id);
            if(tmp != null){
                return JsonResultUtil.getErrorJson("您已经预约该活动");
            }
        } catch (Exception e) {
            return JsonResultUtil.getSuccessJson("系统异常!");
        }
        return JsonResultUtil.getSuccessJson("已预约");


    }

}
