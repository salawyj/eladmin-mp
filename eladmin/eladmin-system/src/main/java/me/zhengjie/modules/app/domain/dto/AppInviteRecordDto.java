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
package me.zhengjie.modules.app.domain.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
* @description /
* @author lijunhui
* @date 2025-04-09
**/
@Data
public class AppInviteRecordDto implements Serializable {

    @ApiModelProperty(value = "ID")
    private Long recordId;

    @ApiModelProperty(value = "邀请用户id")
    private Long inviterId;


    @ApiModelProperty(value = "被邀请用户id")
    private Long inviteeId;

    @ApiModelProperty(value = "0-未发放 1-已发放")
    private Integer rewardStatus;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "创建日期")
    private Timestamp createTime;

    @NotNull
    @ApiModelProperty(value = "邀请码")
    private String inviteCode;
    public void copy(AppInviteRecordDto source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
