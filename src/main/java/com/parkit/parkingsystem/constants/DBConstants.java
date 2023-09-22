package com.parkit.parkingsystem.constants;


public class DBConstants {

    public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";
    public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";

    public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
    public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
    public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME  limit 1";
    public static final String DISCOUNT_COUNT = "SELECT COUNT(*) FROM ticket_paid WHERE VEHICLE_REG_NUMBER = ?";
    public static final String ARCHIVE_PRINTED_TICKET = "INSERT INTO ticket_paid (PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) SELECT PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME FROM ticket WHERE ID = ?";
    public static final String DELETE_PRINTED_TICKET = "DELETE FROM ticket WHERE ID = ?";
    public static final String NO_DUPE_VEHICLE_REG = "SELECT COUNT(*) FROM ticket WHERE VEHICLE_REG_NUMBER= ?";
    public static final String TEST_FARE_DISCOUNT ="SELECT PRICE FROM ticket_paid WHERE ID = 3 and VEHICLE_REG_NUMBER= ?";
    public static final String TEST_FARE ="SELECT PRICE FROM ticket_paid WHERE VEHICLE_REG_NUMBER= ?";
    public static final String TEST_OUTTIME ="SELECT OUT_TIME FROM ticket_paid WHERE VEHICLE_REG_NUMBER= ?";
    public static final String DISCOUNT_NEED_TICKET = "insert into ticket_paid(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";

}
