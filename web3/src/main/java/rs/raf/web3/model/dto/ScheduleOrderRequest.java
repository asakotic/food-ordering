package rs.raf.web3.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ScheduleOrderRequest {
    private LocalDateTime scheduledTime;
    private List<DishOrderDto> dishOrderDtos;
}
