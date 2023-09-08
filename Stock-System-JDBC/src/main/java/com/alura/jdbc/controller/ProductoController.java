package com.alura.jdbc.controller;

import com.alura.jdbc.com.alura.jdbc.fantory.ConnectionFactory;
import com.alura.jdbc.modelo.Categoria;
import com.alura.jdbc.modelo.Producto;
import com.alura.jdbc.DAO.productoDAO;

import java.util.List;

public class ProductoController {

	private productoDAO productoDAO;
	public ProductoController(){
		this.productoDAO = new productoDAO(new ConnectionFactory().recuperaConexion());
	}
	public int modificar(String nombre, String descripcion, Integer cantidad, Integer id) {
		return productoDAO.modificar(nombre, descripcion, cantidad, id);
	}

	public int eliminar(Integer id) {
		return productoDAO.eliminar(id);
	}


	public List<Producto> listar(){
		return productoDAO.listar();
	}

	public List<Producto> listar(Categoria categoria){
		return productoDAO.listar(categoria.getId());
	}
	public void guardar(Producto producto, Integer categoriaid) {
		producto.setCategoriaId(categoriaid);
		productoDAO.guardar(producto);
	}

}