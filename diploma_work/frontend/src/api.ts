import {BookSaveDto, UserAuthDto} from "./types";
import {toFormData} from "./utils";

const getRequestCommonOptions = () => {
    const authHeaders = localStorage.getItem("accessToken")
        ? {headers: {"Authorization": localStorage.getItem("accessToken")!!}}
        : {}

    const basicHeaders = {
        headers: {
            "Content-Type": "application/json",
            "Accept": "*/*"
        }
    }

    return {
        ...basicHeaders,
        ...authHeaders
    };
}

export const getAuth = () => fetch(`/api/auth`, {
    ...getRequestCommonOptions()
})

export const authenticate = (formData: UserAuthDto) => fetch(`/api/auth/login`, {
    method: "POST",
    body: JSON.stringify(formData),
    ...getRequestCommonOptions()
});

export const saveNewBook = (formData: BookSaveDto) => fetch(`/api/books`, {
    method: "POST",
    body: toFormData(formData),
    ...getRequestCommonOptions()
});

export const getGenres = () => fetch(`/api/genres`, {
    ...getRequestCommonOptions()
})
    .then(async response => await response.json());

export const getAuthors = () => fetch(`/api/authors`, {
    ...getRequestCommonOptions()
})
    .then(async response => await response.json());

export const updateBook = (bookId: number, formData: BookSaveDto) => fetch(`/api/books/${bookId}`, {
    method: "PUT",
    body: toFormData(formData),
    ...getRequestCommonOptions()
})

export const getBookById = (bookId: number) => fetch(`/api/books/${bookId}`, {
    ...getRequestCommonOptions()
})
    .then(async response => await response.json());

export const getBooks = () => fetch("/api/books", {
    ...getRequestCommonOptions()
})
    .then(async response => await response.json());

export const deleteBook = (bookId: number) => fetch(`/api/books/${bookId}`, {
    method: "DELETE",
    ...getRequestCommonOptions()
})