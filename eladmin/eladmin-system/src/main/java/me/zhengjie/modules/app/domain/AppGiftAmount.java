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

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@TableName("app_gift_amount")
public class AppGiftAmount extends BaseEntity implements Serializable {

    @TableId(value = "gift_amount_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID")
    private Long giftAmountId;


    @ApiModelProperty(value = "app用户id")
    private Long appUserId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "获赠金额")
    private BigDecimal giftAmount;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "获赠余额")
    private BigDecimal giftBalance;

    @ApiModelProperty(value = "人民币，元")
    private String unitMoney;

    @ApiModelProperty(value = "失效时间")
    private Timestamp invalidTime;

    @ApiModelProperty(value = "0-失效 1-有效")
    private Integer giftStatus;

    @TableField(exist = false)
    @ApiModelProperty(value = "邀请人数")
    private Long inviteNum;

    public void copy(AppGiftAmount source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
