package com.simon.mapper;

import com.simon.common.mapper.CrudMapper;
import com.simon.dto.DictTypeDto;
import com.simon.dto.EasyUiTreeGridDto;
import com.simon.model.DictType;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author simon
 */
public interface DictTypeMapper extends CrudMapper<DictType> {
    /**
     * 根据字典组编码查询字典列表
     * @param groupCode
     * @return
     */
    List<DictType> getByGroupCode(@Param("groupCode") String groupCode, @Param("language") String language);

    List<EasyUiTreeGridDto> getTreeGridDtos(@Param("groupCode") String groupCode, @Param("language") String language);

    @Override
    List<DictType> getList(@Param("map") Map<String, Object> map);

    /**
     * 根据id返回字典dto
     * @param id 子字典id
     * @return 字典dto
     */
    DictTypeDto getDtoById(@Param("id") Long id, @Param("language") String language);
}