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
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
* @description /
* @author lijunhui
* @date 2025-04-08
**/
@Data
@TableName("app_invite_code")
public class AppInviteCode implements Serializable {

    @TableId(value = "code")
    @ApiModelProperty(value = "邀请码")
    private String code;

    @NotNull
    @ApiModelProperty(value = "所属用户ID")
    private Long appUserId;

    @ApiModelProperty(value = "0-未使用 1-已使用")
    private Integer status;

    @ApiModelProperty(value = "createTime")
    private Timestamp createTime;

    @ApiModelProperty(value = "过期时间")
    private Timestamp expireTime;

    public void copy(AppInviteCode source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
