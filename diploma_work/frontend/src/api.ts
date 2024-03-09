import {BookSaveDto, UserAuthDto} from "./types";
import {toFormData} from "./utils";

const getAuthData = () => (localStorage.getItem("accessToken")
    ? {headers: {"Authorization": localStorage.getItem("accessToken")!!}}
    : {}
);

export const getAuth = () => fetch(`/api/auth`, {
    ...getAuthData()
})

export const authenticate = (formData: UserAuthDto) => fetch(`/api/auth/login`, {
    method: "POST",
    body: JSON.stringify(formData),
    headers: {
        "Content-Type": "application/json",
        "Accept": "*/*"
    }
});

export const saveNewBook = (formData: BookSaveDto) => fetch(`/api/books`, {
    method: "POST",
    body: toFormData(formData),
    ...getAuthData()
});

export const getGenres = () => fetch(`/api/genres`, {
    ...getAuthData()
})
    .then(async response => await response.json());

export const getAuthors = () => fetch(`/api/authors`, {
    ...getAuthData()
})
    .then(async response => await response.json());

export const updateBook = (bookId: number, formData: BookSaveDto) => fetch(`/api/books/${bookId}`, {
    method: "PUT",
    body: toFormData(formData),
    ...getAuthData()
})

export const getBookById = (bookId: number) => fetch(`/api/books/${bookId}`, {
    ...getAuthData()
})
    .then(async response => await response.json());

export const getBooks = () => fetch("/api/books", {
    ...getAuthData()
})
    .then(async response => await response.json());

export const deleteBook = (bookId: number) => fetch(`/api/books/${bookId}`, {
    method: "DELETE",
    ...getAuthData()
})