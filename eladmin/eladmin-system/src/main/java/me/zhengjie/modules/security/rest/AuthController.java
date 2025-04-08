/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.security.rest;

import cn.hutool.core.util.IdUtil;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousDeleteMapping;
import me.zhengjie.annotation.rest.AnonymousGetMapping;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.config.properties.RsaProperties;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.config.CaptchaConfig;
import me.zhengjie.modules.security.config.enums.LoginCodeEnum;
import me.zhengjie.modules.security.config.LoginProperties;
import me.zhengjie.modules.security.config.SecurityProperties;
import me.zhengjie.modules.security.security.TokenProvider;
import me.zhengjie.modules.security.service.UserDetailsServiceImpl;
import me.zhengjie.modules.security.service.dto.AppAuthUserDto;
import me.zhengjie.modules.security.service.dto.AuthUserDto;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.security.service.OnlineUserService;
import me.zhengjie.modules.security.service.dto.SMSSendDto;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.Job;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.utils.RsaUtils;
import me.zhengjie.utils.RedisUtils;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.StringUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Api(tags = "系统：系统授权接口")
public class AuthController {

    private final SecurityProperties properties;
    private final RedisUtils redisUtils;
    private final OnlineUserService onlineUserService;
    private final TokenProvider tokenProvider;
    private final CaptchaConfig captchaConfig;
    private final LoginProperties loginProperties;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;
    /**
     * app用户默认信息，
     */
    private final Long APP_USER_DEFAULT_DEPT_ID =19L;
    private final Long APP_USER_DEFAULT_JOB_ID =10L;
    private final Long APP_USER_DEFAULT_ROLE_ID =3L;
    private final String APP_USER_DEFAULT_EAMIL ="test888@888.com";


