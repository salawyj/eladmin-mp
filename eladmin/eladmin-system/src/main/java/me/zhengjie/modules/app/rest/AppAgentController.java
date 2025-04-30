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
import me.zhengjie.modules.app.domain.AppAgent;
import me.zhengjie.modules.app.service.AppAgentService;
import me.zhengjie.modules.app.domain.dto.AppAgentQueryCriteria;
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
* @date 2025-04-19
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "appAgent")
@RequestMapping("/api/appAgent")
public class AppAgentController {

    private final AppAgentService appAgentService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('appAgent:list')")
    public void exportAppAgent(HttpServletResponse response, AppAgentQueryCriteria criteria) throws IOException {
        appAgentService.download(appAgentService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询appAgent")
    @PreAuthorize("@el.check('appAgent:list')")
    public ResponseEntity<PageResult<AppAgent>> queryAppAgent(AppAgentQueryCriteria criteria){
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(appAgentService.queryAll(criteria,page),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增appAgent")
    @ApiOperation("新增appAgent")
    @PreAuthorize("@el.check('appAgent:add')")
    public ResponseEntity<Object> createAppAgent(@Validated @RequestBody AppAgent resources){
        appAgentService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改appAgent")
    @ApiOperation("修改appAgent")
    @PreAuthorize("@el.check('appAgent:edit')")
    public ResponseEntity<Object> updateAppAgent(@Validated @RequestBody AppAgent resources){
        appAgentService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除appAgent")
    @ApiOperation("删除appAgent")
    @PreAuthorize("@el.check('appAgent:del')")
    public ResponseEntity<Object> deleteAppAgent(@ApiParam(value = "传ID数组[]") @RequestBody List<Long> ids) {
        appAgentService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/app/pageList")
    @ApiOperation("查询appAgent")
    @PreAuthorize("@el.check('appAgent:appPageList')")
    public ResponseEntity<PageResult<AppAgent>> queryPageAppAgent(AppAgentQueryCriteria criteria){
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(appAgentService.queryAll(criteria,page),HttpStatus.OK);
    }
}