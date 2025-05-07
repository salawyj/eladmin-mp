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
package me.zhengjie.modules.app.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author 李军辉
 */
@Getter
@Setter
public class AppAgentPayDto {


    @NotBlank
    @ApiModelProperty(value = "对话框交互会话的唯一标识符，将所有相关的消息分组到同一个对话中，确保 LLM 针对同一个主题和上下文持续对话")
    private String conversation_id;

    @NotBlank
    @ApiModelProperty(value = "分配给每个应用用户的唯一标识符，用以区分不同的对话用户")
    private String user_id;

    @ApiModelProperty(value = "应用 ID，系统会向每个 Workflow 应用分配一个唯一的标识符，用以区分不同的应用，并通过此参数记录当前应用的基本信息")
    private String app_id;

    @ApiModelProperty(value = "Workflow ID，用于记录当前 Workflow 应用内所包含的所有节点信息")
    private String workflow_id;

    @ApiModelProperty(value = "Workflow 应用运行 ID，用于记录 Workflow 应用中的运行情况")
    private String workflow_run_id ;
    @ApiModelProperty(value = "LLM返回数据")
    private String msg ;
}
