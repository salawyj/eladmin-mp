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
import me.zhengjie.modules.app.domain.AppUser;
import me.zhengjie.modules.app.service.AppUserService;
import me.zhengjie.modules.app.domain.dto.AppUserQueryCriteria;
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
 * @date 2025-04-05
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "appUser")
@RequestMapping("/api/appUser")
public class AppUserController {

    private final AppUserService appUserService;





    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('appUser:list')")
    public void exportAppUser(HttpServletResponse response, AppUserQueryCriteria criteria) throws IOException {
        appUserService.download(appUserService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询appUser")
    @PreAuthorize("@el.check('appUser:list')")
    public ResponseEntity<PageResult<AppUser>> queryAppUser(AppUserQueryCriteria criteria){
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(appUserService.queryAll(criteria,page),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增appUser")
    @ApiOperation("新增appUser")
    @PreAuthorize("@el.check('appUser:add')")
    public ResponseEntity<Object> createAppUser(@Validated @RequestBody AppUser resources){
        appUserService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改appUser")
    @ApiOperation("修改appUser")
    @PreAuthorize("@el.check('appUser:edit')")
    public ResponseEntity<Object> updateAppUser(@Validated @RequestBody AppUser resources){
        appUserService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除appUser")
    @ApiOperation("删除appUser")
    @PreAuthorize("@el.check('appUser:del')")
    public ResponseEntity<Object> deleteAppUser(@ApiParam(value = "传ID数组[]") @RequestBody List<Long> ids) {
        appUserService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}