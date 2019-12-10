/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195;

/**
 *
 * @author A Suzuki
 */
public class CustomerAddress {
    private final int customerId;
    private final String name;
    private final int addressId;
    private final String address;
    private final String address2;
    private final int cityId;
    private final String city;
    private final int countryId;
    private final String country;
    private final String phone;
    private final String postalCode;
    
    public CustomerAddress(int id, String name, int addressId, String address,
            String address2, int cityId, String city, int countryId, String country, String phone, String postalCode){
        this.customerId = id;
        this.name = name;
        this.addressId = addressId;
        this.address = address;
        this.address2 = address2;
        this.cityId = cityId;
        this.city = city;
        this.countryId = countryId;
        this.country = country;
        this.phone = phone;
        this.postalCode = postalCode;
    }
    
public int getCustomerId() {
        return this.customerId;
    }

    public String getName() {
        return this.name;
    }

    public int getAddressId() {
        return this.addressId;
    }

    public String getAddress() {
        return this.address;
    }

    public String getAddress2() {
        return this.address2;
    }

    public int getCityId() {
        return this.cityId;
    }

    public String getCity() {
        return this.city;
    }

    public int getCountryId() {
        return this.countryId;
    }

    public String getCountry() {
        return this.country;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getPostalCode() {
        return postalCode;
    }
    
}
