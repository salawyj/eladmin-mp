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

import me.zhengjie.modules.app.domain.AppAmount;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.modules.app.service.AppAmountService;
import me.zhengjie.modules.app.domain.dto.AppAmountQueryCriteria;
import me.zhengjie.modules.app.mapper.AppAmountMapper;
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
public class AppAmountServiceImpl extends ServiceImpl<AppAmountMapper, AppAmount> implements AppAmountService {

    private final AppAmountMapper appAmountMapper;

    @Override
    public PageResult<AppAmount> queryAll(AppAmountQueryCriteria criteria, Page<Object> page){
        return PageUtil.toPage(appAmountMapper.findAll(criteria, page));
    }

    @Override
    public List<AppAmount> queryAll(AppAmountQueryCriteria criteria){
        return appAmountMapper.findAll(criteria);
    }


    @Override
    public AppAmount findById(String userId){
        return appAmountMapper.findByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(AppAmount resources) {
        appAmountMapper.insert(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AppAmount resources) {
        AppAmount appAmount = getById(resources.getAmountId());
        appAmount.copy(resources);
        appAmountMapper.updateById(appAmount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(List<Long> ids) {
        appAmountMapper.deleteBatchIds(ids);
    }

    @Override
    public void download(List<AppAmount> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AppAmount appAmount : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("app用户id", appAmount.getAppUserId());
            map.put("充值总额", appAmount.getRechargeTotal());
            map.put("获赠总额", appAmount.getGiftTotal());
            map.put("充值余额", appAmount.getRechargeBalance());
            map.put("获赠余额", appAmount.getGiftBalance());
            map.put("人民币，元", appAmount.getUnitMoney());
            map.put("邀请数量", appAmount.getInviteNum());
            map.put("创建者", appAmount.getCreateBy());
            map.put("更新者", appAmount.getUpdateBy());
            map.put("创建日期", appAmount.getCreateTime());
            map.put("更新时间", appAmount.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}