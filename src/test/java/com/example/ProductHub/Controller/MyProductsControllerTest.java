package com.example.ProductHub.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.ProductHub.Service.MyProductsService;
import com.example.ProductHub.View.MyProducts;
import com.example.ProductHub.View.MyProductsResponse;

public class MyProductsControllerTest {
    
    @Mock
    private MyProductsService service;
    
    @InjectMocks
    private MyProductsController controller;

    private static List<MyProducts> dummyProducts;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @BeforeAll
    public static void setup() {
    	dummyProducts = createDummyProducts();
    }

    private static List<MyProducts> createDummyProducts() {
        List<MyProducts> dummyProducts = new ArrayList<>();
        dummyProducts.add(new MyProducts(1,  "type1", "name1",  40,  "S3",  100, 1));
        dummyProducts.add(new MyProducts(2,  "type2", "name2",  30,  "M1",  150, 2));
        dummyProducts.add(new MyProducts(3,  "type3", "name3",  50,  "L2",  200, 3));
        dummyProducts.add(new MyProducts(4,  "type1", "name4",  60,  "XL3", 250, 4));
        dummyProducts.add(new MyProducts(5,  "type2", "name5",  70,  "S4",  300, 5));
        dummyProducts.add(new MyProducts(6,  "type3", "name6",  80,  "M2",  350, 6));
        dummyProducts.add(new MyProducts(7,  "type1", "name7",  90,  "L3",  400, 7));
        dummyProducts.add(new MyProducts(8,  "type2", "name8",  100, "XL4", 450, 8));
        dummyProducts.add(new MyProducts(9,  "type3", "name9",  110, "S5",  500, 9));
        dummyProducts.add(new MyProducts(10, "type1", "name10", 120, "M3",  550, 10));
        dummyProducts.add(new MyProducts(11, "type2", "name11", 130, "L4",  600, 11));
        dummyProducts.add(new MyProducts(12, "type3", "name12", 140, "XL5", 650, 12));
        dummyProducts.add(new MyProducts(13, "type1", "name13", 150, "S6",  700, 13));
        dummyProducts.add(new MyProducts(14, "type2", "name14", 160, "M4",  750, 14));
        dummyProducts.add(new MyProducts(15, "type3", "name15", 170, "L5",  800, 15));
        return dummyProducts;
    }
    
    private static final int    PRODUCT_ID_EXISITING = 1;
    private static final int    PRODUCT_ID_NOT_EXISITING = 123;
    private static final String PRODUCT_TYPE_EXISTING = "type1";
    private static final String PRODUCT_TYPE_NON_EXISTING = "typex";
    private static final String STORAGE_ID_EXISTING = "M1";
    private static final String STORAGE_ID_NON_EXISTING = "M23";
    private static final String STORAGE_SIZE_EXISTING = "S";
    private static final String STORAGE_SIZE_NON_EXISTING = "T";
    private static final String PRODUCT_NAME_EXISTING = "name1";
    private static final String PRODUCT_NAME_NON_EXISTING = "name123";

    
    @Test
    public void test_getAllProducts_AllProductsFetched() {
        when(service.fetchAllProducts()).thenReturn(dummyProducts);
        List<MyProducts> products = controller.getAllProducts();
        assertEquals(dummyProducts, products);
        verify(service, times(1)).fetchAllProducts();
    }
    
    @Test
    public void test_getAllProducts_NoProductsExist() {
        when(service.fetchAllProducts()).thenReturn(null);
        assertNull(controller.getAllProducts());
        verify(service, times(1)).fetchAllProducts();
    }
    
    @Test
    public void test_getProductById_ProductExists() {
    	Stream<MyProducts> S= dummyProducts.stream().filter(product->product.getPid()==PRODUCT_ID_EXISITING);
        when(service.fetchById(PRODUCT_ID_EXISITING)).thenReturn(S.findFirst());
        Optional<MyProducts> myProduct = controller.getProductById(PRODUCT_ID_EXISITING);
        assertTrue(myProduct.isPresent());
        verify(service).fetchById(PRODUCT_ID_EXISITING);
    }
    
    @Test
    public void test_getProductById_ProductNotExists() {
    	Stream<MyProducts> S= dummyProducts.stream().filter(product->product.getPid()==PRODUCT_ID_NOT_EXISITING);
        when(service.fetchById(PRODUCT_ID_NOT_EXISITING)).thenReturn(S.findFirst());
        Optional<MyProducts> product = controller.getProductById(PRODUCT_ID_NOT_EXISITING);
        assertTrue(product.isEmpty());
        verify(service).fetchById(PRODUCT_ID_NOT_EXISITING);
    }
    
