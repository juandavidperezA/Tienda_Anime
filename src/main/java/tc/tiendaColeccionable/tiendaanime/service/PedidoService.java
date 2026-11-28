package tc.tiendaColeccionable.tiendaanime.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tc.tiendaColeccionable.tiendaanime.dto.DetallePedidoRequest;
import tc.tiendaColeccionable.tiendaanime.dto.DetallePedidoResponse;
import tc.tiendaColeccionable.tiendaanime.dto.PedidosRequest;
import tc.tiendaColeccionable.tiendaanime.dto.PedidosResponse;
import tc.tiendaColeccionable.tiendaanime.entity.*;
import tc.tiendaColeccionable.tiendaanime.repository.PedidoRepository;
import tc.tiendaColeccionable.tiendaanime.repository.ProductosRepository;
import tc.tiendaColeccionable.tiendaanime.repository.UsuariosRepository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Validated
@Transactional(readOnly = true)
public class PedidoService {
    private static final BigDecimal MONTO_PROMOCION_10= new BigDecimal("100000");
    private static final BigDecimal MONTO_PROMOCION_25= new BigDecimal("300000");
    private static final BigDecimal TASA_PROMOCION_10= new BigDecimal("0.10");
    private static final BigDecimal TASA_PROMOCION_25= new BigDecimal("0.25");

    private final PedidoRepository pedidoRepository;
    private final ProductosRepository productosRepository;
    private final UsuariosRepository usuariosRepository;


    public PedidoService(PedidoRepository pedidoRepository, ProductosRepository productosRepository, UsuariosRepository usuariosRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productosRepository = productosRepository;
        this.usuariosRepository = usuariosRepository;
    }

    @Transactional
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public PedidosResponse crear(@NotNull @Valid PedidosRequest pedidosRequest){
        Usuarios usuarios= usuariosRepository.findById(pedidosRequest.usuarioId()).orElseThrow(()->new IllegalArgumentException("No existe un usuario con ese ID=" + pedidosRequest.usuarioId()));

        Pedidos pedidos= new Pedidos(usuarios);

        Set<Long> productosIncluidos= new HashSet<>();

        for(DetallePedidoRequest detalle: pedidosRequest.detalles()){
            if(!productosIncluidos.add(detalle.productoId())){
                throw new IllegalArgumentException("El producto esta repetido con la ID="+ detalle.productoId());
            }
            Productos productos = productosRepository.findById(detalle.productoId())
                    .orElseThrow(()->new IllegalArgumentException("No se encontro productos con el ID="+ detalle.productoId()));

            pedidos.agregarDetalle(productos, detalle.cantidad());
        }

        BigDecimal subtotal= pedidos.calcularSubtotal();

        BigDecimal tasaDescuento= determinarTasaDescuento(subtotal);

        pedidos.calcularTotales(tasaDescuento);

        Pedidos guardar= pedidoRepository.save(pedidos);

        return toResponse(guardar);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<PedidosResponse> listar(){
        return pedidoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public Optional<PedidosResponse> buscarPorId(Long pedidoId){
        Usuarios usuario= usuarioActual();

        if(esAdmin()){
            return pedidoRepository.findById(pedidoId).map(this::toResponse);
        }

        return pedidoRepository.findByIdAndUsuario_Id(pedidoId, usuario.getId()).map(this::toResponse);
    }

    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public List<PedidosResponse> buscarLosMios(){
        Usuarios usuario= usuarioActual();

        return pedidoRepository.findByUsuario_Id(usuario.getId()).stream().map(this::toResponse).toList();
    }


    private DetallePedidoResponse toDetallePedidoResponse(DetallePedido detallePedido){
        return new DetallePedidoResponse(detallePedido.getId(),
                detallePedido.getProductos().getNombre(),detallePedido.getCantidad(),detallePedido.getPrecioUnitario(),detallePedido.calcularImporte());
    }

    private PedidosResponse toResponse(Pedidos pedidos){
        List<DetallePedidoResponse> detalles=pedidos.getDetalles().stream().map(this::toDetallePedidoResponse).toList();

        return new PedidosResponse(pedidos.getId(),pedidos.getUsuario().getId(),pedidos.getFecha(),pedidos.getSubtotal(),pedidos.getDescuento(),pedidos.getTotal(),pedidos.getEstado(),detalles);

    }

    private BigDecimal determinarTasaDescuento(BigDecimal subtotal){
        if(subtotal.compareTo(MONTO_PROMOCION_25)>=0){
            return TASA_PROMOCION_25;
        }

        if(subtotal.compareTo(MONTO_PROMOCION_10)>=0){
            return TASA_PROMOCION_10;
        }

        return BigDecimal.ZERO;
    }

    private Usuarios usuarioActual(){
        Authentication authentication= SecurityContextHolder
                .getContext()
                .getAuthentication();

        if(authentication==null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())){
            throw new BadCredentialsException("Autenticacion requerida");
        }
        Long usuarioId;
        try {
            usuarioId = Long.valueOf(authentication.getName());
        } catch (NumberFormatException exception) {
            throw new BadCredentialsException(
                    "Identidad inválida"
            );
        }

        return usuariosRepository.findById(usuarioId)
                .orElseThrow(() -> new BadCredentialsException(
                        "Usuario no disponible"
                ));


    }
    private boolean esAdmin() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN")
                );
    }
}
