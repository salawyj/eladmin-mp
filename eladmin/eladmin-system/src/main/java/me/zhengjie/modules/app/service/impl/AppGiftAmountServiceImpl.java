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

import me.zhengjie.modules.app.domain.AppGiftAmount;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.modules.app.service.AppGiftAmountService;
import me.zhengjie.modules.app.domain.dto.AppGiftAmountQueryCriteria;
import me.zhengjie.modules.app.mapper.AppGiftAmountMapper;
import me.zhengjie.utils.SecurityUtils;
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
public class AppGiftAmountServiceImpl extends ServiceImpl<AppGiftAmountMapper, AppGiftAmount> implements AppGiftAmountService {

    private final AppGiftAmountMapper appGiftAmountMapper;

    @Override
    public PageResult<AppGiftAmount> queryAll(AppGiftAmountQueryCriteria criteria, Page<Object> page){
        return PageUtil.toPage(appGiftAmountMapper.findAll(criteria, page));
    }

    @Override
    public List<AppGiftAmount> queryAll(AppGiftAmountQueryCriteria criteria){
        return appGiftAmountMapper.findAll(criteria);
    }


    @Override
    public List<AppGiftAmount> queryAllByUser(AppGiftAmountQueryCriteria criteria){
        return appGiftAmountMapper.findAllByUser(criteria, SecurityUtils.getCurrentUserId().toString());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(AppGiftAmount resources) {
        appGiftAmountMapper.insert(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AppGiftAmount resources) {
        AppGiftAmount appGiftAmount = getById(resources.getGiftAmountId());
        appGiftAmount.copy(resources);
        appGiftAmountMapper.updateById(appGiftAmount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(List<Long> ids) {
        appGiftAmountMapper.deleteBatchIds(ids);
    }

    @Override
    public void download(List<AppGiftAmount> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AppGiftAmount appGiftAmount : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("app用户id", appGiftAmount.getAppUserId());
            map.put("获赠金额", appGiftAmount.getGiftAmount());
            map.put("获赠余额", appGiftAmount.getGiftBalance());
            map.put("人民币，元", appGiftAmount.getUnitMoney());
            map.put("创建者", appGiftAmount.getCreateBy());
            map.put("更新者", appGiftAmount.getUpdateBy());
            map.put("创建日期", appGiftAmount.getCreateTime());
            map.put("更新时间", appGiftAmount.getUpdateTime());
            map.put("失效时间", appGiftAmount.getInvalidTime());
            map.put("0-失效 1-有效", appGiftAmount.getGiftStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}