    @Test
    public void test_getProductsByType_ProductsExist() {
        List<MyProducts> products = dummyProducts.stream()
                .filter(dp -> dp.getPtype().equals(PRODUCT_TYPE_EXISTING))
                .collect(Collectors.toList());
        when(service.fetchByType(PRODUCT_TYPE_EXISTING)).thenReturn(products);
        List<MyProducts> myProducts = controller.getProductByType(PRODUCT_TYPE_EXISTING);
        assertEquals(products, myProducts);
        verify(service).fetchByType(PRODUCT_TYPE_EXISTING);
    }
    
    @Test
    public void test_getProductsByType_ProductsNotExist() {
        List<MyProducts> products = dummyProducts.stream()
                .filter(product -> product.getPtype().equals(PRODUCT_TYPE_NON_EXISTING))
                .collect(Collectors.toList());
        when(service.fetchByType(PRODUCT_TYPE_NON_EXISTING)).thenReturn(products);
        List<MyProducts> myProducts = controller.getProductByType(PRODUCT_TYPE_NON_EXISTING);
        assertEquals(products.size(), myProducts.size());
        verify(service).fetchByType(PRODUCT_TYPE_NON_EXISTING);
    }
    
    @Test
    public void test_getProductsByStorageId_ProductsExist() {
    	List<MyProducts> products = dummyProducts.stream()
    			.filter(product->product.getStorage_id()==STORAGE_ID_EXISTING).toList();
        when(service.fetchByStorageId(STORAGE_ID_EXISTING)).thenReturn(products);
        List<MyProducts> myProducts = controller.getProductsByStorageID(STORAGE_ID_EXISTING);
        assertEquals(products, myProducts);
        verify(service).fetchByStorageId(STORAGE_ID_EXISTING);
    }
    
    @Test
    public void test_getProductsByStorageId_ProductsNotExist() {
    	List<MyProducts> products = dummyProducts.stream()
    			.filter(product->product.getStorage_id()==STORAGE_ID_NON_EXISTING).toList();
        when(service.fetchByStorageId(STORAGE_ID_NON_EXISTING)).thenReturn(products);
        List<MyProducts> myProducts = controller.getProductsByStorageID(STORAGE_ID_NON_EXISTING);
        assertEquals(products, myProducts);
        assertEquals(0,myProducts.size());
        verify(service).fetchByStorageId(STORAGE_ID_NON_EXISTING);
    }
    
    @Test
    public void test_getProductsByStorageSize_ProductsExist() {
    	List<MyProducts> products = dummyProducts.stream()
    			.filter(product->product.getStorage_id().startsWith(STORAGE_SIZE_EXISTING)).toList();
    	when(service.fetchByStorageSize(STORAGE_SIZE_EXISTING)).thenReturn(products);
    	List<MyProducts> myProducts = controller.getProductsByStorageSize(STORAGE_SIZE_EXISTING);
    	assertEquals(products,myProducts);
    	assertEquals(4,myProducts.size());
    	verify(service).fetchByStorageSize(STORAGE_SIZE_EXISTING);
    }
    
    @Test
    public void test_getProductsByStorageSize_ProductsNotExist() {
    	List<MyProducts> products = dummyProducts.stream()
    			.filter(product->product.getStorage_id().startsWith(STORAGE_SIZE_NON_EXISTING)).toList();
    	when(service.fetchByStorageSize(STORAGE_SIZE_NON_EXISTING)).thenReturn(products);
    	List<MyProducts> myProducts = controller.getProductsByStorageSize(STORAGE_SIZE_NON_EXISTING);
    	assertTrue(myProducts.isEmpty());
    	verify(service).fetchByStorageSize(STORAGE_SIZE_NON_EXISTING);
    }
    
    @Test
    public void test_getProductsByName_ProductExists() {
    	Optional<MyProducts> product = dummyProducts.stream().filter(p->p.getPname()==PRODUCT_NAME_EXISTING).findFirst();
        when(service.fetchByName("name1")).thenReturn(product.orElseGet(null));
        MyProducts p = controller.getProductByName(PRODUCT_NAME_EXISTING);
        assertEquals(product.orElse(null),p);
        verify(service).fetchByName(PRODUCT_NAME_EXISTING);
    }
    
    @Test
    public void test_getProductsByName_ProductNotExists() {
    	Optional<MyProducts> product = dummyProducts.stream().filter(p->p.getPname()==PRODUCT_NAME_NON_EXISTING).findFirst();
        when(service.fetchByName(PRODUCT_NAME_NON_EXISTING)).thenReturn(product.orElse(null));
        MyProducts p = controller.getProductByName(PRODUCT_NAME_NON_EXISTING);
        assertNull(p);
        verify(service).fetchByName(PRODUCT_NAME_NON_EXISTING);
    }
    
