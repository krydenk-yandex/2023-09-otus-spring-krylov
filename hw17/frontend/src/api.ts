import {BookSaveDto} from "./types";
import {toFormData} from "./utils";

export const saveNewBook = (formData: BookSaveDto) => fetch(`/api/books`, {
    method: "POST",
    body: toFormData(formData)
});

export const getGenres = () => fetch(`/api/genres`)
    .then(async response => await response.json());

export const getAuthors = () => fetch(`/api/authors`)
    .then(async response => await response.json());

export const updateBook = (bookId: number, formData: BookSaveDto) => fetch(`/api/books/${bookId}`, {
    method: "PUT",
    body: toFormData(formData)
})

export const getBookById = (bookId: number) => fetch(`/api/books/${bookId}`)
    .then(async response => await response.json());

export const getBooks = () => fetch("/api/books")
    .then(async response => await response.json());

export const deleteBook = (bookId: number) => fetch(`/api/books/${bookId}`, {
    method: "DELETE"
})