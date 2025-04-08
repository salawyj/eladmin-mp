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
package me.zhengjie.modules.app.service;

import me.zhengjie.modules.app.domain.AppUser;
import me.zhengjie.modules.app.domain.dto.AppUserQueryCriteria;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.security.service.dto.AppJwtUserDto;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.utils.PageResult;

/**
 * @description 服务接口
 * @author lijunhui
 * @date 2025-04-05
 **/
public interface AppUserService extends IService<AppUser> {

    /**
     * 查询数据分页
     * @param criteria 条件
     * @param page 分页参数
     * @return PageResult
     */
    PageResult<AppUser> queryAll(AppUserQueryCriteria criteria, Page<Object> page);

    /**
     * 查询所有数据不分页
     * @param criteria 条件参数
     * @return List<AppUserDto>
     */
    List<AppUser> queryAll(AppUserQueryCriteria criteria);

    /**
     * 创建
     * @param resources /
     */
    void create(AppUser resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(AppUser resources);

    /**
     * 多选删除
     * @param ids /
     */
    void deleteAll(List<Long> ids);

    /**
     * 导出数据
     * @param all 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<AppUser> all, HttpServletResponse response) throws IOException;

    AppUser getByPhone(String phone);

}