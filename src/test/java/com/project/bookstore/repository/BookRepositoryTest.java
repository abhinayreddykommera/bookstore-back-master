package com.project.bookstore.repository;

import com.project.bookstore.model.Book;
import com.project.bookstore.model.Language;
import com.project.bookstore.util.IsbnGenerator;
import com.project.bookstore.util.NumberGenerator;
import com.project.bookstore.util.TextUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.util.Date;

import static org.junit.Assert.*;
// This is a Test on Repository Class to check if all the Database related functionality are working properly
@RunWith(Arquillian.class)
public class BookRepositoryTest {

    @Inject
    private BookRepository bookRepository;

    // Since we are catching the exception here we expect the test to pass. If we don't put
    //Expection expected statement we shall get error in out tests.
    @Test(expected=Exception.class)
    public void findWithInvalidId(){
        bookRepository.find(null);
    }

    @Test(expected=Exception.class)
    public void createInvalidBook(){

        //Here we pass title as null and test the functionality.
        //initially without any changes/bean validations the title takes
        // null values and test passes. But it is not desirable.
        Book book = new Book("isnb",null,12F,123,Language.ENGLISH,new Date(),"http://image","description");
        book=bookRepository.create(book);
        Long bookId= book.getId();

    }


    @org.junit.Test
    public void create() throws Exception {
        //Test Counting Books
        assertEquals(Long.valueOf(0), bookRepository.countAll());
        assertEquals(0, bookRepository.findAll().size());
        //create a book
        Book book = new Book("isnb","a  title",12F,123,Language.ENGLISH,new Date(),"http://image","description");
        book=bookRepository.create(book);
        Long bookId= book.getId();

        // Check created book
        assertNotNull(bookId);

        //Find Created book
        Book bookFound= bookRepository.find(bookId);

        //check the found book is correct
        assertEquals("a title",bookFound.getTitle());
        assertTrue(bookFound.getIsbn().startsWith("13"));

        //Test Counting Books
        assertEquals(Long.valueOf(1), bookRepository.countAll());
        assertEquals(1, bookRepository.findAll().size());

        //Deleting the Book
        bookRepository.delete(bookId);
        assertEquals(Long.valueOf(0), bookRepository.countAll());
        assertEquals(0, bookRepository.findAll().size());

    }

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(BookRepository.class)// add all depending classes over here in package or it will throw errors
                .addClass(Book.class)
                .addClass(Language.class)
                .addClass(TextUtil.class)
                .addClass(NumberGenerator.class)
                .addClass(IsbnGenerator.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml");
    }

}
