package com.jno.cloud.framework.auth.controller;


import com.jno.cloud.framework.util.result.Result;
import com.jno.cloud.framework.util.tool.CommonUtil;
import com.jno.cloud.framework.util.tool.CreateVerifyCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

//@ApiImplicitParams：用在请求的方法上，包含一组参数说明
//@ApiImplicitParam：对单个参数的说明
//        name：参数名
//        value：参数的说明、描述
//        required：参数是否必须必填
//        paramType：参数放在哪个地方
//        · query --> 请求参数的获取：@RequestParam
//	        · header --> 请求参数的获取：@RequestHeader
//	        · path（用于restful接口）--> 请求参数的获取：@PathVariable
//	        · body（请求体）-->  @RequestBody User user
//        · form（普通表单提交）
//        dataType：参数类型，默认String，其它值dataType="Integer"
//        defaultValue：参数的默认值

/**
 * 验证码接口
 */
@RestController
@RequestMapping("/captcha")
@Api(value = "验证码接口")
public class CaptchaController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping(value = "/init",method = RequestMethod.GET)
    @ApiOperation(value = "验证码初始化", notes = "随机验证码初始")
    public Result initCaptcha() {
        String captchaId = CommonUtil.GUID();
        String code = new CreateVerifyCode().randomStr(4);
        // 缓存验证码
        redisTemplate.opsForValue().set(captchaId, code,2L, TimeUnit.MINUTES);
        return new Result(Result.SUCCESS, captchaId, "初始化验证码");
    }

    @RequestMapping(value = "/draw/{captchaId}", method = RequestMethod.GET)
    @ApiOperation(value = "绘制验证码", notes = "具体我也不清楚")
    @ApiImplicitParam(name = "captchaId", value = "验证码编号", paramType = "query", required = true, dataType = "String")
    public void drawCaptcha(@PathVariable("captchaId") String captchaId, HttpServletResponse response) throws IOException {
        //得到验证码 生成指定验证码
        String code=redisTemplate.opsForValue().get(captchaId);
        CreateVerifyCode vCode = new CreateVerifyCode(116,36,4,10,code);
        response.setContentType("image/png");
        vCode.write(response.getOutputStream());
    }


}
