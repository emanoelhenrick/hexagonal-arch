package br.com.fullcycle.hexagonal.infraestructure.dtos;

public record NewEventDTO(
    String name,
    String date,
    Integer totalSpots,
    Long partnerId
) {

}
