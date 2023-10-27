package com.gathergrid.gathergridfeatures.service;

import com.gathergrid.gathergridfeatures.domain.Event;
import com.gathergrid.gathergridfeatures.domain.Ticket;
import com.gathergrid.gathergridfeatures.repository.TicketRepository;
import com.gathergrid.gathergridfeatures.repository.interfacesImpl.EventRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService() {
        ticketRepository = new TicketRepository();
    }

    public Ticket createTicket(Ticket ticket) {
        validateTicket(ticket);
        return ticketRepository.save(ticket);
    }

    public void updateTicket(Ticket ticket) throws Exception {
        validateTicket(ticket);
        Ticket existingTicket = ticketRepository.find(ticket.getId());
        if (existingTicket != null) {
            ticketRepository.update(ticket);
        } else {
            throw new Exception("Ticket not found");
        }
    }

    public void deleteTicket(long id) throws Exception {
        Ticket existingTicket = ticketRepository.find(id);
        if (existingTicket != null) {
            ticketRepository.delete(id);
        } else {
            throw new Exception("Ticket not found");
        }
    }

    public Ticket findTicket(long id) {
        return ticketRepository.find(id);
    }

    public List<Ticket> findAllTickets() {
        return ticketRepository.findAll();
    }

    EventRepositoryImpl eventRepository = new EventRepositoryImpl();

    public List<Ticket> findAllEventTickets(long id) {
        Event event = eventRepository.find(id);
        if (event != null)
            return ticketRepository.finAllEventTickets(event.getId());
        return new ArrayList<>();
    }

    private void validateTicket(Ticket ticket) {
        if (ticket.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        if (ticket.getQuantityAvailable() < 0) {
            throw new IllegalArgumentException("Quantity available cannot be negative");
        }
        if (ticket.getType() == null) {
            throw new IllegalArgumentException("Ticket type must be specified");
        }
    }
}