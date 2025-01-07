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

    public void setCreate(Boolean create) {
        this.create = create;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }
}
