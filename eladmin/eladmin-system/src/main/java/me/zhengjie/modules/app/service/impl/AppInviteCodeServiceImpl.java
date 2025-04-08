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

import me.zhengjie.modules.app.domain.AppInviteCode;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.modules.app.service.AppInviteCodeService;
import me.zhengjie.modules.app.domain.dto.AppInviteCodeQueryCriteria;
import me.zhengjie.modules.app.mapper.AppInviteCodeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import me.zhengjie.utils.PageUtil;
import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import me.zhengjie.utils.PageResult;

/**
* @description 服务实现
* @author lijunhui
* @date 2025-04-08
**/
@Service
@RequiredArgsConstructor
public class AppInviteCodeServiceImpl extends ServiceImpl<AppInviteCodeMapper, AppInviteCode> implements AppInviteCodeService {

    private final AppInviteCodeMapper appInviteCodeMapper;

    @Override
    public PageResult<AppInviteCode> queryAll(AppInviteCodeQueryCriteria criteria, Page<Object> page){
        return PageUtil.toPage(appInviteCodeMapper.findAll(criteria, page));
    }

    @Override
    public List<AppInviteCode> queryAll(AppInviteCodeQueryCriteria criteria){
        return appInviteCodeMapper.findAll(criteria);
    }

    /**
     * / 生成唯一邀请码（6位字母数字组合）
     */
    private String generateCode(Long userId) {
        String base = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // 去除易混淆字符
        Random random = new Random(userId + System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(base.length());
            sb.append(base.charAt(index));
        }
        return sb.toString();
    }
    @Override
    public AppInviteCode findByUserId(String userId){
        AppInviteCode appInviteCode = appInviteCodeMapper.findEffectByUserId(userId);
        if(appInviteCode == null ){
            appInviteCode = new AppInviteCode();
            appInviteCode.setAppUserId(Long.valueOf(userId));
            appInviteCode.setCode(this.generateCode(Long.valueOf(userId)));
            appInviteCode.setStatus(0);
            appInviteCodeMapper.insert(appInviteCode);
//            save(appInviteCode);
        }
        //查询是否有邀请码，如果过期，生成新的+++++
        return appInviteCode;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(AppInviteCode resources) {
        appInviteCodeMapper.insert(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AppInviteCode resources) {
        AppInviteCode appInviteCode = getById(resources.getCode());
        appInviteCode.copy(resources);
        appInviteCodeMapper.updateById(appInviteCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(List<String> ids) {
        appInviteCodeMapper.deleteBatchIds(ids);
    }

    @Override
    public void download(List<AppInviteCode> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AppInviteCode appInviteCode : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("所属用户ID", appInviteCode.getAppUserId());
            map.put("0-未使用 1-已使用", appInviteCode.getStatus());
            map.put(" createTime",  appInviteCode.getCreateTime());
            map.put("过期时间", appInviteCode.getExpireTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}