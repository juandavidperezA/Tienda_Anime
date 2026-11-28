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
import tc.tiendaColeccionable.tiendaanime.entity.Clientes;
import tc.tiendaColeccionable.tiendaanime.entity.DetallePedido;
import tc.tiendaColeccionable.tiendaanime.entity.Pedidos;
import tc.tiendaColeccionable.tiendaanime.entity.Productos;
import tc.tiendaColeccionable.tiendaanime.repository.ClienteRepository;
import tc.tiendaColeccionable.tiendaanime.repository.PedidoRepository;
import tc.tiendaColeccionable.tiendaanime.repository.ProductosRepository;

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
    private final ClienteRepository clienteRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProductosRepository productosRepository, ClienteRepository clienteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productosRepository = productosRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public PedidosResponse crear(@NotNull @Valid PedidosRequest pedidosRequest){
        Clientes cliente= clienteRepository.findById(pedidosRequest.clienteId()).orElseThrow(()->new IllegalArgumentException("No existe un Cliente con ese ID=" + pedidosRequest.clienteId()));

        Pedidos pedidos= new Pedidos(cliente);

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
        Usuario usuario= usuarioActual();

        if(esAdmin()){
            return pedidoRepository.findById(pedidoId).map(this::toResponse);
        }

        return pedidoRepository.findByIdAndUsuario_Id(pedidoId, usuario.getId()).map(this::toResponse);
    }

    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public List<PedidosResponse> buscarLosMios(){
        Usuario usuario= usuarioActual();

        return pedidoRepository.findByUsuario_Id(usuario.getid()).stream().map(this::toResponse).toList();
    }


    private DetallePedidoResponse toDetallePedidoResponse(DetallePedido detallePedido){
        return new DetallePedidoResponse(detallePedido.getId(),
                detallePedido.getProductos().getNombre(),detallePedido.getCantidad(),detallePedido.getPrecioUnitario(),detallePedido.calcularImporte());
    }

    private PedidosResponse toResponse(Pedidos pedidos){
        List<DetallePedidoResponse> detalles=pedidos.getDetalles().stream().map(this::toDetallePedidoResponse).toList();

        return new PedidosResponse(pedidos.getId(),pedidos.getClientes().getId(),pedidos.getFecha(),pedidos.getSubtotal(),pedidos.getDescuento(),pedidos.getTotal(),pedidos.getEstado(),detalles);

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

    private Usuario usuarioActual(){
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

        return usuarioRepository.findById(usuarioId)
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
