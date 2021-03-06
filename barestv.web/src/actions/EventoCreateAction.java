package actions;



import beans.*;

import com.opensymphony.xwork2.ActionSupport;

import configuracion.C;
import factoria.FactoriaDAO;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
/**
 * Esta clase es llamada cuando se muestra el producto
 * se obtiene el producto y se examina si el cliente que visita si ha comprado el
 * producto
 * @author alumno
 *
 */
public class EventoCreateAction extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private String descripcion;
	private String fechainicial;
	private String fechafinal;
	private String categoria;
	private String user;
	
	private Map<String, Object> session;
	
	private ArrayList<String> categorias;
	public void validate() {
		Boolean hayErrores = false;
		
		if(nombre.equals("")){
			addFieldError("nombre", getText("errors.vacio"));
			hayErrores = true;
		}
		if(descripcion.equals("")){
			addFieldError("descripcion", getText("errors.vacio"));
			hayErrores = true;
		}
		if(categoria.equals("")){
			addFieldError("categoria", getText("errors.vacio"));
			hayErrores = true;
		}
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		try{
			format.parse(fechainicial);
		}catch(Exception e){
			addFieldError("fechainicial", "Formato: dd/MM/yyyy HH:mm");
			hayErrores = true;
		}
		try{
			format.parse(fechafinal);
		}catch(Exception e){
			addFieldError("fechafinal", "Formato: dd/MM/yyyy HH:mm");
			hayErrores = true;
		}
		if (hayErrores){
			addActionError(getText("errors.login"));
		}
	}
	
	public String execute() throws Exception {
		
		try{
                        categorias = new ArrayList<String>();
                        ArrayList<Categoria> cats = FactoriaDAO.getCategoriaDAO(C.baseDatos).getAll();
                        for (Categoria c : cats){
                            categorias.add(c.getNombreCat());
                        }
                        
			Usuario u = (Usuario)session.get("usuario");
			if (u == null) return "login";

			
			
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			Date s = format.parse(fechainicial);
			Date s1 = format.parse(fechafinal);
			
			
			if (u.isEsAdmin()){
				
			}else{
				user = u.getUsuario();
			}
			Evento e = new Evento(nombre, user,descripcion, false,
					new Timestamp(s.getTime()), new Timestamp(s1.getTime()), categoria);
			try{
				FactoriaDAO.getEventoDAO(C.baseDatos).remove(e.getTitulo(), user);
				FactoriaDAO.getEventoDAO(C.baseDatos).add(user, e);
			}catch(Exception ex){
				System.out.println("Error con la factoria Evento y "+user);
			}
			
			return "success";
				
		}catch(Exception e){
			return "error";
		}
	}
	
	

	

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the fechainicial
	 */
	public String getFechainicial() {
		return fechainicial;
	}

	/**
	 * @param fechainicial the fechainicial to set
	 */
	public void setFechainicial(String fechainicial) {
		this.fechainicial = fechainicial;
	}

	/**
	 * @return the fechafinal
	 */
	public String getFechafinal() {
		return fechafinal;
	}

	/**
	 * @param fechafinal the fechafinal to set
	 */
	public void setFechafinal(String fechafinal) {
		this.fechafinal = fechafinal;
	}

	/**
	 * @return the categorias
	 */
	public ArrayList<String> getCategorias() {
		return categorias;
	}

	/**
	 * @param categorias the categorias to set
	 */
	public void setCategorias(ArrayList<String> categorias) {
		this.categorias = categorias;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Map<String, Object> getSession() {
		return session;
	}
	public void setSession(Map<String, Object> arg0) {
		this.session = arg0;
	}
}