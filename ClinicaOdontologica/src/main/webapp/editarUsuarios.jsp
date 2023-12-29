<%@page import="logica.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="components/header.jsp"%>
<%@include file="components/body1.jsp" %>
        <h1>Edicion de Usuarios</h1>
        <p>Este es el apartado para modificar un usuario del sistema.</p>
        
        <% Usuario usu = (Usuario) request.getSession().getAttribute("usuEditar"); %>

        <form class="user" action="SvUsuarios" method="post">
                <div class="form-group row">
                        <div class="col-sm-6 mb-3">
                              <input type="text" class="form-control form-control-user" id="nombreusu" name= "nombreusu"
                                     placeholder="Nombre Usuario" value="<%=usu.getNombreUsuario() %>">
                        </div>
                        <div class="col-sm-6 mb-3">
                                <input class="form-control form-control-user" id="contrasenia" name="contrasenia"
                                      placeholder="Contrasenia" type="password" value="<%=usu.getContrasenia()%>">
                          </div>
                        <div class="col-sm-6 mb-3">
                                <input type="text" class="form-control form-control-user" id="rol" name="rol"
                                       placeholder="Rol" value="<%=usu.getRol()%>">
                          </div>
                         
                </div>
                <!--Proximamente: todo lo que respecta a horarios y usuarios-->
                <button href="SvUsuarios" class="btn btn-primary btn-user btn-block" type="submit">
                    Guardar Modificacion
                </button>
                <hr>
               
            </form>
<%@include file="components/body2.jsp" %>
