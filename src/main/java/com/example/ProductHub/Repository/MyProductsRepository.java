package com.example.ProductHub.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.ProductHub.View.MyProducts;

@Repository
public interface MyProductsRepository extends JpaRepository<MyProducts, Integer>{

	List<MyProducts> findByPtype(String type);
	
	MyProducts findByPname(String pname);
	
    @Query("SELECT p FROM MyProducts p WHERE p.storage_id LIKE ?1%")
    List<MyProducts> findByStorageIdStartingWith(String storeSizePrefix);
    
    @Query("SELECT p from MyProducts p WHERE p.storage_id = ?1")
    List<MyProducts> findByStorageId(String sid);

}
