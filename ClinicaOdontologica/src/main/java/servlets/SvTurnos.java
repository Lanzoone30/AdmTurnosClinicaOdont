package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import logica.Controladora;
import logica.Odontologo;
import logica.Paciente;
import logica.Turno;

@WebServlet(name = "SvTurnos", urlPatterns = {"/SvTurnos"})
public class SvTurnos extends HttpServlet {

    Controladora control = new Controladora();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
            HttpSession session = request.getSession();
            
            List<Odontologo> listaOdo = new ArrayList<Odontologo>();
            listaOdo = control.getOdontologos();
            session.setAttribute("listaOdo", listaOdo);
            
            List<Paciente> listaPacientes = new ArrayList<Paciente>();
            listaPacientes = control.getPaciente();
            session.setAttribute("listaPacientes", listaPacientes);
            
            request.getRequestDispatcher("altaTurnos.jsp").forward(request, response);
            //response.sendRedirect("verTurnos.jsp");
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String afeccion = request.getParameter("afeccion");
            String hora = request.getParameter("hora");
        
        
       SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
       Date fecha = null;
       
       try {
           fecha = formatoFecha.parse(request.getParameter("fecha"));
       } catch(ParseException e) {
           System.out.println("fecha invalida");
           e.printStackTrace();
           response.sendRedirect("index.jsp");
           return;
       }
       int idOdontologo = Integer.parseInt(request.getParameter("odontologo"));
       int idPaciente = Integer.parseInt(request.getParameter("paciente"));
       
       control.crearTurno(afeccion, hora, fecha, idOdontologo, idPaciente);
       
       response.sendRedirect("index.jsp");
       
       } catch(Exception e) {
            System.out.println("Algo salio mal en el post");
            e.printStackTrace();
            response.sendRedirect("error.jsp");
       }
    
    
    }

   
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
