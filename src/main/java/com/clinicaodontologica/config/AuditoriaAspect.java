package com.clinicaodontologica.config;

import com.clinicaodontologica.model.TipoAccion;
import com.clinicaodontologica.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Aspecto que registra en auditoria las operaciones de escritura de los
 * servicios. Intercepta los metodos {@code crear}, {@code actualizar} y
 * {@code eliminar} de cada servicio y registra la accion con el usuario
 * autenticado.
 */
@Aspect
@Component
@RequiredArgsConstructor
public class AuditoriaAspect {

    private final AuditoriaService auditoriaService;

    /**
     * Registra la creacion de una entidad tras la ejecucion exitosa del
     * metodo.
     *
     * @param joinPoint punto de ejecucion interceptado
     * @param resultado entidad creada
     */
    @AfterReturning(pointcut = "execution(* com.clinicaodontologica.service.*Service.crear(..))", returning = "resultado")
    public void registrarCreacion(JoinPoint joinPoint, Object resultado) {
        auditoriaService.registrar(nombreEntidad(joinPoint), TipoAccion.CREAR,
                resumen(resultado, "Creado"));
    }

    /**
     * Registra la modificacion de una entidad tras la ejecucion exitosa del
     * metodo.
     *
     * @param joinPoint punto de ejecucion interceptado
     * @param resultado entidad modificada
     */
    @AfterReturning(pointcut = "execution(* com.clinicaodontologica.service.*Service.actualizar(..))", returning = "resultado")
    public void registrarModificacion(JoinPoint joinPoint, Object resultado) {
        auditoriaService.registrar(nombreEntidad(joinPoint), TipoAccion.MODIFICAR,
                resumen(resultado, "Modificado"));
    }

    /**
     * Registra la eliminacion de una entidad tras la ejecucion exitosa del
     * metodo.
     *
     * @param joinPoint punto de ejecucion interceptado
     */
    @AfterReturning(pointcut = "execution(* com.clinicaodontologica.service.*Service.eliminar(..))")
    public void registrarEliminacion(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String id = args.length > 0 ? String.valueOf(args[0]) : "?";
        auditoriaService.registrar(nombreEntidad(joinPoint), TipoAccion.ELIMINAR,
                "Eliminado id=" + id);
    }

    /** Audits turno state changes and clinical-note updates. */
    @AfterReturning(pointcut = "execution(* com.clinicaodontologica.service.*Service.cambiarEstado(..))"
            + " || execution(* com.clinicaodontologica.service.*Service.registrarNota(..))",
            returning = "resultado")
    public void registrarCambioClinico(JoinPoint joinPoint, Object resultado) {
        auditoriaService.registrar(nombreEntidad(joinPoint), TipoAccion.MODIFICAR,
                resumen(resultado, joinPoint.getSignature().getName()));
    }

    /**
     * Deriva el nombre de la entidad del nombre de la clase del servicio
     * (ej: "TurnoService" -&gt; "Turno").
     *
     * @param joinPoint punto de ejecucion
     * @return nombre de la entidad
     */
    private String nombreEntidad(JoinPoint joinPoint) {
        String clase = joinPoint.getTarget().getClass().getSimpleName();
        return clase.replace("Service", "");
    }

    /**
     * Construye un resumen legible del cambio.
     *
     * @param resultado entidad afectada
     * @param accion texto de la accion
     * @return resumen del cambio
     */
    private String resumen(Object resultado, String accion) {
        if (resultado == null) {
            return accion;
        }
        return accion + " " + resultado.getClass().getSimpleName()
                + " [" + resultado.toString() + "]";
    }
}
