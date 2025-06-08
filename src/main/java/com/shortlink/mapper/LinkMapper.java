package com.shortlink.mapper;


import com.shortlink.model.Link;
//import com.shortlink.repository.LinkRepository;

import dto.LinkFullDTO;
import dto.LinkInputDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;



@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)

public abstract class LinkMapper {


    public abstract Link map(LinkInputDTO dto);

    public abstract LinkInputDTO map(Link link);

    public abstract Link mapFull(LinkFullDTO dto);

    public abstract LinkFullDTO mapFull(Link link);

}
