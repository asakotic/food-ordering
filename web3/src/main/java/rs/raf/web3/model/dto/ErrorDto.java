package rs.raf.web3.model.dto;

import java.time.LocalDateTime;

public class ErrorDto {
    private String message;
    private LocalDateTime timestamp;
    private String email;

    public ErrorDto( String message, LocalDateTime timestamp, String email) {
        this.message = message;
        this.timestamp = timestamp;
        this.email = email;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

