package org.university.zoomanagementsystem.stripe;

public class CheckoutResponse {
    private String status;
    private String message;
    private String checkoutId;
    private String checkoutLink;

    public CheckoutResponse() {
        this.status = null;
        this.message = null;
        this. checkoutId = null;
        this.checkoutLink = null;
    }

    public CheckoutResponse(String status, String message, String checkoutId, String checkoutLink) {
        this.status = status;
        this.message = message;
        this.checkoutId = checkoutId;
        this.checkoutLink = checkoutLink;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCheckoutId() {
        return checkoutId;
    }

    public void setCheckoutId(String checkoutId) {
        this.checkoutId = checkoutId;
    }

    public String getCheckoutLink() {
        return checkoutLink;
    }

    public void setCheckoutLink(String checkoutLink) {
        this.checkoutLink = checkoutLink;
    }
}
