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
import pe.com.andinadistribuidora.api.request.InventarioMovimientoRequestDto;
import pe.com.andinadistribuidora.api.response.InventarioMovimientoResponseDto;
import pe.com.andinadistribuidora.entity.Almacen;
import pe.com.andinadistribuidora.entity.InventarioMovimiento;
import pe.com.andinadistribuidora.entity.InventarioMovimiento.TipoMovimiento;
import pe.com.andinadistribuidora.entity.Producto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InventarioMovimientoMapper {
    
    InventarioMovimientoMapper INSTANCE = Mappers.getMapper(InventarioMovimientoMapper.class);
    
    // Request -> Entity (crear)
    @Mappings({
        @Mapping(target = "tipoMovimiento", source = "tipoMovimiento", qualifiedByName = "stringToEnum"),
        @Mapping(target = "almacen", source = "almacenId", qualifiedByName = "almacenRef"),
        @Mapping(target = "producto", source = "productoId", qualifiedByName = "productoRef")
    })
    InventarioMovimiento toEntity(InventarioMovimientoRequestDto dto);
    
    // Entity -> Response
    @Mappings({
        @Mapping(target = "almacenId", source = "almacenId"),
        @Mapping(target = "almacenNombre", source = "almacen.nombre"),
        @Mapping(target = "productoId", source = "productoId"),
        @Mapping(target = "productoNombre", source = "producto.nombre"),
        @Mapping(target = "productoSku", source = "producto.sku"),
        @Mapping(target = "tipoMovimiento", expression = "java(entity.getTipoMovimiento().name())"),
        @Mapping(target = "costoTotal", expression = "java(calcularCostoTotal(entity))")
    })
    InventarioMovimientoResponseDto toResponseDto(InventarioMovimiento entity);
    
    // Update parcial
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "almacen", ignore = true),
        @Mapping(target = "producto", ignore = true),
        @Mapping(target = "tipoMovimiento", source = "tipoMovimiento", qualifiedByName = "stringToEnum")
    })
    void updateEntityFromDto(InventarioMovimientoRequestDto dto, @MappingTarget InventarioMovimiento entity);
    
    @Named("stringToEnum")
    default TipoMovimiento stringToEnum(String tipo) {
        if (tipo == null) return null;
        return TipoMovimiento.valueOf(tipo.toUpperCase());
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
    
    default java.math.BigDecimal calcularCostoTotal(InventarioMovimiento entity) {
        if (entity.getCostoUnitario() == null || entity.getCantidad() == null) {
            return java.math.BigDecimal.ZERO;
        }
        return entity.getCostoUnitario().multiply(entity.getCantidad());
    }
}