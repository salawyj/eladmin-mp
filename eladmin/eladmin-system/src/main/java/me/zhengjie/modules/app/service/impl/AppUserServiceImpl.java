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
package me.zhengjie.modules.app.service.impl;

import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.app.domain.AppUser;
import me.zhengjie.modules.security.service.UserCacheManager;
import me.zhengjie.modules.security.service.dto.AppJwtUserDto;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.modules.app.service.AppUserService;
import me.zhengjie.modules.app.domain.dto.AppUserQueryCriteria;
import me.zhengjie.modules.app.mapper.AppUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import me.zhengjie.utils.PageUtil;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import me.zhengjie.utils.PageResult;

/**
 * @description 服务实现
 * @author lijunhui
 * @date 2025-04-05
 **/
@Service
@RequiredArgsConstructor
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements AppUserService {

    private final AppUserMapper appUserMapper;
    private final UserCacheManager userCacheManager;


    @Override
    public PageResult<AppUser> queryAll(AppUserQueryCriteria criteria, Page<Object> page){
        return PageUtil.toPage(appUserMapper.findAll(criteria, page));
    }

    @Override
    public List<AppUser> queryAll(AppUserQueryCriteria criteria){
        return appUserMapper.findAll(criteria);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(AppUser resources) {
        appUserMapper.insert(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AppUser resources) {
        AppUser appUser = getById(resources.getUserId());
        appUser.copy(resources);
        appUserMapper.updateById(appUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(List<Long> ids) {
        appUserMapper.deleteBatchIds(ids);
    }

    @Override
    public void download(List<AppUser> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AppUser appUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户名", appUser.getUsername());
            map.put("昵称", appUser.getNickName());
            map.put("性别", appUser.getGender());
            map.put("手机号码", appUser.getPhone());
            map.put("邮箱", appUser.getEmail());
            map.put("头像地址", appUser.getAvatarName());
            map.put("头像真实路径", appUser.getAvatarPath());
            map.put("密码", appUser.getPassword());
            map.put("状态：1启用、0禁用", appUser.getEnabled());
            map.put("创建者", appUser.getCreateBy());
            map.put("更新者", appUser.getUpdateBy());
            map.put("修改密码的时间", appUser.getPwdResetTime());
            map.put("创建日期", appUser.getCreateTime());
            map.put("更新时间", appUser.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public AppUser getByPhone(String phone) {
        return appUserMapper.findByPhone(phone);
    }

}