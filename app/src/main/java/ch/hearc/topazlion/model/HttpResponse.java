package ch.hearc.topazlion.model;

public class HttpResponse {
    
    private long id = 0;
    private int status = 0;
    private String message = "";

    public HttpResponse(long id, int status, String message) {
        this.id = id;
        this.status = status;
        this.message = message;
    }

    public HttpResponse() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}
