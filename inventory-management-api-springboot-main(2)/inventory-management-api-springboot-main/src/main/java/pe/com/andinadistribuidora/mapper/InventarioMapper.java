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
import pe.com.andinadistribuidora.api.request.InventarioRequestDto;
import pe.com.andinadistribuidora.api.response.InventarioResponseDto;
import pe.com.andinadistribuidora.entity.Almacen;
import pe.com.andinadistribuidora.entity.Inventario;
import pe.com.andinadistribuidora.entity.Inventario.InventarioId;
import pe.com.andinadistribuidora.entity.Producto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InventarioMapper {
    
    InventarioMapper INSTANCE = Mappers.getMapper(InventarioMapper.class);
    
    // Request -> Entity (crear)
    @Mappings({
        @Mapping(target = "id", source = ".", qualifiedByName = "createInventarioId"),
        @Mapping(target = "almacen", source = "almacenId", qualifiedByName = "almacenRef"),
        @Mapping(target = "producto", source = "productoId", qualifiedByName = "productoRef")
    })
    Inventario toEntity(InventarioRequestDto dto);
    
    // Entity -> Response
    @Mappings({
        @Mapping(target = "almacenId", source = "id.almacenId"),
        @Mapping(target = "almacenNombre", source = "almacen.nombre"),
        @Mapping(target = "productoId", source = "id.productoId"),
        @Mapping(target = "productoNombre", source = "producto.nombre"),
        @Mapping(target = "productoSku", source = "producto.sku"),
        @Mapping(target = "bajStock", expression = "java(entity.getCantidad().compareTo(entity.getStockMin()) < 0)"),
        @Mapping(target = "stockCritico", expression = "java(entity.getCantidad().compareTo(entity.getStockMin().multiply(java.math.BigDecimal.valueOf(0.5))) <= 0)")
    })
    InventarioResponseDto toResponseDto(Inventario entity);
    
    // Update parcial
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "almacen", ignore = true),
        @Mapping(target = "producto", ignore = true)
    })
    void updateEntityFromDto(InventarioRequestDto dto, @MappingTarget Inventario entity);
    
    @Named("createInventarioId")
    default InventarioId createInventarioId(InventarioRequestDto dto) {
        if (dto.getAlmacenId() == null || dto.getProductoId() == null) return null;
        return new InventarioId(dto.getAlmacenId(), dto.getProductoId());
    }
    
    @Named("almacenRef")
    default Almacen mapAlmacenRef(Integer almacenId) {
        if (almacenId == null) return null;
        return Almacen.builder().id(almacenId).build();
    }
    
    @Named("productoRef")
    default Producto mapProductoRef(Integer productoId) {
        if (productoId == null) return null;
        return Producto.builder().id(productoId).build();
    }
}