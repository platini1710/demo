package com.tienda.retail.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import com.tienda.retail.model.Productos;
import com.tienda.retail.model.ProductosDetalle;

@Component
public class ConsultaProductosServiceImplement implements ConsultaProductosService {
	  @PersistenceContext
	  EntityManager entityManager; 
	  
	  
		@Override
		public List<Productos> allProductos() {
			// TODO Auto-generated method stub
	    	List<Productos>  listProductos=entityManager.createQuery(
	                "SELECT p FROM Productos p").getResultList();
	    	return listProductos;
		}  
		
		@Override
		public Productos findProducto(Long id) {
			// TODO Auto-generated method stub
		    	return  entityManager.find(Productos.class,id);
		}
}
