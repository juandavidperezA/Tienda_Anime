package tc.tiendaColeccionable.tiendaanime.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/API/Pedidos")
@Tag(name = "Pedidos",description = "Creacion y consultas de pedidos")
public class PedidoController {
}
