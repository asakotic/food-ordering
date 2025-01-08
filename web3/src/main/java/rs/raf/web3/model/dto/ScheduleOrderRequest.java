package rs.raf.web3.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ScheduleOrderRequest {
    private LocalDateTime scheduledTime;
    private List<DishOrderDto> dishOrderDtos;

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public List<DishOrderDto> getDishOrderDtos() {
        return dishOrderDtos;
    }

    public void setDishOrderDtos(List<DishOrderDto> dishOrderDtos) {
        this.dishOrderDtos = dishOrderDtos;
    }
}
