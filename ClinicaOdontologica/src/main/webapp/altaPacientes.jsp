<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="components/header.jsp"%>
<%@include file="components/body1.jsp" %>
        <h1>Alta Pacientes</h1>
        <p>Este es el apartado para dar de alta los diferentes pacientes.</p>

        <form class="user" action="SvPacientes" method="post">
                <div class="form-group row">
                         <div class="col-sm-6 mb-3">
                            <input type="text" class="form-control form-control-user" id="dni" name="dni"
                                    placeholder="Dni">
                        </div>
                        <div class="col-sm-6 mb-3">
                                <input type="text" class="form-control form-control-user" id="nombre" name="nombre"
                                      placeholder="Nombre">
                          </div>
                        <div class="col-sm-6 mb-3">
                                <input type="text" class="form-control form-control-user" id="apellido" name="apellido"
                                      placeholder="Apellido">
                          </div>
                          <div class="col-sm-6 mb-3">
                              <input type="text" class="form-control form-control-user" id="telefono" name="telefono"
                                      placeholder="Telefono">
                          </div>
                          
                         
                </div>
                <!--Proximamente: todo lo que respecta a horarios y usuarios-->
                <button href="SvPacientes" class="btn btn-primary btn-user btn-block" type="submit">
                    Crear Paciente
                </button>
                <hr>
               
            </form>
<%@include file="components/body2.jsp" %>
