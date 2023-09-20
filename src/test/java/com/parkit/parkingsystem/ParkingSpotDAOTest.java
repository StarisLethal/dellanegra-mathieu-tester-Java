package com.parkit.parkingsystem;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;


public class ParkingSpotDAOTest {

    ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
    @Mock
    private DataBaseConfig dataBaseConfig;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseConfig;
    }

    @Test
    public void getNextAvailableSlotTest() throws SQLException, ClassNotFoundException {

        ParkingType parkingType = ParkingType.CAR;

        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        parkingSpotDAO.getNextAvailableSlot(parkingType);


        verify(preparedStatement).executeQuery();
        verify(dataBaseConfig).closeConnection(connection);
    }

    @Test
    public void updateParkingTest() throws SQLException, ClassNotFoundException {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);

        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        parkingSpotDAO.updateParking(parkingSpot);


        verify(preparedStatement).executeUpdate();
        verify(dataBaseConfig).closeConnection(connection);
    }

}
