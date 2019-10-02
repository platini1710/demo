package com.tienda.retail.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.tienda.retail.model.Productos;
import com.tienda.retail.model.ProductosDetalle;

public interface ConsultaProductosService {
	public List<Productos> allProductos();
	public Productos findProducto(Long id) ;
}
