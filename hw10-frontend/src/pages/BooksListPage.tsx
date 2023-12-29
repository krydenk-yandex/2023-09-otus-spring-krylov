import React, {useCallback, useEffect, useState} from 'react';
import {Book} from "../types";
import {Link} from "react-router-dom";
import {deleteBook as deleteBookApi, getBooks} from "../api";

function BooksListPage() {
    const [books, setBooks] = useState<Book[] | null>(null);

    useEffect(() => {
        getBooks()
            .then(books => {
                setBooks(books as Book[])
            });
    }, [])

    const deleteBook = useCallback((id: number) => {
        deleteBookApi(id).then(() => {
            setBooks(books!.filter(b => b.id != id));
        })
    }, [books])

    return (
        <div className="books-list-page">
            <h1>Книги</h1>
            <div className='create-book-container'>
                <Link to={`/books/create`}>Создать книгу</Link>
            </div>
            {books ? (
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Название</th>
                            <th>Автор</th>
                            <th>Жанры</th>
                            <th></th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                    {books.map(book => (
                        <tr key={book.id}>
                            <td>{book.id}</td>
                            <td>{book.title}</td>
                            <td>{book.author.fullName}</td>
                            <td>{book.genres.map(g => g.name).join(', ')}</td>
                            <td>
                                <Link to={`/books/edit/${book.id}`}>Редактировать</Link>
                            </td>
                            <td>
                                <a href='#' onClick={() => deleteBook(book.id)}>
                                    Удалить
                                </a>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            ) : 'Загружаем книги...'}
        </div>
    );
}

export default BooksListPage;
