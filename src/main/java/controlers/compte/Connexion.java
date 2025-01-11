package controlers.compte;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Utilisateur;
import model.storage.StockageInterface;
import model.storage.exception.StockageAccesException;
import model.storage.exception.StockageStructureException;
import model.storage.exception.StockageValeurException;
import model.storage.structure.StructureInterface;
import model.storage.structure.TableEnum;
import model.storage.structure.UtilisateurStructure;

/**
 * Servlet permettant à un utilisateur de se connecter
 * A la fin le compte doit être créé et l'utilisateur redirigé vers une autre page.
 * Si jamais une erreur survient, un message d'erreur doit être affiché sur la JSP avec le formulaire.
 * Dans le cas d'une requête GET, on redirige directement vers la page JSP.
 * 
 * @author Théo KALIFA
 */
public class Connexion extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        afficherJSP("", request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nomUtilisateur = request.getParameter("nomUtilisateur");
        String motDePasse = request.getParameter("motDePasse");

        // Vérification de l'existence des champs
        if (nomUtilisateur == null || motDePasse == null) {
            afficherJSP("Le nom d'utilisateur et le mot de passe sont requis.", request, response);
            return;
        }

        nomUtilisateur = nomUtilisateur.trim();
        motDePasse = motDePasse.trim();

        if (nomUtilisateur.isEmpty() || motDePasse.isEmpty()) {
            afficherJSP("Le nom d'utilisateur et le mot de passe sont requis.", request, response);
            return;
        }

        // Connexion au compte
        Map<StructureInterface, Object> config = new HashMap<>();
        config.put(UtilisateurStructure.NOM_UTILISATEUR, nomUtilisateur);
        config.put(UtilisateurStructure.MOT_DE_PASSE, motDePasse);

        try {
            Map<StructureInterface, Object> donnee = StockageInterface.getInstance().getPremiereEntrees(TableEnum.UTILISATEUR, config);
            if (donnee == null) {
                afficherJSP("Le nom d'utilisateur ou le mot de passe est incorrect.", request, response);
                return;
            }

            request.getSession().setAttribute(Utilisateur.CLEF_UTILISATEUR_SESSION, new Utilisateur((String) donnee.get(UtilisateurStructure.NOM_UTILISATEUR), null));
        } catch (StockageAccesException | StockageValeurException | StockageStructureException e1) {
            throw new ServletException("Erreur lors de l'accès au système de stockage", e1);
        }

        // Redirection vers la page d'entrée de code
        response.sendRedirect(getServletContext().getContextPath() + "/lobby/enterCode");
    }

    private void afficherJSP(String erreur, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("messageErreur", erreur);
        request.getRequestDispatcher("/WEB-INF/jsp/compte/connexion.jsp").forward(request, response);
    }
}
