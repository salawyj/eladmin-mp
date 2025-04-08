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
import me.zhengjie.modules.app.domain.AppInviteRules;
import me.zhengjie.modules.app.service.AppInviteRulesService;
import me.zhengjie.modules.app.domain.dto.AppInviteRulesQueryCriteria;
import lombok.RequiredArgsConstructor;
import java.util.List;
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
* @date 2025-04-08
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "AppInviteRules")
@RequestMapping("/api/appInviteRules")
public class AppInviteRulesController {

    private final AppInviteRulesService appInviteRulesService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('appInviteRules:list')")
    public void exportAppInviteRules(HttpServletResponse response, AppInviteRulesQueryCriteria criteria) throws IOException {
        appInviteRulesService.download(appInviteRulesService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询AppInviteRules")
    @PreAuthorize("@el.check('appInviteRules:list')")
    public ResponseEntity<PageResult<AppInviteRules>> queryAppInviteRules(AppInviteRulesQueryCriteria criteria){
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(appInviteRulesService.queryAll(criteria,page),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增AppInviteRules")
    @ApiOperation("新增AppInviteRules")
    @PreAuthorize("@el.check('appInviteRules:add')")
    public ResponseEntity<Object> createAppInviteRules(@Validated @RequestBody AppInviteRules resources){
        appInviteRulesService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改AppInviteRules")
    @ApiOperation("修改AppInviteRules")
    @PreAuthorize("@el.check('appInviteRules:edit')")
    public ResponseEntity<Object> updateAppInviteRules(@Validated @RequestBody AppInviteRules resources){
        appInviteRulesService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除AppInviteRules")
    @ApiOperation("删除AppInviteRules")
    @PreAuthorize("@el.check('appInviteRules:del')")
    public ResponseEntity<Object> deleteAppInviteRules(@ApiParam(value = "传ID数组[]") @RequestBody List<Long> ids) {
        appInviteRulesService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}