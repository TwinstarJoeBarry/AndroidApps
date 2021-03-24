package edu.ncc.nest.nestapp.GuestDatabaseRegistration.DatabaseClasses;

import androidx.annotation.NonNull;

/**
 *
 * Copyright (C) 2019 The LibreFoodPantry Developers.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/**
 * GuestRegistryEntry: Represents a entry into the GuestRegistry database
 * (See {@link GuestRegistryHelper}).
 */
public class GuestRegistryEntry {
    private long id;
    private String name;
    private String email;
    private String phone;
    private String date;
    private String address;
    private String city;
    private String zipcode;
    private String state;
    private String additionalInfo;
    private String nameOfVolunteer;
    private String nccID;
    private String barcode;

    public GuestRegistryEntry() {

        this.name = null;
        this.email = null;
        this.phone = null;
        this.date = null;
        this.address = null;
        this.city = null;
        this.zipcode = null;
        this.state = null;
        this.additionalInfo = null;
        this.nameOfVolunteer = null;
        this.nccID = null;
        this.barcode = null;

    }

    // parameterized constructor
    public GuestRegistryEntry(String name, String email, String phone, String date, String address, String city, String zipcode,
                              String state, String additionalInfo, String nameOfVolunteer, String nccID, String barcode) {

        this.name = name;
        this.email = email;
        this.phone = phone;
        this.date = date;
        this.address = address;
        this.city = city;
        this.zipcode = zipcode;
        this.state = state;
        this.additionalInfo = additionalInfo;
        this.nameOfVolunteer = nameOfVolunteer;
        this.nccID = nccID;
        this.barcode = barcode;

    }

    // Getter Methods
    public long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getDate() { return date; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getZipcode() { return zipcode; }
    public String getState() { return state; }
    public String getAdditionalInfo() { return additionalInfo; }
    public String getNameOfVolunteer() { return nameOfVolunteer; }
    public String getNccID() { return nccID; }
    public String getBarcode() { return barcode; }

    // Setter Methods
    public void setId(long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setDate(String date) { this.date = date; }
    public void setAddress(String address) { this.address = address; }
    public void setCity(String city) { this.city = city; }
    public void setZipcode(String zipcode) { this.zipcode = zipcode; }
    public void setState(String state) { this.state = state; }
    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }
    public void setNameOfVolunteer(String nameOfVolunteer) { this.nameOfVolunteer = nameOfVolunteer; }
    public void setNccID(String nccID) { this.nccID = nccID; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    /**
     * equals method --
     * Returns whether or not the id variables of the two {@link GuestRegistryEntry} objects are
     * equal.
     * @param otherEntry The entry to compare to
     * @return true if the entry has the same id, false otherwise
     */
    @Override
    public boolean equals(Object otherEntry) {

        if (otherEntry instanceof GuestRegistryEntry)

            return (this.id == ((GuestRegistryEntry) otherEntry).id);

        return false;

    }

    @NonNull
    @Override
    public String toString() {

        // Will be used by the ArrayAdapter in the ListView

        return id + ": " + name + " - " + email + " - " + phone + " - " + date + " - " + address +
                " - " + city + " - " + zipcode + " - " + state + " - " + additionalInfo + " - "
                + nameOfVolunteer + " - " + nccID;

    }

}