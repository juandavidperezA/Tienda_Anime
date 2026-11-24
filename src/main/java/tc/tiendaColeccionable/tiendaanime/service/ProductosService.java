package tc.tiendaColeccionable.tiendaanime.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tc.tiendaColeccionable.tiendaanime.dto.ProductoResponse;
import tc.tiendaColeccionable.tiendaanime.dto.ProductosRequest;
import tc.tiendaColeccionable.tiendaanime.entity.Categorias;
import tc.tiendaColeccionable.tiendaanime.entity.Productos;
import tc.tiendaColeccionable.tiendaanime.repository.CategoriaRepository;
import tc.tiendaColeccionable.tiendaanime.repository.ProductosRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@Validated
@Transactional(readOnly = true)
public class ProductosService {

    private final ProductosRepository productosRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductosService(ProductosRepository productosRepository, CategoriaRepository categoriaRepository) {
        this.productosRepository = productosRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public ProductoResponse crear(@NotNull @Valid ProductosRequest productosRequest){
        Categorias categorias= categoriaRepository.findById(productosRequest.categoriaId())
                .orElseThrow(()->new IllegalArgumentException("No existe la categoria con el ID="+ productosRequest.categoriaId()));

        BigDecimal precio= productosRequest.precio().setScale(2, RoundingMode.UNNECESSARY);

        Productos productos= new Productos(productosRequest.nombre().strip(),categorias, precio,productosRequest.cantidad());

        Productos guardar= productosRepository.save(productos);

        return toResponse(guardar);
    }


    public List<ProductoResponse> listar(){
        return productosRepository.findAll().stream().map(this::toResponse).toList();
    }

    public Optional<ProductoResponse> buscarPorId(@NotNull @Positive Long id){
        return productosRepository.findById(id).map(this::toResponse);
    }

    public List<ProductoResponse> buscarPorCategoria(@NotNull @Positive Long id){
        return productosRepository.findByCategoria_Id(id).stream().map(this::toResponse).toList();
    }

    public List<ProductoResponse> buscarPorNombre(@NotBlank String nombreProducto){
        return productosRepository.findByNombre(nombreProducto.strip()).stream().map(this::toResponse).toList();
    }

    private ProductoResponse toResponse(Productos productos){
        return new ProductoResponse(productos.getId(),productos.getNombre(),productos.getCategoria().getId()
                ,productos.getCategoria().getNombre(),productos.getPrecio(),productos.getCantidad());
    }
}
