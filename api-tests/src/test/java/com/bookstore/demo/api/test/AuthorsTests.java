package com.bookstore.demo.api.test;

import com.bookstore.demo.api.framework.dto.AuthorDto;
import com.bookstore.demo.api.framework.service.AuthorsService;
import com.bookstore.demo.datagenerator.AuthorsDataGenerator;
import com.bookstore.demo.utils.FakeData;
import com.bookstore.demo.utils.ReflectiveUtils;
import com.bookstore.demo.utils.StringUtils;
import com.bookstore.demo.utils.constants.HttpStatuses;
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

public class AuthorsTests extends BaseTest {

    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final int RANDOM_STRING_LENGTH = 9;

    private AuthorsService authorsService;

    private static Stream<Arguments> validAuthorDataProvider() {
        return Stream.of(
                Arguments.of("All valid fields", AuthorsDataGenerator.generateAllFieldsAuthorData()),
                Arguments.of("Max length sting fields", AuthorsDataGenerator.generateMaxLengthStringFieldsAuthorData()),
                Arguments.of("Only required field", AuthorsDataGenerator.generateRequiredFieldsAuthorData())
        );
    }

    private static Stream<Arguments> validNotRequiredUpdateFieldsDataProvider() {
        return Stream.of(
                Map.of(FIRST_NAME, FakeData.firstName()),
                Map.of(LAST_NAME, FakeData.lastName())
        ).map(Arguments::of);
    }

    @BeforeEach
    public void setupEach() {
        authorsService = new AuthorsService(apiConfig);
    }

    @DisplayName("Get author by valid ID")
    @Test
    public void getAuthorByValidIdTest() {
        AuthorDto authorDto = authorsService.getAuthor(1);
        assertThat(authorDto.getId()).isEqualTo(1);
    }

    @DisplayName("Get author by invalid ID")
    @Test
    public void getAuthorByInvalidIdTest() {
        authorsService.getAuthorExceptional(AuthorsDataGenerator.getNotExistingAuthorId());
        assertThat(authorsService.getResponseStatusCode()).isEqualTo(HttpStatuses.NOT_FOUND_STATUS);
    }

    @DisplayName("Get author with boundary ID value")
    @ParameterizedTest(name = "{displayName}: {0} value")
    @CsvSource(value = {
            "2147483647, 404",
            "2147483648, 400"
    })
    public void getAuthorBoundaryIdValueTest(Long id, int httpStatusCode) {
        authorsService.getAuthorExceptional(id);
        assertThat(authorsService.getResponseStatusCode()).isEqualTo(httpStatusCode);
    }

    @DisplayName("Get author with string ID value")
    @Test
    public void getAuthorStringIdValueTest() {
        authorsService.getAuthorExceptional(StringUtils.generateStringOfLength(new Random().nextInt(RANDOM_STRING_LENGTH)));
        assertThat(authorsService.getResponseStatusCode()).isEqualTo(HttpStatuses.BAD_REQUEST_STATUS);
    }

    @DisplayName("Get all authors list")
    @Test
    public void getAllAuthorListTest() {
        authorsService.getAuthorList();
        assertThat(authorsService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);
    }

    @DisplayName("All authors list contains created author")
    @Test
    public void getAuthorListWithCreatedAuthorTest() {
        int createdAuthorId = authorsService.createAuthor(AuthorsDataGenerator.generateAllFieldsAuthorData()).getId();
        assertThat(authorsService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);

        List<AuthorDto> authorList = authorsService.getAuthorList();
        assertThat(authorsService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);
        assertThat(authorList.stream().map(AuthorDto::getId).toList()).contains(createdAuthorId);
    }

    @DisplayName("Get authors by book id")
    @Test
    public void getAuthorListByBookIdTest() {
        int bookId = AuthorsDataGenerator.getExistingBookId();
        List<AuthorDto> authorList = authorsService.getAuthorsByBookId(bookId);
        assertThat(authorsService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);
        assertThat(authorList.stream().mapToInt(AuthorDto::getBookId).allMatch(id -> id == bookId)).isTrue();
    }

    @DisplayName("Create author")
    @ParameterizedTest(name = "{displayName}: {0}")
    @MethodSource("validAuthorDataProvider")
    public void createAuthorTest(String description, AuthorDto expectedAuthorDto) {
        AuthorDto actualAuthorDto = authorsService.createAuthor(expectedAuthorDto);
        assertThat(authorsService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);
        verifyAuthors(actualAuthorDto, expectedAuthorDto);

        actualAuthorDto = authorsService.getAuthor(expectedAuthorDto.getId());
        assertThat(authorsService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);
        verifyAuthors(actualAuthorDto, expectedAuthorDto);
    }

