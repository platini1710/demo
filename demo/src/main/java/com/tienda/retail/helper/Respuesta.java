package com.tienda.retail.helper;

import java.util.List;

import com.tienda.retail.model.Productos;
import com.tienda.retail.model.ProductosDetalle;

public class Respuesta {
private Productos  producto;
private List<Productos>  listProducto;
private List<ProductosDetalle>  listProductoDetalle;
private String msg;
public Productos getProducto() {
	return producto;
}
public void setProducto(Productos producto) {
	this.producto = producto;
}
public String getMsg() {
	return msg;
}
public void setMsg(String msg) {
	this.msg = msg;
}
public List<Productos> getListProducto() {
	return listProducto;
}
public void setListProducto(List<Productos> listProducto) {
	this.listProducto = listProducto;
}
public List<ProductosDetalle> getListProductoDetalle() {
	return listProductoDetalle;
}
public void setListProductoDetalle(List<ProductosDetalle> listProductoDetalle) {
	this.listProductoDetalle = listProductoDetalle;
}


}
