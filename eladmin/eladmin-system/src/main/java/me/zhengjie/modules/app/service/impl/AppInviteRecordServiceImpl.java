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

import me.zhengjie.modules.app.domain.AppInviteRecord;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.modules.app.service.AppInviteRecordService;
import me.zhengjie.modules.app.domain.dto.AppInviteRecordQueryCriteria;
import me.zhengjie.modules.app.mapper.AppInviteRecordMapper;
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
* @date 2025-04-09
**/
@Service
@RequiredArgsConstructor
public class AppInviteRecordServiceImpl extends ServiceImpl<AppInviteRecordMapper, AppInviteRecord> implements AppInviteRecordService {

    private final AppInviteRecordMapper appInviteRecordMapper;

    @Override
    public PageResult<AppInviteRecord> queryAll(AppInviteRecordQueryCriteria criteria, Page<Object> page){
        return PageUtil.toPage(appInviteRecordMapper.findAll(criteria, page));
    }

    @Override
    public List<AppInviteRecord> queryAll(AppInviteRecordQueryCriteria criteria){
        return appInviteRecordMapper.findAll(criteria);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(AppInviteRecord resources) {
        appInviteRecordMapper.insert(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AppInviteRecord resources) {
        AppInviteRecord appInviteRecord = getById(resources.getRecordId());
        appInviteRecord.copy(resources);
        appInviteRecordMapper.updateById(appInviteRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(List<Long> ids) {
        appInviteRecordMapper.deleteBatchIds(ids);
    }

    @Override
    public void download(List<AppInviteRecord> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AppInviteRecord appInviteRecord : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("邀请用户id", appInviteRecord.getInviterId());
            map.put("被邀请用户id", appInviteRecord.getInviteeId());
            map.put("0-未发放 1-已发放", appInviteRecord.getRewardStatus());
            map.put("创建者", appInviteRecord.getCreateBy());
            map.put("创建日期", appInviteRecord.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}