package com.deoxys.dev.dstr.presentation.servlet.controller.admin.item;

import com.deoxys.dev.dstr.persistence.dao.MongoItemDao;
import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.domain.model.ItemStatus;
import com.mongodb.MongoClient;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deoxys on 28.05.16.
 */

@WebServlet(name = "ItemEdit", urlPatterns = "/item/edit")
public class ItemEditServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(ItemAddServlet.class);

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String itemId = req.getParameter("id");
        if (itemId == null || itemId.equals("")) {
            throw new ServletException("Wrong item id");
        }

        String category = req.getParameter("category");
        String priceStr = req.getParameter("price");
        String currency = req.getParameter("currency");
        String stockedStr = req.getParameter("status.stocked");
        String reservedStr = req.getParameter("status.reserved");
        String soldStr = req.getParameter("status.sold");

        Item item = new Item();
        item.setId(itemId);
        item.setCategory(category);

        List<String> errors = new ArrayList<>();

        if (category == null || category.equals(""))
            errors.add("Введіть категорію");

        double price = -1.0;

        if (priceStr == null || priceStr.equals("")) {
            errors.add("Введіть ціну");
        } else if ((price = Double.parseDouble(priceStr)) <= 0.0) {
            errors.add("Ціну введено невірно");
        } else item.setPrice(price);

        if (currency == null || currency.equals("")) {
            errors.add("Виберіть валюту");
        } else item.setCurrency(currency);

        int stocked = -1, reserved = -1, sold = -1;

        if (stockedStr == null || stockedStr.equals("")) {
            stocked = 0;
        } else if ((stocked = Integer.parseInt(stockedStr)) < 0) {
            errors.add("Поле 'залишилось' введено невірно");
        }

        if (reservedStr == null || reservedStr.equals("")) {
            reserved = 0;
        } else if ((reserved = Integer.parseInt(reservedStr)) < 0) {
            errors.add("Поле 'зарезервовано' введено невірно");
        }

        if (soldStr == null || soldStr.equals("")) {
            sold = 0;
        } else if ((sold = Integer.parseInt(soldStr)) < 0) {
            errors.add("Поле 'продано' введено невірно");
        }
        item.setStatus(new ItemStatus(stocked, reserved, sold));

        if (errors.isEmpty()) {
            MongoClient mongo = (MongoClient) req.getServletContext()
                    .getAttribute("MONGO_CLIENT");
            MongoItemDao itemDAO = new MongoItemDao(mongo);

            if (itemDAO.updateItem(item)) {
                logger.info("Item=" + item + " was successfully updated");
            } else {
                logger.error("Item=" + item + " was not updated");
            }
            resp.sendRedirect(req.getContextPath() + "/items");
        } else {
            req.setAttribute("error", errors.get(0));
            req.setAttribute("item", item);
            req.getRequestDispatcher("/WEB-INF/jsp/itemedit.jsp").forward(req, resp);
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String itemId = req.getParameter("id");
        if (itemId == null || itemId.equals("")) {
            throw new ServletException("Wrong item id");
        }
        MongoClient mongo = (MongoClient) req.getServletContext().getAttribute("MONGO_CLIENT");
        MongoItemDao itemDAO = new MongoItemDao(mongo);
        Item item = itemDAO.findItem(new ObjectId(itemId));
        req.setAttribute("item", item);
        req.getRequestDispatcher("/WEB-INF/jsp/itemedit.jsp").forward(req, resp);
    }
}
