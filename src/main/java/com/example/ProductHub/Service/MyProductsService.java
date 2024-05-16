package com.example.ProductHub.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ProductHub.Repository.MyProductsRepository;
import com.example.ProductHub.View.MyProducts;

@Service
public class MyProductsService {
	@Autowired
	MyProductsRepository repo;
	
	public boolean existsById(int id) {
		return repo.existsById(id);
	}
	
	public List<MyProducts> fetchAllProducts(){
		return (List<MyProducts>) repo.findAll();
	}
	
	public Optional<MyProducts> fetchById(int id) {
		return repo.findById(id);
	}
	
	public List<MyProducts> fetchByType(String type){
		return repo.findByPtype(type);
	}
	
	public List<MyProducts> fetchByStorageSize(String sid){
		return repo.findByStorageIdStartingWith(sid);
	}

	public List<MyProducts> fetchByStorageId(String sid) {
		return repo.findByStorageId(sid);
	}

	public MyProducts saveMyProduct(MyProducts product) {
		return repo.save(product);
	}
	
	public void removeProduct(int id) {
		  repo.deleteById(id);	
	}

	public MyProducts fetchByName(String pname) {
		return repo.findByPname(pname);
	}
}
