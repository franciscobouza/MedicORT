package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import daos.DaoCentroMedico;
import daos.DaoConsultaMedica;
import daos.DaoProfesional;
import daos.DaoUsuario;
import daos.exceptions.ErrorDeConexionException;
import daos.exceptions.NoExisteException;
import daos.exceptions.YaExisteException;
import entities.CentroMedico;
import entities.ConsultaMedica;
import entities.Profesional;
import entities.Usuario;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {
  
    public static Result index() {
        return ok(index.render());
    }
  
    public static Result login() {
    	try {
			JSONObject uno = new JSONObject();
    		DynamicForm form = Form.form().bindFromRequest();
    		String email = form.get("email"), password = form.get("password");
    		if(isStringInvalid(email) || isStringInvalid(password))
    		{
				uno.put("mensaje", "Los parámetros 'email' y 'password' deben ser enviados.");
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
    		}
			try {
				DaoUsuario daoUsuario = new DaoUsuario();
				int idUsu = daoUsuario.attemptToLogin(email, password);
				Usuario usu = null;
				if(idUsu != -1)
				{
					usu = daoUsuario.getById(idUsu);
					uno.put("nombre", usu.nombre);
					uno.put("email", usu.email);
				}
				uno.put("resultado_login", idUsu!=-1);
				uno.put("id_usuario", idUsu);
				response().setHeader("Access-Control-Allow-Origin", "*");
				if(idUsu!=-1) {
					uno.put("retorno", "OK");
					return ok(uno.toString());
				}else {
					uno.put("retorno", "ERROR");
					return internalServerError(uno.toString());
				}
			} catch (ErrorDeConexionException e) {
				e.printStackTrace();
				uno.put("mensaje", e.getMessage());
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			} catch (NoExisteException e) {
				e.printStackTrace();
				uno.put("mensaje", e.getMessage());
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			response().setHeader("Access-Control-Allow-Origin", "*");
    		try {
				JSONObject uno = new JSONObject();
				uno.put("mensaje", "Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
				uno.put("retorno", "ERROR");
				return internalServerError(uno.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
				return internalServerError("Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
			}
		}
    }
  
    public static Result registrar() {
    	try {
    		JSONObject uno = new JSONObject();

    		DynamicForm form = Form.form().bindFromRequest();
    		String email = form.get("email"), password = form.get("password"), nombre = form.get("nombre"), apellido = form.get("apellido"), documento = form.get("documento"), telefono = form.get("telefono");
    		if(isStringInvalid(email) || isStringInvalid(password) || isStringInvalid(nombre) || isStringInvalid(apellido) || isStringInvalid(documento) || isStringInvalid(telefono))
    		{
				uno.put("mensaje", "Los parámetros 'email', 'password', 'nombre', 'apellido', 'telefono' y 'documento' deben ser enviados.");
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
    		}
    		
			try {
				int idUsuario = new DaoUsuario().addUsuario(nombre, apellido, email, password, documento, telefono);
				uno.put("retorno", "OK");
				uno.put("idUsuario", idUsuario);
				response().setHeader("Access-Control-Allow-Origin", "*");
				return ok(uno.toString());

			} catch (ErrorDeConexionException | YaExisteException e) {
				e.printStackTrace();
				uno.put("mensaje", e.getMessage());
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			response().setHeader("Access-Control-Allow-Origin", "*");
    		try {
				JSONObject uno = new JSONObject();
				uno.put("mensaje", "Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
				uno.put("retorno", "ERROR");
				return internalServerError(uno.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
				return internalServerError("Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
			}
		}
    }
    
    private static JSONObject armarCentroMedico(CentroMedico c) throws JSONException {
		JSONObject uno = new JSONObject(c);
//		uno.put("nombre", c.nombre);
//		uno.put("direccion", c.direccion);
//		JSONObject dos = new JSONObject();
//		dos.put("latitud", c.localizacion.latitud);
//		dos.put("longitud", c.localizacion.longitud);
//		uno.put("localizacion", dos);
		return uno;
    }
    
    private static JSONObject armarConsultaMedica(ConsultaMedica c) throws JSONException {
		JSONObject uno = new JSONObject(c);
//		uno.put("nombre", c.nombre);
//		uno.put("direccion", c.direccion);
//		JSONObject dos = new JSONObject();
//		dos.put("latitud", c.localizacion.latitud);
//		dos.put("longitud", c.localizacion.longitud);
//		uno.put("localizacion", dos);
		return uno;
    }
    
    private static JSONObject armarProfesional(Profesional p, boolean completo) throws JSONException {
		JSONObject uno = new JSONObject();
		uno.put("id", p.getId());
		uno.put("nombre", p.nombre);
		uno.put("apellido", p.apellido);
		uno.put("especialidad", p.especialidad);
		uno.put("foto", p.foto);
		if(completo) {
			uno.put("titulo", p.titulo);
			uno.put("puntuacion", p.puntuacion);
			uno.put("centroMedico", armarCentroMedico(p.centroMedico));			
		}
		return uno;
    }
    
    private static JSONArray armarListaProfesionales(List<Profesional> profs, boolean completo) throws JSONException {
		JSONArray uno = new JSONArray();
		for(Profesional prof : profs){
			uno.put(armarProfesional(prof, completo));
		}
		return uno;
    }
    
    private static JSONArray armarListaString(List<String> lista) throws JSONException {
		JSONArray uno = new JSONArray();
		for(String str : lista){
			uno.put(str);
		}
		return uno;
    }
    
    private static JSONArray armarListaCentrosMedicos(List<CentroMedico> centros) throws JSONException {
		JSONArray uno = new JSONArray();
		for(CentroMedico centro : centros){
			uno.put(armarCentroMedico(centro));
		}
		return uno;
    }
    
    private static JSONArray armarListaConsultasMedicas(List<ConsultaMedica> consultas) throws JSONException {
		JSONArray uno = new JSONArray();
		for(ConsultaMedica consulta: consultas){
			uno.put(armarConsultaMedica(consulta));
		}
		return uno;
    }
    
    public static Result getProfesionales()  {
    	try {
			JSONObject uno = new JSONObject();
			try {
				List<Profesional> profs = new DaoProfesional().getAll();
				uno.put("retorno", "OK");
				uno.put("profesionales", armarListaProfesionales(profs, false));
				response().setHeader("Access-Control-Allow-Origin", "*");
				return ok(uno.toString());
			} catch (ErrorDeConexionException | NoExisteException e) {
				e.printStackTrace();
				uno.put("mensaje", e.getMessage());
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			response().setHeader("Access-Control-Allow-Origin", "*");
    		try {
				JSONObject uno = new JSONObject();
				uno.put("mensaje", "Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
				uno.put("retorno", "ERROR");
				return internalServerError(uno.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
				return internalServerError("Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
			}
		}
    }
    
    public static Result getEspecialidades()  {
    	try {
			JSONObject uno = new JSONObject();
			try {
				List<String> especs = new DaoProfesional().getAllEspecialidades();
				uno.put("retorno", "OK");
				uno.put("especialidades", armarListaString(especs));
				response().setHeader("Access-Control-Allow-Origin", "*");
				return ok(uno.toString());
			} catch (ErrorDeConexionException e) {
				e.printStackTrace();
				uno.put("mensaje", e.getMessage());
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			response().setHeader("Access-Control-Allow-Origin", "*");
    		try {
				JSONObject uno = new JSONObject();
				uno.put("mensaje", "Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
				uno.put("retorno", "ERROR");
				return internalServerError(uno.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
				return internalServerError("Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
			}
		}
    }
    
    public static Result getProfesionalesPorEspecialidad()  {
    	try {
			JSONObject uno = new JSONObject();
			DynamicForm form = Form.form().bindFromRequest();
			String especialidad = form.get("especialidad");
			if(isStringInvalid(especialidad))
    		{
				uno.put("mensaje", "El parámetro 'especialidad' debe ser enviado.");
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
    		}
			try {
				List<Profesional> profs = new DaoProfesional().getAllByEspecialidad(especialidad);
				uno.put("retorno", "OK");
				uno.put("profesionales", armarListaProfesionales(profs, false));
				response().setHeader("Access-Control-Allow-Origin", "*");
				return ok(uno.toString());
			} catch (ErrorDeConexionException | NoExisteException e) {
				e.printStackTrace();
				uno.put("mensaje", e.getMessage());
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			response().setHeader("Access-Control-Allow-Origin", "*");
    		try {
				JSONObject uno = new JSONObject();
				uno.put("mensaje", "Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
				uno.put("retorno", "ERROR");
				return internalServerError(uno.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
				return internalServerError("Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
			}
		}
    }
    
    public static Result getDetalleProfesional()  {
    	try {
			JSONObject uno = new JSONObject();
			DynamicForm form = Form.form().bindFromRequest();
    		String nombre = form.get("nombre"), apellido = form.get("apellido"), strId = form.get("id");
    		boolean listo = false;
    		int id = -1;
    		if(!isStringInvalid(strId))
    			listo = true;
			if(!listo && (isStringInvalid(nombre) || isStringInvalid(apellido)))
    		{
				uno.put("mensaje", "Los parámetros 'nombre' y 'apellido' deben ser enviados.");
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
    		}
			try {
				Profesional prof = null;
				if(listo) {
    				id = Integer.parseInt(strId);
    				prof = new DaoProfesional().getById(id);
				}else
					prof = new DaoProfesional().getByNombreApellido(nombre, apellido);
				uno.put("retorno", "OK");
				uno.put("profesional", armarProfesional(prof, true));
				response().setHeader("Access-Control-Allow-Origin", "*");
				return ok(uno.toString());
			} catch (ErrorDeConexionException e) {
				e.printStackTrace();
				uno.put("mensaje", e.getMessage());
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
	    	} catch (NoExisteException e) {
				e.printStackTrace();
				uno.put("mensaje", e.getMessage());
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
	    	} catch (NumberFormatException e) {
				e.printStackTrace();
				uno.put("mensaje", "El parámetro 'id' debe ser numérico");
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			response().setHeader("Access-Control-Allow-Origin", "*");
    		try {
				JSONObject uno = new JSONObject();
				uno.put("mensaje", "Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
				uno.put("retorno", "ERROR");
				return internalServerError(uno.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
				return internalServerError("Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
			}
		}
    }
    
    public static Result getCentrosMedicos()  {
    	try {
			JSONObject uno = new JSONObject();
			try {
				List<CentroMedico> centros = new DaoCentroMedico().getAll();
				uno.put("retorno", "OK");
				uno.put("centrosMedicos", armarListaCentrosMedicos(centros));
				response().setHeader("Access-Control-Allow-Origin", "*");
				return ok(uno.toString());
			} catch (ErrorDeConexionException e) {
				e.printStackTrace();
				uno.put("mensaje", e.getMessage());
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			response().setHeader("Access-Control-Allow-Origin", "*");
    		try {
				JSONObject uno = new JSONObject();
				uno.put("mensaje", "Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
				uno.put("retorno", "ERROR");
				return internalServerError(uno.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
				return internalServerError("Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
			}
		}
    }
    
    public static Result asignarPuntajeAProfesional() {
    	try {
    		JSONObject uno = new JSONObject();

    		DynamicForm form = Form.form().bindFromRequest();
    		String nombre = form.get("nombre"), apellido = form.get("apellido");
    		String strPuntaje = form.get("puntaje");
    		double puntaje = 0.0;
    		
    		if(isStringInvalid(nombre) || isStringInvalid(apellido) || isStringInvalid(strPuntaje))
    		{
				uno.put("mensaje", "Los parámetros 'nombre', 'apellido' y 'puntaje' deben ser enviados.");
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
    		}
    		
    		try {
				puntaje = Double.parseDouble(strPuntaje);
			} catch (NumberFormatException e1) {
				uno.put("mensaje", "El puntaje enviado debe ser numérico.");
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			}
    		
			try {
				double nuevoPuntaje = new DaoProfesional().setNewScore(nombre, apellido, puntaje);
				uno.put("retorno", "OK");
				uno.put("nuevoPuntaje", nuevoPuntaje);
				response().setHeader("Access-Control-Allow-Origin", "*");
				return ok(uno.toString());

			} catch (ErrorDeConexionException | NoExisteException e) {
				e.printStackTrace();
				uno.put("mensaje", e.getMessage());
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			response().setHeader("Access-Control-Allow-Origin", "*");
    		try {
				JSONObject uno = new JSONObject();
				uno.put("mensaje", "Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
				uno.put("retorno", "ERROR");
				return internalServerError(uno.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
				return internalServerError("Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
			}
		}
    }
      
    public static Result fijarConsultaMedica() {
    	try {
    		JSONObject uno = new JSONObject();

    		DynamicForm form = Form.form().bindFromRequest();
    		String email = form.get("email"), strFechaHora = form.get("fechaHora"), strIdProfesional = form.get("idProfesional"), strIdCentroMedico = form.get("idCentroMedico"),  nombreProfesional = form.get("nombreProfesional"), apellidoProfesional = form.get("apellidoProfesional"), nombreCentroMedico = form.get("nombreCentroMedico");
    		Date fechaHora;
    		int idProfesional = -1, idCentroMedico = -1;
    		boolean listo = false;
    		try {
				fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(strFechaHora);
			} catch (ParseException e2) {
				uno.put("mensaje", "El parámetro 'fechaHora' debe tener formato 'dd/MM/yyyy HH:mm'.");
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			}
    		if(!isStringInvalid(strIdCentroMedico) && !isStringInvalid(strIdProfesional))
    			listo = true;
    		if(isStringInvalid(email) || !listo && (isStringInvalid(nombreCentroMedico) || isStringInvalid(nombreProfesional) || isStringInvalid(apellidoProfesional)))
    		{
				uno.put("mensaje", "Los parámetros 'email', 'nombreCentroMedico', 'nombreProfesional' y 'apellidoProfesional' deben ser enviados.");
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
    		}
    		
			try {
				DaoConsultaMedica daoConsultaMedica = new DaoConsultaMedica();
				DaoUsuario daoUsuario = new DaoUsuario();
				DaoProfesional daoProfesional = new DaoProfesional();
				DaoCentroMedico daoCentroMedico = new DaoCentroMedico();
				if(listo) {
					idCentroMedico = Integer.parseInt(strIdCentroMedico);
					idProfesional = Integer.parseInt(strIdProfesional);
					daoConsultaMedica.addNewConsultaMedica(new ConsultaMedica(fechaHora, daoUsuario.getUsuario(email), daoProfesional.getById(idProfesional),daoCentroMedico.getById(idCentroMedico)));
				}else
					daoConsultaMedica.addNewConsultaMedica(new ConsultaMedica(fechaHora, daoUsuario.getUsuario(email), daoProfesional.getByNombreApellido(nombreProfesional, apellidoProfesional),daoCentroMedico.getByNombre(nombreCentroMedico)));
				uno.put("retorno", "OK");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return ok(uno.toString());

			} catch (ErrorDeConexionException | NoExisteException e) {
				e.printStackTrace();
				uno.put("mensaje", e.getMessage());
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			}catch (NumberFormatException e) {
				e.printStackTrace();
				uno.put("mensaje", "Los parámetros 'idCentroMedico' e 'idProfesional' deben ser numéricos");
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			response().setHeader("Access-Control-Allow-Origin", "*");
    		try {
				JSONObject uno = new JSONObject();
				uno.put("mensaje", "Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
				uno.put("retorno", "ERROR");
				return internalServerError(uno.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
				return internalServerError("Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
			}
		}
    }
    
    public static Result getHistorialConsultasMedicas()  {
    	try {
			JSONObject uno = new JSONObject();
			
			DynamicForm form = Form.form().bindFromRequest();
    		String email = form.get("email");
    		if(isStringInvalid(email))
    		{
				uno.put("mensaje", "El parámetro 'email' debe ser enviado.");
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
    		}
			try {
				List<ConsultaMedica> consultas = new DaoConsultaMedica().getAllByEmail(email);
				uno.put("retorno", "OK");
				uno.put("consultasMedicas", armarListaConsultasMedicas(consultas));
				response().setHeader("Access-Control-Allow-Origin", "*");
				return ok(uno.toString());
			} catch (ErrorDeConexionException | NoExisteException e) {
				e.printStackTrace();
				uno.put("mensaje", e.getMessage());
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			response().setHeader("Access-Control-Allow-Origin", "*");
    		try {
				JSONObject uno = new JSONObject();
				uno.put("mensaje", "Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
				uno.put("retorno", "ERROR");
				return internalServerError(uno.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
				return internalServerError("Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
			}
		}
    }
    
    private static boolean isStringInvalid(String str) {
    	return str == null || str.isEmpty();
    }
    
}
