package com.tienda.retail.service;

import java.util.List;

import com.tienda.retail.model.Productos;
import com.tienda.retail.model.ProductosDetalle;

public interface ConsultaProductoDetalleService {
	  public  List<ProductosDetalle> allDetalle(Long id) ;
}
