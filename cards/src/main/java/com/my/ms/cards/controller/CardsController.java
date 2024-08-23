package com.my.ms.cards.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.my.ms.cards.constants.CardsConstants;
import com.my.ms.cards.dto.CardsDTO;
import com.my.ms.cards.dto.ErrorResponseDTO;
import com.my.ms.cards.dto.ResponseDTO;
import com.my.ms.cards.service.CardsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

@Tag(name = "CRUD REST APIs for Cards in EazyBank", description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH AND DELETE card details")
@RestController
@RequestMapping(path = "/api/v1", produces = { MediaType.APPLICATION_JSON_VALUE })
@AllArgsConstructor
@Validated
public class CardsController {

	private CardsService cardsService;

	@Operation(summary = "Create Card REST API", description = "REST API to create new Card inside EazyBank")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	@PostMapping("/create")
	public ResponseEntity<ResponseDTO> createCard(
			@Valid @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
		cardsService.createCard(mobileNumber);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDTO(CardsConstants.STATUS_201, CardsConstants.MESSAGE_201));
	}

	@Operation(summary = "Fetch Card Details REST API", description = "REST API to fetch card details based on a mobile number")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	@GetMapping("/fetch")
	public ResponseEntity<CardsDTO> fetchCardDetails(
			@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
		CardsDTO cardsDto = cardsService.fetchCard(mobileNumber);
		return ResponseEntity.status(HttpStatus.OK).body(cardsDto);
	}

	@Operation(summary = "Update Card Details REST API", description = "REST API to update card details based on a card number")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "417", description = "Expectation Failed"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	 @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateCardDetails(@Valid @RequestBody CardsDTO cardsDto) {
        boolean isUpdated = cardsService.updateCard(cardsDto);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDTO(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_UPDATE));
        }
    }

	@Operation(summary = "Delete Card Details REST API", description = "REST API to delete Card details based on a mobile number")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "417", description = "Expectation Failed"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	@DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> deleteCardDetails(@RequestParam
                                                                @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                                String mobileNumber) {
        boolean isDeleted = cardsService.deleteCard(mobileNumber);
        if(isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDTO(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_DELETE));
        }
    }
}
