export type Book = {
    id: number,
    title: string,

    author: Author,

    genres: Genre[],
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
}