    @Log("后端用户登录")
    @ApiOperation("后端登录授权")
    @AnonymousPostMapping(value = "/login")
    public ResponseEntity<Object> login(@Validated @RequestBody AuthUserDto authUser, HttpServletRequest request) throws Exception {
        // 密码解密
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, authUser.getPassword());
        // 查询验证码
        String code = redisUtils.get(authUser.getUuid(), String.class);
        // 清除验证码
        redisUtils.del(authUser.getUuid());
        if (StringUtils.isBlank(code)) {
            throw new BadRequestException("验证码不存在或已过期");
        }
        if (StringUtils.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
            throw new BadRequestException("验证码错误");
        }
        // 获取用户信息
        JwtUserDto jwtUser = userDetailsService.loadUserByUsername(authUser.getUsername());
        // 验证用户密码
        if (!passwordEncoder.matches(password, jwtUser.getPassword())) {
            throw new BadRequestException("登录密码错误");
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌
        String token = tokenProvider.createToken(jwtUser);
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", properties.getTokenStartWith() + token);
            put("user", jwtUser);
        }};
        if (loginProperties.isSingleLogin()) {
            // 踢掉之前已经登录的token
            onlineUserService.kickOutForUsername(authUser.getUsername());
        }
        // 保存在线信息
        onlineUserService.save(jwtUser, token, request);
        // 返回登录信息
        return ResponseEntity.ok(authInfo);
    }
    @Log("前端用户登录注册")
    @ApiOperation("前端登录授权注册")
    @AnonymousPostMapping(value = "/app/login")
    public ResponseEntity<Object> login(@Validated @RequestBody AppAuthUserDto authUser, HttpServletRequest request) throws Exception {
        // 密码解密
//        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, authUser.getPassword());
       //设置用户名
        authUser.setUsername(userService.createUsernameByPhone(authUser.getPhone()));
        // 查询验证码
        String code = redisUtils.get(authUser.getUsername(), String.class);
        // 清除验证码
        redisUtils.del(authUser.getUsername());
        if (StringUtils.isBlank(code)) {
            throw new BadRequestException("验证码不存在或已过期");
        }
        if (StringUtils.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
            throw new BadRequestException("验证码错误");
        }
        // //注册，新增用户,权限，
        if(authUser.getMode().equals("1")){
            try {
                this.createUser(authUser);
            }catch (Exception e){
                e.printStackTrace();
                throw new BadRequestException("注册失败");
            }
        }
        //查询用户
        JwtUserDto jwtUser = userDetailsService.loadUserByUsername(authUser.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌
        String token = tokenProvider.createToken(jwtUser);
        // 返回 token 与 用户信息
        JwtUserDto finalJwtUser = jwtUser;
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", properties.getTokenStartWith() + token);
            put("user", finalJwtUser);
        }};
        if (loginProperties.isSingleLogin()) {
            // 踢掉之前已经登录的token
            onlineUserService.kickOutForUsername(jwtUser.getUsername());
        }
        // 保存在线信息
        onlineUserService.save(jwtUser, token, request);
        // 返回登录信息
        return ResponseEntity.ok(authInfo);
    }

    /**
     * 创建新用户
     * @param authUser
     * @throws Exception
     */
    private void createUser(AppAuthUserDto authUser) throws Exception {
        User user = new User();
        user.setUsername(authUser.getUsername());
        user.setPhone(authUser.getPhone());
        user.setEnabled(true);
        user.setIsAdmin(false);
        user.setNickName(RandomStringUtils.randomNumeric(18));
        user.setEmail(APP_USER_DEFAULT_EAMIL);
        {
            Dept dept = new Dept();
            dept.setId(APP_USER_DEFAULT_DEPT_ID);
            user.setDept(dept);
        }
        {
            Job job = new Job();
            job.setId(APP_USER_DEFAULT_JOB_ID);
            Set<Job> jobs = new HashSet<>();
            jobs.add(job);
            user.setJobs(jobs);
        }
        {
            Role role = new Role();
            role.setId(APP_USER_DEFAULT_ROLE_ID);
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
        }
        user.setPassword(passwordEncoder.encode("123456"));
        userService.create(user);
    }

    @ApiOperation("获取手机验证码")
    @AnonymousGetMapping(value = "/phone/code")
    public  ResponseEntity<Object> getCode(@Validated @RequestBody AppAuthUserDto authUser) {
        // 通过阿里云发验证码给手机，
        //保存到数据库
        // 1. 校验手机号格式
//        if (!RegexUtils.isPhoneValid(dto.getPhone())) {
//            return ApiResult.fail("手机号格式错误");
//        }

        // 2. 生成6位随机验证码
        String code = RandomStringUtils.randomNumeric(6);
        log.info("code:{}",code);

        // 3. 保存验证码记录（包含IP限流）
//        smsService.saveCode(dto.getPhone(), code, request.getRemoteAddr());

        // 4. 调用短信服务（实际项目替换为阿里云/腾讯云SDK）
//        boolean success = mockSendSms(dto.getPhone(), code);
        String username = userService.createUsernameByPhone(authUser.getPhone());
        // 保存
        redisUtils.set(username, code, new CaptchaConfig().getExpiration(), TimeUnit.MINUTES);

        boolean success =true;
        SMSSendDto smsSendDto = new SMSSendDto ();
        if(success){
            smsSendDto = new SMSSendDto("success","发送成功");
        }else {
            smsSendDto = new SMSSendDto("fail","发送失败");
        }
        return ResponseEntity.ok(smsSendDto);
    }
    @ApiOperation("获取用户信息")
    @GetMapping(value = "/info")
    public ResponseEntity<UserDetails> getUserInfo() {
        JwtUserDto jwtUser = (JwtUserDto) SecurityUtils.getCurrentUser();
        return ResponseEntity.ok(jwtUser);
    }

    @ApiOperation("获取图形验证码")
    @AnonymousGetMapping(value = "/code")
    public ResponseEntity<Object> getCode() {
        // 获取运算的结果
        Captcha captcha = captchaConfig.getCaptcha();
        String uuid = properties.getCodeKey() + IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == LoginCodeEnum.ARITHMETIC.ordinal() && captchaValue.contains(".")) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        // 保存
        redisUtils.set(uuid, captchaValue, captchaConfig.getExpiration(), TimeUnit.MINUTES);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return ResponseEntity.ok(imgResult);
    }

    @ApiOperation("退出登录")
    @AnonymousDeleteMapping(value = "/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        String token = tokenProvider.getToken(request);
        onlineUserService.logout(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
