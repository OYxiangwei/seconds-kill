package com.oy.secondskill.web;

import com.alibaba.fastjson.JSONObject;
import com.oy.secondskill.common.entity.Result;
import com.oy.secondskill.common.entity.SecondsKill;
import com.oy.secondskill.common.utils.HttpClient;
import com.oy.secondskill.common.utils.IPUtils;
import com.oy.secondskill.queue.activemq.ActiveMQSender;
import com.oy.secondskill.service.ISeckillService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Api(tags = "秒杀商品")
@RestController
@RequestMapping("/seckillPage")
public class SeckillPageController {
    @Autowired
    private ISeckillService seckillService;

    @Autowired
    private ActiveMQSender activeMQSender;

    @Autowired
    private HttpClient httpClient;
    @Value("${qq.captcha.url}")
    private String url;
    @Value("${qq.captcha.aid}")
    private String aid;
    @Value("${qq.captcha.AppSecretKey}")
    private String appSecretKey;

    @ApiOperation(value = "秒杀商品列表", nickname = "oyxw")
    @PostMapping("/list")
    public Result list() {
        //返回JSON数据、前端VUE迭代即可
        List<SecondsKill> List = seckillService.getSeckillList();
        return Result.ok(List);
    }

    @PostMapping("/startSeckill")
    public Result  startSeckill(String ticket, String randstr, HttpServletRequest request) {
        HttpMethod method =HttpMethod.POST;
        MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
        params.add("aid", aid);
        params.add("AppSecretKey", appSecretKey);
        params.add("Ticket", ticket);
        params.add("Randstr", randstr);
        params.add("UserIP", IPUtils.getIpAddr());
        String msg = httpClient.client(url,method,params);
        /**
         * response: 1:验证成功，0:验证失败，100:AppSecretKey参数校验错误[required]
         * evil_level:[0,100]，恶意等级[optional]
         * err_msg:验证错误信息[optional]
         */
        //{"response":"1","evil_level":"0","err_msg":"OK"}
        JSONObject json = JSONObject.parseObject(msg);
        String response = (String) json.get("response");
        if("1".equals(response)){
            //进入队列、假数据而已
            Destination destination = new ActiveMQQueue("seckill.queue");
            activeMQSender.sendChannelMess(destination,1000+";"+1);
            return Result.ok();
        }else{
            return Result.error("验证失败");
        }
    }

}
