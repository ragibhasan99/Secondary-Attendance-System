package com.example.myattendance;

/**
 * The {@code Date} class represents a date value in the attendance system.
 * It provides a simple way to store, retrieve, and modify a date string.
 * <p>
 * The class contains a field for the date, a constructor to initialize it,
 * and getter and setter methods to access and modify the date.
 * </p>
 */
public class Date {

    // The date string representing the date value.
    private String date;

    /**
     * Constructs a {@code Date} object with the specified date.
     *
     * @param date The date to initialize the object with.
     */
    public Date(String date) {
        this.date = date;
    }

    /**
     * Retrieves the current value of the date.
     *
     * @return A string representing the current date.
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets a new value for the date.
     *
     * @param date The new date value to set.
     */
    public void setDate(String date) {
        this.date = date;
    }
}
