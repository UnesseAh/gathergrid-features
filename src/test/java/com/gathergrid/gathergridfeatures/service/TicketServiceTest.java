package com.gathergrid.gathergridfeatures.service;

import com.gathergrid.gathergridfeatures.domain.Ticket;
import com.gathergrid.gathergridfeatures.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class TicketServiceTest {
    private TicketService ticketService;
    private TicketRepository ticketRepository;
    @BeforeEach
    void setUp() {
        ticketRepository = Mockito.mock(TicketRepository.class);
        ticketService = new TicketService();
    }

    @Test
    public void testCreateTicket() {
        // Prepare test data
        Ticket ticket = new Ticket();

        // Mock the repository behavior
        Mockito.when(ticketRepository.save(ticket)).thenReturn(ticket);

        // Perform the test
        Ticket result = ticketService.createTicket(ticket);

        // Verify the result
        assertEquals(ticket, result);
    }
}