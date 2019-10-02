package com.tienda.retail.service;

import com.tienda.retail.model.Productos;

public interface RegistraProductosServices {
	  public void save(Productos producto) ;
	  public void update(Productos producto) ;
		public void delete(Productos producto);

}
