<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="components/header.jsp"%>
<%@include file="components/body1.jsp" %>
        <h1>Alta Usuarios</h1>
        <p>Este es el apartado para dar de alta los diferentes usuarios del sistema.</p>

        <form class="user" action="SvUsuarios" method="post">
                <div class="form-group row">
                        <div class="col-sm-6 mb-3">
                              <input type="text" class="form-control form-control-user" id="nombreusu" name= "nombreusu"
                                    placeholder="Nombre Usuario">
                        </div>
                        <div class="col-sm-6 mb-3">
                                <input class="form-control form-control-user" id="contrasenia" name="contrasenia"
                                      placeholder="Contrasenia" type="password">
                          </div>
                        <div class="col-sm-6 mb-3">
                                <input type="text" class="form-control form-control-user" id="rol" name="rol"
                                      placeholder="Rol">
                          </div>
                         
                </div>
                <!--Proximamente: todo lo que respecta a horarios y usuarios-->
                <button href="SvUsuarios" class="btn btn-primary btn-user btn-block" type="submit">
                    Crear Usuario
                </button>
                <hr>
               
            </form>
<%@include file="components/body2.jsp" %>
