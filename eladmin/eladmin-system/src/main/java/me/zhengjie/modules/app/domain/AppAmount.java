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

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import java.sql.Timestamp;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import me.zhengjie.base.BaseEntity;

/**
* @description /
* @author lijunhui
* @date 2025-04-08
**/
@Data
@TableName("app_amount")
public class AppAmount extends BaseEntity implements Serializable {

    @TableId(value = "amount_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID")
    private Long amountId;

    @NotNull
    @ApiModelProperty(value = "app用户id")
    private Long appUserId;

    @ApiModelProperty(value = "充值总额")
    private BigDecimal rechargeTotal;

    @ApiModelProperty(value = "获赠总额")
    private BigDecimal giftTotal;

    @ApiModelProperty(value = "充值余额")
    private BigDecimal rechargeBalance;

    @ApiModelProperty(value = "获赠余额")
    private BigDecimal giftBalance;

    @ApiModelProperty(value = "人民币，元")
    private String unitMoney;

    @NotNull
    @ApiModelProperty(value = "邀请人数量")
    private Long inviteNum;
    @NotNull
    @ApiModelProperty(value = "获赠人数量")
    private Long giftNum;

    public void copy(AppAmount source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
