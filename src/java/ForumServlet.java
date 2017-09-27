/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static com.sun.org.apache.xerces.internal.util.FeatureState.is;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author User
 */
@WebServlet(urlPatterns = {"/forum/*"})
public class ForumServlet extends HttpServlet {
    
    private ArrayList<Mensagem> mensagens;
    
    public ForumServlet()
    {
        mensagens = new ArrayList<>();
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
         
        if( request.getSession().getAttribute("usuario") == null ||
            request.getSession().getAttribute("senha") == null)
        {
            return;
        }
        
        ServletContext ctx = getServletContext();
        int acessos = 0;
        try
        {
            acessos = (int)ctx.getAttribute("acessos");    
        }
        catch(Exception e)
        {
            acessos = 1;
        }
        ctx.setAttribute("acessos",acessos+1 );
        
        String mensagem = request.getParameter("mensagem");
        String email = request.getParameter("email");
        
        if( mensagem != null || email != null)
        {
            Mensagem msg = new Mensagem(mensagem,email);
            RepositorioMensagens.salvaMensagens(ctx, msg);
            //mensagens.add(msg);    
        }
        
        mensagens = RepositorioMensagens.recuperaMensagens(ctx);
            
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<style>" +
                ".tableForum {\n" +
                "border-collapse: collapse;\n" +
                "width: 100%;\n" +
                "}th, td {" +
                "border-bottom: 1px solid #ddd;padding: 15px;" +
                "}" +
                "</style>");
            out.println("<title>Mini Forum</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<center><h1><font face=\"verdana\">MiniForum | ACESSOS: "+acessos+"</h1>");
            out.println("<form action=\"\\miniforum\\LogoutServlet\" method=\"POST\">");
            out.println("<input type=\"submit\" value=\"LOGOUT\">");
            out.println("</form>");
            out.println("<br><br><table class=\"tableForum\">");
            out.println("<tr>");
            out.println("<td><b>E-MAIL</b></td>");
            out.println("<td><b>MENSAGEM</b></td>");
            out.println("</tr>");
            for(Mensagem m : mensagens)
            {
                out.println("<tr>");
                out.println("<td>" + m.getRemetente() + "</td>");
                out.println("<td>\""+ m.getMensagem() + "\"</td>");
                out.println("</tr>");
            }
            out.println("</table>");
            out.println("<h2>Responder:</h2>");
            out.println("<form action=\"gravar\" method=\"POST\">\n" +
            "            <table>\n" +
            "                <tr>\n" +
            "                    <td>Mensagem:</td>\n" +
            "                    <td><input type=\"text\" name=\"mensagem\"></td>\n" +
            "                </tr>\n" +
            "                <tr>\n" +
            "                    <td>Email:</td>\n" +
            "                    <td><input type=\"text\" name=\"email\"></td>\n" +
            "                </tr>\n" +
            "                <tr>\n" +
            "                    <td><input type=\"submit\"></td>\n" +
            "                <td></td></tr>\n" +
            "            </table>\n" +
            "        </form>");
            
            out.println("</center></font></body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