    @DisplayName("Create author with not required fields")
    @ParameterizedTest(name = "{displayName}: {0}")
    @MethodSource("validNotRequiredUpdateFieldsDataProvider")
    public void createAuthorWithNotRequiredFieldsTest(Map<String, Object> updatedFieldMap) {
        AuthorDto expectedAuthorDto = AuthorsDataGenerator.generateRequiredFieldsAuthorData();
        ReflectiveUtils.setFieldsForObject(expectedAuthorDto, updatedFieldMap);

        AuthorDto actualAuthorDto = authorsService.createAuthor(expectedAuthorDto);
        assertThat(authorsService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);
        verifyAuthors(actualAuthorDto, expectedAuthorDto);

        actualAuthorDto = authorsService.getAuthor(expectedAuthorDto.getId());
        verifyAuthors(actualAuthorDto, expectedAuthorDto);
    }

    @DisplayName("Update author")
    @ParameterizedTest(name = "{displayName}: {0}")
    @MethodSource("validAuthorDataProvider")
    public void updateAuthorTest(String description, AuthorDto updateAuthorDto) {
        int createdAuthorId = authorsService.createAuthor(AuthorsDataGenerator.generateAllFieldsAuthorData()).getId();
        assertThat(authorsService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);
        AuthorDto updatedAuthor = authorsService.updateAuthor(updateAuthorDto, createdAuthorId);
        assertThat(authorsService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);
        verifyAuthors(updatedAuthor, updateAuthorDto);
        verifyAuthors(authorsService.getAuthor(createdAuthorId), updatedAuthor);
    }

    @DisplayName("Update author with not required fields")
    @ParameterizedTest(name = "{displayName}: {0}")
    @MethodSource("validNotRequiredUpdateFieldsDataProvider")
    public void updateAuthorWithNotRequiredFieldsTest(Map<String, Object> updatedFieldMap) {
        int createdAuthorId = authorsService.createAuthor(AuthorsDataGenerator.generateAllFieldsAuthorData()).getId();
        assertThat(authorsService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);

        AuthorDto updateAuthorDto = AuthorsDataGenerator.generateRequiredFieldsAuthorData();
        ReflectiveUtils.setFieldsForObject(updateAuthorDto, updatedFieldMap);
        AuthorDto updatedAuthor = authorsService.updateAuthor(updateAuthorDto, createdAuthorId);

        assertThat(authorsService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);
        verifyAuthors(updatedAuthor, updateAuthorDto);
        verifyAuthors(authorsService.getAuthor(createdAuthorId), updatedAuthor);
    }

    @DisplayName("Update not existing author")
    @Test
    public void updateNotExistingAuthorTest() {
        authorsService.updateAuthorExceptional(AuthorsDataGenerator.generateAllFieldsAuthorData(),
                AuthorsDataGenerator.getNotExistingAuthorId());
        assertThat(authorsService.getResponseStatusCode()).isEqualTo(HttpStatuses.NOT_FOUND_STATUS);
    }

    @DisplayName("Delete existing author")
    @Test
    public void deleteExistingAuthorTest() {
        int createdAuthorId = authorsService.createAuthor(AuthorsDataGenerator.generateAllFieldsAuthorData()).getId();
        authorsService.getAuthor(createdAuthorId);
        assertThat(authorsService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);

        authorsService.deleteAuthor(createdAuthorId);
        assertThat(authorsService.getResponseStatusCode()).isEqualTo(HttpStatuses.OK_STATUS);

        authorsService.getAuthorExceptional(createdAuthorId);
        assertThat(authorsService.getResponseStatusCode()).isEqualTo(HttpStatuses.NOT_FOUND_STATUS);
    }

    @DisplayName("Delete Author by invalid ID")
    @Test
    public void deleteAuthorByInvalidIdTest() {
        authorsService.deleteAuthor(AuthorsDataGenerator.getNotExistingAuthorId());
        assertThat(authorsService.getResponseStatusCode()).isEqualTo(HttpStatuses.NOT_FOUND_STATUS);
    }

    @DisplayName("Delete author with boundary ID value")
    @ParameterizedTest(name = "{displayName}: {0} value")
    @CsvSource(value = {
            "2147483647, 404",
            "2147483648, 400"
    })
    public void deleteAuthorBoundaryIdValueTest(Long id, int httpStatusCode) {
        authorsService.deleteAuthor(id);
        assertThat(authorsService.getResponseStatusCode()).isEqualTo(httpStatusCode);
    }

    @DisplayName("Delete author with string ID value")
    @Test
    public void deleteAuthorStringIdValueTest() {
        authorsService.deleteAuthor(StringUtils.generateStringOfLength(new Random().nextInt(RANDOM_STRING_LENGTH)));
        assertThat(authorsService.getResponseStatusCode()).isEqualTo(HttpStatuses.BAD_REQUEST_STATUS);
    }

    private void verifyAuthors(AuthorDto actual, AuthorDto expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getBookId()).isEqualTo(expected.getBookId());
        assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
    }
}
