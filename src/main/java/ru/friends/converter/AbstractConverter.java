package ru.friends.converter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

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

    @SneakyThrows
    public DTO toDto(VO vo) {
        DTO dto = dtoClass.newInstance();
        BeanUtils.copyProperties(vo, dto);
        return dto;
    }

    public Page<VO> toVo(Page<DTO> dtoPage) {
        List<VO> voList = toVo(dtoPage.getContent());
        return new PageImpl<>(voList, null, dtoPage.getTotalElements());
    }

    public List<VO> toVo(Collection<DTO> dtoList) {
        return dtoList.stream().map(this::toVo).collect(Collectors.toList());
    }

    @SneakyThrows
    public VO toVo(DTO dto) {
        VO vo = voClass.newInstance();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }

}
