package com.deoxys.dev.dstr.domain.service;

import com.deoxys.dev.dstr.domain.converter.CustomerReader;
import com.deoxys.dev.dstr.domain.model.Customer;
import com.deoxys.dev.dstr.persistence.dao.CustomerDAO;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

/**
 * Created by deoxys on 07.07.16.
 */

public class CustomerService extends PostgresService<Customer> {
    Logger logger = Logger.getLogger(CustomerService.class);

    private CustomerDAO dao;

    public CustomerService() {
        super(new CustomerReader());
        dao = new CustomerDAO(dataSource);
    }

    public void loadCustomer(HttpServletRequest req) {
    }

    public void loadCustomers(HttpServletRequest req) {
    }

    public void login(HttpServletRequest req) {
        Customer customer = requestReader.read(req);
        Customer shouldBe = null;
        try {
            shouldBe = dao.get(customer.getEmail());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        String error = null;
        if (shouldBe == null)
            error = "Email doesn't match";
        else if ( ! customer.validPassword(shouldBe.getPassword()))
            error = "Password doesn't match";
        else if ( ! shouldBe.isEnabled())
            error = "Account was closed";
        if (error != null) req.setAttribute("error", error);
        else req.getSession().setAttribute("customer", shouldBe);
    }

    public void register(HttpServletRequest req) {
    }

    public void logout(HttpServletRequest req) {
        HttpSession ses = req.getSession(false);
        if (ses != null) ses.invalidate();
        else req.setAttribute("error", "Session not found");
    }

    public void swapCustomerStatus(HttpServletRequest req) {
    }
}