  @Test
  void test_saveProduct_AddNewProduct() {
      MyProducts product = new MyProducts();
      product.setPid(PRODUCT_ID_NOT_EXISITING);
      product.setPname("Sample Product"); 
      
      when(service.existsById(PRODUCT_ID_NOT_EXISITING)).thenReturn(false); // No existing product with ID 1
      when(service.saveMyProduct(product)).thenReturn(product); // Return the saved product
      
      ResponseEntity<MyProductsResponse> response = controller.saveProduct(product);
       
      // Verify that the product was saved successfully
      assertEquals(HttpStatus.OK, response.getStatusCode());
      
      assertEquals(product, response.getBody().getProducts());
      
      assertEquals("Product saved successfully", response.getBody().getMessage());
      
      verify(service).existsById(PRODUCT_ID_NOT_EXISITING);
      verify(service).saveMyProduct(product);
      
  }
  
  @Test
  void test_saveProduct_ProductWithIdExists() {
      MyProducts product = new MyProducts();
      product.setPid(PRODUCT_ID_EXISITING); 
      product.setPname("Sample Product");
      
      when(service.existsById(PRODUCT_ID_EXISITING)).thenReturn(true); 
      
      ResponseEntity<MyProductsResponse> response = controller.saveProduct(product);
       
      assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
      assertEquals("Product with ID " + product.getPid() + " already exists.",  response.getBody().getMessage());
      
      verify(service).existsById(PRODUCT_ID_EXISITING);
      verify(service, never()).saveMyProduct(product);
  }
  
  @Test
  void test_updateProduct_ProductExists() {
      // Create a sample product
      MyProducts product = new MyProducts();
      product.setPid(PRODUCT_ID_EXISITING); // Assuming product with ID 1 exists
      product.setPname("Updated Product");

      // Mock the service method to return the updated product
      when(service.existsById(PRODUCT_ID_EXISITING)).thenReturn(true);
      when(service.saveMyProduct(product)).thenReturn(product);

      // Call the controller method
      ResponseEntity<MyProducts> response = controller.updateProduct(product);

      // Verify the response
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertEquals(product, response.getBody());

      // Verify that the service methods were called
      verify(service).existsById(PRODUCT_ID_EXISITING);
      verify(service).saveMyProduct(product);
  }

  @Test
  void test_updateProduct_NotExists() {
      // Create a sample product
      MyProducts product = new MyProducts();
      product.setPid(PRODUCT_ID_NOT_EXISITING); // Assuming product with ID 1 doesn't exist

      // Mock the service method to return false, indicating product doesn't exist
      when(service.existsById(PRODUCT_ID_NOT_EXISITING)).thenReturn(false);

      // Call the controller method
      ResponseEntity<MyProducts> response = controller.updateProduct(product);

      // Verify the response
      assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
      assertEquals(null, response.getBody());

      // Verify that the service method was called
      verify(service).existsById(PRODUCT_ID_NOT_EXISITING);
      // Ensure that saveMyProduct method is not called when the product doesn't exist
      verify(service, never()).saveMyProduct(product);
  }
  
  @Test
  void testDeleteProduct_ProductRemoved() {
    
      // Mock the service method to return true, indicating product exists
      when(service.existsById(PRODUCT_ID_EXISITING)).thenReturn(true);

      // Call the controller method
      ResponseEntity<String> response = controller.deleteProduct(PRODUCT_ID_EXISITING);

      // Verify the response
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertEquals("Record with " + PRODUCT_ID_EXISITING + " deleted", response.getBody());

      // Verify that the service method was called
      verify(service).existsById(PRODUCT_ID_EXISITING);
      verify(service).removeProduct(PRODUCT_ID_EXISITING);
  }

  @Test
  void testDeleteProduct_ProductNotFound() {
     
      when(service.existsById(PRODUCT_ID_NOT_EXISITING)).thenReturn(false); 

      // Call the controller method
      ResponseEntity<String> response = controller.deleteProduct(PRODUCT_ID_NOT_EXISITING);

      // Verify the response
      assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
      assertEquals("Record not found", response.getBody());

      // Verify that the service method was called
      verify(service).existsById(PRODUCT_ID_NOT_EXISITING);
      // Ensure that removeProduct method is not called when the product doesn't exist
      verify(service, never()).removeProduct(PRODUCT_ID_NOT_EXISITING);
  }
}
