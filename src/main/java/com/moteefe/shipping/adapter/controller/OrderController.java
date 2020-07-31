package com.moteefe.shipping.adapter.controller;

import com.moteefe.shipping.adapter.controller.dto.ErrorResponseDTO;
import com.moteefe.shipping.adapter.controller.dto.OrderRequestDTO;
import com.moteefe.shipping.adapter.controller.dto.OrderResponseDTO;
import com.moteefe.shipping.usecase.CreateShipment;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
@Api(tags = "Order controller")
class OrderController {

    private final CreateShipment createShipmentUseCase;

    @PostMapping
    @ApiResponses({
            @ApiResponse(code = 200, message = "The order was successfully accepted.", response = OrderResponseDTO.class),
            @ApiResponse(code = 400, message = "Error while performing pre-validations against the order creation.", response = ErrorResponseDTO.class)
    })
    OrderResponseDTO create(
            @ApiParam(value = "The representation of an order.", required = true)
            @Valid @RequestBody final OrderRequestDTO orderRequest) {
        log.debug("Performing an order. [ORDER={}]", orderRequest);
        final var shipment = createShipmentUseCase.create(orderRequest.toOrder());
        return new OrderResponseDTO(shipment);
    }
}
