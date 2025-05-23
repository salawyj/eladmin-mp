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
package ${package}.domain.dto;

import lombok.Data;
<#if queryHasTimestamp>
import java.sql.Timestamp;
</#if>
<#if queryHasBigDecimal>
import java.math.BigDecimal;
</#if>
<#if betweens?? && (betweens?size > 0)>
import java.util.List;
</#if>
import io.swagger.annotations.ApiModelProperty;

/**
* @author ${author}
* @date ${date}
**/
@Data
public class ${className}QueryCriteria{

    @ApiModelProperty(value = "页码", example = "1")
    private Integer page = 1;

    @ApiModelProperty(value = "每页数据量", example = "10")
    private Integer size = 10;
<#if queryColumns??>
    <#list queryColumns as column>

        <#if column.remark != ''>
    @ApiModelProperty(value = "${column.remark}")
        <#else>
    @ApiModelProperty(value = "${column.changeColumnName}")
        </#if>
    private ${column.columnType} ${column.changeColumnName};
    </#list>
</#if>
<#if betweens??>
    <#list betweens as column>
    private List<${column.columnType}> ${column.changeColumnName};
    </#list>
</#if>
}