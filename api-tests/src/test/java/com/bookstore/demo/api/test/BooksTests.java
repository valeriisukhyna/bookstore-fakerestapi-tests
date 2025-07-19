package com.bookstore.demo.api.test;

import com.bookstore.demo.api.framework.dto.BookDto;
import com.bookstore.demo.api.framework.service.BooksService;
import com.bookstore.demo.datagenerator.BooksDataGenerator;
import com.bookstore.demo.utils.FakeData;
import com.bookstore.demo.utils.constants.HttpStatuses;
import com.bookstore.demo.utils.ReflectiveUtils;
import com.bookstore.demo.utils.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class BooksTests extends BaseTest {

    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String EXCERPT = "excerpt";
    private static final int RANDOM_STRING_LENGTH = 9;

    private BooksService booksService;

    private static Stream<Arguments> validBookDataProvider() {
        return Stream.of(
                Arguments.of("All valid fields", BooksDataGenerator.generateAllFieldsBookData()),
                Arguments.of("Max length sting fields", BooksDataGenerator.generateMaxStringLengthBookData()),
                Arguments.of("Only required field", BooksDataGenerator.generateRequiredFieldsBookData())
        );
    }

    private static Stream<Arguments> validNotRequiredUpdateFieldsDataProvider() {
        return Stream.of(
                Map.of(TITLE, FakeData.title()),
                Map.of(DESCRIPTION, FakeData.description()),
                Map.of(EXCERPT, FakeData.excerpt())
        ).map(Arguments::of);
    }

    @BeforeEach
    public void setupEach() {
        booksService = new BooksService(apiConfig);
    }

    @DisplayName("Get book by valid ID")
    @Test
    public void getBookByValidIdTest() {
        BookDto bookDto = booksService.getBook(1);
        assertThat(bookDto.getId()).isEqualTo(1);
    }

    @DisplayName("Get book by invalid ID")
    @Test
    public void getBookByInvalidIdTest() {
        booksService.getBookExceptional(BooksDataGenerator.getNotExistingBookId());
        assertThat(booksService.getResponseStatusCode()).isEqualTo(HttpStatuses.NOT_FOUND_STATUS);
    }

    @DisplayName("Get book with boundary ID value")
    @ParameterizedTest(name = "{displayName}: {0} value")
    @CsvSource(value = {
            "2147483647, 404",
            "2147483648, 400"
    })
    public void getBookBoundaryIdValueTest(Long id, int httpStatusCode) {
        booksService.getBookExceptional(id);
        assertThat(booksService.getResponseStatusCode()).isEqualTo(httpStatusCode);
    }

    @DisplayName("Get book with string ID value")
    @Test
    public void getBookStringIdValueTest() {
        booksService.getBookExceptional(StringUtils.generateStringOfLength(new Random().nextInt(RANDOM_STRING_LENGTH)));
        assertThat(booksService.getResponseStatusCode()).isEqualTo(HttpStatuses.BAD_REQUEST_STATUS);
    }

    @DisplayName("Get all books list")
    @Test
    public void getAllBooksListTest() {
        booksService.getBookList();
        assertThat(booksService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);
    }

    @DisplayName("All books list contains created book")
    @Test
    public void getBooksListWithCreatedBookTest() {
        int createdBookId = booksService.createBook(BooksDataGenerator.generateAllFieldsBookData()).getId();
        assertThat(booksService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);

        List<BookDto> booksList = booksService.getBookList();
        assertThat(booksService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);
        assertThat(booksList.stream().map(BookDto::getId).toList()).contains(createdBookId);
    }

    @DisplayName("Create book")
    @ParameterizedTest(name = "{displayName}: {0}")
    @MethodSource("validBookDataProvider")
    public void createBookTest(String description, BookDto expectedBookDto) {
        BookDto actualBookDto = booksService.createBook(expectedBookDto);
        assertThat(booksService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);
        verifyBooks(actualBookDto, expectedBookDto);

        actualBookDto = booksService.getBook(expectedBookDto.getId());
        assertThat(booksService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);
        verifyBooks(actualBookDto, expectedBookDto);
    }

    @DisplayName("Create book with not required fields")
    @ParameterizedTest(name = "{displayName}: {0}")
    @MethodSource("validNotRequiredUpdateFieldsDataProvider")
    public void createBookWithNotRequiredFieldsTest(Map<String, Object> updatedFieldMap) {
        BookDto expectedBookDto = BooksDataGenerator.generateRequiredFieldsBookData();
        ReflectiveUtils.setFieldsForObject(expectedBookDto, updatedFieldMap);

        BookDto actualBookDto = booksService.createBook(expectedBookDto);
        assertThat(booksService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);
        verifyBooks(actualBookDto, expectedBookDto);

        actualBookDto = booksService.getBook(expectedBookDto.getId());
        verifyBooks(actualBookDto, expectedBookDto);
    }

    @DisplayName("Update existing book")
    @ParameterizedTest(name = "{displayName}: {0}")
    @MethodSource("validBookDataProvider")
    public void updateBookTest(String description, BookDto updateBookDto) {
        int createdBookId = booksService.createBook(BooksDataGenerator.generateAllFieldsBookData()).getId();
        assertThat(booksService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);
        BookDto updatedBook = booksService.updateBook(updateBookDto, createdBookId);
        assertThat(booksService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);
        verifyBooks(updatedBook, updateBookDto);
        verifyBooks(updatedBook, booksService.getBook(updatedBook.getId()));
    }

    @DisplayName("Update book with not required fields")
    @ParameterizedTest(name = "{displayName}: {0}")
    @MethodSource("validNotRequiredUpdateFieldsDataProvider")
    public void updateBookWithNotRequiredFieldsTest(Map<String, Object> updatedFieldMap) {
        int createdBookId = booksService.createBook(BooksDataGenerator.generateAllFieldsBookData()).getId();
        assertThat(booksService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);

        BookDto updateBookDto = BooksDataGenerator.generateRequiredFieldsBookData();
        ReflectiveUtils.setFieldsForObject(updateBookDto, updatedFieldMap);
        BookDto updatedBook = booksService.updateBook(updateBookDto, createdBookId);

        assertThat(booksService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);
        verifyBooks(updatedBook, updateBookDto);
        verifyBooks(booksService.getBook(createdBookId), updatedBook);
    }

    @DisplayName("Update not existing book")
    @Test
    public void updateNotExistingBookTest() {
        booksService.updateBookExceptional(BooksDataGenerator.generateAllFieldsBookData(),
                BooksDataGenerator.getNotExistingBookId());
        assertThat(booksService.getResponseStatusCode()).isEqualTo(HttpStatuses.NOT_FOUND_STATUS);
    }

    @DisplayName("Delete existing book")
    @Test
    public void deleteExistingBookTest() {
        int createdBookId = booksService.createBook(BooksDataGenerator.generateAllFieldsBookData()).getId();
        booksService.getBook(createdBookId);
        assertThat(booksService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);

        booksService.deleteBook(createdBookId);
        assertThat(booksService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);

        booksService.getBookExceptional(createdBookId);
        assertThat(booksService.getResponseStatusCode()).isEqualTo(HttpStatuses.NOT_FOUND_STATUS);
    }

    @DisplayName("Delete book with an invalid ID")
    @Test
    public void deleteBookWithInvalidIdTest() {
        booksService.deleteBook(BooksDataGenerator.getNotExistingBookId());
        assertThat(booksService.getResponseStatusCode()).isEqualTo(HttpStatuses.NOT_FOUND_STATUS);
    }

    @DisplayName("Delete Books with boundary ID value")
    @ParameterizedTest(name = "{displayName}: {0} value")
    @CsvSource(value = {
            "2147483647, 404",
            "2147483648, 400"
    })
    public void deleteBookBoundaryIdValueTest(Long id, int httpStatusCode) {
        booksService.deleteBook(id);
        assertThat(booksService.getResponseStatusCode()).isEqualTo(httpStatusCode);
    }

    @DisplayName("Delete book with string ID value")
    @Test
    public void deleteBookStringIdValueTest() {
        booksService.deleteBook(StringUtils.generateStringOfLength(new Random().nextInt(RANDOM_STRING_LENGTH)));
        assertThat(booksService.getResponseStatusCode()).isEqualTo(HttpStatuses.BAD_REQUEST_STATUS);
    }

    private void verifyBooks(BookDto actual, BookDto expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getExcerpt()).isEqualTo(expected.getExcerpt());
        assertThat(actual.getPageCount()).isEqualTo(expected.getPageCount());
        assertThat(actual.getPublishDate()).isEqualTo(expected.getPublishDate());
    }
}
