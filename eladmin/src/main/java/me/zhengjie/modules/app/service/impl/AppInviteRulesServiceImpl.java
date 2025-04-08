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

import me.zhengjie.modules.app.domain.AppInviteRules;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.modules.app.service.AppInviteRulesService;
import me.zhengjie.modules.app.domain.dto.AppInviteRulesQueryCriteria;
import me.zhengjie.modules.app.mapper.AppInviteRulesMapper;
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
* @date 2025-04-08
**/
@Service
@RequiredArgsConstructor
public class AppInviteRulesServiceImpl extends ServiceImpl<AppInviteRulesMapper, AppInviteRules> implements AppInviteRulesService {

    private final AppInviteRulesMapper appInviteRulesMapper;

    @Override
    public PageResult<AppInviteRules> queryAll(AppInviteRulesQueryCriteria criteria, Page<Object> page){
        return PageUtil.toPage(appInviteRulesMapper.findAll(criteria, page));
    }

    @Override
    public List<AppInviteRules> queryAll(AppInviteRulesQueryCriteria criteria){
        return appInviteRulesMapper.findAll(criteria);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(AppInviteRules resources) {
        appInviteRulesMapper.insert(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AppInviteRules resources) {
        AppInviteRules appInviteRules = getById(resources.getRuleId());
        appInviteRules.copy(resources);
        appInviteRulesMapper.updateById(appInviteRules);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(List<Long> ids) {
        appInviteRulesMapper.deleteBatchIds(ids);
    }

    @Override
    public void download(List<AppInviteRules> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AppInviteRules appInviteRules : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", appInviteRules.getName());
            map.put("邀请人数", appInviteRules.getInviteNum());
            map.put("获赠余额", appInviteRules.getGiftBalance());
            map.put("创建者", appInviteRules.getCreateBy());
            map.put("更新者", appInviteRules.getUpdateBy());
            map.put("创建日期", appInviteRules.getCreateTime());
            map.put("更新时间", appInviteRules.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}