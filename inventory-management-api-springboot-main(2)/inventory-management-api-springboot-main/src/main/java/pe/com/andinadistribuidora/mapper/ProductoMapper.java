package pe.com.andinadistribuidora.mapper;

import java.time.LocalDateTime;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import pe.com.andinadistribuidora.api.request.ProductoRequestDto;
import pe.com.andinadistribuidora.api.response.ProductoResponseDto;
import pe.com.andinadistribuidora.entity.Categoria;
import pe.com.andinadistribuidora.entity.Producto;
import pe.com.andinadistribuidora.entity.Unidad;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductoMapper {
    
    ProductoMapper INSTANCE = Mappers.getMapper(ProductoMapper.class);
    
    // Request -> Entity (crear)
    @Mappings({
        @Mapping(target = "categoria", source = "categoriaId", qualifiedByName = "categoriaRef"),
        @Mapping(target = "unidad", source = "unidadId", qualifiedByName = "unidadRef"),
        @Mapping(target = "creadoEn", expression = "java(getCurrentDateTime())"),
        @Mapping(target = "ultimaActualizacion", expression = "java(getCurrentDateTime())")
    })
    Producto toEntity(ProductoRequestDto dto);
    
    // Entity -> Response
    @Mappings({
        @Mapping(target = "categoriaId", source = "categoria.id"),
        @Mapping(target = "categoriaNombre", source = "categoria.nombre"),
        @Mapping(target = "unidadId", source = "unidad.id"),
        @Mapping(target = "unidadNombre", source = "unidad.nombre")
    })
    ProductoResponseDto toResponseDto(Producto entity);
    
    // Update parcial
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "categoria", source = "categoriaId", qualifiedByName = "categoriaRef"),
        @Mapping(target = "unidad", source = "unidadId", qualifiedByName = "unidadRef"),
        @Mapping(target = "creadoEn", ignore = true),
        @Mapping(target = "ultimaActualizacion", expression = "java(getCurrentDateTime())")
    })
    void updateEntityFromDto(ProductoRequestDto dto, @MappingTarget Producto entity);
    
    @Named("categoriaRef")
    default Categoria mapCategoriaRef(Integer categoriaId) {
        if (categoriaId == null) return null;
        return Categoria.builder().id(categoriaId).build();
    }
    
    @Named("unidadRef")
    default Unidad mapUnidadRef(Integer unidadId) {
        if (unidadId == null) return null;
        return Unidad.builder().id(unidadId).build();
    }
    
    default LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}