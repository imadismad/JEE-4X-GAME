package controlers.test;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.SimulationPartie;

import java.io.IOException;

public class SimulationGrille extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Appeler la méthode de simulation
        SimulationPartie.simulerPartie(request.getSession());

        // Rediriger vers l'affichage de la grille
        response.sendRedirect(request.getContextPath() + "/partie/afficherGrille");
    }
}
