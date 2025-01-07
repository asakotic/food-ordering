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

    public Boolean getCreate() {
        return create;
    }

    public Boolean getRead() {
        return read;
    }

    public Boolean getUpdate() {
        return update;
    }

    public Boolean getDelete() {
        return delete;
    }

    public Boolean getSearch() {
        return search;
    }

    public Boolean getOrder() {
        return order;
    }

    public Boolean getCancel() {
        return cancel;
    }

    public Boolean getTrack() {
        return track;
    }

    public Boolean getSchedule() {
        return schedule;
    }
}
