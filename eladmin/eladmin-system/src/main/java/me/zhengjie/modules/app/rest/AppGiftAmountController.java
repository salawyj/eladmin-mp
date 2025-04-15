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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.app.domain.AppAmount;
import me.zhengjie.modules.app.domain.AppGiftAmount;
import me.zhengjie.modules.app.service.AppAmountService;
import me.zhengjie.modules.app.service.AppGiftAmountService;
import me.zhengjie.modules.app.domain.dto.AppGiftAmountQueryCriteria;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import me.zhengjie.modules.app.service.AppInviteRecordService;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.enums.UnitEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.utils.PageResult;

/**
* @author lijunhui
* @date 2025-04-08
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "获赠金额")
@RequestMapping("/api/appGiftAmount")
public class AppGiftAmountController {

    private final AppGiftAmountService appGiftAmountService;
    private final AppInviteRecordService appInviteRecordService;
    private final AppAmountService appAmountService;


    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('appGiftAmount:list')")
    public void exportAppGiftAmount(HttpServletResponse response, AppGiftAmountQueryCriteria criteria) throws IOException {
        appGiftAmountService.download(appGiftAmountService.queryAll(criteria), response);
    }

    @GetMapping(value = "/pageList")
    @ApiOperation("查询获赠金额，赠送账单")
    @PreAuthorize("@el.check('appGiftAmount:list')")
    public ResponseEntity<PageResult<AppGiftAmount>> queryAppGiftAmount(AppGiftAmountQueryCriteria criteria,@RequestParam String userId){
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(appGiftAmountService.queryAll(criteria,page),HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    @Log("新增获赠金额")
    @ApiOperation("领取获赠金额")
    @PreAuthorize("@el.check('appGiftAmount:add')")
    public ResponseEntity<Object> createAppGiftAmount(@Validated @RequestBody AppGiftAmount resources){
        //新增获赠金额
        resources.setAppUserId(SecurityUtils.getCurrentUserId());
        resources.setUnitMoney(UnitEnum.RMB.getDescription());
        resources.setGiftStatus(1);
        //获取当前时间
        Timestamp timestamp0 = new Timestamp(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        c.setTime(timestamp0);
        c.add(Calendar.DATE, 25); // 加* 天
        resources.setInvalidTime(new Timestamp(c.getTimeInMillis()));
        appGiftAmountService.create(resources);

        //获赠金额记录领取,改为已经领取，+++后面再加

        //修改用户金额
        {
            AppAmount appAmount = appAmountService.findById(SecurityUtils.getCurrentUserId().toString());
            Long num = appAmount.getInviteNum()+1;
            appAmount.setGiftNum(num);
            appAmount.setGiftTotal(appAmount.getGiftTotal().add(resources.getGiftAmount()));
            appAmount.setGiftBalance(appAmount.getGiftBalance().add(resources.getGiftAmount()));
            appAmountService.update(appAmount);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改获赠金额")
    @ApiOperation("修改获赠金额")
    @PreAuthorize("@el.check('appGiftAmount:edit')")
    public ResponseEntity<Object> updateAppGiftAmount(@Validated @RequestBody AppGiftAmount resources){
        appGiftAmountService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除获赠金额")
    @ApiOperation("删除获赠金额")
    @PreAuthorize("@el.check('appGiftAmount:del')")
    public ResponseEntity<Object> deleteAppGiftAmount(@ApiParam(value = "传ID数组[]") @RequestBody List<Long> ids) {
        appGiftAmountService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}