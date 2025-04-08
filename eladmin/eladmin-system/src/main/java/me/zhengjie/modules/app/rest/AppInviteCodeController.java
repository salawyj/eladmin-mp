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
import me.zhengjie.modules.app.domain.AppInviteCode;
import me.zhengjie.modules.app.service.AppInviteCodeService;
import me.zhengjie.modules.app.domain.dto.AppInviteCodeQueryCriteria;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.utils.PageResult;

/**
* @author lijunhui
* @date 2025-04-08
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "邀请码")
@RequestMapping("/api/appInviteCode")
public class AppInviteCodeController {

    private final AppInviteCodeService appInviteCodeService;


    @GetMapping(value = "/queryInfo")
    @ApiOperation("查询邀请码")
    @PreAuthorize("@el.check('appInviteCode:info')")
    public ResponseEntity<AppInviteCode> queryAppInviteCodeInfo(@RequestBody String userId){
        return new ResponseEntity<>(appInviteCodeService.findByUserId(userId),HttpStatus.OK);
    }


    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('appInviteCode:list')")
    public void exportAppInviteCode(HttpServletResponse response, AppInviteCodeQueryCriteria criteria) throws IOException {
        appInviteCodeService.download(appInviteCodeService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询邀请码")
    @PreAuthorize("@el.check('appInviteCode:list')")
    public ResponseEntity<PageResult<AppInviteCode>> queryAppInviteCode(AppInviteCodeQueryCriteria criteria){
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(appInviteCodeService.queryAll(criteria,page),HttpStatus.OK);
    }




    @PostMapping
    @Log("新增邀请码")
    @ApiOperation("新增邀请码")
    @PreAuthorize("@el.check('appInviteCode:add')")
    public ResponseEntity<Object> createAppInviteCode(@Validated @RequestBody AppInviteCode resources){
        appInviteCodeService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改邀请码")
    @ApiOperation("修改邀请码")
    @PreAuthorize("@el.check('appInviteCode:edit')")
    public ResponseEntity<Object> updateAppInviteCode(@Validated @RequestBody AppInviteCode resources){
        appInviteCodeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除邀请码")
    @ApiOperation("删除邀请码")
    @PreAuthorize("@el.check('appInviteCode:del')")
    public ResponseEntity<Object> deleteAppInviteCode(@ApiParam(value = "传ID数组[]") @RequestBody List<String> ids) {
        appInviteCodeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}