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
package me.zhengjie.modules.app.rest;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.modules.app.domain.AppAmount;
import me.zhengjie.modules.app.service.AppAmountService;
import me.zhengjie.modules.app.domain.dto.AppAmountQueryCriteria;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import me.zhengjie.modules.app.service.dto.AppAgentPayDto;
import me.zhengjie.modules.security.config.CaptchaConfig;
import me.zhengjie.modules.security.service.dto.AppAuthUserDto;
import me.zhengjie.modules.security.service.dto.SMSSendDto;
import me.zhengjie.utils.SecurityUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.utils.PageResult;

/**
* @author lijunhui
* @date 2025-04-08
**/
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "用户金额")
@RequestMapping("/api/appAmount")
public class AppAmountController {

    private final AppAmountService appAmountService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('appAmount:list')")
    public void exportAppAmount(HttpServletResponse response, AppAmountQueryCriteria criteria) throws IOException {
        appAmountService.download(appAmountService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询用户金额")
    @PreAuthorize("@el.check('appAmount:list')")
    public ResponseEntity<PageResult<AppAmount>> queryAppAmount(AppAmountQueryCriteria criteria){
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(appAmountService.queryAll(criteria,page),HttpStatus.OK);
    }


    @GetMapping(value = "/app/queryInfo")
    @ApiOperation("手机端查询用户金额")
    @PreAuthorize("@el.check('appAmount:info')")
    public ResponseEntity<AppAmount> queryAppAmountInfo(){
        AppAmount appAmount = appAmountService.findById(SecurityUtils.getCurrentUserId().toString());
        if(appAmount == null){
            appAmount =new AppAmount();
            appAmount.setAppUserId(SecurityUtils.getCurrentUserId());
            appAmount.setGiftBalance(new BigDecimal(0));
            appAmount.setGiftTotal(new BigDecimal(0));
            appAmount.setInviteNum(0L);
            appAmount.setRechargeBalance(new BigDecimal(0));
            appAmount.setRechargeTotal(new BigDecimal(0));
            appAmount.setGiftNum(0L);
            appAmountService.create(appAmount);
        }
        return new ResponseEntity<>(appAmount,HttpStatus.OK);
    }


    @PostMapping(value = "/add")
    @Log("新增用户金额")
    @ApiOperation("新增用户金额")
    @PreAuthorize("@el.check('appAmount:add')")
    public ResponseEntity<Object> createAppAmount(@Validated @RequestBody AppAmount resources){

        appAmountService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改用户金额")
    @ApiOperation("修改用户金额")
    @PreAuthorize("@el.check('appAmount:edit')")
    public ResponseEntity<Object> updateAppAmount(@Validated @RequestBody AppAmount resources){
        appAmountService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除用户金额")
    @ApiOperation("删除用户金额")
    @PreAuthorize("@el.check('appAmount:del')")
    public ResponseEntity<Object> deleteAppAmount(@ApiParam(value = "传ID数组[]") @RequestBody List<Long> ids) {
        appAmountService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @ApiOperation("智能体的支付api")
    @AnonymousPostMapping(value = "/phone/pay")
    public  ResponseEntity<Object>  getCode(@Validated @RequestBody AppAgentPayDto appAgentPayDto) {

        log.info("记录一条从智能体来的支付信息");
        log.info(appAgentPayDto.getConversation_id());
        log.info("getApp_id:{}",appAgentPayDto.getApp_id());
        log.info("getUser_id:{}",appAgentPayDto.getUser_id());
        log.info("getWorkflow_id:{}",appAgentPayDto.getWorkflow_id());
        log.info("getWorkflow_run_id:{}",appAgentPayDto.getWorkflow_run_id());
        log.info("getMsg:{}",appAgentPayDto.getMsg());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}