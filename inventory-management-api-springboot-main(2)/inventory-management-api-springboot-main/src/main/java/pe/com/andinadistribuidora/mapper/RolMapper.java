package pe.com.andinadistribuidora.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import pe.com.andinadistribuidora.api.request.RolRequestDto;
import pe.com.andinadistribuidora.api.response.RolResponseDto;
import pe.com.andinadistribuidora.entity.Rol;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RolMapper {
    
    RolMapper INSTANCE = Mappers.getMapper(RolMapper.class);
    
    // Request -> Entity (crear)
    Rol toEntity(RolRequestDto dto);
    
    // Entity -> Response
    RolResponseDto toResponseDto(Rol entity);
    
    // Update parcial: ignora nulos (útil para PATCH o PUT con campos opcionales)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(RolRequestDto dto, @MappingTarget Rol entity);
}