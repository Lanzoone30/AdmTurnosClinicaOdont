<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="components/header.jsp"%>
<%@include file="components/body1.jsp" %>
<%@page import="java.util.List" %>
<%@page import= "logica.Odontologo"%>
<%@page import= "logica.Paciente"%>
        <h1>Alta Turnos</h1>
        <p>Este es el apartado para dar los turnos del sistema.</p>
        
        <form class="user" action="SvTurnos" method="POST">
                <div class="form-group row">
                        <div class="col-sm-6 mb-3">
                              <input type="text" class="form-control form-control-user" id="afeccion" name="afeccion"
                                    placeholder="Afeccion">
                        </div>
                        <div class="col-sm-6 mb-3">
                                <input type="date" class="form-control form-control-user" id="fecha" name="fecha"
                                      placeholder="Fecha">
                          </div>
                        <div class="col-sm-6 mb-3">
                                <input type="text" class="form-control form-control-user" id="hora" name="hora"
                                      placeholder="Hora">
                          </div>
                    
                        <div class="col-sm-6 mb-3">
                            <label for="odontologo">Seleccione un odontólogo:</label>
                            <select class="form-control" id="odontologo" name="odontologo">
                                <option value="">Seleccionar odontólogo</option>
                                <% for (Odontologo odontologo : (List<Odontologo>)session.getAttribute("listaOdo")) { %>
                                    <option value="<%=odontologo.getId()%>"><%=odontologo.getNombre()%></option>
                                <% } %>
                            </select>
                        </div>
                        <div class="col-sm-6 mb-3">
                            <label for="paciente">Seleccione un paciente:</label>
                            <select class="form-control" id="paciente" name="paciente">
                                <option value="">Seleccionar paciente</option>
                                <% for (Paciente paciente : (List<Paciente>)session.getAttribute("listaPacientes")) { %>
                                    <option value="<%=paciente.getId()%>"><%=paciente.getNombre()%></option>
                                <% } %>
                            </select>
                        </div>

                    
                </div>
                <button href="SvTurnos" class="btn btn-primary btn-user btn-block">
                    Crear Turno
                </button>
                <hr>
            </form>
<%@include file="components/body2.jsp" %>
