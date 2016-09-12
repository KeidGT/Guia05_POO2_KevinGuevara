/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import com.sv.udb.modelo.Alumnos;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.primefaces.context.RequestContext;

/**
 *
 * @author REGISTRO
 */
@Named(value = "alumnosBean")
@ViewScoped
public class AlumnosBean implements Serializable{
    private Alumnos objeAlum;
    private boolean guardar;
    private String pool;
    private List<Alumnos> listAlum;
    private int codigo;
    public Alumnos getObjeAlum() {
        return objeAlum;
    }

    public void setObjeAlum(Alumnos objeAlum) {
        this.objeAlum = objeAlum;
    }

    public List<Alumnos> getListAlum() {
        return listAlum;
    }

    public void setListAlum(List<Alumnos> listAlum) {
        this.listAlum = listAlum;
    }

    public boolean isGuardar() {
        return guardar;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    
    /**
     * Creates a new instance of AlumnosBean
     */
    
    public AlumnosBean() {
    }
    
    @PostConstruct
    public void init()
    {
        this.objeAlum = new Alumnos();
        this.guardar = true;
        this.pool = "jpool";
        consTodo();
    }
    
    public void guar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(pool);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try
        {
            em.persist(this.objeAlum);
            tx.commit();
            this.guardar = true;
            this.listAlum.add(this.objeAlum); //Agrega el objeto a la lista para poderse mostrar en tabla
            this.objeAlum = new Alumnos(); // Limpiar
            ctx.execute("setMessage('MESS_SUCC', 'Datos guardados')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Error al guardar ')");
            System.out.println("ERROR: " +ex);
            //tx.rollback();
            //ex.printStackTrace();
        }
        finally
        {
            em.close();
            emf.close();            
        }
    }
    public void modificar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(pool);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Alumnos obj = null;
        try
        {
            
            obj = em.find(Alumnos.class, objeAlum.getCodiAlum());
            obj.setNombAlum(objeAlum.getNombAlum());
            obj.setApelAlum(objeAlum.getApelAlum());
            obj.setFechNaciAlum(objeAlum.getFechNaciAlum());
            obj.setMailAlum(objeAlum.getMailAlum());
            obj.setDireAlum(objeAlum.getDireAlum());
            obj.setTeleAlum(objeAlum.getTeleAlum());
            tx.commit();
            ctx.execute("setMessage('MESS_SUCC', 'Datos modificados')");
            
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Error al modificar ')");
            System.out.println("ERROR: " +ex);
            //tx.rollback();
        }
        em.close();
        emf.close();
    }
    public void eliminar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(pool);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();       
        tx.begin();
        Alumnos respo = null;
        try{
            respo = em.find(Alumnos.class, codigo);
            if(respo != null)
            {
                em.remove(respo);
                tx.commit();
                ctx.execute("setMessage('MESS_SUCC', 'Datos eliminados')");
            }
        }catch(Exception e){
            ctx.execute("setMessage('MESS_ERRO', 'Error al eliminar ')");
            tx.rollback();
        }
        em.close();
        emf.close();
    }
    public void consTodo()
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(pool);
        EntityManager em = emf.createEntityManager();
        try
        {
            this.listAlum = em.createNamedQuery("Alumnos.findAll", Alumnos.class).getResultList();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            em.close();
            emf.close();            
        }
    }
    public void cons(){
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(pool);
        EntityManager em = emf.createEntityManager();
        
        try{
            objeAlum = em.find(Alumnos.class, codigo);
        }catch(Exception e){
            e.printStackTrace();
        }            
    }
}
