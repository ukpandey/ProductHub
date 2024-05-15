package com.example.ProductHub;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/myproducts")
public class MyProductsController {
	@Autowired
	MyProductsService service;
	
	@GetMapping
	public List<MyProducts> getAllProducts(){
		return service.fetchAllProducts();
	}
	
	@GetMapping("id/{id}")
	public Optional<MyProducts> getProductById(@PathVariable int id){
		return service.fetchById(id);
	}
	
	@GetMapping("type/{ptype}")
	public List<MyProducts> getProductByType(@PathVariable String ptype){
		return service.fetchByType(ptype);
	}
	
	@GetMapping("sid/{sid}")
	public List<MyProducts> getProductsByStorageID(@PathVariable String sid){
		return service.fetchByStorageId(sid);
	}
	
	@GetMapping("size/{size}")
	public List<MyProducts> getProductsByStorageSize(@PathVariable String size){
		return service.fetchByStorageSize(size);
	}
	
	@GetMapping("name/{pname}")
	public MyProducts getProductByName(@PathVariable String pname){
		return service.fetchByName(pname);
	}
	
	@PostMapping("add")
	public ResponseEntity<MyProductsResponse> saveProduct(@RequestBody MyProducts product){
		
		if(product.getPname() == null) {
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					 .body(new MyProductsResponse("Failed to save product."));
		}
		
	    if(service.existsById(product.getPid())) {
	          return ResponseEntity.badRequest()
	        		  .body(new MyProductsResponse("Product with ID " + product.getPid() + " already exists."));
	    }
	        
	    MyProducts savedProduct = service.saveMyProduct(product);
	    if(savedProduct != null) {
	         return ResponseEntity.ok()
	        		 .body(new MyProductsResponse(savedProduct, "Product saved successfully"));
	    }
	    else {
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	        		 .body(new MyProductsResponse("Failed to save product."));
	    }
	 }
   
	@PutMapping("edit")
	public ResponseEntity<MyProducts> updateProduct(@RequestBody MyProducts updatedProduct){
	        if (!service.existsById(updatedProduct.getPid())) {
	            return ResponseEntity.notFound().build();
	        }


	        // Update the product in the database
	        MyProducts savedProduct = service.saveMyProduct(updatedProduct);

	        // Return the updated product
	        return ResponseEntity.ok(savedProduct);
	}
	  
	@DeleteMapping("id/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable int id){
	    if (!service.existsById(id)) {
	    	return new ResponseEntity<String>("Record not found", HttpStatus.NOT_FOUND);
	    }
	        
	    service.removeProduct(id);
	       return new ResponseEntity<String>("Record with "+id+ " deleted", HttpStatus.OK);
	 }

}

