package pe.com.andinadistribuidora.mapper;

import java.time.LocalDateTime;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pe.com.andinadistribuidora.api.request.AlmacenRequestDto;
import pe.com.andinadistribuidora.api.response.AlmacenResponseDto;
import pe.com.andinadistribuidora.entity.Almacen;
import pe.com.andinadistribuidora.entity.Almacen.TipoAlmacen;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AlmacenMapper {
    
    AlmacenMapper INSTANCE = Mappers.getMapper(AlmacenMapper.class);
    
    // Request DTO -> Entity (String -> Enum)
    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "stringToTipoAlmacen")
    @Mapping(target = "ultimaActualizacion", expression = "java(getCurrentDateTime())")
    @Mapping(target = "id", ignore = true)
    Almacen toEntity(AlmacenRequestDto dto);
    
    // Entity -> Response DTO (Enum -> String)
    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "tipoAlmacenToString")
    AlmacenResponseDto toResponseDto(Almacen entity);
    
    // Update
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "stringToTipoAlmacen")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ultimaActualizacion", expression = "java(getCurrentDateTime())")
    void updateEntityFromDto(AlmacenRequestDto dto, @MappingTarget Almacen entity);
    
    // Conversión String -> Enum
    @Named("stringToTipoAlmacen")
    default TipoAlmacen stringToTipoAlmacen(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            return TipoAlmacen.PRINCIPAL; // Valor por defecto
        }
        try {
            return TipoAlmacen.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            return TipoAlmacen.PRINCIPAL;
        }
    }
    
    // Conversión Enum -> String
    @Named("tipoAlmacenToString")
    default String tipoAlmacenToString(TipoAlmacen tipo) {
        return tipo != null ? tipo.name() : "PRINCIPAL";
    }
    
    default LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}