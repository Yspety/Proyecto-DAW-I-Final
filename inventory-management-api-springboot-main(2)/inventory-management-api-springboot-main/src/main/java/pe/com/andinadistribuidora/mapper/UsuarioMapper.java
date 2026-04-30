package pe.com.andinadistribuidora.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import pe.com.andinadistribuidora.api.request.UsuarioRequestDto;
import pe.com.andinadistribuidora.api.response.UsuarioResponseDto;
import pe.com.andinadistribuidora.entity.Rol;
import pe.com.andinadistribuidora.entity.Usuario;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsuarioMapper {
    
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);
    
    // Request -> Entity (crear)
    @Mappings({
        @Mapping(target = "rol", source = "rolId", qualifiedByName = "rolRef"),
        @Mapping(target = "id", ignore = true), // El ID es autogenerado
        @Mapping(target = "creadoEn", ignore = true) // Se genera automáticamente en BD
    })
    Usuario toEntity(UsuarioRequestDto dto);
    
    // Entity -> Response
    @Mappings({
        @Mapping(target = "rolId", source = "rol.id"),
        @Mapping(target = "rolDescripcion", source = "rol.descripcion")
    })
    UsuarioResponseDto toResponseDto(Usuario entity);
    
    // Update parcial: ignora nulos (útil para PATCH o PUT con campos opcionales)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "rol", source = "rolId", qualifiedByName = "rolRef"),
        @Mapping(target = "id", ignore = true), // No actualizar el ID
        @Mapping(target = "creadoEn", ignore = true) // No actualizar la fecha de creación
    })
    void updateEntityFromDto(UsuarioRequestDto dto, @MappingTarget Usuario entity);
    
    // Método helper para crear referencia de Rol
    @Named("rolRef")
    default Rol mapRolRef(Integer rolId) {
        if (rolId == null) return null;
        return Rol.builder().id(rolId).build();
    }
}