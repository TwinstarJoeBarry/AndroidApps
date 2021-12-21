package edu.ncc.nest.nestapp.GuestDatabaseRegistration.DatabaseClasses;

/**
 *
 * Copyright (C) 2020 The LibreFoodPantry Developers.
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

import androidx.annotation.NonNull;

/**
 * GuestRegistryEntry: Represents a entry into the GuestRegistry database
 * (See {@link GuestRegistryHelper}).
 */
public class GuestRegistryEntry {
    private long id;

    // first fragment information
    private String name;
    private String phone;
    private String nccID;

    private String date; // why are we collecting date?

    // second fragment information
    private String address;
    private String city;
    private String zipcode;
    private String state;
    private String affiliation;
    private String age;
    private String gender;

    // third fragment information
    private String diet;
    private String programs;
    private String snap;
    private String employment;
    private String health;
    private String housing;
    private String income;

    // fourth fragment information
    private String householdNum;
    private String childcareStatus;
    private String children1;
    private String children5;
    private String children12;
    private String children18;

    private String referralInfo;
    private String additionalInfo;
    private String nameOfVolunteer;
    private String barcode;

    public GuestRegistryEntry() {

        this.name = null;
        this.phone = null;
        this.nccID = null;
        this.date = null;

        this.address = null;
        this.city = null;
        this.zipcode = null;
        this.state = null;
        this.affiliation = null;
        this.age = null;
        this.gender = null;

        this.diet = null;
        this.programs = null;
        this.snap = null;
        this.employment = null;
        this.health = null;
        this.housing = null;
        this.income = null;

        this.householdNum = null;
        this.childcareStatus = null;
        this.children1 = null;
        this.children5 = null;
        this.children12 = null;
        this.children18 = null;

        this.referralInfo = null;
        this.additionalInfo = null;
        this.nameOfVolunteer = null;
        this.barcode = null;
    }

    // parameterized constructor
    public GuestRegistryEntry(String name, String phone, String nccID, String date, String address, String city, String zipcode,
                              String state, String affiliation, String age, String gender, String diet, String programs,
                              String snap, String employment, String health, String housing, String income, String householdNum,
                              String childcareStatus, String children1, String children5, String children12, String children18,
                              String referralInfo, String additionalInfo, String nameOfVolunteer, String barcode) {

        this.name = name;
        this.phone = phone;
        this.nccID = nccID;
        this.date = date;

        this.address = address;
        this.city = city;
        this.zipcode = zipcode;
        this.state = state;
        this.affiliation = affiliation;
        this.age = age;
        this.gender = gender;

        this.diet = diet;
        this.programs = programs;
        this.snap = snap;
        this.employment = employment;
        this.health = health;
        this.housing = housing;
        this.income = income;

        this.householdNum = householdNum;
        this.childcareStatus = childcareStatus;
        this.children1 = children1;
        this.children5 = children5;
        this.children12 = children12;
        this.children18 = children18;

        this.referralInfo = referralInfo;
        this.additionalInfo = additionalInfo;
        this.nameOfVolunteer = nameOfVolunteer;
        this.barcode = barcode;

    }

    // Getter Methods
    public long getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getNccID() { return nccID; }
    public String getDate() { return date; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getZipcode() { return zipcode; }
    public String getState() { return state; }
    public String getAffiliation() { return affiliation; }
    public String getAge() { return age; }
    public String getGender () { return gender; }

    public String getDiet() { return diet; }
    public String getPrograms() { return programs; }
    public String getSnap() { return snap; }
    public String getEmployment() { return employment; }
    public String getHealth() { return health; }
    public String getHousing() { return housing; }
    public String getIncome() { return income; }

    public String getHouseholdNum() { return householdNum; }
    public String getChildcareStatus() { return childcareStatus; }
    public String getChildren1() { return children1; }
    public String getChildren5() { return children5; }
    public String getChildren12() { return children12; }
    public String getChildren18() { return children18; }

    public String getReferralInfo() { return referralInfo; }
    public String getAdditionalInfo() { return additionalInfo; }
    public String getNameOfVolunteer() { return nameOfVolunteer; }
    public String getBarcode() { return barcode; }

    // Setter Methods
    public void setId(long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setNccID(String nccID) { this.nccID = nccID; }
    public void setDate(String date) { this.date = date; }
    public void setAddress(String address) { this.address = address; }
    public void setCity(String city) { this.city = city; }
    public void setZipcode(String zipcode) { this.zipcode = zipcode; }
    public void setState(String state) { this.state = state; }
    public void setAffiliation(String affiliation) { this.affiliation = affiliation; }
    public void setAge(String age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }

    public void setDiet(String diet) { this.diet = diet; }
    public void setPrograms(String programs) { this.programs = programs; }
    public void setSnap(String snap) { this.snap = snap; }
    public void setEmployment(String employment) { this.employment = employment; }
    public void setHealth(String health) { this.health = health; }
    public void setHousing(String housing) { this.housing = housing; }
    public void setIncome(String income) { this.income = income; }

    public void setHouseholdNum(String householdNum) { this.householdNum = householdNum; }
    public void setChildcareStatus(String childcareStatus) { this.childcareStatus = childcareStatus; }
    public void setChildren1(String children1) { this.children1 = children1; }
    public void setChildren5(String children5) { this.children5 = children5; }
    public void setChildren12(String children12) { this.children12 = children12; }
    public void setChildren18(String children18) { this.children18 = children18; }

    public void setReferralInfo(String referralInfo) { this.referralInfo = referralInfo; }
    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }
    public void setNameOfVolunteer(String nameOfVolunteer) { this.nameOfVolunteer = nameOfVolunteer; }
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

        return id + ": " + name  + " - " + phone + " - " + date + " - " + address +
                " - " + city + " - " + zipcode + " - " + state + " - " + additionalInfo + " - "
                + nameOfVolunteer + " - " + nccID;

    }

}