import React, {FormEvent, useCallback, useEffect, useState} from 'react';
import {Author, BookSaveDto, Genre} from "../types";
import {useNavigate} from "react-router-dom";
import {getSelectInputValue, toFormData} from "../utils";

function BookCreatePage() {
    const navigate = useNavigate();

    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [authors, setAuthors] = useState<Author[] | null>(null);
    const [genres, setGenres] = useState<Genre[] | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [formData, setFormData] = useState<BookSaveDto>({
        title: "",
        authorId: 1,
        genresIds: []
    })

    const onSubmit = useCallback((e: FormEvent) => {
        e.preventDefault();
        fetch(`/books`, {
            method: "PUT",
            body: toFormData(formData)
        }).then((response) => {
            if (response.ok) {
                navigate("/")
            }
            throw new Error("Что-то пошло не так");
        } )
            .catch((error) => setError(error.message))
    }, [formData]);


    useEffect(() => {
        Promise.all([
            fetch(`/genres`)
                .then(async response => await response.json()),
            fetch(`/authors`)
                .then(async response => await response.json()),
        ]).then(([genres, authors]) => {
            setAuthors(authors);
            setGenres(genres);
            setIsLoading(false);
        })
    }, [])


    const handleTextChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData(prevState => ( {...prevState, [e.target.name]: e.target.value}));
    }, []);

    const handleSelectChange = useCallback((e: React.ChangeEvent<HTMLSelectElement>) => {
        setFormData(prevState => ( {...prevState, [e.target.name]: getSelectInputValue(e)}));
    }, []);

    return (
        <div className="books-list-page">
            <h1>Создание книги</h1>
            {!isLoading ? (
                <form onSubmit={onSubmit}
                      method="post"
                      className="form"
                >
                    <div className="form-field">
                        <label htmlFor="name-input">Название:</label>
                        <input id="name-input" type="text" name="title" value={formData.title} onChange={handleTextChange}/>
                    </div>
                    <div className="form-field">
                        <label htmlFor="author-input">Автор:</label>
                        <select id="author-input" name="authorId" onChange={handleSelectChange}>
                            {authors!.map(author => (
                                <option value={author.id} key={author.id}
                                        selected={author.id === formData.authorId}
                                        label={author.fullName}
                                />
                            ))}
                        </select>
                    </div>
                    <div className="form-field">
                        <label htmlFor="id-input">Жанры:</label>
                        <select id="author-input" name="genresIds" multiple onChange={handleSelectChange}>
                            {genres!.map(genre => (
                                <option value={genre.id} key={genre.id}
                                        selected={formData.genresIds.includes(genre.id)}>
                                    {genre.name}
                                </option>
                            ))}
                        </select>
                    </div>
                    <button type="submit">Сохранить</button>
                    {error && (
                        <div className="form-error">{error}</div>
                    )}
                </form>
            ) : 'Загружаем жанры и авторов...'}
        </div>
    );
}

export default BookCreatePage;
