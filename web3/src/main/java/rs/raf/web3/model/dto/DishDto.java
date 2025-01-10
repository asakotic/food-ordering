package rs.raf.web3.model.dto;

public class DishDto {
    private Long dishId;
    private String name;
    private double price;

    public DishDto(Long dishId, String name, double price) {
        this.dishId = dishId;
        this.name = name;
        this.price = price;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
