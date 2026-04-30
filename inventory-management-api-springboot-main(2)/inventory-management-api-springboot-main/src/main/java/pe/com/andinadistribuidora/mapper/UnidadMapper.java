package pe.com.andinadistribuidora.mapper;

import java.time.LocalDateTime;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import pe.com.andinadistribuidora.api.request.UnidadRequestDto;
import pe.com.andinadistribuidora.api.response.UnidadResponseDto;
import pe.com.andinadistribuidora.entity.Unidad;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UnidadMapper {
    
    UnidadMapper INSTANCE = Mappers.getMapper(UnidadMapper.class);
    
    // Request -> Entity (crear)
    @Mapping(target = "ultimaActualizacion", expression = "java(getCurrentDateTime())")
    Unidad toEntity(UnidadRequestDto dto);
    
    // Entity -> Response
    UnidadResponseDto toResponseDto(Unidad entity);
    
    // Update parcial
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ultimaActualizacion", expression = "java(getCurrentDateTime())")
    void updateEntityFromDto(UnidadRequestDto dto, @MappingTarget Unidad entity);
    
    default LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}