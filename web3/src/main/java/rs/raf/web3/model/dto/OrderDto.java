package rs.raf.web3.model.dto;

import rs.raf.web3.model.Dish;

import java.util.List;

public class OrderDto {
    private Long id;
    private String status;
    private String createdBy;
    private String createdAt;
    private List<ResDishDto> dishes;

    public OrderDto() {
    }

    public OrderDto(OrderDto orederDto) {
        this.id = orederDto.id;
        this.status = orederDto.status;
        this.createdBy = orederDto.createdBy;
        this.createdAt = orederDto.createdAt;
        this.dishes = orederDto.dishes;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<ResDishDto> getDishes() {
        return dishes;
    }

    public void setDishes(List<ResDishDto> dishes) {
        this.dishes = dishes;
    }
}
