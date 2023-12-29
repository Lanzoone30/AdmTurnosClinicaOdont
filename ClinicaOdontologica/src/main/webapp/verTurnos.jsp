<%@page import="logica.Turno"%>
<%@page import="logica.Odontologo"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="components/header.jsp"%>
<%@include file="components/body1.jsp" %>

 <!-- Begin Page Content -->
                <div class="container-fluid">

                    <!-- Page Heading -->
                    <h1 class="h3 mb-2 text-gray-800">Ver Turnos</h1>
                    <p class="mb-4">A continuacion podra visualizar la lista completa de turnos</p>

                    <!-- DataTales Example -->
                    <div class="card shadow mb-4">
                        <div class="card-header py-3">
                            <h6 class="m-0 font-weight-bold text-primary">Turnos</h6>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th>Afeccion</th>
                                            <th>Fecha</th>
                                            <th>Hora</th>
                                            <th>Doctor</th>
                                            <th>Paciente</th>
                                            <th style="width: 210px">Accion</th>
                                        </tr>
                                    </thead>
                                    <tfoot>
                                        <tr>
                                            <th>Afeccion</th>
                                            <th>Fecha</th>
                                            <th>Hora</th>
                                            <th>Doctor</th>
                                            <th>Paciente</th>
                                            <th style="width: 210px">Accion</th>
                                        </tr>
                                    </tfoot>
                                    
                                    <%
                                        List<Turno> listaTurno = (List)request.getSession().getAttribute("listaTurno"); 
                                    %>
                                    
                                    <tbody>
                                    <%
                                       for (Turno tur : listaTurno){
                                    %>
                                        <tr>
                                            <td><%=tur.getAfeccion()%></td>
                                            <td><%=tur.getFecha_turno()%></td>
                                            <td><%=tur.getHora_turno()%></td>
                                            <td><%=tur.getOdonto()%></td>
                                            <td><%=tur.getPacien()%></td>
                                            
                                            <td style="display: flex; width: 230px">
                                                <form name="eliminar" action="SvEliminarTurno" method="POST"> <!--es para mandar el cod al servlet -->
                                                    <button type="submit" class="btn btn-primary btn-user btn-block" style="background-color: red; margin-right: 5px;">
                                                        <i class="fas fa-trash-alt"></i> Eliminar
                                                    </button>
                                                    <input type="hidden" name="id" value="<%=tur.getId_turno()%>">
                                                </form>
                                                
                                            </td>
                                        </tr>
                                    <%
                                        }
                                    %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                </div>
                <!-- /.container-fluid -->

            </div>
            <!-- End of Main Content -->
            
            <!-- Page level plugins -->
    <script src="vendor/datatables/jquery.dataTables.min.js"></script>
    <script src="vendor/datatables/dataTables.bootstrap4.min.js"></script>

    <!-- Page level custom scripts -->
    <script src="js/demo/datatables-demo.js"></script>

<%@include file="components/body2.jsp" %>