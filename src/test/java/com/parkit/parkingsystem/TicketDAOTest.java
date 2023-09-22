package com.parkit.parkingsystem;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TicketDAOTest {

    int idTest = 1;
    String vehicleReg = "ABCDEF";
    double price = 2.50;
    @Mock
    private DataBaseConfig dataBaseConfig;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;
    private TicketDAO ticketDAO;
    private Ticket ticket;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseConfig;
    }

    @Test
    public void testSaveTicket() throws SQLException, ClassNotFoundException {
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(new ParkingSpot(1, null, false));
        ticket.setVehicleRegNumber(vehicleReg);
        ticket.setPrice(price);
        ticket.setInTime(new Date());
        ticket.setOutTime(new Date());

        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.execute()).thenReturn(true);

        ticketDAO.saveTicket(ticket);

        verify(preparedStatement).execute();
    }

    @Test
    public void testGetTicket() {
        try {
            String vehicleRegNumber = "ABCEFG";

            when(dataBaseConfig.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(DBConstants.GET_TICKET)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt(1)).thenReturn(1);
            when(resultSet.getString(6)).thenReturn("CAR");
            when(resultSet.getInt(2)).thenReturn(1);
            when(resultSet.getString(3)).thenReturn(vehicleRegNumber);
            when(resultSet.getDouble(4)).thenReturn(10.0);
            when(resultSet.getTimestamp(5)).thenReturn(new Timestamp(System.currentTimeMillis()));
            when(resultSet.getTimestamp(6)).thenReturn(new Timestamp(System.currentTimeMillis()));

            Ticket result = ticketDAO.getTicket(vehicleRegNumber);

            assertNotNull(result);
            assertEquals(vehicleRegNumber, result.getVehicleRegNumber());
            verify(preparedStatement).executeQuery();


        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testGetNbTicket() throws SQLException, ClassNotFoundException {
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(new ParkingSpot(1, null, false));
        ticket.setVehicleRegNumber(vehicleReg);
        ticket.setPrice(price);
        ticket.setInTime(new Date());
        ticket.setOutTime(new Date());

        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.execute()).thenReturn(true);


        ticketDAO.getNbTicket(vehicleReg);

        verify(preparedStatement).execute();
    }

    @Test
    public void testVehicleRegAlreadyPark() throws SQLException, ClassNotFoundException {
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(new ParkingSpot(1, null, false));
        ticket.setVehicleRegNumber(vehicleReg);
        ticket.setPrice(price);
        ticket.setInTime(new Date());
        ticket.setOutTime(new Date());

        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.execute()).thenReturn(true);

        ticketDAO.vehicleRegAlreadyPark(vehicleReg);

        verify(preparedStatement).execute();
    }

    @Test
    public void testArchiveTicket() throws SQLException, ClassNotFoundException {
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(new ParkingSpot(1, null, false));
        ticket.setVehicleRegNumber(vehicleReg);
        ticket.setPrice(price);
        ticket.setInTime(new Date());
        ticket.setOutTime(new Date());

        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.execute()).thenReturn(true);

        ticketDAO.archiveTicket(idTest);

        verify(preparedStatement).execute();
    }

    @Test
    public void testRemoveTicket() throws SQLException, ClassNotFoundException {
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(new ParkingSpot(1, null, false));
        ticket.setVehicleRegNumber(vehicleReg);
        ticket.setPrice(price);
        ticket.setInTime(new Date());
        ticket.setOutTime(new Date());

        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.execute()).thenReturn(true);

        ticketDAO.removeTicket(idTest);

        verify(preparedStatement).execute();
    }
}