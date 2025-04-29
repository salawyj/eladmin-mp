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

import me.zhengjie.modules.app.domain.AppAgent;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.modules.app.service.AppAgentService;
import me.zhengjie.modules.app.domain.dto.AppAgentQueryCriteria;
import me.zhengjie.modules.app.mapper.AppAgentMapper;
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
* @date 2025-04-19
**/
@Service
@RequiredArgsConstructor
public class AppAgentServiceImpl extends ServiceImpl<AppAgentMapper, AppAgent> implements AppAgentService {

    private final AppAgentMapper appAgentMapper;

    @Override
    public PageResult<AppAgent> queryAll(AppAgentQueryCriteria criteria, Page<Object> page){
        return PageUtil.toPage(appAgentMapper.findAll(criteria, page));
    }

    @Override
    public List<AppAgent> queryAll(AppAgentQueryCriteria criteria){
        return appAgentMapper.findAll(criteria);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(AppAgent resources) {
        appAgentMapper.insert(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AppAgent resources) {
        AppAgent appAgent = getById(resources.getAgentId());
        appAgent.copy(resources);
        appAgentMapper.updateById(appAgent);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(List<Long> ids) {
        appAgentMapper.deleteBatchIds(ids);
    }

    @Override
    public void download(List<AppAgent> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AppAgent appAgent : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", appAgent.getName());
            map.put("智能体的链接", appAgent.getAgentUrl());
            map.put("描述", appAgent.getDescription());
            map.put("创建者", appAgent.getCreateBy());
            map.put("更新者", appAgent.getUpdateBy());
            map.put("创建日期", appAgent.getCreateTime());
            map.put("更新时间", appAgent.getUpdateTime());
            map.put("图标", appAgent.getIcon());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}