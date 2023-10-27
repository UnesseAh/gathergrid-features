package com.gathergrid.gathergridfeatures.controller;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;

import com.gathergrid.gathergridfeatures.domain.Category;
import com.gathergrid.gathergridfeatures.domain.Event;
import com.gathergrid.gathergridfeatures.service.CategoryService;
import com.gathergrid.gathergridfeatures.service.EventService;
import com.gathergrid.gathergridfeatures.domain.User;
import com.gathergrid.gathergridfeatures.domain.Reservation;
import com.gathergrid.gathergridfeatures.domain.Ticket;
import com.gathergrid.gathergridfeatures.service.ReservationService;
import com.gathergrid.gathergridfeatures.service.TicketService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = {"/myBooking", "/event", "/profile", "/Dashboard","/reservation","/showEvent"})
public class pagesServlet extends HttpServlet {

    TicketService ticketService = new TicketService();
    ReservationService reservationService = new ReservationService();
    EventService eventService = new EventService();
    public void init() {
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServletException {
        String path = request.getServletPath();
        EventService eventService = new EventService();
        CategoryService categoryService = new CategoryService();
//        HttpSession session = request.getSession(false);
        switch (path) {
            case "/myBooking":
                request.setAttribute("url","/myBooking");
                this.getServletContext().getRequestDispatcher("/WEB-INF/booked.jsp").forward(request, response);
                break;
            case "/event":
                request.setAttribute("url","/events");
                List<Category> categories = categoryService.getAllCategories();
                List<Event> events = getFiltredEvents(request, response);
//                List<Event> events = eventService.getAll();
                request.setAttribute("events", events);
                request.setAttribute("categories", categories);
                this.getServletContext().getRequestDispatcher("/WEB-INF/events.jsp").forward(request, response);
                break;
            case "/profile":
                this.getServletContext().getRequestDispatcher("/WEB-INF/profile.jsp").forward(request, response);
                request.setAttribute("url","/profile");
                break;
            case "/Dashboard":
                request.setAttribute("url","/Dashboard");
//                User user = (User) session.getAttribute("user");
                List<Event> listEvent = eventService.fetchAllEventOfUser(1L);
                request.setAttribute("listEvent",listEvent);
                List<Category> listCategory = categoryService.getAllCategories();
                request.setAttribute("listCategory",listCategory);
                this.getServletContext().getRequestDispatcher("/WEB-INF/homeUser.jsp").forward(request, response);
                break;
            case "/reservation":
                try {
                    ValidationReservation(request, response);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/showEvent":
                String Id = request.getParameter("id");
                int idEvent = parseAndValidate(Id);
                Event event = eventService.findById(idEvent);
                request.setAttribute("event",event);
                this.getServletContext().getRequestDispatcher("/WEB-INF/showEvent.jsp").forward(request, response);
                break;
            default:
                response.sendError(response.SC_NOT_FOUND);
                break;
        }
    }

    private List<Event> getFiltredEvents(HttpServletRequest request, HttpServletResponse response) {
        EventService eventService = new EventService();
        String search = request.getParameter("search");
        if (search == null) {
            return eventService.filterEvents("", "", "", "");
        }
        String fromDate = (String) request.getParameter("fromdate");
        String toDate = (String) request.getParameter("todate");
        String text = (String) request.getParameter("text");
        String categoryId = (String) request.getParameter("category");

        return eventService.filterEvents(fromDate, toDate, text, categoryId);
    }

    public static void checkSessionNotEmpty(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session==null){
            request.getServletContext().getRequestDispatcher("signin");
        }
    }
    public void ValidationReservation(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String vipParam = request.getParameter("vip");
        String standardParam = request.getParameter("Standard");
        String id = request.getParameter("idEvent");

        int numVip = parseAndValidate(vipParam);
        int numStandard = parseAndValidate(standardParam);
        int idEvent = parseAndValidate(id);

        List<Ticket> tickets = ticketService.findAllEventTickets(idEvent);

        Ticket ticketVip ;
        Ticket ticketStandard ;
        if (tickets.isEmpty()){
            request.setAttribute("message","This Event Doesn't have any Tickets !!");
            this.getServletContext().getRequestDispatcher("/event").forward(request,response);
        }
        if(tickets.get(0).getType().name().equals("VIP")){
            ticketVip = tickets.get(0);
            ticketStandard = tickets.get(1);
        }
        else {
            ticketVip = tickets.get(1);
            ticketStandard = tickets.get(0);
        }
        checkAvailablityOfTypeTicket(ticketVip, ticketStandard, numStandard, numVip, request, response);

        checkTicketTypeAndSave(numStandard, numVip, ticketVip, ticketStandard , request, response);


    }

    private void checkAvailablityOfTypeTicket(Ticket Vip, Ticket Standard,int numStandard,int numVip, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Vip.getType().name().isEmpty()&&numVip>0){
            request.setAttribute("message","This Event Has Not this Vip Type !!");
            this.getServletContext().getRequestDispatcher("/event").forward(request,response);
        }
        if(Standard.getType().name().isEmpty()&&numStandard>0){
            request.setAttribute("message","This Event Has Not this Standard Type !!");
            this.getServletContext().getRequestDispatcher("/event").forward(request,response);
        }
    }

    private void checkTicketTypeAndSave(int numStandard,int numVip,Ticket Vip, Ticket Standard , HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        //check date
        if(!Vip.getEvent().getDate().isAfter(LocalDateTime.now())|| !Standard.getEvent().getDate().isAfter(LocalDateTime.now())){
            request.setAttribute("message","This Event is Expired !!");
            this.getServletContext().getRequestDispatcher("/event").forward(request,response);
        }else if(Standard.getQuantityAvailable()<numStandard ){
            request.setAttribute("message","Tickets is not Available !!");
            this.getServletContext().getRequestDispatcher("/event").forward(request,response);
        }
        else if(Vip.getQuantityAvailable()<numVip ){
            request.setAttribute("message","Tickets is not Available !!");
            this.getServletContext().getRequestDispatcher("/event").forward(request,response);
        }
        else {
            Reservation reservation = new Reservation(LocalDateTime.now(),user,Vip);
            Reservation reservation2 = new Reservation(LocalDateTime.now(),user,Standard);
            Vip.setQuantityAvailable(Vip.getQuantityAvailable()-numVip);
            Standard.setQuantityAvailable(Standard.getQuantityAvailable()-numStandard);
            reservationService.save(reservation);
            ticketService.updateTicket(Vip);
            reservationService.save(reservation2);
            ticketService.updateTicket(Standard);
            request.setAttribute("message", "Reservation has been Successfully !!");

        }

        this.getServletContext().getRequestDispatcher("/event").forward(request,response);
    }
    private int parseAndValidate(String param) {
        if (param == null || param.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(param);
    }
}