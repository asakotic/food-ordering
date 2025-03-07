package rs.raf.web3.model.dto;

import lombok.Data;
import rs.raf.web3.model.Dish;

@Data
public class DishOrderDto {
    private Dish dish;
    private int quantity;

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
