package ru.friends.converter;

import com.google.common.base.Throwables;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public abstract class AbstractConverter<DTO, VO> {

    Class<DTO> dtoClass;
    Class<VO> voClass;

    public List<DTO> toDto(Collection<VO> voCollection) {
        return voCollection.stream().map(this::toDto).collect(Collectors.toList());
    }

    public DTO toDto(VO vo) {
        try {
            DTO dto = dtoClass.newInstance();
            BeanUtils.copyProperties(vo, dto);
            return dto;
        } catch (InstantiationException | IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }

    public List<VO> toVo(Collection<DTO> dtoCollection) {
        return dtoCollection.stream().map(this::toVo).collect(Collectors.toList());
    }

    public VO toVo(DTO dto) {
        try {
            VO vo = voClass.newInstance();
            BeanUtils.copyProperties(dto, vo);
            return vo;
        } catch (InstantiationException | IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }

}
