package tc.tiendaColeccionable.tiendaanime.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tc.tiendaColeccionable.tiendaanime.dto.PedidosRequest;
import tc.tiendaColeccionable.tiendaanime.dto.PedidosResponse;
import tc.tiendaColeccionable.tiendaanime.service.PedidoService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/API/Pedidos")
@Tag(name = "Pedidos",description = "Creacion y consultas de pedidos")
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @Operation(
            summary ="Crear un pedido",
            description = """
                    Recibe productos y cantidades.
                    El Servidor obtiene los ´recops deñ catalogo 
                    y calcula subtotal, descuento y total.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Recurso creado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos invalidos",
                    content = @Content
            )
    })
    public ResponseEntity<PedidosResponse> crear(@RequestBody @Valid PedidosRequest request){
        PedidosResponse pedido = pedidoService.crear(request);
        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pedido.id())
                .toUri();
        return ResponseEntity.created(ubicacion)
                .body(pedido);
    }

    @GetMapping
    @Operation(summary = "Listar pedidos")
    public ResponseEntity<List<PedidosResponse>> listar(){
        return ResponseEntity.ok(pedidoService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar los pedidos por ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Recurso encontrado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ID invalido",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "El pedido no existe",
                    content = @Content
            )
    })
    public ResponseEntity<PedidosResponse> buscarPorId(@PathVariable("id") @Positive Long id){
        return pedidoService.buscarPorId(id).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @GetMapping("/misPedidos")
    @Operation(summary = "Consultar mis pedidos")
    public ResponseEntity<List<PedidosResponse>> ConsultarMisPedidos(){
        return ResponseEntity.ok(pedidoService.buscarLosMios());
    }
}
