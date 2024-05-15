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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
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
    
    private static List<MyProducts> dummyProducts;
    private static final int    PRODUCT_ID_EXISITING = 1;
    private static final int    PRODUCT_ID_NOT_EXISITING = 123;
    private static final String INVALID_ID = "ABC";
    private static final String PRODUCT_TYPE_EXISTING = "type1";
    private static final String PRODUCT_TYPE_NON_EXISTING = "typex";
    private static final String STORAGE_ID_EXISTING = "M1";
    private static final String STORAGE_ID_NON_EXISTING = "M23";
    private static final String STORAGE_SIZE_EXISTING = "S";
    private static final String STORAGE_SIZE_NON_EXISTING = "T";
    private static final String PRODUCT_NAME_EXISTING = "name1";
    private static final String PRODUCT_NAME_NON_EXISTING = "name123";
    
    @BeforeEach
    public void setUp() {
//        MockitoAnnotations.initMocks(this);
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

    @Test
    public void test_fetchAllProducts_AllProductsFetched() {
        when(repository.findAll()).thenReturn(dummyProducts);

        List<MyProducts> result = service.fetchAllProducts();

        assertEquals(dummyProducts,result);
        verify(repository).findAll();
    }
    
    @Test
    public void test_fetchAllProducts_NoProductsFound() {
    	when(repository.findAll()).thenReturn(null);
    	List<MyProducts> result = service.fetchAllProducts();
    	assertNull(result);
    	verify(repository).findAll();
    }
    
    @Test
    public void test_fetchAllProducts_ExceptionOccured() {
    	when(repository.findAll()).thenThrow(RuntimeException.class);
    	assertThrowsExactly(RuntimeException.class, () -> service.fetchAllProducts());
    	verify(repository).findAll();
    }

    @Test
    public void test_fetchById_ProductWithIdExists() {
    	Stream<MyProducts> S= dummyProducts.stream().filter(product->product.getPid()==PRODUCT_ID_EXISITING);
        MyProducts product = new MyProducts();
        product.setPid(1);
        when(repository.findById(PRODUCT_ID_EXISITING)).thenReturn(S.findFirst());

        Optional<MyProducts> result = service.fetchById(PRODUCT_ID_EXISITING);

        assertTrue(result.isPresent());
        assertEquals(PRODUCT_ID_EXISITING, result.get().getPid());
        verify(repository).findById(PRODUCT_ID_EXISITING);
    }
    
    @Test
    void test_fetchById_ProductWithIdNotExists() {
    	Stream<MyProducts> S= dummyProducts.stream().filter(product->product.getPid()==PRODUCT_ID_NOT_EXISITING);
        when(repository.findById(PRODUCT_ID_NOT_EXISITING)).thenReturn(S.findFirst());
        Optional<MyProducts> result = service.fetchById(PRODUCT_ID_NOT_EXISITING);
        assertFalse(result.isPresent());
        verify(repository).findById(PRODUCT_ID_NOT_EXISITING);
    }

    @Test
    void test_fetchById_InvalidID() {
        when(repository.findById(anyInt())).thenThrow(NumberFormatException.class);
        assertThrows(NumberFormatException.class, () -> service.fetchById(Integer.parseInt(INVALID_ID)));
    }


    @Test
    public void test_fetchByType_TypeExists() {
        List<MyProducts> products = dummyProducts.stream()
                .filter(dp -> dp.getPtype().equals(PRODUCT_TYPE_EXISTING))
                .collect(Collectors.toList());
    
        when(repository.findByPtype(PRODUCT_TYPE_EXISTING)).thenReturn(products);

        List<MyProducts> myProducts = service.fetchByType(PRODUCT_TYPE_EXISTING);

        assertEquals(products, myProducts);
        verify(repository).findByPtype(PRODUCT_TYPE_EXISTING);
    }
    
    @Test
    public void test_fetchByType_TypeNotExists() {
        List<MyProducts> products = dummyProducts.stream()
                .filter(product -> product.getPtype().equals(PRODUCT_TYPE_NON_EXISTING))
                .collect(Collectors.toList());
        when(repository.findByPtype(PRODUCT_TYPE_NON_EXISTING)).thenReturn(products);

        List<MyProducts> result = service.fetchByType(PRODUCT_TYPE_NON_EXISTING);

        assertEquals(products, result);
        verify(repository).findByPtype(PRODUCT_TYPE_NON_EXISTING);
    }

    @Test
    public void test_existsById_CheckTrueOrFalse() {
        when(repository.existsById(PRODUCT_ID_EXISITING)).thenReturn(true);
        when(repository.existsById(PRODUCT_ID_NOT_EXISITING)).thenReturn(false);
        assertTrue(service.existsById(PRODUCT_ID_EXISITING));
        assertFalse(service.existsById(PRODUCT_ID_NOT_EXISITING));
        verify(repository).existsById(PRODUCT_ID_EXISITING);
        verify(repository).existsById(PRODUCT_ID_NOT_EXISITING);
    }
    
    @Test
    public void test_fetchByStorageId_ProductsExist() {
    	List<MyProducts> products = dummyProducts.stream()
    			.filter(product->product.getStorage_id()==STORAGE_ID_EXISTING).toList();
    	when(repository.findByStorageId(STORAGE_ID_EXISTING)).thenReturn(products);
    	List<MyProducts> myProducts = service.fetchByStorageId(STORAGE_ID_EXISTING);
    	assertEquals(products, myProducts);
    	verify(repository).findByStorageId(STORAGE_ID_EXISTING);
    }
    
    @Test
    public void test_fetchByStorageId_PrductsNotExist() {
    	List<MyProducts> products = dummyProducts.stream()
    			.filter(product->product.getStorage_id()==STORAGE_ID_NON_EXISTING).toList();
    	when(repository.findByStorageId(STORAGE_ID_NON_EXISTING)).thenReturn(products);
    	List<MyProducts> myProducts = service.fetchByStorageId(STORAGE_ID_NON_EXISTING);
    	assertEquals(products, myProducts);
    	assertTrue(myProducts.isEmpty());
    	verify(repository).findByStorageId(STORAGE_ID_NON_EXISTING);
    }
    
    @Test
    public void fetchByStorageSize_ProductsExist() {
    	List<MyProducts> products = dummyProducts.stream()
    		.filter(product->product.getStorage_id().startsWith(STORAGE_SIZE_EXISTING)).toList();
    	when(repository.findByStorageIdStartingWith(STORAGE_SIZE_EXISTING)).thenReturn(products);
    	List<MyProducts> myProducts = service.fetchByStorageSize(STORAGE_SIZE_EXISTING);
    	assertEquals(products, myProducts);
    	verify(repository).findByStorageIdStartingWith(STORAGE_SIZE_EXISTING);
    }
    
    @Test
    public void test_fetchByStorageSize_ProductsNotExist() {
    	List<MyProducts> products = dummyProducts.stream()
        		.filter(product->product.getStorage_id().startsWith(STORAGE_SIZE_NON_EXISTING)).toList();
        when(repository.findByStorageIdStartingWith(STORAGE_SIZE_NON_EXISTING)).thenReturn(products);
        List<MyProducts> myProducts = service.fetchByStorageSize(STORAGE_SIZE_NON_EXISTING);
        assertEquals(products, myProducts);
        assertTrue(myProducts.isEmpty());
        verify(repository).findByStorageIdStartingWith(STORAGE_SIZE_NON_EXISTING);
    }
    
    @Test
    public void test_fetchByName_ProductExist() {
    	Optional<MyProducts> product = dummyProducts.stream()
    			.filter(p->p.getPname()==PRODUCT_NAME_EXISTING).findFirst();
        
    	when(repository.findByPname(PRODUCT_NAME_EXISTING)).thenReturn(product.orElse(null));
    	MyProducts myProducts = service.fetchByName(PRODUCT_NAME_EXISTING);
    	assertEquals(product.orElse(null),myProducts);
    	verify(repository).findByPname(PRODUCT_NAME_EXISTING);
    }
    
    @Test
    public void test_fetchByName_ProductNotExist() {
    	Optional<MyProducts> product = dummyProducts.stream()
    			.filter(p->p.getPname()==PRODUCT_NAME_NON_EXISTING).findFirst();
        
    	when(repository.findByPname(PRODUCT_NAME_NON_EXISTING)).thenReturn(product.orElse(null));
    	MyProducts myProduct = service.fetchByName(PRODUCT_NAME_NON_EXISTING);
    	assertEquals(product.orElse(null),myProduct);
    	assertNull(myProduct);
    	verify(repository).findByPname(PRODUCT_NAME_NON_EXISTING);
    }
    
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
        service.removeProduct(PRODUCT_ID_EXISITING);
        verify(repository, times(1)).deleteById(PRODUCT_ID_EXISITING);
    }
    
}
