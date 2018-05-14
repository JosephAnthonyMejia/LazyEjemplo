package controller;

import model.Agenda;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import dao.AgendaFacade;




import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@Named("agendaController")
@SessionScoped
public class AgendaController implements Serializable {

    @EJB
    private dao.AgendaFacade ejbFacade;
    private List<Agenda> items = null;
    private Agenda selected;
    private LazyDataModel<Agenda> lazyModel;

    public LazyDataModel<Agenda> getLazyModel() {
        return lazyModel;
    }

//    lazyModel = new LazyAgendaDataModel();
    public void setLazyModel(LazyDataModel<Agenda> lazyModel) {
        this.lazyModel = lazyModel;
    }

    @PostConstruct
    public void init() {      
        lazyModel = new LazyDataModel<Agenda>() {
            @Override
            public List<Agenda> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                List<Agenda> list = ejbFacade.getAgendaList(first, pageSize);
                lazyModel.setRowCount(ejbFacade.getRowCount());
                return list;               
            }
        };
    }

    public AgendaController() {
    }

    public Agenda getSelected() {
        return selected;
    }

    public void setSelected(Agenda selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AgendaFacade getFacade() {
        return ejbFacade;
    }

    public Agenda prepareCreate() {
        selected = new Agenda();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AgendaCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AgendaUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AgendaDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

//    public List<Agenda> getItems() {
//        if (items == null) {
//            items = getFacade().getAgendaList();
//        }
//        return items;
//    }
    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Agenda getAgenda(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Agenda> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Agenda> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Agenda.class)
    public static class AgendaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AgendaController controller = (AgendaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "agendaController");
            return controller.getAgenda(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Agenda) {
                Agenda o = (Agenda) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Agenda.class.getName()});
                return null;
            }
        }

    }

}
