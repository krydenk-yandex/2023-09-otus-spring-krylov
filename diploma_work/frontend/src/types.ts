export type Chapter = {
    uuid: string,
    title: string,
    text: string,
    book: {
        id: number,
        title: string,
    },
    nextChapter?: {
        uuid: string,
        title: string,
    }
    prevChapter?: {
        uuid: string,
        title: string,
    }
}

export type User = {
    id: number,
    username: string,
    authorities: string[]
}

export type UserAuthDto = {
    username: string,
    password: string,
}

export type Book = {
    id: number,
    title: string,
    coverUrl: string,

    author: Author,

    genres: Genre[],
}

export type BookWithChapters = Book & {
    chapters: Chapter[]
}

export type Author = {
    id: number,
    fullName: string,
}

export type Genre = {
    id: number,
    name: string,
}

export type BookSaveDto = {
    title: string,
    authorId: number,
    genresIds: number[],
    chapters: {
        title: string,
        text: string,
    }[],
    coverBase64?: String
}

export type ValidationErrorDto = Record<string, string>;