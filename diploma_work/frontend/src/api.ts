import {BookSaveDto, UserAuthDto} from "./types";
import {ACCESS_TOKEN_KEY} from "./utils";

const getRequestCommonOptions = (): Record<string, any> => {
    const authHeaders = localStorage.getItem(ACCESS_TOKEN_KEY)
        ? {"Authorization": localStorage.getItem(ACCESS_TOKEN_KEY)!!}
        : {};

    const basicHeaders = {
        "Content-Type": "application/json",
        "Accept": "application/json"
    };

    return {
        headers: {
            ...basicHeaders,
            ...authHeaders
        }
    };
}

export const getChaptersByBookId = (bookId: number) => fetch(`/api/chapters/by-book/${bookId}`, getRequestCommonOptions())
    .then(async response => await response.json());

export const getChapterByUuid = (uuid: string) => fetch(`/api/chapters/${uuid}`, getRequestCommonOptions())
    .then(async response => await response.json());

export const getAuth = () => fetch(`/api/auth`, getRequestCommonOptions())

export const authenticate = (formData: UserAuthDto) => fetch(`/api/auth/login`, {
    method: "POST",
    body: JSON.stringify(formData),
    ...getRequestCommonOptions()
});

export const saveNewBook = (formData: BookSaveDto) => fetch(`/api/books`, {
    method: "POST",
    body: JSON.stringify(formData),
    ...getRequestCommonOptions()
});

export const getGenres = () => fetch(`/api/genres`, getRequestCommonOptions())
    .then(async response => await response.json());

export const getAuthors = () => fetch(`/api/authors`, getRequestCommonOptions())
    .then(async response => await response.json());

export const updateBook = (bookId: number, formData: BookSaveDto) => fetch(`/api/books/${bookId}`, {
    method: "PUT",
    body: JSON.stringify(formData),
    ...getRequestCommonOptions()
})

export const getBookById = (bookId: number) => fetch(`/api/books/${bookId}`, getRequestCommonOptions())
    .then(async response => await response.json());

export const getBooks = () => fetch("/api/books", getRequestCommonOptions())
    .then(async response => await response.json());

export const deleteBook = (bookId: number) => fetch(`/api/books/${bookId}`, {
    method: "DELETE",
    ...getRequestCommonOptions()
})