package controller;

import com.mongodb.MongoClient;
import dao.MongoUserDAO;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by deoxys on 17.05.16.
 */
@WebServlet("/addUser")
public class AddUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String psswd = request.getParameter("password");
        if (name == null || name.equals(""))
            request.setAttribute("error", "Ім'я введено невірно");
        else if (email == null || !email.matches("\\w+@\\w+\\.\\w+"))
            request.setAttribute("error", "Електронну пошту введено невірно");
        else if (psswd == null || psswd.length() < 8)
            request.setAttribute("error", "Занадто короткий пароль");
        else {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(psswd);
            MongoClient mongo = (MongoClient) request.getServletContext()
                    .getAttribute("MONGO_CLIENT");
            MongoUserDAO userDAO = new MongoUserDAO(mongo);
            userDAO.createUser(user);
            System.out.println("Додано нового користувача з id=" + user.getId());
            request.setAttribute("success", "Додано нового користувача");
            List<User> users = userDAO.findAllUsers();
            request.setAttribute("users", users);
        }
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }
}