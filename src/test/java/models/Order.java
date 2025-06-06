package models;

public class Order {
    private String firstName = "Naruto";
    private String lastName = "Uchiha";
    private String address = "Konoha, 142 apt.";
    private int metroStation = 4;
    private String phone = "+78003553535";
    private int rentTime = 5;
    private String deliveryDate = "2023-06-06";
    private String comment = "Saske, come back to Konoha";
    private String[] color;

    public Order(String[] color) {
        this.color = color;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public int getMetroStation() { return metroStation; }
    public String getPhone() { return phone; }
    public int getRentTime() { return rentTime; }
    public String getDeliveryDate() { return deliveryDate; }
    public String getComment() { return comment; }
    public String[] getColor() { return color; }
}