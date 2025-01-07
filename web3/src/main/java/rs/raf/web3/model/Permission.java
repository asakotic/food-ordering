package rs.raf.web3.model;


import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Permission {
    private Boolean create;
    private Boolean read;
    private Boolean update;
    private Boolean delete;

    private Boolean search;
    private Boolean order;
    private Boolean cancel;
    private Boolean track;
    private Boolean schedule;

}
