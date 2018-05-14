/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.Agenda;

/**
 *
 * @author bionic
 */
@Stateless
public class AgendaFacade extends AbstractFacade<Agenda> {

    @PersistenceContext(unitName = "LazyEjemploPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AgendaFacade() {
        super(Agenda.class);
    }
    public List<Agenda> getAgendaList(int firts, int pageSize){
        Query query= em.createQuery("SELECT a FROM Agenda a");
        query.setFirstResult(firts);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }
    public int getRowCount(){ 
        
        Query query= em.createQuery("Select count(a.id) From Agenda a");
        return ((Long) query.getSingleResult()).intValue();
        
    }
    
}
