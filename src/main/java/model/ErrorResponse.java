package model;

/**
 * A standardised error response body.
 * All error responses across the API use this structure so clients
 * always get a predictable JSON format when something goes wrong.
 */
public class ErrorResponse {

    private String error;    // short error category, e.g. "Not Found"
    private String message;  // human-readable description of what went wrong
    private int status;      // the HTTP status code

    public ErrorResponse() {}

    public ErrorResponse(String error, String message, int status) {
        this.error = error;
        this.message = message;
        this.status = status;
    }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
}
