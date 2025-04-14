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

import me.zhengjie.annotation.Log;
import me.zhengjie.modules.app.domain.AppAmount;
import me.zhengjie.modules.app.domain.AppInviteCode;
import me.zhengjie.modules.app.domain.AppInviteRecord;
import me.zhengjie.modules.app.domain.dto.AppInviteRecordDto;
import me.zhengjie.modules.app.service.AppAmountService;
import me.zhengjie.modules.app.service.AppInviteCodeService;
import me.zhengjie.modules.app.service.AppInviteRecordService;
import me.zhengjie.modules.app.domain.dto.AppInviteRecordQueryCriteria;
import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.utils.PageResult;

/**
* @author lijunhui
* @date 2025-04-09
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "邀请记录")
@RequestMapping("/api/appInviteRecord")
public class AppInviteRecordController {

    private final AppInviteRecordService appInviteRecordService;
    private final AppInviteCodeService appInviteCodeService;
    private final AppAmountService appAmountService;


    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('appInviteRecord:list')")
    public void exportAppInviteRecord(HttpServletResponse response, AppInviteRecordQueryCriteria criteria) throws IOException {
        appInviteRecordService.download(appInviteRecordService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询邀请记录")
    @PreAuthorize("@el.check('appInviteRecord:list')")
    public ResponseEntity<PageResult<AppInviteRecord>> queryAppInviteRecord(AppInviteRecordQueryCriteria criteria){
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(appInviteRecordService.queryAll(criteria,page),HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    @Log("新增邀请记录")
    @ApiOperation("新增邀请记录")
    @PreAuthorize("@el.check('appInviteRecord:add')")
    public ResponseEntity<Object> createAppInviteRecord(@Validated @RequestBody AppInviteRecordDto resources){
        AppInviteCode appInviteCode =  appInviteCodeService.findEffectByCode(resources.getInviteCode());
        //邀请用户id为空的时候，查询
        if(resources.getInviterId() == null){
            resources.setInviterId(appInviteCode.getAppUserId());
        }
        AppInviteRecord appInviteRecord = new AppInviteRecord();
        BeanUtils.copyProperties(resources,appInviteRecord);
        appInviteRecord.setRewardStatus(0);
        //        新增邀请记录
        appInviteRecordService.create(appInviteRecord);
        //修改邀请码状态
        appInviteCode.setStatus(1);
        appInviteCodeService.update(appInviteCode);
        //修改邀请数量
        {
            AppAmount appAmount = appAmountService.findById(appInviteCode.getAppUserId().toString());
            Long num = appAmount.getInviteNum()+1;
            appAmount.setInviteNum(num);
            appAmountService.update(appAmount);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改邀请记录")
    @ApiOperation("修改邀请记录")
    @PreAuthorize("@el.check('appInviteRecord:edit')")
    public ResponseEntity<Object> updateAppInviteRecord(@Validated @RequestBody AppInviteRecord resources){
        appInviteRecordService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除邀请记录")
    @ApiOperation("删除邀请记录")
    @PreAuthorize("@el.check('appInviteRecord:del')")
    public ResponseEntity<Object> deleteAppInviteRecord(@ApiParam(value = "传ID数组[]") @RequestBody List<Long> ids) {
        appInviteRecordService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}