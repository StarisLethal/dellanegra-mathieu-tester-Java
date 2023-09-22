package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    double fareOneHourWithoutDiscount = 1.5;
    double fareOneHourWithDiscount = 1.425;
    String vehicleReg = "ABCDEF";
    Date date = new Date();
    private static final Logger logger = LogManager.getLogger("ParkingDataBaseIT");
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static FareCalculatorService fareCalculatorService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterEach
    private void setUpAfterEachTest() {
        Ticket ticket = null;
        //dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown() {
        //dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void testParkingACar() {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        when(inputReaderUtil.readSelection()).thenReturn(1);

        parkingService.processIncomingVehicle();

        Ticket test =  ticketDAO.getTicket(vehicleReg);

        assertEquals(test.getVehicleRegNumber(), vehicleReg);
    }

    @Test
    public void testParkingLotExit() {

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        createTicket();
        parkingService.processExitingVehicle();

        double currentFare = (double) Math.round(getFare(vehicleReg) * 10) / 10; // 4.248 --> 4.25

        assertEquals(fareOneHourWithoutDiscount, currentFare);
        assertNotNull(getOutTime(vehicleReg));
    }

    @Test
    public void testParkingLotExitRecurringUser() {

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        createRecurringUserTicket();

        createTicket();

        parkingService.processExitingVehicle();

        double currentFareOneHourWithDiscount = getFareDiscount(vehicleReg);
        assertEquals(fareOneHourWithDiscount, currentFareOneHourWithDiscount, 0.01);

    }

    public Date getOutTime(String vehicleRegNumber) {
        Connection con = null;
        try {
            con = dataBaseTestConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.TEST_OUTTIME);
            ps.setString(1, vehicleRegNumber);
            ps.execute();
            ResultSet rs = ps.executeQuery();
            try {
                while (rs.next()) {
                    Date outTime = rs.getTimestamp(1);
                    return outTime;
                }
            } finally {
                rs.close();
            }
        } catch (Exception ex) {
            logger.error("Error find vehicle reg info", ex);
        } finally {
            dataBaseTestConfig.closeConnection(con);
        }
        return null;
    }

    public double getFare(String vehicleRegNumber) {
        Connection con = null;
        try {
            con = dataBaseTestConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.TEST_FARE);
            ps.setString(1, vehicleRegNumber);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double fare = rs.getDouble(1);
                return fare;
            }
        } catch (Exception ex) {
            logger.error("Error finding vehicle reg info", ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                logger.error("Error closing database connection", e);
            }
        }


        return 666.0;
    }
    public double getFareDiscount(String vehicleRegNumber) {
        Connection con = null;
        try {
            con = dataBaseTestConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.TEST_FARE_DISCOUNT);
            ps.setString(1, vehicleRegNumber);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double fare = rs.getDouble(1);
                return fare;
            }
        } catch (Exception ex) {
            logger.error("Error finding vehicle reg info", ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                logger.error("Error closing database connection", e);
            }
        }


        return 666.0;
    }

    public void createTicket() {
        Date dateAvant = new Date();
        dateAvant.setHours(dateAvant.getHours() - 1);

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setId(1);
        ticket.setVehicleRegNumber(vehicleReg);
        ticket.setPrice(0);
        ticket.setInTime(dateAvant);

        ticketDAO.saveTicket(ticket);

    }

    public void createRecurringUserTicket() {
        Date dateAvant = new Date();
        dateAvant.setHours(dateAvant.getHours() - 1);

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setId(1);
        ticket.setVehicleRegNumber(vehicleReg);
        ticket.setPrice(0);
        ticket.setInTime(dateAvant);

        needTicket(ticket);
        needTicket(ticket);

    }

    public boolean needTicket(Ticket ticket){
        Connection con = null;
        try {
            con = dataBaseTestConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.DISCOUNT_NEED_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            //ps.setInt(1,ticket.getId());
            ps.setInt(1,ticket.getParkingSpot().getId());
            ps.setString(2, ticket.getVehicleRegNumber());
            ps.setDouble(3, ticket.getPrice());
            ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
            ps.setTimestamp(5, (ticket.getOutTime() == null)?null: (new Timestamp(ticket.getOutTime().getTime())) );
            return ps.execute();
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }finally {
            dataBaseTestConfig.closeConnection(con);
            return false;
        }
    }
}