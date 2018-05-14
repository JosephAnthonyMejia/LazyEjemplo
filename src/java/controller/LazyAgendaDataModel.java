/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import model.Agenda;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author bionic
 */
public class LazyAgendaDataModel extends LazyDataModel<Agenda> {
    @EJB
    private dao.AgendaFacade ejbFacade;
    
    public LazyAgendaDataModel() {
      this.setRowCount(5000);
      
  }

  @Override
  public List<Agenda> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

      List<Agenda> list = ejbFacade.getAgendaList(first, pageSize);
      return list;
  }
    
}
