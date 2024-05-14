package com.example.ProductHub;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class MyProductsServiceTest {

    @Mock
    private MyProductsRepository repository;

    @InjectMocks
    private MyProductsService service;
    
    @BeforeEach
    public void setUp() {
//        MockitoAnnotations.initMocks(this);
    	MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_fetchAllProducts_AllProductsFetched() {
        List<MyProducts> productList = new ArrayList<>();
        productList.add(new MyProducts());
        productList.add(new MyProducts());
        when(repository.findAll()).thenReturn(productList);

        List<MyProducts> result = service.fetchAllProducts();

        assertEquals(2, result.size());
    }
    
    @Test
    public void test_fetchAllProducts_NoProductsFound() {
    	List<MyProducts> productList = new ArrayList<>();
    	when(repository.findAll()).thenReturn(productList);
    	List<MyProducts> result = service.fetchAllProducts();
    	assertEquals(0, result.size());
    }
    
    @Test
    public void test_fetchAllProducts_ExceptionOccured() {
    	when(repository.findAll()).thenThrow(RuntimeException.class);
    	assertThrowsExactly(RuntimeException.class, () -> service.fetchAllProducts());
    }

    @Test
    public void test_fetchById_ProductWithIdExists() {
        MyProducts product = new MyProducts();
        product.setPid(1);
        when(repository.findById(1)).thenReturn(Optional.of(product));

        Optional<MyProducts> result = service.fetchById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getPid());
    }
    
    @Test
    void test_fetchById_ProductWithIdNotExists() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        Optional<MyProducts> result = service.fetchById(1);
        assertFalse(result.isPresent());
    }

    @Test
    void test_fetchById_InvalidID() {
        when(repository.findById(anyInt())).thenThrow(NumberFormatException.class);
        assertThrows(NumberFormatException.class, () -> service.fetchById(Integer.parseInt("abc")));
    }


    @Test
    public void test_fetchByType_TypeExists() {
        List<MyProducts> productList = new ArrayList<>();
        MyProducts product = new MyProducts();
        product.setPtype("type");
        productList.add(product);
        when(repository.findByPtype("type")).thenReturn(productList);

        List<MyProducts> result = service.fetchByType("type");

        assertEquals(product.getPtype(), result.get(0).getPtype());
        assertEquals(1,result.size());
    }
    
    @Test
    public void test_fetchByType_TypeNotExists() {
        List<MyProducts> productList = new ArrayList<>();
        when(repository.findByPtype("type")).thenReturn(productList);

        List<MyProducts> result = service.fetchByType("type");

        assertEquals(0,result.size());
    }

    @Test
    public void test_existsById_CheckTrueOrFalse() {
        when(repository.existsById(1)).thenReturn(true);

        assertTrue(service.existsById(1));
        assertFalse(service.existsById(2));
    }
    
    @Test
    public void test_fetchByStorageId_ProductsExist() {
    	List<MyProducts> productList = new ArrayList<>();
    	MyProducts product = new MyProducts();
    	product.setStorage_id("sid");
    	productList.add(product);
    	when(repository.findByStorageId("sid")).thenReturn(productList);
    	assertEquals(product.getStorage_id(), service.fetchByStorageId("sid").get(0).getStorage_id());
    	assertEquals(1,service.fetchByStorageId("sid").size());
    }
    
    @Test
    public void test_fetchByStorageId_PrductsNotExist() {
    	
    	when(repository.findByStorageId("sid")).thenReturn(new ArrayList<MyProducts>());
    	assertEquals(0,service.fetchByStorageId("sid").size());
    }
    
    @Test
    public void fetchByStorageSize_ProductsExist() {
    	List<MyProducts> productList = new ArrayList<>();
    	MyProducts product = new MyProducts();
    	product.setStorage_id("S3");
    	productList.add(product);
    	when(repository.findByStorageIdStartingWith("S")).thenReturn(productList);
    	assertEquals(1,service.fetchByStorageSize("S").size());
    }
    
    @Test
    public void test_fetchByStorageSize_ProductsNotExist() {
    	List<MyProducts> productList = new ArrayList<>();
    	when(repository.findByStorageIdStartingWith("S")).thenReturn(productList);
    	assertEquals(0,service.fetchByStorageSize("S").size());
    }
    
    @Test
    public void test_fetchByName_ProductExist() {
    	MyProducts p = new MyProducts();
    	p.setPname("pname");
    	when(repository.findByPname("pname")).thenReturn(p);
    	assertEquals(p.getPname(), service.fetchByName("pname").getPname());
    }
    
    @Test
    public void test_fetchByName_ProductNotExist() {
    	when(repository.findByPname("pname")).thenReturn(new MyProducts());
    	assertEquals(null, service.fetchByName("pname").getPname());
    }
    
//    @Test
//    void test_saveProduct_Success() {
//        MyProducts product = new MyProducts();
//        product.setPid(1);
//        product.setPname("Sample Product");
//        
//        when(service.existsById(1)).thenReturn(false); // No existing product with ID 1
//        when(service.saveMyProduct(product)).thenReturn(product); // Return the saved product
//        
//        ResponseEntity<MyProductsResponse> response = controller.saveProduct(product);
//        
//        // Verify that the product was saved successfully
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Product saved successfully: " + product, response.getBody());
//        assertEquals("Product saved successfully", response.getBody().getMessage());
//        assertEquals(product, response.getBody().getProducts());
//        
////        // Verify interactions with the repository
////        verify(repository, times(1)).existsById(1);
////        verify(repository, times(1)).save(product);
//    }
    
    @Test
    void test_saveMyProduct_Success() {
    	
    	MyProducts product = Mockito.mock(MyProducts.class);
    	when(repository.save(product)).thenReturn(product);
    	assertEquals(product, service.saveMyProduct(product));
    }
    
    @Test
    void test_saveMyProduct_Unsucessfull() {
    	MyProducts product = Mockito.mock(MyProducts.class);
    	when(repository.save(product)).thenReturn(null);
    	assertNull(service.saveMyProduct(product));
    }
    
    @Test
    void test_removeProduct() {
        int productId = 123;
        service.removeProduct(productId);
        verify(repository, times(1)).deleteById(productId);
    }
    
}
