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
package me.zhengjie.modules.app.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import java.sql.Timestamp;
import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import me.zhengjie.base.BaseEntity;
import me.zhengjie.modules.system.domain.Role;

/**
 * @description /
 * @author lijunhui
 * @date 2025-04-08
 **/
@Data
@TableName("app_invite_rules")
public class AppInviteRules extends BaseEntity implements Serializable {

    @TableId(value = "rule_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID")
    private Long ruleId;

    @NotBlank
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "邀请人数")
    private Long inviteNum;

    @ApiModelProperty(value = "获赠余额")
    private BigDecimal giftBalance;

    @TableField(exist = false)
    @ApiModelProperty(value = "是否已经领取余额奖励 0未领取，1领取")
    private String giftStatus;

    public void copy(AppInviteRules source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
