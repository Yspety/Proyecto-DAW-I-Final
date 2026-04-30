package pe.com.andinadistribuidora.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import pe.com.andinadistribuidora.api.request.CategoriaRequestDto;
import pe.com.andinadistribuidora.api.response.CategoriaResponseDto;
import pe.com.andinadistribuidora.entity.Categoria;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoriaMapper {
    
    CategoriaMapper INSTANCE = Mappers.getMapper(CategoriaMapper.class);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ultimaActualizacion", ignore = true)
    Categoria toEntity(CategoriaRequestDto dto);
    
    CategoriaResponseDto toResponseDto(Categoria entity);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ultimaActualizacion", ignore = true)
    void updateEntityFromDto(CategoriaRequestDto dto, @MappingTarget Categoria entity);
}