package com.tienda.retail.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tienda.retail.exception.ResourceFoundException;
import com.tienda.retail.exception.ResourceNotFoundException;
import com.tienda.retail.helper.Constantes;
import com.tienda.retail.helper.Respuesta;
import com.tienda.retail.model.Productos;
import com.tienda.retail.service.ConsultaProductosService;
import com.tienda.retail.service.RegistraProductosServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/registro/producto")

public class ProductRegistrationController {
	@Autowired
	private RegistraProductosServices registraProductosServices;
	@Autowired
	private ConsultaProductosService consultaProductosService;
	   
	private static final Logger logger = LoggerFactory.getLogger(ProductRegistrationController.class);
	   
	/**
	 * Update user response entity.
	 *
	 * @param userId      the user id
	 * @param userDetails the user details
	 * @return the response entity
	 * @throws ResourceNotFoundException the resource not found exception
	 */
	@PutMapping("/update")
	public Respuesta updateProducto(@Valid @RequestBody Productos producto) {

		Productos prod = consultaProductosService.findProducto(producto.getId());
		Respuesta response = new Respuesta();
		logger.debug("update ");
		logger.debug("producto " + producto);
		try {
			if (prod == null) {
				logger.debug("producto nulo");
				throw new ResourceNotFoundException(Constantes.msgNotFound);
			}

			prod.setCodigoProducto(producto.getCodigoProducto());
			prod.setNombre(producto.getNombre());
			prod.setDescripcion(producto.getDescripcion());

			registraProductosServices.update(prod);
			response.setProducto(prod);
			response.setMsg(Constantes.update);
		} catch (ResourceNotFoundException e) {
			logger.error(e.getMessage());
			response.setMsg(Constantes.msgNotFound);
		} catch (Exception e) {
			response.setMsg(e.getMessage());
		}
		return response;
	}

	/**
	 * Update user response entity.
	 *
	 * @param producto the user id
	 * @return the response entity
	 * @throws ResourceNotFoundException the resource not found exception
	 */
	@PostMapping("/save")
	public Respuesta saveProducto(@Valid @RequestBody Productos producto) {

		Productos prod = consultaProductosService.findProducto(producto.getId());
		Respuesta response = new Respuesta();
		logger.info("In prod" + prod);

		try {
			if (prod != null) {
				logger.debug("producto existe");
				throw new ResourceFoundException(Constantes.msgFound);
			}
			registraProductosServices.update(producto);
			response.setProducto(prod);
			response.setMsg(Constantes.insert);
		} catch (ResourceFoundException e) {
			logger.error(e.getMessage());
			response.setMsg(Constantes.msgFound);
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.setMsg(e.getMessage());
		}
		return response;
	}

	
	

	/**
	 * Delete user map.
	 *
	 * @param Id the producto id
	 * @return object  Respuesta
	 * @throws Exception the exception
	 */
	@DeleteMapping("/delete/{id}")
	public Respuesta deleteUser(@PathVariable(value = "id") Long Id)  {
		Respuesta response = new Respuesta();
		Productos prod = consultaProductosService.findProducto(Id);
		logger.info("In prod" + prod);

		try {
			if (prod == null) {
				throw new ResourceFoundException(Constantes.msgFound);
			}
			registraProductosServices.delete(prod);
			response.setProducto(prod);
			response.setMsg(Constantes.delete);
		} catch (ResourceFoundException e) {
			logger.error(e.getMessage());
			response.setMsg(Constantes.msgNotFound);
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.setMsg(e.getMessage());
		}
		return response;

	}
	
	  

}