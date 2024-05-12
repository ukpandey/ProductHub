package com.example.ProductHub;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyProductsRepository extends CrudRepository<MyProducts, Integer>{

	List<MyProducts> findByPtype(String type);
	
	MyProducts findByPname(String pname);
	
    @Query("SELECT p FROM MyProducts p WHERE p.storage_id LIKE ?1%")
    List<MyProducts> findByStorageIdStartingWith(String storeSizePrefix);
    
    @Query("SELECT p from MyProducts p WHERE p.storage_id = ?1")
    List<MyProducts> findByStorageId(String sid);

}
